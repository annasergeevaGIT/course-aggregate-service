package at.aggregate_service.dto.aggregate;

import at.aggregate_service.exception.CourseAggregateException;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Comparator;

@Getter
@AllArgsConstructor
public enum RatedCourseSort {
    AZ(Comparator.comparing(RatedCourse::getName)),
    ZA(Comparator.comparing(RatedCourse::getName).reversed()),
    DATE_ASC(Comparator.comparing(RatedCourse::getCreatedAt)),
    DATE_DESC(Comparator.comparing(RatedCourse::getCreatedAt).reversed()),
    PRICE_ASC(Comparator.comparing(RatedCourse::getPrice)),
    PRICE_DESC(Comparator.comparing(RatedCourse::getPrice).reversed()),
    RATE_ASC(Comparator.comparing(RatedCourse::getWilsonScore)),
    RATE_DESC(Comparator.comparing(RatedCourse::getWilsonScore).reversed());

    @JsonCreator
    public static RatedCourseSort fromString(String str) {
        try {
            return RatedCourseSort.valueOf(str.toUpperCase());
        } catch (IllegalArgumentException e) {
            var msg = "Failed to create CourseSort from string: %s".formatted(str);
            throw new CourseAggregateException(msg, HttpStatus.BAD_REQUEST);
        }
    }

    private final Comparator<RatedCourse> comparator; //for sorting streams and collections
}
