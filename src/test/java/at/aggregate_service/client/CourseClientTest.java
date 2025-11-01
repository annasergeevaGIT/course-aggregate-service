package at.aggregate_service.client;

import at.aggregate_service.BaseTest;
import at.aggregate_service.dto.aggregate.RatedCourseSort;
import at.aggregate_service.dto.course.Category;
import at.aggregate_service.exception.CourseAggregateException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import static at.aggregate_service.testutil.TestConstants.*;
import static at.aggregate_service.testutil.TestDateProvider.drinksCourseList;
import static at.aggregate_service.testutil.TestDateProvider.courseOneItem;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class CourseClientTest extends BaseTest {

    @Autowired
    private CourseClient client;

    @Test
    void getCoursesForCategory_returnsCorrectResponse() {
        stubForCorrectCourseListResponse();

        var mono = client.getCoursesForCategory(Category.ENGINEERING, RatedCourseSort.DATE_DESC);
        StepVerifier.create(mono)
                .expectNextMatches(response ->
                        response.equals(drinksCourseList()))
                .verifyComplete();
        wiremock.verify(1, getRequestedFor(urlEqualTo(getCourseListUrl())));
    }

    @Test
    void getCoursesForCategory_returnsCorrectResponseAfterRetriesSucceed() {
        stubForCourseListWithRetriesAndSuccess();

        var mono = client.getCoursesForCategory(Category.ENGINEERING, RatedCourseSort.DATE_DESC);
        StepVerifier.create(mono)
                .expectNextMatches(response ->
                        response.equals(drinksCourseList()))
                .verifyComplete();
        wiremock.verify(3, getRequestedFor(urlEqualTo(getCourseListUrl())));
    }

    @Test
    void getCoursesForCategory_returnsErrorOn500Error() {
        stubForCourseList500Error();

        var mono = client.getCoursesForCategory(Category.ENGINEERING, RatedCourseSort.DATE_DESC);
        StepVerifier.create(mono)
                .expectError(CourseAggregateException.class)
                .verify();
        wiremock.verify(3, getRequestedFor(urlEqualTo(getCourseListUrl())));
    }

    @Test
    void getCoursesForCategory_returnsErrorOnTimeout() {
        stubForCourseListTimeout();

        var mono = client.getCoursesForCategory(Category.ENGINEERING, RatedCourseSort.DATE_DESC);
        StepVerifier.create(mono)
                .expectError(CourseAggregateException.class)
                .verify();
        wiremock.verify(3, getRequestedFor(urlEqualTo(getCourseListUrl())));
    }

    @Test
    void getCoursesForCategory_returnsErrorAfterRetriesFailWithDifferentReasons() {
        stubForCourseListWithRetriesAndFailure();

        var mono = client.getCoursesForCategory(Category.ENGINEERING, RatedCourseSort.DATE_DESC);
        StepVerifier.create(mono)
                .expectError(CourseAggregateException.class)
                .verify();
        wiremock.verify(3, getRequestedFor(urlEqualTo(getCourseListUrl())));
    }

    @Test
    void getCoursesForCategory_opensCircuitBreakerAfterAllAttempts() {
        // circuitbreaker minimum-number-of-calls = 6 in tests
        // retry max-attempts = 3
        stubForCourseList500Error();
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(COURSE_BACKEND);
        for (int i = 1; i <= 4; i++) {
            var mono = client.getCoursesForCategory(Category.ENGINEERING, RatedCourseSort.DATE_DESC);
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
        wiremock.verify(6, getRequestedFor(urlEqualTo(getCourseListUrl())));
    }

    @Test
    void getCourse_returnsCorrectResponse() {
        stubForCorrectCourseResponse();

        var mono = client.getCourse(COURSE_ONE_ID);
        StepVerifier.create(mono)
                .expectNextMatches(response ->
                        response.equals(courseOneItem()))
                .verifyComplete();
        wiremock.verify(1, getRequestedFor(urlEqualTo(getCourseUrl())));
    }

    @Test
    void getCourse_returnsCorrectResponseAfterRetriesSucceed() {
        stubForCourseWithRetriesAndSuccess();

        var mono = client.getCourse(COURSE_ONE_ID);
        StepVerifier.create(mono)
                .expectNextMatches(response ->
                        response.equals(courseOneItem()))
                .verifyComplete();
        wiremock.verify(3, getRequestedFor(urlEqualTo(getCourseUrl())));

    }

    @Test
    void getCourse_returnsExceptionOn500Error() {
        stubForCourse500Error();

        var mono = client.getCourse(COURSE_ONE_ID);
        StepVerifier.create(mono)
                .expectError(CourseAggregateException.class)
                .verify();
        wiremock.verify(3, getRequestedFor(urlEqualTo(getCourseUrl())));
    }

    @Test
    void getCourse_returnsExceptionOnTimeout() {
        stubForCourseTimeout();

        var mono = client.getCourse(COURSE_ONE_ID);
        StepVerifier.create(mono)
                .expectError(CourseAggregateException.class)
                .verify();
        wiremock.verify(3, getRequestedFor(urlEqualTo(getCourseUrl())));
    }

    @Test
    void getCourse_returnsErrorAfterRetriesFailWithDifferentReasons() {
        stubForCourseWithRetriesAndFailure();

        var mono = client.getCourse(COURSE_ONE_ID);
        StepVerifier.create(mono)
                .expectError(CourseAggregateException.class)
                .verify();
        wiremock.verify(3, getRequestedFor(urlEqualTo(getCourseUrl())));
    }

    @Test
    void getCourse_opensCircuitBreakerAfterAllAttempts() {
        CircuitBreaker courseCircuitBreaker = circuitBreakerRegistry.circuitBreaker(COURSE_BACKEND);
        // circuitbreaker minimum-number-of-calls = 6 in tests
        // retry max-attempts = 3
        stubForCourseTimeout();
        for (int i = 1; i <= 4; i++) {
            var mono = client.getCourse(COURSE_ONE_ID);
            Class<? extends Throwable> expectError;
            if (i < 3) {
                expectError = CourseAggregateException.class;
                assertThat(courseCircuitBreaker.getState()).isEqualTo(CircuitBreaker.State.CLOSED);
            } else {
                expectError = CallNotPermittedException.class;
                assertThat(courseCircuitBreaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);
            }
            StepVerifier.create(mono)
                    .expectError(expectError)
                    .verify();
        }
        wiremock.verify(6, getRequestedFor(urlEqualTo(getCourseUrl())));
    }
}