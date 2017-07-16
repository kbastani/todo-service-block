package functions.adapters.aws.lambda;

import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.invoke.LambdaFunction;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import functions.adapters.FunctionInvoker;
import functions.adapters.FunctionRegistry;
import org.springframework.stereotype.Component;

/**
 * Provides a configurer for invoking remote AWS Lambda functions using {@link LambdaInvokerFactory}.
 * This component also manages the authenticated session for an IAM user that has provided valid
 * access keys to access AWS resources.
 *
 * @author kbastani
 */
@Component
public class LambdaFunctionInvoker extends FunctionInvoker {

	private final AwsProperties amazonProperties;

	/**
	 * Create a new instance of the {@link LambdaFunctionInvoker} with the bucket name and access credentials
	 */
	public LambdaFunctionInvoker(AwsProperties amazonProperties) {
		this.amazonProperties = amazonProperties;
	}

	/**
	 * Creates a proxy instance of a supplied interface that contains methods annotated with
	 * {@link LambdaFunction}. Provides automatic credential support to authenticate with an IAM
	 * access keys using {@link BasicSessionCredentials} auto-configured from Spring Boot
	 * configuration properties in {@link AwsProperties}.
	 *
	 * @param type
	 * @param <T>
	 * @return
	 */
	@Override
	protected <T extends FunctionRegistry> T getRegistryInstance(Class<T> type) {
		return LambdaInvokerFactory.builder()
			.lambdaClient(AWSLambdaClientBuilder.standard()
				.withRegion(Regions.US_EAST_1)
				.withCredentials(new LambdaCredentialsProvider(amazonProperties))
				.build())
			.build(type);
	}
}
