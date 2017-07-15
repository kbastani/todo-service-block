package demo.functions.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("development")
@EnableDiscoveryClient
@Configuration
public class FunctionConfig {
}
