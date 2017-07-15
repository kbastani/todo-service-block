package demo.function;

import com.amazonaws.services.lambda.invoke.LambdaFunction;
import com.amazonaws.services.lambda.model.LogType;
import demo.view.MetricView;
import org.springframework.boot.actuate.metrics.Metric;

public interface FunctionService {
    @LambdaFunction(functionName="metrics-function", logType = LogType.Tail)
    MetricView metricsFunction(Metric event);
}
