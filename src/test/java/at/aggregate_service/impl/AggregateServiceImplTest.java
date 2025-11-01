package at.aggregate_service.impl;

import at.aggregate_service.BaseTest;
import at.aggregate_service.dto.aggregate.CourseAggregate;
import at.aggregate_service.dto.aggregate.CourseAggregateList;
import at.aggregate_service.dto.aggregate.RatedCourse;
import at.aggregate_service.dto.aggregate.RatedCourseSort;
import at.aggregate_service.dto.course.Category;
import at.aggregate_service.dto.feedback.FeedbackSort;
import at.aggregate_service.exception.CourseAggregateException;
import at.aggregate_service.service.AggregateServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


import java.util.Comparator;

import static at.aggregate_service.testutil.TestConstants.COURSE_ONE_ID;
import static at.aggregate_service.testutil.TestDateProvider.*;

class AggregateServiceImplTest extends BaseTest {

    @Autowired
    private AggregateServiceImpl aggregateService;

    @Test
    void getCourseAggregateInfo_returnsCorrectResponse() {
        stubForCorrectRatedFeedbacksList();
        stubForCorrectCourseResponse();

        StepVerifier.create(getCourseAggregateMono())
                .expectNextMatches(response ->
                        response.equals(expectedCourseAggregate()))
                .verifyComplete();
    }

    @Test
    void getCourseAggregateInfo_returnsErrorWhenCourseServiceUnavailable() {
        stubForCorrectRatedFeedbacksList();
        stubForCourse500Error();

        StepVerifier.create(getCourseAggregateMono())
                .expectError(CourseAggregateException.class)
                .verify();
    }

    @Test
    void getCourseAggregateInfo_returnsFallbackWhenFeedbackServiceUnavailable() {
        stubForRatedFeedbacksList500Error();
        stubForCorrectCourseResponse();

        StepVerifier.create(getCourseAggregateMono())
                .expectNextMatches(response ->
                        response.equals(expectedCourseAggregateWithFallback()))
                .verifyComplete();
    }

    @Test
    void getCourseAggregateInfo_returnsErrorWhenCourseServiceTimedOut() {
        stubForCorrectRatedFeedbacksList();
        stubForCourseTimeout();

        StepVerifier.create(getCourseAggregateMono())
                .expectError(CourseAggregateException.class)
                .verify();
    }

    @Test
    void getCourseAggregateInfo_returnsFallbackWhenFeedbackServiceTimedOut() {
        stubForRatedFeedbacksListTimeout();
        stubForCorrectCourseResponse();

        StepVerifier.create(getCourseAggregateMono())
                .expectNextMatches(response ->
                        response.equals(expectedCourseAggregateWithFallback()))
                .verifyComplete();
    }

    @Test
    void getCoursesWithRatings_returnsCorrectResponse() {
        stubForCorrectCourseListResponse();
        stubForCorrectCourseRatingsResponse();

        StepVerifier.create(getCourseRatingsMono())
                .expectNextMatches(response ->
                        response.equals(expectedCourseAggregateList(Comparator.comparing(RatedCourse::getCreatedAt).reversed())))
                .verifyComplete();
    }

    @Test
    void getCoursesWithRatings_returnsErrorWhenCourseServiceUnavailable() {
        stubForCourseList500Error();
        stubForCorrectCourseRatingsResponse();

        StepVerifier.create(getCourseRatingsMono())
                .expectError(CourseAggregateException.class)
                .verify();
    }

    @Test
    void getCoursesWithRatings_returnsErrorWhenFeedbackServiceUnavailable() {
        stubForCorrectCourseListResponse();
        stubForCourseRatings500Error();

        StepVerifier.create(getCourseRatingsMono())
                .expectError(CourseAggregateException.class)
                .verify();
    }

    @Test
    void getCoursesWithRatings_returnsErrorWhenCourseServiceTimedOut() {
        stubForCourseListTimeout();
        stubForCorrectCourseRatingsResponse();

        StepVerifier.create(getCourseRatingsMono())
                .expectError(CourseAggregateException.class)
                .verify();
    }

    @Test
    void getCoursesWithRatings_returnsErrorWhenFeedbackServiceTimedOut() {
        stubForCorrectCourseListResponse();
        stubForCourseRatingsTimeout();

        StepVerifier.create(getCourseRatingsMono())
                .expectError(CourseAggregateException.class)
                .verify();
    }

    private Mono<CourseAggregate> getCourseAggregateMono() {
        return aggregateService.getCourseAggregateInfo(COURSE_ONE_ID, FeedbackSort.DATE_ASC, 0, 10);
    }

    private Mono<CourseAggregateList> getCourseRatingsMono() {
        return aggregateService.getCoursesWithRatings(Category.ENGINEERING, RatedCourseSort.DATE_DESC);
    }
}