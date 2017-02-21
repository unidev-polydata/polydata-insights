package com.unidev.polyinsights.model;


import javax.validation.constraints.NotNull;

/**
 * Insight value object for recording data
 */
public class InsightRequest {

    @NotNull
    private String tenant;
    @NotNull
    private String key;
    @NotNull
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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("InsightRequest{");
        sb.append("tenant='").append(tenant).append('\'');
        sb.append(", key='").append(key).append('\'');
        sb.append(", value='").append(value).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
