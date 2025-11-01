package at.aggregate_service.dto.feedback;

import at.aggregate_service.exception.CourseAggregateException;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.http.HttpStatus;

public enum FeedbackSort {
    DATE_ASC,
    DATE_DESC;

    @JsonCreator
    public static FeedbackSort fromString(String str) {
        try {
            return FeedbackSort.valueOf(str.toUpperCase());
        } catch (IllegalArgumentException e) {
            var msg = "Failed to create FeedbackSort from string: %s".formatted(str);
            throw new CourseAggregateException(msg, HttpStatus.BAD_REQUEST);
        }
    }
}
