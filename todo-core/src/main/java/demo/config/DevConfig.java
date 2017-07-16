package demo.config;

import demo.function.FunctionService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;

/**
 * Configures the application for a local development environment. Implements the {@link FunctionService} using
 * a discovery service for function invocations instead of using handlers on AWS Lambda.
 *
 * @author Kenny Bastani
 */
@Configuration
@Profile(value = {"development", "!cloud"})
public class DevConfig {

	@Bean
	@ConditionalOnProperty(name = "spring.datasource.initialize", havingValue = "true")
	protected CommandLineRunner commandLineRunner(MongoOperations operations) {
		return (args) -> {
			// Setup the streaming data endpoint
			if (operations.collectionExists("metrics")) {
				operations.dropCollection("metrics");
			}

			CollectionOptions options = new CollectionOptions(Integer.MAX_VALUE, Integer.MAX_VALUE, true);
			operations.createCollection("metrics", options);
		};
	}
}
