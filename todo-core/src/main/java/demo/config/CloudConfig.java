package demo.config;

import amazon.aws.AWSLambdaConfigurerAdapter;
import com.amazonaws.services.lambda.AWSLambda;
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
@Profile({"cloud"})
public class CloudConfig extends AbstractCloudConfig {

    @Bean
    public FunctionInvoker lambdaInvoker(AWSLambdaConfigurerAdapter configurerAdapter) {
        FunctionService functionService = configurerAdapter.getFunctionInstance(FunctionService.class);
        return new FunctionInvoker(functionService, configurerAdapter.getLambdaClient());
    }

    public static class FunctionInvoker {
        FunctionInvoker(FunctionService functionService, AWSLambda awsLambda) {
            this.functionService = functionService;
            this.awsLambda = awsLambda;
        }

        private final FunctionService functionService;
        private final AWSLambda awsLambda;

        public FunctionService getFunctionService() {
            return functionService;
        }

        public AWSLambda getAwsLambda() {
            return awsLambda;
        }
    }

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
