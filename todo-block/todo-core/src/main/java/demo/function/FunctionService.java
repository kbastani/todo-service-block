package demo.function;

import com.amazonaws.services.lambda.invoke.LambdaFunction;
import com.amazonaws.services.lambda.model.LogType;

import java.util.Map;

public interface FunctionService {
    @LambdaFunction(functionName="metrics-function", logType = LogType.Tail)
    Map<String, Object> metricsFunction(Map event);
}
