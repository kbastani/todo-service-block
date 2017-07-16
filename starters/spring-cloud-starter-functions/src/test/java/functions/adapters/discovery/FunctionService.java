package functions.adapters.discovery;

import com.amazonaws.services.lambda.invoke.LambdaFunction;
import functions.adapters.FunctionRegistry;

public interface FunctionService extends FunctionRegistry {
	@LambdaFunction(functionName = "test-function")
	String testInvoke(String foo);
}
