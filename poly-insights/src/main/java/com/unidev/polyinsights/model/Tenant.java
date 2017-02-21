package com.unidev.polyinsights.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "tenant")
public class Tenant {

    @Id
    private String tenant;

    private Map<String, InsightType> types;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Tenant{");
        sb.append("tenant='").append(tenant).append('\'');
        sb.append(", types=").append(types);
        sb.append('}');
        return sb.toString();
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public Map<String, InsightType> getTypes() {
        return types;
    }

    public void setTypes(Map<String, InsightType> types) {
        this.types = types;
    }
}
