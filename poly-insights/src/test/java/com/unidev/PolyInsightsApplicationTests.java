package com.unidev;

import com.unidev.polyinsights.Application;
import com.unidev.polyinsights.model.Tenant;
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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, PolyInsightsApplicationTests.class})
@SpringBootConfiguration
public class PolyInsightsApplicationTests {

	@Autowired
	private TenantDAO tenantDAO;

	@Test
	public void contextLoads() {

	}

	@Test
	public void tenantCrud() {

		Tenant tenant = new Tenant();
		tenant.setTenant("potato");

		tenantDAO.save(tenant);

		Tenant dbTenant = tenantDAO.findOne("potato");

	}

}
