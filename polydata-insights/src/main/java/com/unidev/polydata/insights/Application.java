package com.unidev.polydata.insights;

import com.unidev.platform.j2ee.common.WebUtils;
import java.util.concurrent.Executor;
import org.jminix.console.servlet.MiniConsoleServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@SpringBootApplication
@EnableDiscoveryClient
@EnableMongoRepositories
@EnableAsync(mode = AdviceMode.PROXY)
@EnableSwagger2
@Configuration
@ComponentScan("com.unidev.polydata.insights")
public class Application implements ServletContextInitializer {

	public static final String VERSION = "0.0.1";

	@Value("${update.threads:5}")
	private int updateThreadCount;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public WebUtils webUtils() {
		return new WebUtils();
	}

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.addServlet("JmxMiniConsoleServlet", MiniConsoleServlet.class).addMapping("/jmx/*");
    }

	@Bean(name="taskExecutor")
	public Executor asyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(updateThreadCount);
		executor.setMaxPoolSize(updateThreadCount * 2);
		executor.setQueueCapacity(updateThreadCount * 10);
		executor.setThreadNamePrefix("async-executor-");
		executor.initialize();
		return executor;
	}

	@Bean
	public Docket apiDocs() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build();
	}

}

