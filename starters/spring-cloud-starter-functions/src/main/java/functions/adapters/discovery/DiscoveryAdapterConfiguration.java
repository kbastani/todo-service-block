package functions.adapters.discovery;

import functions.adapters.FunctionInvoker;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Adapter auto-configuration for invoking functions that are registered with a discovery service.
 *
 * @author Kenny Bastani
 */
@Configuration
@EnableConfigurationProperties
@ConditionalOnClass(SimpleDiscoveryClient.class)
@ConditionalOnProperty(prefix = "spring.cloud.function", name = "enabled", matchIfMissing = true)
public class DiscoveryAdapterConfiguration {

	/**
	 * Auto-configures a {@link LoadBalanced} {@link RestTemplate} if one is not already registered as
	 * a bean.
	 *
	 * @return a load balanced {@link RestTemplate}
	 */
	@Bean
	@LoadBalanced
	@ConditionalOnMissingBean(RestTemplate.class)
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	/**
	 * Creates a {@link DiscoveryFunctionInvoker} bean that is used to lookup and invoke functional services using
	 * a configured {@link DiscoveryClient}.
	 *
	 * @param restTemplate is a load balanced {@link RestTemplate}
	 * @return a new instance of {@link FunctionInvoker}
	 */
	@Bean
	@ConditionalOnProperty(prefix = "spring.cloud.function", name = "adapter", havingValue = "discovery_client")
	public FunctionInvoker functionInvoker(RestTemplate restTemplate) {
		return new DiscoveryFunctionInvoker(restTemplate);
	}
}
