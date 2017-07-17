package functions.adapters;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties class for describing the platform adapter for looking up and invoking functional
 * services.
 *
 * @author Kenny Bastani
 */
@Configuration
@ConfigurationProperties(prefix = "spring.cloud.function")
public class FunctionProperties {

	private Boolean enabled = true;
	private FunctionPlatformProvider adapter = FunctionPlatformProvider.DISCOVERY_CLIENT;

	/**
	 * Enables or disables auto-configuration for a functional service platform adapter.
	 *
	 * @return a flag indicating whether or not to enable functional service invocations
	 */
	public Boolean getEnabled() {
		return enabled;
	}

	/**
	 * Enables or disables auto-configuration for a functional service platform adapter.
	 *
	 * @param enabled is a flag indicating whether or not to enable functional service invocations
	 */
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Selects the target platform adapter for invoking functional services.
	 *
	 * @return the desired platform for looking up and invoking functional services
	 */
	public FunctionPlatformProvider getAdapter() {
		return adapter;
	}

	/**
	 * Selects the target platform adapter for invoking functional services.
	 *
	 * @param adapter is the desired platform for looking up and invoking functional services
	 */
	public void setAdapter(FunctionPlatformProvider adapter) {
		this.adapter = adapter;
	}
}
