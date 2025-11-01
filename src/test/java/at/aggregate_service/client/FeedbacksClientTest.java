package at.aggregate_service.client;

import at.aggregate_service.BaseTest;
import at.aggregate_service.dto.feedback.FeedbackSort;
import at.aggregate_service.dto.feedback.RatedFeedbacksList;
import at.aggregate_service.dto.feedback.RatingsList;
import at.aggregate_service.exception.CourseAggregateException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static at.aggregate_service.testutil.TestConstants.COURSE_ONE_ID;
import static at.aggregate_service.testutil.TestConstants.FEEDBACK_BACKEND;
import static at.aggregate_service.testutil.TestDateProvider.*;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class FeedbacksClientTest extends BaseTest {

    @Autowired
    private FeedbacksClient client;

    @Test
    void getCourseRatings_returnsCorrectResponse() {
        stubForCorrectCourseRatingsResponse();

        StepVerifier.create(getRatingsListMono())
                .expectNextMatches(response ->
                        response.equals(ratingsList()))
                .verifyComplete();
        wiremock.verify(1, postRequestedFor(urlEqualTo(getCourseRatingsUrl())));
    }

    @Test
    void getCourseRatings_returnsCorrectResponseAfterRetries() {
        stubForCourseRatingsRetriesSuccess();

        var mono = getRatingsListMono();
        StepVerifier.create(mono)
                .expectNextMatches(response ->
                        response.equals(ratingsList()))
                .verifyComplete();
        wiremock.verify(3, postRequestedFor(urlEqualTo(getCourseRatingsUrl())));
    }

    @Test
    void getCourseRatings_returnsErrorOn500Status() {
        stubForCourseRatings500Error();
        var mono = getRatingsListMono();

        StepVerifier.create(mono)
                .expectError(CourseAggregateException.class)
                .verify();

        wiremock.verify(3, postRequestedFor(urlEqualTo(getCourseRatingsUrl())));
    }

    @Test
    void getCourseRatings_returnsErrorOnTimeout() {
        stubForCourseRatingsTimeout();

        StepVerifier.create(getRatingsListMono())
                .expectError(CourseAggregateException.class)
                .verify();
        wiremock.verify(3, postRequestedFor(urlEqualTo(getCourseRatingsUrl())));
    }

    @Test
    void getCourseRatings_returnsErrorAfterRetries() {
        stubForCourseRatingsRetriesFailure();

        var mono = getRatingsListMono();
        StepVerifier.create(mono)
                .expectError(CourseAggregateException.class)
                .verify();
        wiremock.verify(3, postRequestedFor(urlEqualTo(getCourseRatingsUrl())));
    }

    @Test
    void getCourseRatings_opensCircuitBreakerAfterAllAttempts() {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(FEEDBACK_BACKEND);
        // circuitbreaker minimum-number-of-calls = 6 in tests
        // retry max-attempts = 3
        stubForCourseRatings500Error();
        for (int i = 1; i < 5; i++) {
            var mono = getRatingsListMono();
            Class<? extends Throwable> expectError;
            if (i < 3) {
                expectError = CourseAggregateException.class;
                assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.CLOSED);
            } else {
                expectError = CallNotPermittedException.class;
                assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);
            }
            StepVerifier.create(mono)
                    .expectError(expectError)
                    .verify();
        }
        wiremock.verify(6, postRequestedFor(urlEqualTo(getCourseRatingsUrl())));
    }

    @Test
    void getFeedbacksWithCourseRating_returnsCorrectResponse() {
        stubForCorrectRatedFeedbacksList();

        StepVerifier.create(getRatedFeedbackListMono())
                .expectNextMatches(response -> {
                    return response.equals(ratedFeedbacksList());
                })
                .verifyComplete();
        wiremock.verify(1, getRequestedFor(urlEqualTo(getFeedbacksOfCourseOneUrl())));
    }

    @Test
    void getFeedbacksWithCourseRating_returnsCorrectResponseAfterRetries() {
        stubForRatedFeedbacksListRetriesSuccess();
        var expected = ratedFeedbacksList();
        var mono = getRatedFeedbackListMono();
        StepVerifier.create(mono)
                .expectNextMatches(response -> {
                    return response.equals(expected);
                })
                .verifyComplete();
        wiremock.verify(3, getRequestedFor(urlEqualTo(getFeedbacksOfCourseOneUrl())));
    }

    @Test
    void getFeedbacksWithCourseRating_returnsFallbackOn500Status() {
        stubForRatedFeedbacksList500Error();

        StepVerifier.create(getRatedFeedbackListMono())
                .expectNextMatches(response -> {
                    return response.equals(RatedFeedbacksList.fallbackResponse(COURSE_ONE_ID));
                })
                .verifyComplete();
        wiremock.verify(3, getRequestedFor(urlEqualTo(getFeedbacksOfCourseOneUrl())));
    }

    @Test
    void getFeedbacksWithCourseRating_returnsFallbackOnTimeout() {
        stubForRatedFeedbacksListTimeout();

        StepVerifier.create(getRatedFeedbackListMono())
                .expectNextMatches(response -> {
                    return response.equals(RatedFeedbacksList.fallbackResponse(COURSE_ONE_ID));
                })
                .verifyComplete();
        wiremock.verify(3, getRequestedFor(urlEqualTo(getFeedbacksOfCourseOneUrl())));
    }

    @Test
    void getFeedbacksWithCourseRating_returnsFallbackAfterRetries() {
        stubForRatedFeedbacksListRetriesFailure();

        StepVerifier.create(getRatedFeedbackListMono())
                .expectNextMatches(response -> {
                    return response.equals(RatedFeedbacksList.fallbackResponse(COURSE_ONE_ID));
                })
                .verifyComplete();
        wiremock.verify(3, getRequestedFor(urlEqualTo(getFeedbacksOfCourseOneUrl())));
    }

    @Test
    void getFeedbacksWithCourseRating_returnsDefault_and_opensCircuitBreakerAfterAllAttempts() {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(FEEDBACK_BACKEND);
        // circuitbreaker minimum-number-of-calls = 6 in tests
        // retry max-attempts = 3
        stubForRatedFeedbacksList500Error();
        for (int i = 1; i < 5; i++) {
            var mono = getRatedFeedbackListMono();
            if (i < 3) {
                assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.CLOSED);
            } else {
                assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);
            }
            StepVerifier.create(mono)
                    .expectNextMatches(ratedFeedbacksList ->
                            ratedFeedbacksList.equals(RatedFeedbacksList.fallbackResponse(COURSE_ONE_ID)))
                    .verifyComplete();

        }
        wiremock.verify(6, getRequestedFor(urlEqualTo(getFeedbacksOfCourseOneUrl())));
    }

    private Mono<RatedFeedbacksList> getRatedFeedbackListMono() {
        return client.getFeedbacksWithCourseRating(COURSE_ONE_ID, 0, 10, FeedbackSort.DATE_ASC);
    }

    private Mono<RatingsList> getRatingsListMono() {
        return client.getCourseRatings(getRatingsRequest());
    }
}