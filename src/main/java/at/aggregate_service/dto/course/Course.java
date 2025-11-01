package at.aggregate_service.dto.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Course {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Category category;
    private long duration;
    private Difficulty difficulty;
    private String imageUrl;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private ModuleCollection moduleCollection;
}
