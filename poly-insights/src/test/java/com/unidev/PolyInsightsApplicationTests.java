package com.unidev;

import com.unidev.polyinsights.Application;
import com.unidev.polyinsights.model.*;
import com.unidev.polyinsights.service.InsightNotAccepted;
import com.unidev.polyinsights.service.PolyInsights;
import com.unidev.polyinsights.service.TenantDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, PolyInsightsApplicationTests.class})
@SpringBootConfiguration
public class PolyInsightsApplicationTests {

	@Autowired
	private TenantDAO tenantDAO;

	@Autowired
	private PolyInsights polyInsights;

	@Test
	public void contextLoads() {

	}

	@Test
	public void tenantCrud() {

		Tenant tenant = new Tenant();
		tenant.setTenant("potato");

		InsightType insightType = new InsightType();
		insightType.setInterval(1000);
		insightType.setName("Test");
		insightType.setValues(new HashSet<>(Arrays.asList("1", "2", "3")));
		tenant.addType(insightType);

		tenantDAO.save(tenant);

		Tenant dbTenant = tenantDAO.findOne("potato");
		assertThat(dbTenant, is(notNullValue()));
		assertThat(dbTenant.getTenant(), is("potato"));
		assertThat(dbTenant.getTypes(), is(notNullValue()));
		assertThat(dbTenant.getTypes().size(), is(1));
		assertThat(dbTenant.getTypes().get("Test"), is(notNullValue()));
		assertThat(dbTenant.getTypes().get("Test").getName(), is("Test"));

	}

	@Test
	public void insightPersist() {

		Tenant tenant = new Tenant();
		tenant.setTenant("test_tenant");

		InsightType insightType = new InsightType();
		insightType.setInterval(1000);
		insightType.setName("test_insight_type");
		insightType.setValues(new HashSet<>(Arrays.asList("1", "2", "3")));
		tenant.addType(insightType);

		tenantDAO.save(tenant);


		InsightRequest insightRequest = new InsightRequest();
		insightRequest.setTenant("test_tenant");
		insightRequest.setKey("test_insight");
		insightRequest.setType("test_insight_type");
		insightRequest.setValue("2");

		Insight insight = polyInsights.logInsight(insightRequest, "potato", new HashMap());

		assertThat(insight, is(notNullValue()));
		assertThat(insight.getKey(), is("test_insight"));
		assertThat(insight.getValue(), is(2L));

	}


	@Test
	public void insightTimeInterval() {
		Tenant tenant = new Tenant();
		tenant.setTenant("test_tenant");

		InsightType insightType = new InsightType();
		insightType.setInterval(1000 * 60);
		insightType.setSameInsightInterval(1000 * 60);
		insightType.setName("test_insight");
		insightType.setValues(new HashSet<>(Arrays.asList("1", "2", "3")));
		tenant.addType(insightType);

		tenantDAO.save(tenant);

		String clientId = "client_id_" + System.currentTimeMillis();

		InsightRequest insightRequest = new InsightRequest();
		insightRequest.setTenant("test_tenant");
		insightRequest.setType("test_insight");
		insightRequest.setKey("test_insight");
		insightRequest.setValue("2");

		polyInsights.logInsight(insightRequest, clientId, new HashMap());

		InsightRequest insightRequest2 = new InsightRequest();
		insightRequest2.setTenant("test_tenant");
		insightRequest2.setType("test_insight");
		insightRequest2.setKey("test_insight2");
		insightRequest2.setValue("1");

		try {
			polyInsights.logInsight(insightRequest2, clientId, new HashMap());
			fail();
		} catch (InsightNotAccepted e) {
			assertThat(e.getMessage(), is("Logging insight too often, global limit"));
		}
	}

	@Test
	public void sameInsightTimeout() {
		Tenant tenant = new Tenant();
		tenant.setTenant("test_tenant");

		InsightType insightType = new InsightType();
		insightType.setInterval(0);
		insightType.setSameInsightInterval(1000 * 60);
		insightType.setName("test_insight");
		insightType.setValues(new HashSet<>(Arrays.asList("1", "2", "3")));
		tenant.addType(insightType);

		tenantDAO.save(tenant);

		String clientId = "client_id_" + System.currentTimeMillis();

		InsightRequest insightRequest = new InsightRequest();
		insightRequest.setTenant("test_tenant");
		insightRequest.setType("test_insight");
		insightRequest.setKey("test_insight");
		insightRequest.setValue("2");

		polyInsights.logInsight(insightRequest, clientId, new HashMap());

		InsightRequest insightRequest2 = new InsightRequest();
		insightRequest2.setTenant("test_tenant");
		insightRequest2.setType("test_insight");
		insightRequest2.setKey("test_insight");
		insightRequest2.setValue("1");

		try {
			polyInsights.logInsight(insightRequest2, clientId, new HashMap());
			fail();
		} catch (InsightNotAccepted e) {
			assertThat(e.getMessage(), is("Logging insight too often, item limit"));
		}



	}

	@Test
	public void listTopKeysByValueSum() {
		InsightQuery insightQuery = new InsightQuery();
		insightQuery.setTenant("test_tenant");
		insightQuery.setInsight("test_insight_type");
		insightQuery.setInterval(TimeInterval.MONTH);
		polyInsights.listTopKeysByValueSum(insightQuery);
	}

	@Test
	public void listTopKeysByAverageValue() {
		InsightQuery insightQuery = new InsightQuery();
		insightQuery.setTenant("test_tenant");
		insightQuery.setInsight("test_insight_type");
		insightQuery.setInterval(TimeInterval.MONTH);
		polyInsights.listTopKeysByAverageValue(insightQuery);
	}

	@Test
	public void fetchInsightStatsByKey() {
		InsightQuery insightQuery = new InsightQuery();
		insightQuery.setTenant("test_tenant");
		insightQuery.setInsight("test_insight_type");
		insightQuery.setKey("test_insight2");
		insightQuery.setInterval(TimeInterval.MONTH);
		polyInsights.fetchInsightStatsByKey(insightQuery);
	}

}
