package demo.functions;

import demo.functions.event.MetricEvent;
import demo.functions.view.MetricView;
import org.springframework.cloud.function.adapter.aws.SpringBootRequestHandler;

/**
 * This is the request handler that maps to the function bean in {@link MetricsFunction}
 *
 * @author Kenny Bastani
 */
public class Handler extends SpringBootRequestHandler<MetricEvent, MetricView> {
}
