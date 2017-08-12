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
import java.util.concurrent.CompletableFuture;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


/**
 * Service for results calculation and store in mongodb
 */
@Service
public class ResultsUpdateService implements IResultsUpdateService {

    public static final String INSIGHTS_KEY = "_insights";
    private static Logger LOG = LoggerFactory.getLogger(ResultsUpdateService.class);
    @Autowired
    @Lazy
    private IPolyInsights polyInsights;

    @Override
    @Async
    public CompletableFuture<Document> updateResults(Tenant tenant, Insight insight,
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

            if (save && !tenant.getCreateResultIfMissing()) {
                return CompletableFuture.completedFuture(null);
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

            Document typeDocument = (Document) document.get(INSIGHTS_KEY);
            if (typeDocument == null) {
                typeDocument = new Document();
            }
            typeDocument.put(insightRecord.getType(), insightDocument);
            document.put(INSIGHTS_KEY, typeDocument);

            if (save) {
                collection.insertOne(document);
            } else {
                collection.updateOne(eq("_id", insight.getKey()), new Document("$set", document));
            }

            return CompletableFuture.completedFuture(document);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Failed to store stats", e);
            throw new RuntimeException(e);
        }
    }

}
