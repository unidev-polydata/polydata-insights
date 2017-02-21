package com.unidev.polyinsights.service;


import com.unidev.polyinsights.model.InsightRequest;
import com.unidev.polyinsights.model.InsightType;
import com.unidev.polyinsights.model.Tenant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for doing operations on insights
 */
@Service
public class PolyInsights {

    private static Logger LOG = LoggerFactory.getLogger(PolyInsights.class);

    @Autowired
    private TenantDAO tenantDAO;

    /**
     * Log insight for storage
     * @param insightRecord
     */
    public void logInsight(InsightRequest insightRecord) {

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






    }

}
