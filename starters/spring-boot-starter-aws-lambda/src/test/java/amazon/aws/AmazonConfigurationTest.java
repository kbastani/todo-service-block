package amazon.aws;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;

public class AmazonConfigurationTest {

	private AnnotationConfigApplicationContext context;

	@After
	public void tearDown() {
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	public void enabledInvoker() {
		load(EmptyConfiguration.class,
			"amazon.aws.access-key-id=AJGLDLSXKDFLS",
			"amazon.aws.access-key-secret=XSDFSDFLKKHASDFJALASDF",
			"amazon.aws.functions.enabled=true");

		FunctionInvoker actual = null;

		try {
			actual = this.context.getBean(LambdaFunctionInvoker.class);
		} catch (NoSuchBeanDefinitionException ignored) {
		}

		assertNotNull(actual);
	}

	@Test
	public void disabledInvoker() {
		load(EmptyConfiguration.class,
			"amazon.aws.access-key-id=AJGLDLSXKDFLS",
			"amazon.aws.access-key-secret=XSDFSDFLKKHASDFJALASDF",
			"amazon.aws.functions.enabled=false");

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
		applicationContext.register(AmazonAutoConfiguration.class);
		applicationContext.refresh();
		this.context = applicationContext;
	}
}
