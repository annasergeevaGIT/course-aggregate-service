package at.aggregate_service.client;

import at.aggregate_service.exception.CourseAggregate4xxException;
import at.aggregate_service.exception.CourseAggregateException;
import at.aggregate_service.props.ExternalServiceProps;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

public abstract class BaseClient {

    protected final ExternalServiceProps props;

    protected BaseClient(ExternalServiceProps props) { //Retries are managed using Resilience4j
        this.props = props;
    }

    protected <T> Mono<T> applyTimeoutAndHandleExceptions(Mono<T> mono) { //0 or 1 in reactive data stream
        return mono
                .timeout(props.getDefaultTimeout()) // time limit for connecting, sending, and receiving a response.
                .onErrorMap(this::handleThrowable); // at the end of the chain converts errors into microservice friendly exceptions - to handle it later
    }

    private Throwable handleThrowable(Throwable t) {
        return switch (t) {
            case CourseAggregateException e ->
                    e;
            case WebClientResponseException.NotFound e ->
                    new CourseAggregate4xxException(e.getMessage(), HttpStatus.NOT_FOUND);
            case WebClientResponseException.BadRequest e ->
                    new CourseAggregate4xxException(e.getMessage(), HttpStatus.BAD_REQUEST);
            default ->
                    new CourseAggregateException(t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        };

        /* From JDK 21, "Pattern Matching for Switch" (https://openjdk.org/jeps/441) is a standard feature.
           For older JDK versions (17–20), it requires enabling the preview feature with --enable-preview:

        if (t instanceof CourseAggregateException) {
            return t;
        }
        if (t instanceof WebClientResponseException.NotFound) {
            return new CourseAggregate4xxException(t.getMessage(), HttpStatus.NOT_FOUND);
        } else if (t instanceof WebClientResponseException.BadRequest) {
            return new CourseAggregate4xxException(t.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new CourseAggregateException(t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);*/
    }
}
