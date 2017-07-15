package amazon.aws;

import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.invoke.LambdaFunction;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides a configurer for invoking remote AWS Lambda functions using {@link LambdaInvokerFactory}.
 * This component also manages the authenticated session for an IAM user that has provided valid
 * access keys to access AWS resources.
 *
 * @author kbastani
 */
@Component
public class LambdaFunctionInvoker implements FunctionInvoker {

	private final AmazonProperties amazonProperties;
	private final Map<String, Object> functionServiceContainer = new HashMap<String, Object>();

	/**
	 * Create a new instance of the {@link LambdaFunctionInvoker} with the bucket name and access credentials
	 */
	public LambdaFunctionInvoker(AmazonProperties amazonProperties) {
		this.amazonProperties = amazonProperties;
	}

	public <T> T getFunctionService(Class<T> functionService) {
		if (!functionServiceContainer.containsKey(functionService.getName()))
			functionServiceContainer.put(functionService.getName(), getFunctionInstance(functionService));

		return (T) functionServiceContainer.get(functionService.getName());
	}

	/**
	 * Creates a proxy instance of a supplied interface that contains methods annotated with
	 * {@link LambdaFunction}. Provides automatic credential support to authenticate with an IAM
	 * access keys using {@link BasicSessionCredentials} auto-configured from Spring Boot
	 * configuration properties in {@link AmazonProperties}.
	 *
	 * @param type
	 * @param <T>
	 * @return
	 */
	private <T> T getFunctionInstance(Class<T> type) {
		return LambdaInvokerFactory.builder()
			.lambdaClient(AWSLambdaClientBuilder.standard()
				.withRegion(Regions.US_EAST_1)
				.withCredentials(new LambdaCredentialsProvider(amazonProperties))
				.build())
			.build(type);
	}

	public AWSLambda getLambdaClient() {
		return AWSLambdaClientBuilder.standard()
			.withRegion(Regions.US_EAST_1)
			.withCredentials(new LambdaCredentialsProvider(amazonProperties))
			.build();
	}
}
