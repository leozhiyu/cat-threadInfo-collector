package com.weimob.cat.collecter;

import com.dianping.cat.status.StatusExtension;
import com.dianping.cat.status.StatusExtensionRegister;
import com.weimob.cat.registry.Registry;
import java.util.HashMap;
import java.util.Map;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author yaocai.li
 * @date 2020/4/3
 */
@Component

public class ThreadPoolStatusRecorder implements Initializable, StatusExtension {

    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolStatusRecorder.class);

    private ThreadPoolCollector collector;

    private volatile boolean shutdown = false;

    public ThreadPoolStatusRecorder(ThreadPoolCollector collector) {
        if (!collector.initialized()) {
            throw new IllegalStateException("ThreadPoolCollector not init correct");
        }
        this.collector = collector;
    }

    @Override
    public void initialize() throws InitializationException {
        StatusExtensionRegister.getInstance().register(this);
    }

    @Override
    public String getId() {
        return this.collector.getPoolName();
    }

    @Override
    public String getDescription() {
        return this.collector.getPoolName() + " status";
    }

    @Override
    public Map<String, String> getProperties() {
        if (collector.isShutdown()) {
            Registry.getInstance().unRegister(this.collector.getPoolName());
            return new HashMap<>();
        }
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("poolName（线程池名称）-"+collector.getPoolName(), "0");
        dataMap.put("corePoolSize（核心线程数量）", String.valueOf(collector.getCorePoolSize()));
        dataMap.put("maxPoolSize（最大线程数量）", String.valueOf(collector.getMaxPoolSize()));
        dataMap.put("poolSize（当前线程池数量）", String.valueOf(collector.getPoolSize()));
        dataMap.put("activeCount（活跃线程数量）", String.valueOf(collector.getActiveCount()));
        dataMap.put("queueCapacityLoad（队列负载）", String.valueOf(collector.getCapacityLoad()));
        dataMap.put("loadAverage（线程池负载）", String.valueOf(collector.getLoadAverage()));
        dataMap.put("queueCapacity（队列容量）", String.valueOf(collector.getQueueCapacity()));
        dataMap.put("queueRemainingCapacity（队列剩余容量）", String.valueOf(collector.getQueueRemainingCapacity()));
        dataMap.put("queueSize（队列当前大小）", String.valueOf(collector.getQueueSize()));
        dataMap.put("taskCount（当前任务数量）", String.valueOf(collector.getTaskCount()));
        dataMap.put("completedTaskCount(已完成任务数量)", String.valueOf(collector.getCompleteTaskCount()));
        dataMap.put("rejectCount（当前拒绝）", String.valueOf(collector.getRejectCount()));
        dataMap.put("rejectedCount（总拒绝）", String.valueOf(collector.getRejectedCount()));
        //每分钟清除一次
        collector.resetRejectCount();
        return dataMap;
    }

    public void resetCollector(ThreadPoolCollector collector) {
        ThreadPoolCollector old = this.collector;
        old.clear();
        old = null;
        this.collector = collector;
        logger.info("ThreadPoolStatusRecorder reset collector,ThreadPoolExecutor:{}", this.collector.getPoolName());
    }

    public void clear() {
        this.shutdown = true;
        StatusExtensionRegister.getInstance().unregister(this);
        this.collector.clear();
        this.collector = null;
        logger.info("ThreadPoolStatusRecorder clear collector,ThreadPoolExecutor:{}", this.collector.getPoolName());
    }

    public ThreadPoolCollector getCollector() {
        return collector;
    }

    public boolean isShutdown() {
        return this.shutdown;
    }
}

