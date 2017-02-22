package com.unidev.polyinsights.model;

/**
 * Value object used to query insights
 */
public class InsightQuery {

    private String tenant;
    private TimeInterval interval;
    private String insight;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("InsightQuery{");
        sb.append("tenant='").append(tenant).append('\'');
        sb.append(", interval=").append(interval);
        sb.append(", insight='").append(insight).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public TimeInterval getInterval() {
        return interval;
    }

    public void setInterval(TimeInterval interval) {
        this.interval = interval;
    }

    public String getInsight() {
        return insight;
    }

    public void setInsight(String insight) {
        this.insight = insight;
    }
}
