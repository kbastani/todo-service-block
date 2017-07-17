package demo.function;

import com.amazonaws.services.lambda.invoke.LambdaFunction;
import com.amazonaws.services.lambda.model.LogType;
import demo.event.MetricEvent;
import demo.view.MetricView;
import functions.adapters.FunctionRegistry;

public interface FunctionService extends FunctionRegistry {
    @LambdaFunction(functionName="metrics-function", logType = LogType.Tail)
    MetricView metricsFunction(MetricEvent event);
}
