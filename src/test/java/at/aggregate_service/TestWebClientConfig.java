package at.aggregate_service;

import at.aggregate_service.props.ExternalServiceProps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@TestConfiguration
public class TestWebClientConfig {

    @Autowired
    private ExternalServiceProps props;

    @Bean
    public WebClient courseWebClient(WebClient.Builder builder) {
        return builder.baseUrl(props.getCourseServiceUrl()).build();
    }

    @Bean
    public WebClient feedbacksWebClient(WebClient.Builder builder) {
        return builder.baseUrl(props.getFeedbackServiceUrl()).build();
    }
}
