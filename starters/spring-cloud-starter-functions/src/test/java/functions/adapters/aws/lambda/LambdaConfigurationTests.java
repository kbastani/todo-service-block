package functions.adapters.aws.lambda;

import functions.adapters.FunctionInvoker;
import functions.adapters.discovery.DiscoveryAdapterConfiguration;
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
import org.springframework.web.client.RestTemplate;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;

public class LambdaConfigurationTests {

	private AnnotationConfigApplicationContext context;

	@After
	public void tearDown() {
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	public void amazonLambdaFunctionInvokerEnabled() {
		load(EmptyConfiguration.class,
			"amazon.aws.access-key-id=AJGLDLSXKDFLS",
			"amazon.aws.access-key-secret=XSDFSDFLKKHASDFJALASDF",
			"spring.cloud.function.adapter=aws_lambda");

		FunctionInvoker actual = null;

		try {
			actual = this.context.getBean(LambdaFunctionInvoker.class);
		} catch (NoSuchBeanDefinitionException ignored) {
		}

		assertNotNull(actual);
	}

	@Test
	public void amazonLambdaFunctionInvokerDisabled() {
		load(EmptyConfiguration.class,
			"spring.cloud.function.enabled=false");

		FunctionInvoker actual = null;

		try {
			actual = this.context.getBean(LambdaFunctionInvoker.class);
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
