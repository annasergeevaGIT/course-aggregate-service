package at.aggregate_service.dto.feedback;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseRatingInfo {
    private Long courseId;
    private Float wilsonScore;
    private Float avgStars;

    public static CourseRatingInfo defaultRating(Long courseId) {
        return CourseRatingInfo.builder()
                .courseId(courseId)
                .wilsonScore(0.0f)
                .avgStars(0.0f)
                .build();
    }
}
