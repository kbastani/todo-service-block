package demo.functions;

import demo.functions.config.MetricViewRepository;
import demo.functions.event.MetricEvent;
import demo.functions.view.MetricView;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

@SpringBootApplication
public class MetricsFunction {

    private final Logger log = Logger.getLogger(this.getClass());

    public static void main(String[] args) {
        SpringApplication.run(MetricsFunction.class, args);
    }

    @Bean
    @Transactional
    public Function<MetricEvent, MetricView> function(MetricViewRepository viewRepository) {
        return metricEvent -> {
            log.info(metricEvent.toString());
            MetricView metricView = new MetricView(metricEvent);
            metricView = viewRepository.save(metricView);
            return metricView;
        };
    }
}
