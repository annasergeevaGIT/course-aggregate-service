package at.aggregate_service.dto.course;

import at.aggregate_service.exception.CourseAggregateException;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.http.HttpStatus;

public enum Category {
    BUSINESS,
    DESIGN,
    LANGUAGES,
    SCIENCE,
    ARTS,
    ENGINEERING;

    @JsonCreator
    public static Category fromString(String str) {
        try {
            return Category.valueOf(str.toUpperCase());
        } catch (IllegalArgumentException e) {
            var msg = "Failed to create Category from string: %s".formatted(str);
            throw new CourseAggregateException(msg, HttpStatus.BAD_REQUEST);
        }
    }
}
