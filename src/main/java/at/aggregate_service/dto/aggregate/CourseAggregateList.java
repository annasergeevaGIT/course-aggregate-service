package at.aggregate_service.dto.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseAggregateList { // wrapper class was created to return courses with feedback (easy to add new fields to the response)
    private List<RatedCourse> courses;
}
