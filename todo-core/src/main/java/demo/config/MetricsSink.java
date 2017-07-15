package demo.config;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface MetricsSink {
    String INPUT = "metrics";

    @Input(INPUT)
    SubscribableChannel input();
}
