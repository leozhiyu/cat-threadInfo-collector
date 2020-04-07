package com.weimob.cat.executor;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yaocai.li
 * @date 2020/4/5
 */
public class RejectHandlerWrapper implements RejectedExecutionHandler {

    private RejectedExecutionHandler handler;
    private volatile long lastClear = 0L;
    /**
     * 总拒绝数量
     */
    private AtomicInteger rejectedCount = new AtomicInteger(0);
    /**
     * 当前时段内拒绝数量
     */
    private AtomicInteger rejectCount = new AtomicInteger(0);

    public RejectHandlerWrapper(RejectedExecutionHandler handler) {
        this.handler = handler;
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        rejectedCount.incrementAndGet();
        rejectCount.incrementAndGet();
        handler.rejectedExecution(r, executor);

    }

    public AtomicInteger getRejectedCount() {
        return rejectedCount;
    }

    public AtomicInteger getRejectCount() {
        return rejectCount;
    }

    public void resetRejectCount() {
        //一分钟清除一次
        long now = System.currentTimeMillis();
        if ((now - lastClear) >= 60 * 1000) {
            lastClear = now;
            rejectCount.set(0);
        }
    }
}
