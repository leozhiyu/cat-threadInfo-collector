package com.weimob.cat.collecter;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * @author yaocai.li
 * @date 2020/4/4
 */
public class ConfigReader {

    private static final Logger logger = LoggerFactory.getLogger(ConfigReader.class);

    private static final Map<String, Integer> loadThresholdMap = new ConcurrentHashMap<>();
    private static final Map<String, Integer> capacityThresholdMap = new ConcurrentHashMap<>();
    private static final Map<String, Integer> rejectThresholdMap = new ConcurrentHashMap<>();
    private static final Set<String> includes = new CopyOnWriteArraySet<>();
    private static final Set<String> excludes = new CopyOnWriteArraySet<>();

    public static Properties readProperties(String path) {
        Properties properties;
        try {
            properties = PropertiesLoaderUtils.loadAllProperties(path);
            return properties;
        } catch (IOException e) {
            logger.error("read catext.properties error,please check the resource exist", e);
        }
        return null;
    }


    public static Set<String> getProperties(Properties properties, String name) {
        LinkedHashSet<String> includes = new LinkedHashSet<String>();
        String property = properties.getProperty(name);
        if(StringUtils.isBlank(property)){
            return includes;
        }
        String[] configs = property.split(",");
        Collections.addAll(includes, configs);
        return includes;

    }

    public void resolveProperties(Properties properties) throws IOException {
        try {
            includes.addAll(getProperties(properties, "collect-threadPools-includes"));
            excludes.addAll(getProperties(properties, "collect-threadPools-excludes"));
            Set<String> queueThresholds = getProperties(properties, "collect-threadPools-capacityThresholds");
            Set<String> rejectThresholds = getProperties(properties, "collect-threadPools-rejectThresholds");
            Set<String> loadThresholds = getProperties(properties, "collect-threadPools-loadThresholds");
            for (String s : queueThresholds) {
                String[] config = s.split(":");
                if (config.length == 2) {
                    capacityThresholdMap.put(config[0], Integer.parseInt(config[1]));
                }
            }
            for (String s : loadThresholds) {
                String[] config = s.split(":");
                if (config.length == 2) {
                    loadThresholdMap.put(config[0], Integer.parseInt(config[1]));
                }
            }
            for (String s : rejectThresholds) {
                String[] config = s.split(":");
                if (config.length == 2) {
                    rejectThresholdMap.put(config[0], Integer.parseInt(config[1]));
                }
            }
        } catch (Exception e) {
            logger.error("resolveProperties error", e);
            throw new IOException();
        }
    }

    public Integer getLoadThreshold(String name) {
        return loadThresholdMap.get(name);
    }

    public Integer getCapacityThreshold(String name) {
        return capacityThresholdMap.get(name);
    }
    public Integer getRejectThreshold(String name){
        return rejectThresholdMap.get(name);
    }

    public Set<String> getIncludes() {
        return includes;
    }

    public Set<String> getExcludes() {
        return excludes;
    }
}
