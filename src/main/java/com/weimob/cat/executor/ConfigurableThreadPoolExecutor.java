package com.weimob.cat.executor;

import com.weimob.cat.registry.Registry;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yaocai.li
 * @date 2020/4/3
 */
public class ConfigurableThreadPoolExecutor extends ThreadPoolExecutor {

    private static AtomicInteger cl = new AtomicInteger(0);
    private int queueCapacity;
    private String name;
    private int capacityThresholds = -1;
    private int loadThresholds = -1;
    private RejectHandlerWrapper rejectHandlerWrapper;


    public ConfigurableThreadPoolExecutor(int corePoolSize, int maximumPoolSize, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, 60, TimeUnit.SECONDS, workQueue);
        rejectHandlerWrapper = new RejectHandlerWrapper(super.getRejectedExecutionHandler());
        super.setRejectedExecutionHandler(rejectHandlerWrapper);
        this.setRejectedExecutionHandler(rejectHandlerWrapper);
        this.queueCapacity = workQueue.remainingCapacity();
        this.name = "ConfigableThreadPoolExecutor-" + cl.incrementAndGet();
        register();
    }
    public ConfigurableThreadPoolExecutor(String name,int corePoolSize, int maximumPoolSize, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, 60, TimeUnit.SECONDS, workQueue);
        this.name=name;
        rejectHandlerWrapper = new RejectHandlerWrapper(super.getRejectedExecutionHandler());
        super.setRejectedExecutionHandler(rejectHandlerWrapper);
        this.setRejectedExecutionHandler(rejectHandlerWrapper);
        this.queueCapacity = workQueue.remainingCapacity();
        register();
    }

    public ConfigurableThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        rejectHandlerWrapper = new RejectHandlerWrapper(super.getRejectedExecutionHandler());
        super.setRejectedExecutionHandler(rejectHandlerWrapper);
        this.setRejectedExecutionHandler(rejectHandlerWrapper);
        this.queueCapacity = workQueue.remainingCapacity();
        this.name = "ConfigableThreadPoolExecutor-" + cl.incrementAndGet();
        register();
    }
    public ConfigurableThreadPoolExecutor(String name,int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        rejectHandlerWrapper = new RejectHandlerWrapper(super.getRejectedExecutionHandler());
        super.setRejectedExecutionHandler(rejectHandlerWrapper);
        this.setRejectedExecutionHandler(rejectHandlerWrapper);
        this.queueCapacity = workQueue.remainingCapacity();
        this.name = name;
        register();
    }

    public ConfigurableThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        rejectHandlerWrapper = new RejectHandlerWrapper(super.getRejectedExecutionHandler());
        super.setRejectedExecutionHandler(rejectHandlerWrapper);
        this.setRejectedExecutionHandler(rejectHandlerWrapper);
        this.queueCapacity = workQueue.remainingCapacity();
        this.name = "ConfigableThreadPoolExecutor-" + cl.incrementAndGet();
        register();
    }
    public ConfigurableThreadPoolExecutor(String name,int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        rejectHandlerWrapper = new RejectHandlerWrapper(super.getRejectedExecutionHandler());
        super.setRejectedExecutionHandler(rejectHandlerWrapper);
        this.setRejectedExecutionHandler(rejectHandlerWrapper);
        this.queueCapacity = workQueue.remainingCapacity();
        this.name = name;
        register();
    }

    public ConfigurableThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
        rejectHandlerWrapper = new RejectHandlerWrapper(super.getRejectedExecutionHandler());
        super.setRejectedExecutionHandler(rejectHandlerWrapper);
        this.setRejectedExecutionHandler(rejectHandlerWrapper);
        this.queueCapacity = workQueue.remainingCapacity();
        this.name = "ConfigableThreadPoolExecutor-" + cl.incrementAndGet();
        register();
    }

    public ConfigurableThreadPoolExecutor(String name,int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
        rejectHandlerWrapper = new RejectHandlerWrapper(super.getRejectedExecutionHandler());
        super.setRejectedExecutionHandler(rejectHandlerWrapper);
        this.setRejectedExecutionHandler(rejectHandlerWrapper);
        this.queueCapacity = workQueue.remainingCapacity();
        this.name = name;
        register();
    }
    public ConfigurableThreadPoolExecutor(String name,int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
        RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        rejectHandlerWrapper = new RejectHandlerWrapper(super.getRejectedExecutionHandler());
        super.setRejectedExecutionHandler(rejectHandlerWrapper);
        this.setRejectedExecutionHandler(rejectHandlerWrapper);
        this.queueCapacity = workQueue.remainingCapacity();
        this.name = name;
        register();
    }

    public ConfigurableThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
        RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        rejectHandlerWrapper = new RejectHandlerWrapper(super.getRejectedExecutionHandler());
        super.setRejectedExecutionHandler(rejectHandlerWrapper);
        this.setRejectedExecutionHandler(rejectHandlerWrapper);
        this.queueCapacity = workQueue.remainingCapacity();
        this.name = "ConfigableThreadPoolExecutor-" + cl.incrementAndGet();
        register();
    }

    public int getCapacityThresholds() {
        return capacityThresholds;
    }

    public void setCapacityThresholds(int capacityThresholds) {
        this.capacityThresholds = capacityThresholds;
    }

    public int getLoadThresholds() {
        return loadThresholds;
    }

    public void setLoadThresholds(int loadThresholds) {
        this.loadThresholds = loadThresholds;
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
    }

    @Override
    public void shutdown() {
        unRegister();
        super.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        unRegister();
        return super.shutdownNow();
    }

    public int getQueueCapacity() {
        return this.queueCapacity;
    }


    private void register() {
        Registry registry = Registry.getInstance();
        try {
            registry.registerForUnBean(this);
        } catch (Exception e) {
        }
    }

    private void unRegister() {
        Registry registry = Registry.getInstance();
        try {
            registry.unRegister(this.name);
        } catch (Exception e) {
        }

    }

    public String getName() {
        return name;
    }
}
