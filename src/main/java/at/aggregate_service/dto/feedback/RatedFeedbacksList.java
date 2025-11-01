package at.aggregate_service.dto.feedback;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RatedFeedbacksList { // Contains feedbacks, rating info, and an optional error response if Feedback Service is unavailable
    private List<Feedback> feedbacks;
    private CourseRatingInfo courseRating;
    private ErrorResponse errorResponse;

    public static RatedFeedbacksList fallbackResponse(Long courseId) { // Returns a response when Feedback Service is unavailable
        return RatedFeedbacksList.builder()
                .courseRating(CourseRatingInfo.defaultRating(courseId))
                .feedbacks(List.of())
                .errorResponse(ErrorResponse.builder()
                        .message("Did not manage to receive data about course feedbacks and rating.")
                        .reason("Microservice Feedback Service not accessible.")
                        .build())
                .build();
    }
}