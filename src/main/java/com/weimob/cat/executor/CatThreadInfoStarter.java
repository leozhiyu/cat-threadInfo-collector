package com.weimob.cat.executor;

import com.weimob.cat.collecter.ConfigReader;
import com.weimob.cat.registry.Registry;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author yaocai.li
 * @date 2020/4/4
 */
@Component
public class CatThreadInfoStarter implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(CatThreadInfoStarter.class);
    private ApplicationContext applicationContext;

    private Properties properties;

    private ConfigReader configReader = new ConfigReader();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            applicationContext = event.getApplicationContext();
            try {
                if (properties == null) {
                    //todo 热更新
                    properties = ConfigReader.readProperties("catext.properties");
                    configReader.resolveProperties(properties);
                }
            } catch (IOException e) {
                logger.error("catext.properties resolve fail,please check config.", e);
                return;
            }
            Map<String, ThreadPoolExecutor> threadPools = applicationContext.getBeansOfType(ThreadPoolExecutor.class);

            Registry registry = Registry.getInstance();
            if (properties != null) {
                Set<String> includes = configReader.getIncludes();
                Set<String> excludes = configReader.getExcludes();
                if (!includes.isEmpty()) {
                    for (String pool : includes) {
                        ThreadPoolExecutor threadPoolExecutor = threadPools.get(pool);
                        registry.register(pool, threadPoolExecutor);
                    }
                } else if (!excludes.isEmpty()) {
                    for (Entry<String, ThreadPoolExecutor> entry : threadPools.entrySet()) {
                        if (!excludes.contains(entry.getKey())) {
                            registry.register(entry.getKey(), entry.getValue());
                        }
                    }
                } else {
                    for (Entry<String, ThreadPoolExecutor> entry : threadPools.entrySet()) {
                        registry.register(entry.getKey(), entry.getValue());
                    }
                }
            } else {
                //默认注册所有
                for (Entry<String, ThreadPoolExecutor> entry : threadPools.entrySet()) {
                    registry.register(entry.getKey(), entry.getValue());
                }
            }
            threadPools = null;
        }
    }


}
