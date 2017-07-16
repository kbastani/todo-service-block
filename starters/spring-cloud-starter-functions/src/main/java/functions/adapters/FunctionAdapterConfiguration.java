package functions.adapters;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration classes for registering Spring Cloud Function adapters.
 */
@Configuration
@EnableConfigurationProperties(FunctionProperties.class)
@AutoConfigureAfter(FunctionInvoker.class)
public class FunctionAdapterConfiguration {

	private FunctionProperties functionProperties;

	public FunctionAdapterConfiguration(FunctionProperties functionProperties) {
		this.functionProperties = functionProperties;
	}
}
