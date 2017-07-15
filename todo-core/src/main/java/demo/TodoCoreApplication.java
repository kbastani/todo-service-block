package demo;

import demo.config.MetricsSink;
import demo.command.TodoCommandService;
import demo.event.InstanceDetails;
import demo.event.MetricEvent;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;
import org.springframework.http.MediaType;
import org.springframework.metrics.boot.EnableMetrics;
import org.springframework.metrics.instrument.MeterRegistry;
import org.springframework.metrics.instrument.spectator.SpectatorMeterRegistry;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableHypermediaSupport(type = {HypermediaType.HAL})
@EnableMetrics
public class TodoCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoCoreApplication.class, args);
    }

    @Bean
    public MeterRegistry meterRegistry() { return new SpectatorMeterRegistry(); }

    @RestController
    @RequestMapping("/")
    class RootController {

        @GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
        public String rootEndpoint() {
            return "ok";
        }
    }

    @Service
    @Configuration
    @EnableBinding(MetricsSink.class)
    @Profile({"development", "cloud"})
    class MetricProcessor {

        private final Logger log = Logger.getLogger(this.getClass());
        private final TodoCommandService eventService;
        private final EurekaInstanceConfigBean instanceConfig;

        @Value("${version}")
        private String version;

        @Value("${spring.application.name}")
        private String appId;

        public MetricProcessor(TodoCommandService eventService, EurekaInstanceConfigBean instanceConfig) {
            this.eventService = eventService;
            this.instanceConfig = instanceConfig;
        }

        @StreamListener(value = MetricsSink.INPUT)
        public void metrics(Metric<Number> metric) {
            InstanceDetails instanceDetails =
                    new InstanceDetails(instanceConfig.getMetadataMap().get("instanceId"), appId, version);
            eventService.applyMetricEvent(new MetricEvent<>(metric, instanceDetails));
        }
    }
}