package at.aggregate_service.client;

import at.aggregate_service.dto.feedback.GetRatingsRequest;
import at.aggregate_service.dto.feedback.RatedFeedbacksList;
import at.aggregate_service.dto.feedback.RatingsList;
import at.aggregate_service.dto.feedback.FeedbackSort;
import at.aggregate_service.exception.CourseAggregateException;
import at.aggregate_service.props.ExternalServiceProps;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class FeedbacksClient extends BaseClient {

    private static final String FEEDBACK_BACKEND = "feedbackBackend";

    private final WebClient webClient;

    public FeedbacksClient(@Qualifier("feedbacksWebClient") WebClient feedbacksWebClient,
                           ExternalServiceProps props) {
        super(props);
        this.webClient = feedbacksWebClient;
    }

    @CircuitBreaker(name = FEEDBACK_BACKEND, fallbackMethod = "getFeedbacksWithCourseRatingCBFallback")
    @Retry(name = FEEDBACK_BACKEND, fallbackMethod = "getFeedbacksWithCourseRatingRetryFallback")
    public Mono<RatedFeedbacksList> getFeedbacksWithCourseRating(Long courseId, int from, int size, FeedbackSort sort) {
        var mono = webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(props.getCourseFeedbacksPath())
                        .path("/{courseId}")
                        .queryParam("from", from)
                        .queryParam("size", size)
                        .queryParam("sortBy", sort)
                        .build(courseId))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        Mono.error(new CourseAggregateException("Feedback Service unavailable", HttpStatus.SERVICE_UNAVAILABLE)))
                .bodyToMono(RatedFeedbacksList.class);

        return applyTimeoutAndHandleExceptions(mono);
    }

    @CircuitBreaker(name = FEEDBACK_BACKEND)
    @Retry(name = FEEDBACK_BACKEND)
    public Mono<RatingsList> getCourseRatings(GetRatingsRequest request) {
        var mono = webClient
                .post()
                .uri(props.getCourseRatingsPath())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        Mono.error(new CourseAggregateException("Feedback Service unavailable", HttpStatus.SERVICE_UNAVAILABLE)))
                .bodyToMono(RatingsList.class);

        return applyTimeoutAndHandleExceptions(mono);
    }

    private Mono<RatedFeedbacksList> getFeedbacksWithCourseRatingCBFallback(Long courseId, int from, int size, FeedbackSort sort, CallNotPermittedException e) {
        return Mono.just(RatedFeedbacksList.fallbackResponse(courseId));
    }

    private Mono<RatedFeedbacksList> getFeedbacksWithCourseRatingRetryFallback(Long courseId, int from, int size, FeedbackSort sort, CourseAggregateException e) {
        return Mono.just(RatedFeedbacksList.fallbackResponse(courseId));
    }
}