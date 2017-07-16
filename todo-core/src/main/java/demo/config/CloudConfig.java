package demo.config;

import demo.function.FunctionService;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDbFactory;

import javax.sql.DataSource;

/**
 * Configures the application for a cloud environment. The {@link FunctionService} will be registered to
 * invoke Spring Cloud Function apps deployed to AWS Lambda.
 *
 * @author Kenny Bastani
 */
@Configuration
@Profile(value = {"!development", "cloud"})
public class CloudConfig extends AbstractCloudConfig {

    @Bean
    public MongoDbFactory mongoFactory() {
        return connectionFactory().mongoDbFactory();
    }

    @Bean
    public ConnectionFactory rabbitFactory() {
        return connectionFactory().rabbitConnectionFactory();
    }

    @Bean
    public DataSource dataSource() {
        return connectionFactory().dataSource();
    }
}
