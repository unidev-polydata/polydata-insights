package com.unidev.polyinsights.service;


import com.unidev.polyinsights.model.*;
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
    public Insight logInsight(InsightRequest insightRecord, String clientId, Map<String,Object> customData) {

        Tenant tenant = tenantDAO.findOne(insightRecord.getTenant());
        if (tenant == null) {
            LOG.warn("No matched tenant for insight {}", insightRecord);
            throw new InsightNotAccepted("Tenant not found");
        }

        Optional<InsightType> optionalInsightType = tenant.fetchInsight(insightRecord.getType());
        if (!optionalInsightType.isPresent()) {
            LOG.warn("Insight type not accepted {}", insightRecord);
            throw new InsightNotAccepted("Insight type not accepted");
        }

        InsightType insightType = optionalInsightType.get();

        if (!insightType.getValues().contains(insightRecord.getValue())) {
            LOG.warn("Insight value not accepted {}", insightRecord);
            throw new InsightNotAccepted("Insight value not accepted");
        }

        //TODO: add remote service call for checking if key exists

        String collection =  tenant.getTenant() + "." + insightType.getName();

        // check 'global' rate for posting
        Date minDate = new Date(System.currentTimeMillis() - insightType.getInterval());
        Query query = new Query(Criteria.where("clientId").is(clientId).and("date").gte(minDate));

        long count = mongoTemplate.count(query, Insight.class, collection);
        if (count != 0) {
            LOG.warn("Logging insight in interval time {}", insightRecord);
            throw new InsightNotAccepted("Logging insight too often, global limit");
        }

        // check specific key posting rate
        minDate = new Date(System.currentTimeMillis() - insightType.getSameInsightInterval());
        query = new Query(Criteria.where("key").is(insightRecord.getKey()).and("clientId").is(clientId).and("date").gte(minDate));
        count = mongoTemplate.count(query, Insight.class, collection);
        if (count != 0) {
            LOG.warn("Logging insight in interval time {}", insightRecord);
            throw new InsightNotAccepted("Logging insight too often, item limit");
        }

        Insight insight = new Insight();
        insight.setDate(new Date());
        insight.setClientId(clientId);
        insight.setKey(insightRecord.getKey());
        insight.setValue(Long.parseLong(insightRecord.getValue()));
        insight.setCustomData(customData);
        mongoTemplate.save(insight, collection);
        return insight;
    }


    public InsightQueryResponse listTopKeysByValue(InsightQuery insightQuery) {
        return null;
    }

    public InsightQueryResponse listTopKeysByCount(InsightQuery insightQuery) {
        return null;
    }

    public InsightQueryResponse fetchInsightStatsByKey(InsightQuery insightQuery) {

        return null;
    }

}
