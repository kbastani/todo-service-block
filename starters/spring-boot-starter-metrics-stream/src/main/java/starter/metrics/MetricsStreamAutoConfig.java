package starter.metrics;

import org.springframework.boot.actuate.autoconfigure.MetricsChannelAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configures Spring Boot Actuator to stream metrics to a default message channel.
 */
@Configuration
@ConditionalOnClass(MetricsChannelAutoConfiguration.class)
@AutoConfigureBefore(MetricsChannelAutoConfiguration.class)
@EnableBinding(MetricsChannel.class)
public class MetricsStreamAutoConfig {
}
