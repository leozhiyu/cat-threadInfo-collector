package com.weimob.cat.config;

/**
 * @author yaocai.li
 * @date 2020/4/5
 */
public enum DefaultThreadStatusConfig {


    DEFAULT_CAPACITY_THRESHOLD("queueCapacityLoad",80),
    DEFAULT_ACTIVE_THRESHOLD("loadAverage",80),

    DEFAULT_REJECT_THRESHOLD("reject",10);

    private String propertiy;
    private int threshold;


    DefaultThreadStatusConfig(String propertiy, int threshold) {
        this.propertiy = propertiy;
        this.threshold = threshold;
    }

    public String getPropertiy() {
        return propertiy;
    }

    public int getThreshold() {
        return threshold;
    }
}
