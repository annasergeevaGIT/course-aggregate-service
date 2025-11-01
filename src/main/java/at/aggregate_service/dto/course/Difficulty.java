package at.aggregate_service.dto.course;

import at.aggregate_service.exception.CourseAggregateException;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.http.HttpStatus;

public enum Difficulty {
    BEGINNER,
    INTERMEDIATE,
    ADVANCED;

    @JsonCreator
    public static Difficulty fromString(String level) {
        try {
            return Difficulty.valueOf(level.toUpperCase());
        } catch (IllegalArgumentException e) {
            var msg = "Failed to create CourseLevel from string: %s".formatted(level);
            throw new CourseAggregateException(msg, HttpStatus.BAD_REQUEST);
        }
    }
}
