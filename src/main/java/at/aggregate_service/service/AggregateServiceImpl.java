package at.aggregate_service.service;

import at.aggregate_service.client.CourseClient;
import at.aggregate_service.client.FeedbacksClient;
import at.aggregate_service.dto.aggregate.CourseAggregate;
import at.aggregate_service.dto.aggregate.CourseAggregateList;
import at.aggregate_service.dto.aggregate.RatedCourseSort;
import at.aggregate_service.dto.course.Category;
import at.aggregate_service.dto.course.Course;
import at.aggregate_service.dto.feedback.GetRatingsRequest;
import at.aggregate_service.dto.feedback.CourseRatingInfo;
import at.aggregate_service.dto.feedback.RatedFeedbacksList;
import at.aggregate_service.dto.feedback.FeedbackSort;
import at.aggregate_service.mapper.CourseAggregateMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AggregateServiceImpl implements AggregateService {

    private final CourseClient courseClient;
    private final FeedbacksClient feedbacksClient;
    private final CourseAggregateMapper mapper;

    @Override
    public Mono<CourseAggregate> getCourseAggregateInfo(Long courseId, FeedbackSort sort, int from, int size) {
        return Mono.zip(
                        values -> mapper.createCourseAggregate((Course) values[0], (RatedFeedbacksList) values[1]),
                        courseClient.getCourse(courseId),
                        feedbacksClient.getFeedbacksWithCourseRating(courseId, from, size, sort)
                )
                .doOnError(t -> log.error("Failed to getCourseAggregateInfo: {}", t.toString()));
    }

    @Override
    public Mono<CourseAggregateList> getCoursesWithRatings(Category category, RatedCourseSort sort) {
        return courseClient.getCoursesForCategory(category, sort)
                .flatMap(items -> getRatingsForCourses(items)
                        .map(ratingsMap -> mapper.createCourseAggregateList(ratingsMap, items, sort)))
                .doOnError(t -> log.error("Failed to getCoursesWithRatings: {}", t.toString()));
    }

    private Mono<Map<Long, CourseRatingInfo>> getRatingsForCourses(List<Course> items) {
        Set<Long> ids = items.stream().map(Course::getId).collect(Collectors.toSet());
        return feedbacksClient.getCourseRatings(new GetRatingsRequest(ids))
                .map(response -> response.getCourseRatings().stream()
                        .collect(Collectors.toMap(CourseRatingInfo::getCourseId, Function.identity())));
    }
}
