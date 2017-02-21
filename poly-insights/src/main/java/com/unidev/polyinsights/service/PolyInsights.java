package com.unidev.polyinsights.service;


import com.unidev.polyinsights.model.Insight;
import com.unidev.polyinsights.model.InsightRequest;
import com.unidev.polyinsights.model.InsightType;
import com.unidev.polyinsights.model.Tenant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * Service for doing operations on insights
 */
@Service
public class PolyInsights {

    private static Logger LOG = LoggerFactory.getLogger(PolyInsights.class);

    @Autowired
    private TenantDAO tenantDAO;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * Log insight for storage
     * @param insightRecord
     */
    public Insight logInsight(InsightRequest insightRecord, String clinetId, Map<String,Object> customData) {

        Tenant tenant = tenantDAO.findOne(insightRecord.getTenant());
        if (tenant == null) {
            LOG.warn("No matched tenant for insight {}", insightRecord);
            throw new InsightNotAccepted("Tenant not found");
        }

        Optional<InsightType> optionalInsightType = tenant.fetchInsight(insightRecord.getKey());
        if (!optionalInsightType.isPresent()) {
            LOG.warn("Insight type not accepted {}", insightRecord);
            throw new InsightNotAccepted("Insight type not accepted");
        }

        InsightType insightType = optionalInsightType.get();

        if (!insightType.getValues().contains(insightRecord.getValue())) {
            LOG.warn("Insight value not accepted {}", insightRecord);
            throw new InsightNotAccepted("Insight value not accepted");
        }

        String collection =  tenant.getTenant() + "." + insightType.getName();

        Date minDate = new Date(System.currentTimeMillis() - insightType.getInterval());
        Query query = new Query(Criteria.where("clinetId").is("clinetId").and("date").gte(minDate));

        long count = mongoTemplate.count(query, Insight.class, collection);
        if (count != 0) {
            LOG.warn("Logging insight in interval time {}", insightRecord);
            throw new InsightNotAccepted("Logging insight in interval time");
        }

        Insight insight = new Insight();
        insight.setDate(new Date());
        insight.setClientId(clinetId);
        insight.setKey(insightRecord.getKey());
        insight.setValue(Long.parseLong(insightRecord.getValue()));
        insight.setCustomData(customData);
        mongoTemplate.save(insight, collection);
        return insight;
    }

}
