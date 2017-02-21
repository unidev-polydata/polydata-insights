package com.unidev.polyinsights.model;


/**
 * Insight value object for recording data
 */
public class InsightRequest {

    private String tenant;
    private String key;
    private String value;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
