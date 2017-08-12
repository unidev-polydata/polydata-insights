package com.unidev.polydata.insights.controller;

import com.unidev.platform.j2ee.common.WebUtils;
import com.unidev.polydata.domain.BasicPoly;
import com.unidev.polydata.insights.model.HateoasResponse;
import com.unidev.polydata.insights.model.InsightQuery;
import com.unidev.polydata.insights.model.InsightRequest;
import com.unidev.polydata.insights.service.PolyInsights;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API controller
 */
@RestController
@RequestMapping("/api")
public class APIController {

    @Autowired
    private PolyInsights polyInsights;

    @Autowired
    private WebUtils webUtils;

    @PostMapping("insight")
    public void insight(@RequestBody InsightRequest insightRequest,
        HttpServletRequest httpServletRequest) {
        String clientIp = webUtils.getClientIp(httpServletRequest);
        String clientId = httpServletRequest.getHeader("ClientId");
        BasicPoly basicPoly = new BasicPoly();
        basicPoly.put("clientId", clientId);
        polyInsights.logInsight(insightRequest, clientIp + ":" + clientId, basicPoly);
    }

    @PostMapping("insight/value/sum")
    public HateoasResponse listTopKeysByValueSum(@RequestBody InsightQuery insightQuery) {
        return HateoasResponse.hateoasResponse(polyInsights.listTopKeysByValueSum(insightQuery));
    }

    @PostMapping("insight/value/average")
    public HateoasResponse listTopKeysByAverageValue(@RequestBody InsightQuery insightQuery) {
        return HateoasResponse
            .hateoasResponse(polyInsights.listTopKeysByAverageValue(insightQuery));
    }

    @PostMapping("insight/key")
    public HateoasResponse fetchInsightStatsByKey(@RequestBody InsightQuery insightQuery) {
        return HateoasResponse.hateoasResponse(polyInsights.fetchInsightStatsByKey(insightQuery));
    }

}
