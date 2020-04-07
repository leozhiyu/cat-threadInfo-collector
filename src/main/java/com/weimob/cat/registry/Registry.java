package com.weimob.cat.registry;

import com.weimob.cat.collecter.ConfigReader;
import com.weimob.cat.collecter.ThreadPoolStatusRecorder;
import com.weimob.cat.collecter.ThreadPoolCollector;
import com.weimob.cat.collecter.ThreadPoolCollector.ThreadPoolCollectorConfig;
import com.weimob.cat.executor.CatWaringThread;
import com.weimob.cat.executor.ConfigurableThreadPoolExecutor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yaocai.li
 * @date 2020/4/4
 */
public class Registry implements Register {

    private static final Registry registry = new Registry();
    private static final Logger logger = LoggerFactory.getLogger(Register.class);
    private static final Map<String, ThreadPoolStatusRecorder> recorderMap = new ConcurrentHashMap<>();
    private ConfigReader configReader = new ConfigReader();
    private volatile Thread warnThread;

    @Override
    public void register(String name, ThreadPoolExecutor executor) {
        ThreadPoolCollectorConfig collectorConfig = new ThreadPoolCollectorConfig(configReader.getCapacityThreshold(name)
            , configReader.getLoadThreshold(name),configReader.getRejectThreshold(name));
        ThreadPoolCollector collector = new ThreadPoolCollector(name, executor, -1, collectorConfig);
        doRegister(executor, name, collector);
        collectorConfig = null;
    }

    public static Registry getInstance() {
        return registry;
    }

    @Override
    public void unRegister(String poolName) {
        doUnRegister(poolName, recorderMap);
    }

    public void registerForUnBean(ConfigurableThreadPoolExecutor executor) {
        if (executor != null) {
            ThreadPoolCollectorConfig collectorConfig = new ThreadPoolCollectorConfig(executor.getCapacityThresholds()
                , executor.getLoadThresholds());
            ThreadPoolCollector collector = new ThreadPoolCollector(executor.getName(), executor, executor.getQueueCapacity(), collectorConfig);
            doRegister(executor, executor.getName(), collector);
            collectorConfig = null;
        }
    }


    private synchronized void doRegister(ThreadPoolExecutor executor, String name, ThreadPoolCollector collector) {
        if (warnThread == null) {
            warnThread = new Thread(new CatWaringThread());
            warnThread.setName("CatWaringThread");
            warnThread.setDaemon(true);
            warnThread.start();
        }
        ThreadPoolStatusRecorder recorder = null;
        try {
            if (recorderMap.containsKey(name)) {
                recorder = recorderMap.get(name);
                recorder.resetCollector(collector);
            } else {
                recorder = new ThreadPoolStatusRecorder(collector);
                recorder.initialize();
                recorderMap.put(name, recorder);
            }
        } catch (Exception e) {
            logger.error("ThreadPoolStatusExtension register error", e);
            if (recorder != null) {
                recorder.clear();
                recorderMap.remove(name);
            }
            recorder = null;
        }
    }

    public Map<String, ThreadPoolStatusRecorder> getRegister() {
        return recorderMap;
    }

    private synchronized void doUnRegister(String poolName, Map<String, ThreadPoolStatusRecorder> map) {
        ThreadPoolStatusRecorder recorder = map.get(poolName);
        recorder.clear();
        map.remove(poolName);
        logger.info("Registry unRegister recorder:{}", poolName);
    }

}