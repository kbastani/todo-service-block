package amazon.aws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class auto-configures a {@link LambdaFunctionInvoker} bean.
 *
 * @author kbastani
 */
@Configuration
@EnableConfigurationProperties(AmazonProperties.class)
@ConditionalOnProperty(name="amazon.aws.functions.enabled", havingValue = "true")
public class AmazonAutoConfiguration {

    @Autowired
    private AmazonProperties amazonProperties;

    @Bean
    public LambdaFunctionInvoker lambdaAdapter() {
        return new LambdaFunctionInvoker(amazonProperties);
    }
}
