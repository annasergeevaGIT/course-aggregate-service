package at.aggregate_service.mapper;

import static at.aggregate_service.dto.aggregate.RatedCourseSort.*;
import at.aggregate_service.dto.aggregate.CourseAggregate;
import at.aggregate_service.dto.aggregate.CourseAggregateList;
import at.aggregate_service.dto.aggregate.RatedCourse;
import at.aggregate_service.dto.aggregate.RatedCourseSort;
import at.aggregate_service.dto.course.Course;
import at.aggregate_service.dto.feedback.CourseRatingInfo;
import at.aggregate_service.dto.feedback.RatedFeedbacksList;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;


@Component
public class CourseAggregateMapper {

    private static final EnumSet<RatedCourseSort> IN_MEMORY_SORTS = EnumSet.of(RATE_ASC, RATE_DESC);

    public CourseAggregateList createCourseAggregateList(Map<Long, CourseRatingInfo> ratings,
                                                         List<Course> items,
                                                         RatedCourseSort sort) {
        var itemsStream = items.stream().map(course -> {
            CourseRatingInfo ratingInfo = ratings.get(course.getId());
            return RatedCourse.builder()
                    .id(course.getId())
                    .name(course.getName())
                    .description(course.getDescription())
                    .price(course.getPrice())
                    .category(course.getCategory())
                    .duration(course.getDuration())
                    .difficulty(course.getDifficulty())
                    .imageUrl(course.getImageUrl())
                    .createdAt(course.getCreatedAt())
                    .updatedAt(course.getUpdatedAt())
                    .moduleCollection(course.getModuleCollection())
                    .wilsonScore(ratingInfo.getWilsonScore())
                    .avgStars(ratingInfo.getAvgStars())
                    .build();
        });
        if (shouldApplyInMemorySort(sort)) {
            itemsStream = itemsStream.sorted(sort.getComparator());
        }
        var result = itemsStream.toList();

        return CourseAggregateList.builder().courses(result).build();
    }

    public CourseAggregate createCourseAggregate(Course courseItem, RatedFeedbacksList ratedFeedbacksList) {
        return CourseAggregate.builder()
                .course(courseItem)
                .feedbacks(ratedFeedbacksList.getFeedbacks())
                .ratingInfo(ratedFeedbacksList.getCourseRating())
                .errorResponse(ratedFeedbacksList.getErrorResponse())
                .build();
    }

    private boolean shouldApplyInMemorySort(RatedCourseSort sort) {
        return IN_MEMORY_SORTS.contains(sort);
    }

}
