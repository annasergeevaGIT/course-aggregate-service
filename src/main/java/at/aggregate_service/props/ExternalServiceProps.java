package at.aggregate_service.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Data
@ConfigurationProperties(prefix = "external")
public class ExternalServiceProps {
    private final String courseServiceUrl;
    private final String feedbackServiceUrl;
    private final String coursePath;
    private final String courseFeedbacksPath;
    private final String courseRatingsPath;
    private final Duration defaultTimeout;
    private final Duration retryBackoff;
    private final Integer retryCount;
    private final Double retryJitter;
}
