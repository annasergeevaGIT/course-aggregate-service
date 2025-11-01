package at.aggregate_service.dto.aggregate;

import at.aggregate_service.dto.course.Category;
import at.aggregate_service.dto.course.Course;
import at.aggregate_service.dto.course.Difficulty;
import at.aggregate_service.dto.course.ModuleCollection;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.Diff;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class RatedCourse extends Course {
    private Float wilsonScore;
    private Float avgStars;

    @Builder //Lombok doesn't read superclass fields: https://www.baeldung.com/lombok-builder-inheritance
    public RatedCourse(Long id,
                       String name,
                       String description,
                       BigDecimal price,
                       Category category,
                       long duration,
                       Difficulty difficulty,
                       String imageUrl,
                       LocalDateTime updatedAt,
                       LocalDateTime createdAt,
                       ModuleCollection moduleCollection,
                       Float wilsonScore,
                       Float avgStars) {
        super(id, name, description, price, category, duration, difficulty, imageUrl, updatedAt, createdAt, moduleCollection);
        this.wilsonScore = wilsonScore;
        this.avgStars = avgStars;
    }
}
