package at.aggregate_service.service;

import at.aggregate_service.dto.aggregate.CourseAggregate;
import at.aggregate_service.dto.aggregate.CourseAggregateList;
import at.aggregate_service.dto.aggregate.RatedCourseSort;
import at.aggregate_service.dto.course.Category;
import at.aggregate_service.dto.feedback.FeedbackSort;
import reactor.core.publisher.Mono;

public interface AggregateService {

    Mono<CourseAggregate> getCourseAggregateInfo(Long courseId, FeedbackSort sort, int from, int size);

    Mono<CourseAggregateList> getCoursesWithRatings(Category category, RatedCourseSort sort);
}
