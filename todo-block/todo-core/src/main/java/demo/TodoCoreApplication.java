package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableHypermediaSupport(type = {HypermediaType.HAL})
public class TodoCoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(TodoCoreApplication.class, args);
    }

    @RestController
    @RequestMapping("/")
    class RootController {

        @GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
        public String rootEndpoint() {
            return "ok";
        }
    }
}