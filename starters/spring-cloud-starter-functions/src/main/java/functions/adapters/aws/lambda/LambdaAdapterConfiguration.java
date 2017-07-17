package functions.adapters.aws.lambda;

import functions.adapters.FunctionInvoker;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Adapter auto-configuration for invoking AWS Lambda functions.
 *
 * @author Kenny Bastani
 */
@Configuration
@ConditionalOnProperty(prefix = "amazon", name = {"aws.access-key-id", "aws.access-key-secret"})
@EnableConfigurationProperties(value = AwsProperties.class)
public class LambdaAdapterConfiguration {

	private AwsProperties amazonProperties;

	/**
	 * Creates a new {@link LambdaAdapterConfiguration}.
	 *
	 * @param amazonProperties is the configuration properties for AWS Lambda
	 */
	public LambdaAdapterConfiguration(AwsProperties amazonProperties) {
		this.amazonProperties = amazonProperties;
	}

	/**
	 * Registers a {@link LambdaFunctionInvoker} bean that is used to lookup and invoke functional services
	 * on AWS Lambda.
	 *
	 * @return a new instance of {@link FunctionInvoker}
	 */
	@Bean
	@ConditionalOnProperty(prefix = "spring.cloud.function", name = "adapter", havingValue = "aws_lambda")
	public FunctionInvoker functionInvoker() {
		return new LambdaFunctionInvoker(amazonProperties);
	}
}
