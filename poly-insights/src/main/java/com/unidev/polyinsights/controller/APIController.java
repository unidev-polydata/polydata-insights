package com.unidev.polyinsights.controller;

import com.unidev.platform.j2ee.common.WebUtils;
import com.unidev.polydata.domain.BasicPoly;
import com.unidev.polyinsights.model.HateoasResponse;
import com.unidev.polyinsights.model.InsightQuery;
import com.unidev.polyinsights.model.InsightRequest;
import com.unidev.polyinsights.service.PolyInsights;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * API controller
 */
@RestController("/api")
public class APIController {

    @Autowired
    private PolyInsights polyInsights;

    @Autowired
    private WebUtils webUtils;

    @PostMapping("/insight")
    public void insight(@RequestBody InsightRequest insightRequest, HttpServletRequest httpServletRequest) {
        String clientIp = webUtils.getClientIp(httpServletRequest);
        String clientId = httpServletRequest.getHeader("ClientId");
        BasicPoly basicPoly = new BasicPoly();
        basicPoly.put("clientId", clientId);
        polyInsights.logInsight(insightRequest, clientIp + ":" + clientId, basicPoly);
    }

    @PostMapping("/insight/value/sum")
    public HateoasResponse listTopKeysByValueSum(@RequestBody InsightQuery insightQuery) {
        return HateoasResponse.hateoasResponse(polyInsights.listTopKeysByValueSum(insightQuery));
    }

    @PostMapping("/insight/value/average")
    public HateoasResponse listTopKeysByAverageValue(@RequestBody InsightQuery insightQuery) {
        return HateoasResponse.hateoasResponse(polyInsights.listTopKeysByAverageValue(insightQuery));
    }

    @PostMapping("/insight/key")
    public HateoasResponse fetchInsightStatsByKey(@RequestBody InsightQuery insightQuery) {
        return HateoasResponse.hateoasResponse(polyInsights.fetchInsightStatsByKey(insightQuery));
    }

}
