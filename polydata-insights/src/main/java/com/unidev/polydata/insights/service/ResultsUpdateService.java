package com.unidev.polydata.insights.service;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.unidev.polydata.insights.model.Insight;
import com.unidev.polydata.insights.model.InsightQuery;
import com.unidev.polydata.insights.model.InsightRequest;
import com.unidev.polydata.insights.model.Tenant;
import com.unidev.polydata.insights.model.TimeInterval;
import java.util.HashMap;
import java.util.Map;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;


/**
 * Service for
 */
public class ResultsUpdateService {

    private static Logger LOG = LoggerFactory.getLogger(ResultsUpdateService.class);

    public static final String INSIGHTS_KEY = "_insights";

    @Autowired
    private PolyInsights polyInsights;

    @Async
    void updateResults(Tenant tenant, Insight insight,
        InsightRequest insightRecord) {

        MongoClientURI mongoURI = new MongoClientURI(tenant.getResultsUri());
        try (MongoClient mongoClient = new MongoClient(mongoURI)) {

            MongoCollection<Document> collection = mongoClient.getDatabase(mongoURI.getDatabase())
                .getCollection(mongoURI.getCollection());

            Document document = collection.find(eq("_id", insight.getKey())).first();
            boolean save = false;
            if (document == null) {
                save = true;
                document = new Document();
                document.put("_id", insight.getKey());
            }

            if (save == true && !tenant.getCreateResultIfMissing()) {
                return;
            }

            Document insightDocument = new Document();
            InsightQuery insightQuery = new InsightQuery();
            insightQuery.setInsight(insightRecord.getType());
            insightQuery.setTenant(tenant.getTenant());
            insightQuery.setKey(insight.getKey());

            for (TimeInterval timeInterval : TimeInterval.values()) {
                insightQuery.setInterval(timeInterval);
                Map<Long, Long> map = polyInsights.fetchInsightsStatsMap(insightQuery);
                Map<String, Long> statsDocument = new HashMap<>();
                map.forEach((k, v) -> {
                    statsDocument.put(k + "", v);
                });
                insightDocument.put(timeInterval.name().toLowerCase(), statsDocument);
            }

            Document typeDocument = new Document();
            typeDocument.put(insightRecord.getType(), insightDocument);
            document.put(INSIGHTS_KEY, typeDocument);

            if (save) {
                collection.insertOne(document);
            } else {
                collection.updateOne(eq("_id", insight.getKey()), new Document("$set", document));
            }

        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Failed to store stats", e);
        }
    }

}
