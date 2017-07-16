package functions.adapters.aws.lambda;

import functions.adapters.FunctionInvoker;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class auto-configures a {@link LambdaFunctionInvoker} bean.
 *
 * @author kbastani
 */
@Configuration
@ConditionalOnProperty(prefix = "amazon", name = {"aws.access-key-id", "aws.access-key-secret"})
@EnableConfigurationProperties(value = AwsProperties.class)
public class LambdaAdapterConfiguration {

	private AwsProperties amazonProperties;

	public LambdaAdapterConfiguration(AwsProperties amazonProperties) {
		this.amazonProperties = amazonProperties;
	}

	@Bean
	@ConditionalOnProperty(prefix = "spring.cloud.function", name = "adapter", havingValue = "aws_lambda")
	public FunctionInvoker functionInvoker() {
		return new LambdaFunctionInvoker(amazonProperties);
	}
}
