package com.unidev.polyinsights.model;


import java.util.Set;

/**
 * Possible insight type
 */
public class InsightType {

    private String name;
    private Set<String> values;
    private long interval;
    private long sameInsightInterval;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getValues() {
        return values;
    }

    public void setValues(Set<String> values) {
        this.values = values;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public long getSameInsightInterval() {
        return sameInsightInterval;
    }

    public void setSameInsightInterval(long sameInsightInterval) {
        this.sameInsightInterval = sameInsightInterval;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("InsightType{");
        sb.append("name='").append(name).append('\'');
        sb.append(", values=").append(values);
        sb.append(", interval=").append(interval);
        sb.append(", sameInsightInterval=").append(sameInsightInterval);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InsightType that = (InsightType) o;

        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
