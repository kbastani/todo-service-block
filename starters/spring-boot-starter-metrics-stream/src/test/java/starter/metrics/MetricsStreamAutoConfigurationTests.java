package starter.metrics;

import org.junit.After;
import org.junit.Test;
import org.springframework.boot.actuate.autoconfigure.EndpointAutoConfiguration;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageChannel;

import java.util.stream.Stream;

import static org.junit.Assert.assertNotNull;

public class MetricsStreamAutoConfigurationTests {

    private AnnotationConfigApplicationContext context;

    @After
    public void tearDown() {
        if (this.context != null) {
            this.context.close();
        }
    }

    @Test
    public void testMetricsAutoConfigRegistered() {
        load(EmptyConfiguration.class, EndpointAutoConfiguration.class, MetricsStreamAutoConfig.class);
        MessageChannel messageChannel = this.context.getBean("metricsChannel", MessageChannel.class);
        assertNotNull(messageChannel);
    }

    @Test
    public void testMetricsAutoConfigNotRegistered() {
        load(EmptyConfiguration.class, MetricsStreamAutoConfig.class);
        MessageChannel messageChannel = this.context.getBean("metricsChannel", MessageChannel.class);
        assertNotNull(messageChannel);
    }

    @Configuration
    static class EmptyConfiguration {
    }

    private void load(Class<?>... config) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        EnvironmentTestUtils.addEnvironment(applicationContext);
        Stream.of(config).forEach(applicationContext::register);
        applicationContext.refresh();
        this.context = applicationContext;
    }
}
