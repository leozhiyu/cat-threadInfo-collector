package com.weimob.cat.registry;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author yaocai.li
 * @date 2020/4/4
 */
public interface Register {


    void register(String name,ThreadPoolExecutor executor);
    void unRegister(String name);
}
