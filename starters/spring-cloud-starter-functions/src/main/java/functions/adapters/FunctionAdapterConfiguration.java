package functions.adapters;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration class for enabling functional service interfaces to lookup and invoke functions
 * on a target platform.
 *
 * @author Kenny Bastani
 */
@Configuration
@EnableConfigurationProperties(FunctionProperties.class)
@AutoConfigureAfter(FunctionInvoker.class)
public class FunctionAdapterConfiguration {

	private FunctionProperties functionProperties;

	/**
	 * Creates a new {@link FunctionAdapterConfiguration}.
	 *
	 * @param functionProperties contains the configuration for enabling functional service interfaces
	 *                           to lookup and invoke functions on a target platform.
	 */
	public FunctionAdapterConfiguration(FunctionProperties functionProperties) {
		this.functionProperties = functionProperties;
	}
}
