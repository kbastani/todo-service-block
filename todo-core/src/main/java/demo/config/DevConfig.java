package demo.config;

import demo.function.FunctionService;
import demo.view.MetricView;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.web.client.RestTemplate;

/**
 * Configures the application for a local development environment. Implements the {@link FunctionService} using
 * a discovery service for function invocations instead of using handlers on AWS Lambda.
 *
 * @author Kenny Bastani
 */
@Configuration
@Profile("development")
public class DevConfig {

    @Bean
    @LoadBalanced
    protected RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public FunctionService discoveryFunctionService(RestTemplate restTemplate) {
        return new DiscoveryFunctionService(restTemplate);
    }

    @Bean
    CommandLineRunner commandLineRunner(MongoOperations operations) {
        return (args) -> {
            // Setup the streaming data endpoint
//            if (operations.collectionExists("metrics")) {
//                operations.dropCollection("metrics");
//            }

//            CollectionOptions options = new CollectionOptions(Integer.MAX_VALUE, Integer.MAX_VALUE, true);
//            operations.createCollection("metrics", options);
        };
    }

    private static class DiscoveryFunctionService implements FunctionService {
        private RestTemplate restTemplate;

        public DiscoveryFunctionService(RestTemplate restTemplate) {
            this.restTemplate = restTemplate;
        }

        @Override
        public MetricView metricsFunction(Metric event) {
            return restTemplate.postForEntity("http://metrics-function/function", event, MetricView.class).getBody();
        }
    }
}
