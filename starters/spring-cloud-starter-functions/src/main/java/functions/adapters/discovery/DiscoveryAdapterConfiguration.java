package functions.adapters.discovery;

import functions.adapters.FunctionInvoker;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties
@ConditionalOnClass(SimpleDiscoveryClient.class)
@ConditionalOnProperty(prefix = "spring.cloud.function", name = "enabled", matchIfMissing = true)
public class DiscoveryAdapterConfiguration {

	@Bean
	@LoadBalanced
	@ConditionalOnMissingBean(RestTemplate.class)
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	@ConditionalOnProperty(prefix = "spring.cloud.function", name = "adapter", havingValue = "discovery_client")
	public FunctionInvoker functionInvoker(ApplicationContext applicationContext, RestTemplate restTemplate) {
		return new DiscoveryFunctionInvoker(applicationContext, restTemplate);
	}
}
