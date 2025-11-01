package at.aggregate_service.dto.aggregate;

import at.aggregate_service.dto.course.Course;
import at.aggregate_service.dto.feedback.ErrorResponse;
import at.aggregate_service.dto.feedback.CourseRatingInfo;
import at.aggregate_service.dto.feedback.Feedback;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseAggregate { // Aggregates course info with feedbacks, rating, and optional error details
    private Course course;
    private List<Feedback> feedbacks;
    private CourseRatingInfo ratingInfo;
    private ErrorResponse errorResponse;
}
