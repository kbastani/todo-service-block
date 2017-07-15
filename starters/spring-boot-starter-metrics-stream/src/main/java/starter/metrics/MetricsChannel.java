package starter.metrics;


import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface MetricsChannel {
    @Output("metricsChannel")
    MessageChannel output();
}
