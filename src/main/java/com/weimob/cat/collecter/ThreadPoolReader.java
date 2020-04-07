package com.weimob.cat.collecter;

import java.util.HashMap;

/**
 * @author yaocai.li
 * @date 2020/4/4
 */
public interface ThreadPoolReader {

    String getPoolName();

    int getCorePoolSize();

    int getMaxPoolSize();

    int getPoolSize();

    int getActiveCount();

    String getQueueType();

    int getQueueCapacity();

    int getQueueRemainingCapacity();

    int getQueueSize();

    long getTaskCount();

    long getCompleteTaskCount();

    int getRejectCount();

    int getRejectedCount();

    int getLoadAverage();

    int getCapacityLoad();

}
