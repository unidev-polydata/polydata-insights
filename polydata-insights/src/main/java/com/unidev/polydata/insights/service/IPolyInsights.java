package com.unidev.polydata.insights.service;

import com.unidev.polydata.insights.model.Insight;
import com.unidev.polydata.insights.model.InsightQuery;
import com.unidev.polydata.insights.model.InsightQueryResponse;
import com.unidev.polydata.insights.model.InsightRequest;
import java.util.Map;

/**
 * Service for fetching data about insights
 */
public interface IPolyInsights {

    Insight logInsight(InsightRequest insightRecord, String clientId,
        Map<String, Object> customData);

    InsightQueryResponse listTopKeysByValueSum(InsightQuery insightQuery);

    InsightQueryResponse listTopKeysByAverageValue(InsightQuery insightQuery);

    Map<Long, Long> fetchInsightsStatsMap(InsightQuery insightQuery);

    InsightQueryResponse fetchInsightStatsByKey(InsightQuery insightQuery);
}
