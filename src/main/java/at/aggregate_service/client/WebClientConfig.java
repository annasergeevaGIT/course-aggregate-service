package at.aggregate_service.client;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/*
Since a reactive technology stack is used, we need to ensure that there are no blocking operations in the threads handling incoming requests.
Achieving this by using a non-blocking WebClient to communicate with external systems. To make WebClient available as a bean in the Spring context,
declare WebClient.Builder in a configuration class.
 */
@Configuration
public class WebClientConfig { //avoid blocking request threads using non-blocking WebClient for external calls

    @LoadBalanced
    @Bean
    public WebClient.Builder webclientBuilder() {
        return WebClient.builder();
    }
}
