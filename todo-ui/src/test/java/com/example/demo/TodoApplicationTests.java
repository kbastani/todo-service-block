package com.example.demo;

import org.junit.Test;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

public class TodoApplicationTests {

	private AnnotationConfigApplicationContext context;

	@Test
	public void contextLoads() {
		load(EmptyConfiguration.class, "amazon.aws.functions.enabled=false");
	}

	@Configuration
	static class EmptyConfiguration {
	}

	private void load(Class<?> config, String... environment) {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
		EnvironmentTestUtils.addEnvironment(applicationContext, environment);
		applicationContext.register(config);
		applicationContext.refresh();
		this.context = applicationContext;
	}

}
