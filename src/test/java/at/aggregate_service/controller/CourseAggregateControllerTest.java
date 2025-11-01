package at.aggregate_service.controller;

import at.aggregate_service.BaseTest;
import at.aggregate_service.dto.aggregate.CourseAggregate;
import at.aggregate_service.dto.aggregate.CourseAggregateList;
import at.aggregate_service.dto.aggregate.RatedCourse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Comparator;

import static at.aggregate_service.testutil.TestConstants.*;
import static at.aggregate_service.testutil.TestDateProvider.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@AutoConfigureWebTestClient(timeout = "20000")
class CourseAggregateControllerTest extends BaseTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void getCourseAggregateInfo_returnsCorrectResponse() {
        stubForCorrectCourseResponse();
        stubForCorrectRatedFeedbacksList();

        webTestClient.get()
                .uri(BASE_URL + "/" + COURSE_ONE_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CourseAggregate.class)
                .value(response -> {
                    assertThat(response).isEqualTo(expectedCourseAggregate());
                });
    }

    @Test
    void getCourseAggregateInfo_returnsNotFoundWhenCourseItemNotFound() {
        stubForCourseNotFound();
        stubForCorrectRatedFeedbacksList();

        webTestClient.get()
                .uri(BASE_URL + "/1000")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void getCourseAggregateInfo_returnsCorrectResponse_withDefaultRatingAndFeedbacksInfo_whenFeedbackServiceUnavailable() {
        stubForRatedFeedbacksList500Error();
        stubForCorrectCourseResponse();

        webTestClient.get()
                .uri(BASE_URL + "/" + COURSE_ONE_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CourseAggregate.class)
                .value(response -> {
                    assertThat(response).isEqualTo(expectedCourseAggregateWithFallback());
                });
    }

    @Test
    void getCourseAggregateInfo_returns503_whenCourseServiceUnavailable() {
        stubForCourse500Error();
        stubForCorrectRatedFeedbacksList();

        webTestClient.get()
                .uri(BASE_URL + "/" + COURSE_ONE_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.SERVICE_UNAVAILABLE.value());
    }

    @Test
    void getCoursesWithRatings_returnsCorrectResponse() {
        stubForCorrectCourseRatingsResponse();
        stubForCorrectCourseListResponse();

        var expected = expectedCourseAggregateList(Comparator.comparing(RatedCourse::getCreatedAt).reversed());

        webTestClient.get()
                .uri(BASE_URL + "?category=ENGINEERING&sortBy=DATE_DESC")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CourseAggregateList.class)
                .value(response -> {
                    assertThat(response).isEqualTo(expected);
                });
    }

    @Test
    void getCoursesWithRatings_returnsCorrectResponse_whenCourseServiceReturnsEmptyList() {
        stubForEmptyCourseRatingsResponse();
        stubForEmptyCourseListResponse();

        webTestClient.get()
                .uri(BASE_URL + "?category=ENGINEERING&sortBy=DATE_DESC")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CourseAggregateList.class)
                .value(response -> {
                    assertThat(response.getCourses()).isEmpty();
                });
    }

    @Test
    void getCoursesWithRatings_returns503_whenCourseServiceUnavailable() {
        stubForCourseList500Error();
        stubForCorrectCourseRatingsResponse();

        webTestClient.get()
                .uri(BASE_URL + "?category=ENGINEERING&sortBy=DATE_DESC")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.SERVICE_UNAVAILABLE.value());
    }

    @Test
    void getCoursesWithRatings_returns503_whenFeedbackServiceUnavailable() {
        stubForCourseRatings500Error();
        stubForCorrectCourseListResponse();

        webTestClient.get()
                .uri(BASE_URL + "?category=ENGINEERING&sortBy=DATE_DESC")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.SERVICE_UNAVAILABLE.value());
    }
}