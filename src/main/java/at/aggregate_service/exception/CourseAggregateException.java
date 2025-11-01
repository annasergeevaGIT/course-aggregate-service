package at.aggregate_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CourseAggregateException extends RuntimeException {

    private final HttpStatus status;

    public CourseAggregateException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
