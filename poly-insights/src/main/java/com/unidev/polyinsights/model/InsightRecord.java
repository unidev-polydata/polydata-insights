package com.unidev.polyinsights.model;


/**
 * Insight value object for recording data
 */
public class InsightRecord {

    private String tenant;
    private String key;
    private Long value;

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }
}
