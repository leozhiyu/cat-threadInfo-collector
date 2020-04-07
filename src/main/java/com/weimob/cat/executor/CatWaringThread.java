package com.weimob.cat.executor;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.internal.MilliSecondTimer;
import com.weimob.cat.registry.Registry;
import com.weimob.cat.collecter.ThreadPoolStatusRecorder;
import com.weimob.cat.collecter.ThreadPoolCollector;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author yaocai.li
 * @date 2020/4/5
 */
public class CatWaringThread implements Runnable {

    private long m_interval = 60 * 1000;

    @Override
    public void run() {
        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            return;
        }

        while (true) {
            long start = MilliSecondTimer.currentTimeMillis();
            Registry instance = Registry.getInstance();
            Map<String, ThreadPoolStatusRecorder> registers = instance.getRegister();
            for (Entry<String, ThreadPoolStatusRecorder> entry : registers.entrySet()) {
                String s = entry.getKey();
                ThreadPoolStatusRecorder threadPoolStatusRecorder = entry.getValue();
                if (threadPoolStatusRecorder != null && !threadPoolStatusRecorder.isShutdown()) {
                    ThreadPoolCollector collector = threadPoolStatusRecorder.getCollector();
                    if (collector != null && !collector.isShutdown()) {
                        checkThreshold(collector);
                    }
                }
            }
            long elapsed = MilliSecondTimer.currentTimeMillis() - start;
            if (elapsed < m_interval) {
                try {
                    Thread.sleep(m_interval - elapsed);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }

    }


    private void checkThreshold(ThreadPoolCollector collector) {
        //todo 短信告警
        if (collector.getLoadAverage() >= collector.getLoadThresholds()) {
            Cat.logTransaction("ThreadStatusWarning", collector.getPoolName() + "-LoadAverage", System.currentTimeMillis(), Message.FAIL);
        }
        if (collector.getCapacityLoad() >= collector.getCapacityThresholds()) {
            Cat.logTransaction("ThreadStatusWarning", collector.getPoolName() + "-CapacityLoad", System.currentTimeMillis(), Message.FAIL);
        }
        if (collector.getRejectCount() >= collector.getRejectThresholds()) {
            Cat.logTransaction("ThreadStatusWarning", collector.getPoolName() + "-Reject", System.currentTimeMillis(), Message.FAIL);
        }
    }

}
