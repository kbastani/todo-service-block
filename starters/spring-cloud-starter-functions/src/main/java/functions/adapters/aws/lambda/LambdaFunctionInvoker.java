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
 * A {@link FunctionInvoker} for invoking remote AWS Lambda functions using {@link LambdaInvokerFactory}.
 * This component also manages the authenticated session for an IAM user that has provided valid
 * access keys to access AWS resources.
 *
 * @author Kenny Bastani
 */
@Component
public class LambdaFunctionInvoker extends FunctionInvoker {

	private final AwsProperties amazonProperties;

	/**
	 * Create a new instance of the {@link LambdaFunctionInvoker} with the bucket name and access credentials
	 */
	LambdaFunctionInvoker(AwsProperties amazonProperties) {
		this.amazonProperties = amazonProperties;
	}

	/**
	 * Creates a proxy instance of a supplied interface that contains methods annotated with
	 * {@link LambdaFunction}. Provides automatic credential support to authenticate with an IAM
	 * access keys using {@link BasicSessionCredentials} auto-configured from Spring Boot
	 * configuration properties in {@link AwsProperties}.
	 *
	 * @param registry is a {@link FunctionRegistry} that describes functional service invocations
	 * @param <T>      is the type of {@link FunctionRegistry} to retrieve
	 * @return an instance of a {@link FunctionRegistry} that is used to lookup and invoke functions on a
	 * target platform
	 */
	@Override
	protected <T extends FunctionRegistry> T getRegistryInstance(Class<T> registry) {
		return LambdaInvokerFactory.builder()
			.lambdaClient(AWSLambdaClientBuilder.standard()
				.withRegion(Regions.US_EAST_1)
				.withCredentials(new LambdaCredentialsProvider(amazonProperties))
				.build())
			.build(registry);
	}
}
