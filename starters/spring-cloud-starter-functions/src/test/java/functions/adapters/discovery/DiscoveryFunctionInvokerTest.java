package functions.adapters.discovery;

import functions.adapters.FunctionInvoker;
import functions.adapters.aws.lambda.LambdaAdapterConfiguration;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.cloud.client.discovery.composite.CompositeDiscoveryClientAutoConfiguration;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryClientAutoConfiguration;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.commons.util.InetUtilsProperties;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.web.client.RestTemplate;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class DiscoveryFunctionInvokerTest {
	private AnnotationConfigApplicationContext context;

	@After
	public void tearDown() {
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	public void discoveryFunctionInvokerEnabled() {
		load(EmptyConfiguration.class,
			"spring.cloud.function.adapter=discovery_client");

		FunctionInvoker actual = null;

		try {
			actual = this.context.getBean(FunctionInvoker.class);
		} catch (NoSuchBeanDefinitionException ignored) {
		}

		assertNotNull(actual);

		FunctionService functionService = actual.getFunctionService(FunctionService.class);

		MockRestServiceServer server = MockRestServiceServer.bindTo(this.context.getBean(RestTemplate.class))
			.build();

		server.expect(ExpectedCount.once(), requestTo("http://test-function/function"))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
			.andRespond(withSuccess("success", MediaType.TEXT_HTML));

		String result = functionService.testInvoke("test");

		server.verify();

		assertNotNull(result);
	}


	@Test
	public void discoveryFunctionInvokerDisabled() {
		load(EmptyConfiguration.class,
			"spring.cloud.function.enabled=false");

		FunctionInvoker actual = null;

		try {
			actual = this.context.getBean(FunctionInvoker.class);
		} catch (NoSuchBeanDefinitionException ignored) {
		}

		assertNull(actual);

	}

	@Configuration
	static class EmptyConfiguration {
	}

	private void load(Class<?> config, String... environment) {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
		EnvironmentTestUtils.addEnvironment(applicationContext, environment);
		applicationContext.register(config);
		applicationContext.register(InetUtilsProperties.class);
		applicationContext.register(InetUtils.class);
		applicationContext.register(LambdaAdapterConfiguration.class);
		applicationContext.register(DiscoveryAdapterConfiguration.class);
		applicationContext.register(SimpleDiscoveryClientAutoConfiguration.class);
		applicationContext.register(CompositeDiscoveryClientAutoConfiguration.class);
		applicationContext.register(RestTemplate.class);
		applicationContext.refresh();
		this.context = applicationContext;
	}
}