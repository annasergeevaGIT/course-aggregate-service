package at.aggregate_service.client;

import at.aggregate_service.props.ExternalServiceProps;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

/*
Since a reactive technology stack is used, we need to ensure that there are no blocking operations in the threads handling incoming requests.
Achieving this by using a non-blocking WebClient to communicate with external systems. To make WebClient available as a bean in the Spring context,
declare WebClient.Builder in a configuration class.
 */
@RequiredArgsConstructor
@Configuration
@Profile("!test")
public class WebClientConfig { //avoid blocking request threads using non-blocking WebClient for external calls

    private final ExternalServiceProps props;
    private final ReactorLoadBalancerExchangeFilterFunction lbFunction;

    @Bean
    public WebClient courseWebClient(WebClient.Builder loadBalancedBuilder) {
        return loadBalancedBuilder
                .filter(lbFunction)
                .baseUrl(props.getCourseServiceUrl())
                .build();
    }

    @Bean
    public WebClient feedbacksWebClient(WebClient.Builder loadBalancedBuilder) {
        return loadBalancedBuilder
                .filter(lbFunction)
                .baseUrl(props.getFeedbackServiceUrl())
                .build();
    }
}
