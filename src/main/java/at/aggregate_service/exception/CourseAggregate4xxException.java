package at.aggregate_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CourseAggregate4xxException extends RuntimeException {

    private final HttpStatus status;

    public CourseAggregate4xxException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
