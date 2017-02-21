package com.unidev.polyinsights.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

/**
 * Insight record
 */
@Document
@CompoundIndex(name = "key_value", def = "{'key' : 1, 'value' : 1, 'date' : 1}")
public class Insight {

    @Id
    private String _id;

    @Indexed
    private String key;

    @Indexed
    private Long value;

    @Indexed
    private Date date;

    @Indexed
    private String clientId;

    private Map<String, Object> customData;


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Insight{");
        sb.append("_id='").append(_id).append('\'');
        sb.append(", key='").append(key).append('\'');
        sb.append(", value=").append(value);
        sb.append(", date=").append(date);
        sb.append(", clientId='").append(clientId).append('\'');
        sb.append(", customData=").append(customData);
        sb.append('}');
        return sb.toString();
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }
}
