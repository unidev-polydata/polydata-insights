package com.unidev.polydata.insights.service;


import com.unidev.polydata.insights.model.Insight;
import com.unidev.polydata.insights.model.InsightRequest;
import com.unidev.polydata.insights.model.Tenant;
import java.util.concurrent.CompletableFuture;
import org.bson.Document;

public interface ResultsUpdateService {

    CompletableFuture<Document> updateResults(Tenant tenant, Insight insight, InsightRequest insightRecord);
}
