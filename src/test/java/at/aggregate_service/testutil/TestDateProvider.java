package at.aggregate_service.testutil;

import at.aggregate_service.dto.aggregate.CourseAggregate;
import at.aggregate_service.dto.aggregate.CourseAggregateList;
import at.aggregate_service.dto.aggregate.RatedCourse;
import at.aggregate_service.dto.course.Module;
import at.aggregate_service.dto.course.ModuleCollection;
import at.aggregate_service.dto.course.Course;
import at.aggregate_service.dto.feedback.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static at.aggregate_service.testutil.TestConstants.*;

public class TestDateProvider {

    public static RatedFeedbacksList ratedFeedbacksList() {
        return RatedFeedbacksList.builder()
                .feedbacks(List.of(
                        alexReview(),
                        johnReview(),
                        michaelReview(),
                        maxReview(),
                        phillReview()
                ))
                .courseRating(menuOneRatingInfo())
                .build();
    }

    public static Feedback alexReview() {
        return Feedback.builder()
                .id(1L)
                .courseId(COURSE_ONE_ID)
                .createdBy(ALEX)
                .comment("Comment")
                .rate(ALEX_RATE)
                .createdAt(ALEX_FEEDBACK_DATE)
                .build();
    }

    public static Feedback johnReview() {
        return Feedback.builder()
                .id(7L)
                .courseId(COURSE_ONE_ID)
                .createdBy(JOHN)
                .comment("Comment")
                .rate(JOHN_RATE)
                .createdAt(JOHN_FEEDBACK_DATE)
                .build();
    }

    public static Feedback michaelReview() {
        return Feedback.builder()
                .id(8L)
                .courseId(COURSE_ONE_ID)
                .createdBy(MICHAEL)
                .comment("Comment")
                .rate(MICHAEL_RATE)
                .createdAt(MICHAEL_FEEDBACK_DATE)
                .build();
    }

    public static Feedback maxReview() {
        return Feedback.builder()
                .id(9L)
                .courseId(COURSE_ONE_ID)
                .createdBy(MAX)
                .comment("Comment")
                .rate(MAX_RATE)
                .createdAt(MAX_FEEDBACK_DATE)
                .build();
    }

    public static Feedback phillReview() {
        return Feedback.builder()
                .id(10L)
                .courseId(COURSE_ONE_ID)
                .createdBy(PHILL)
                .comment("Comment")
                .rate(PHILL_RATE)
                .createdAt(PHILL_FEEDBACK_DATE)
                .build();
    }

    public static RatingsList ratingsList() {
        return RatingsList.builder()
                .courseRatings(List.of(menuOneRatingInfo(), menuTwoRatingInfo(), menuThreeRatingInfo()))
                .build();
    }

    public static CourseRatingInfo menuOneRatingInfo() {
        return CourseRatingInfo.builder()
                .courseId(COURSE_ONE_ID)
                .wilsonScore(COURSE_ONE_WILSON_SCORE)
                .avgStars(COURSE_ONE_AVG_STARS)
                .build();
    }

    public static CourseRatingInfo menuTwoRatingInfo() {
        return CourseRatingInfo.builder()
                .courseId(COURSE_TWO_ID)
                .wilsonScore(COURSE_TWO_WILSON_SCORE)
                .avgStars(COURSE_TWO_AVG_STARS)
                .build();
    }

    public static CourseRatingInfo menuThreeRatingInfo() {
        return CourseRatingInfo.builder()
                .courseId(COURSE_THREE_ID)
                .wilsonScore(COURSE_THREE_WILSON_SCORE)
                .avgStars(COURSE_THREE_AVG_STARS)
                .build();
    }

    public static List<Course> drinksCourseList() {
        return List.of(
                menuThreeItem(),
                menuTwoItem(),
                courseOneItem()
        );
    }

    public static Course courseOneItem() {
        var item = new Course();
        item.setId(COURSE_ONE_ID);
        item.setName(COURSE_ONE_NAME);
        item.setDescription(COURSE_ONE_DESCRIPTION);
        item.setPrice(COURSE_ONE_PRICE);
        item.setCategory(COURSE_ONE_CATEGORY);
        item.setDuration(COURSE_ONE_DURATION);
        item.setDifficulty(COURSE_ONE_DIFFICULTY);
        item.setImageUrl(COURSE_ONE_IMAGE_URL);
        item.setUpdatedAt(COURSE_ONE_UPDATED_AT);
        item.setCreatedAt(COURSE_ONE_CREATED_AT);
        item.setModuleCollection(moduleCollection());
        return item;
    }

    public static Course menuTwoItem() {
        var item = new Course();
        item.setId(COURSE_TWO_ID);
        item.setName(COURSE_TWO_NAME);
        item.setDescription(COURSE_TWO_DESCRIPTION);
        item.setPrice(COURSE_TWO_PRICE);
        item.setCategory(COURSE_TWO_CATEGORY);
        item.setDuration(COURSE_TWO_DURATION);
        item.setDifficulty(COURSE_TWO_DIFFICULTY);
        item.setImageUrl(COURSE_TWO_IMAGE_URL);
        item.setUpdatedAt(COURSE_TWO_UPDATED_AT);
        item.setCreatedAt(COURSE_TWO_CREATED_AT);
        item.setModuleCollection(moduleCollection());
        return item;
    }

    public static Course menuThreeItem() {
        var item = new Course();
        item.setId(COURSE_THREE_ID);
        item.setName(COURSE_THREE_NAME);
        item.setDescription(COURSE_THREE_DESCRIPTION);
        item.setPrice(COURSE_THREE_PRICE);
        item.setCategory(COURSE_THREE_CATEGORY);
        item.setDuration(COURSE_THREE_DURATION);
        item.setDifficulty(COURSE_THREE_DIFFICULTY);
        item.setImageUrl(COURSE_THREE_IMAGE_URL);
        item.setUpdatedAt(COURSE_THREE_UPDATED_AT);
        item.setCreatedAt(COURSE_THREE_CREATED_AT);
        item.setModuleCollection(moduleCollection());
        return item;
    }

    public static RatedCourse menuOneRatedItem() {
        return RatedCourse.builder()
                .id(COURSE_ONE_ID)
                .name(COURSE_ONE_NAME)
                .description(COURSE_ONE_DESCRIPTION)
                .price(COURSE_ONE_PRICE)
                .category(COURSE_ONE_CATEGORY)
                .duration(COURSE_ONE_DURATION)
                .difficulty(COURSE_ONE_DIFFICULTY)
                .imageUrl(COURSE_ONE_IMAGE_URL)
                .updatedAt(COURSE_ONE_UPDATED_AT)
                .createdAt(COURSE_ONE_CREATED_AT)
                .moduleCollection(moduleCollection())
                .wilsonScore(COURSE_ONE_WILSON_SCORE)
                .avgStars(COURSE_ONE_AVG_STARS)
                .build();
    }

    public static RatedCourse menuTwoRatedItem() {
        return RatedCourse.builder()
                .id(COURSE_TWO_ID)
                .name(COURSE_TWO_NAME)
                .description(COURSE_TWO_DESCRIPTION)
                .price(COURSE_TWO_PRICE)
                .category(COURSE_TWO_CATEGORY)
                .duration(COURSE_TWO_DURATION)
                .difficulty(COURSE_TWO_DIFFICULTY)
                .imageUrl(COURSE_TWO_IMAGE_URL)
                .updatedAt(COURSE_TWO_UPDATED_AT)
                .createdAt(COURSE_TWO_CREATED_AT)
                .moduleCollection(moduleCollection())
                .wilsonScore(COURSE_TWO_WILSON_SCORE)
                .avgStars(COURSE_TWO_AVG_STARS)
                .build();
    }

    public static RatedCourse menuThreeRatedItem() {
        return RatedCourse.builder()
                .id(COURSE_THREE_ID)
                .name(COURSE_THREE_NAME)
                .description(COURSE_THREE_DESCRIPTION)
                .price(COURSE_THREE_PRICE)
                .category(COURSE_THREE_CATEGORY)
                .duration(COURSE_THREE_DURATION)
                .difficulty(COURSE_THREE_DIFFICULTY)
                .imageUrl(COURSE_THREE_IMAGE_URL)
                .updatedAt(COURSE_THREE_UPDATED_AT)
                .createdAt(COURSE_THREE_CREATED_AT)
                .moduleCollection(moduleCollection())
                .wilsonScore(COURSE_THREE_WILSON_SCORE)
                .avgStars(COURSE_THREE_AVG_STARS)
                .build();
    }

    public static ModuleCollection moduleCollection() {
        return new ModuleCollection(
                List.of(
                        Module.builder().name(MODULE_ONE_NAME).duration(MODULE_ONE_DURATION).build(),
                        Module.builder().name(MODULE_TWO_NAME).duration(MODULE_TWO_DURATION).build()
                )
        );
    }

    public static CourseAggregate expectedCourseAggregate() {
        return CourseAggregate.builder()
                .course(courseOneItem())
                .feedbacks(List.of(alexReview(), johnReview(), michaelReview(), maxReview(), phillReview()))
                .ratingInfo(menuOneRatingInfo())
                .build();
    }

    public static CourseAggregate expectedCourseAggregateWithFallback() {
        RatedFeedbacksList fallback = RatedFeedbacksList.fallbackResponse(COURSE_ONE_ID);
        return CourseAggregate.builder()
                .course(courseOneItem())
                .feedbacks(fallback.getFeedbacks())
                .ratingInfo(fallback.getCourseRating())
                .errorResponse(fallback.getErrorResponse())
                .build();
    }

    public static CourseAggregateList expectedCourseAggregateList(Comparator<RatedCourse> comparator) {
        var list = new ArrayList<RatedCourse>();
        list.add(menuOneRatedItem());
        list.add(menuTwoRatedItem());
        list.add(menuThreeRatedItem());
        list.sort(comparator);
        return CourseAggregateList.builder()
                .courses(list)
                .build();
    }

    public static GetRatingsRequest getRatingsRequest() {
        return GetRatingsRequest.builder()
                .courseIds(Set.of(COURSE_ONE_ID, COURSE_TWO_ID, COURSE_THREE_ID))
                .build();
    }
}
