package com.weimob.cat.collecter;

import com.weimob.cat.config.DefaultThreadStatusConfig;
import com.weimob.cat.executor.RejectHandlerWrapper;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yaocai.li
 * @date 2020/4/4
 */
public class ThreadPoolCollector implements ThreadPoolReader {

    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolCollector.class);
    private ThreadPoolExecutor threadPoolExecutor;
    private int queueCapacity;
    private String poolName;
    private String queueType;

    private RejectHandlerWrapper rejectHandlerWrapper;
    private volatile boolean init = false;

    private ThreadPoolCollectorConfig collectorConfig;

    public ThreadPoolCollector(String poolName, ThreadPoolExecutor threadPoolExecutor, int queueCapacity, ThreadPoolCollectorConfig collectorConfig) {
        this.poolName = poolName;
        this.queueType = threadPoolExecutor.getQueue().getClass().getSimpleName();
        this.threadPoolExecutor = threadPoolExecutor;
        RejectedExecutionHandler rejectedExecutionHandler = threadPoolExecutor.getRejectedExecutionHandler();
        if (!(rejectedExecutionHandler instanceof RejectHandlerWrapper)) {
            this.rejectHandlerWrapper = new RejectHandlerWrapper(rejectedExecutionHandler);
            threadPoolExecutor.setRejectedExecutionHandler(this.rejectHandlerWrapper);
        } else {
            this.rejectHandlerWrapper = (RejectHandlerWrapper) rejectedExecutionHandler;
        }
        if (queueCapacity >= 0) {
            this.queueCapacity = queueCapacity;
        } else {
            this.queueCapacity = threadPoolExecutor.getQueue().remainingCapacity();
        }
        this.collectorConfig = collectorConfig;
        init = true;
        logger.info("ThreadPoolCollector:{} init.", poolName);
    }


    @Override
    public String getPoolName() {
        return this.poolName;
    }

    @Override
    public int getCorePoolSize() {
        return threadPoolExecutor.getCorePoolSize();
    }

    @Override
    public int getMaxPoolSize() {
        return threadPoolExecutor.getMaximumPoolSize();
    }

    @Override
    public int getPoolSize() {
        return threadPoolExecutor.getPoolSize();
    }

    @Override
    public int getActiveCount() {
        return threadPoolExecutor.getActiveCount();
    }

    @Override
    public String getQueueType() {
        return this.queueType;
    }

    @Override
    public int getQueueCapacity() {
        return this.queueCapacity;
    }

    @Override
    public int getQueueRemainingCapacity() {
        return threadPoolExecutor.getQueue().remainingCapacity();
    }

    @Override
    public int getQueueSize() {
        return threadPoolExecutor.getQueue().size();
    }

    @Override
    public long getTaskCount() {
        return threadPoolExecutor.getTaskCount();
    }

    @Override
    public long getCompleteTaskCount() {
        return threadPoolExecutor.getCompletedTaskCount();
    }

    @Override
    public int getRejectCount() {
        return rejectHandlerWrapper.getRejectCount().get();
    }

    @Override
    public int getRejectedCount() {
        return rejectHandlerWrapper.getRejectedCount().get();
    }

    @Override
    public int getLoadAverage() {
        return threadPoolExecutor.getActiveCount() * 100 / threadPoolExecutor.getMaximumPoolSize();
    }

    @Override
    public int getCapacityLoad() {
        if (this.queueCapacity == 0) {
            if(getQueueType().equals("SynchronousQueue")){
                return 0;
            }
            return 100;
        }
        return threadPoolExecutor.getPoolSize() * 100 / this.queueCapacity;
    }
    public Integer getRejectThresholds() {
        return collectorConfig.getRejectThresholds();
    }

    public void resetRejectCount() {
        rejectHandlerWrapper.resetRejectCount();
    }

    public Integer getCapacityThresholds() {
        return this.collectorConfig.capacityThresholds;
    }

    public Integer getLoadThresholds() {
        return this.collectorConfig.loadThresholds;
    }

    public boolean initialized() {
        return this.init;
    }

    public void clear() {
        this.collectorConfig = null;
        this.rejectHandlerWrapper = null;
    }

    public boolean isShutdown() {
        return threadPoolExecutor.isShutdown() || threadPoolExecutor.isTerminating() || threadPoolExecutor.isTerminated();
    }

    public static class ThreadPoolCollectorConfig {

        private Integer capacityThresholds;
        private Integer loadThresholds;
        private Integer rejectThresholds;

        public ThreadPoolCollectorConfig() {
            this(DefaultThreadStatusConfig.DEFAULT_CAPACITY_THRESHOLD.getThreshold(),DefaultThreadStatusConfig.DEFAULT_ACTIVE_THRESHOLD.getThreshold(),DefaultThreadStatusConfig.DEFAULT_REJECT_THRESHOLD.getThreshold());
        }
        public ThreadPoolCollectorConfig(Integer capacityThresholds, Integer loadThresholds) {
            this(capacityThresholds,loadThresholds,DefaultThreadStatusConfig.DEFAULT_REJECT_THRESHOLD.getThreshold());
        }
        public ThreadPoolCollectorConfig(Integer capacityThresholds, Integer loadThresholds,Integer rejectThresholds) {
            setLoadThresholds(loadThresholds);
            setCapacityThresholds(capacityThresholds);
            setRejectThresholds(rejectThresholds);
        }

        public Integer getCapacityThresholds() {
            return this.capacityThresholds;
        }

        public Integer getLoadThresholds() {
            return this.loadThresholds;
        }

        public void setCapacityThresholds(Integer queueCapacityLoad) {
            if (queueCapacityLoad == null || queueCapacityLoad < 0 || queueCapacityLoad > 100) {
                this.capacityThresholds = DefaultThreadStatusConfig.DEFAULT_CAPACITY_THRESHOLD.getThreshold();
            } else {
                this.capacityThresholds = queueCapacityLoad;
            }
        }

        public void setLoadThresholds(Integer loadAverage) {
            if (loadAverage == null || loadAverage < 0 || loadAverage > 100) {
                this.loadThresholds = DefaultThreadStatusConfig.DEFAULT_ACTIVE_THRESHOLD.getThreshold();
            } else {
                this.loadThresholds = loadAverage;
            }
        }

        public Integer getRejectThresholds() {
            return rejectThresholds;
        }

        public void setRejectThresholds(Integer rejectThresholds) {
            if(rejectThresholds==null||rejectThresholds<0){
                this.rejectThresholds=DefaultThreadStatusConfig.DEFAULT_REJECT_THRESHOLD.getThreshold();
            }else{
                this.rejectThresholds = rejectThresholds;
            }
        }
    }
}
