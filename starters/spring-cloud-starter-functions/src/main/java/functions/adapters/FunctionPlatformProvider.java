package functions.adapters;

import org.springframework.cloud.client.discovery.DiscoveryClient;

/**
 * A {@link FunctionPlatformProvider} describes the type of adapter that should be used to lookup and invoke
 * functional services.
 *
 * @author Kenny Bastani
 */
public enum FunctionPlatformProvider {
	/**
	 * Targets a configured {@link DiscoveryClient} to lookup and invoke {@link FunctionRegistry} methods.
	 */
	DISCOVERY_CLIENT,

	/**
	 * Targets AWS Lambda functions to lookup and invoke {@link FunctionRegistry} methods.
	 */
	AWS_LAMBDA
}
