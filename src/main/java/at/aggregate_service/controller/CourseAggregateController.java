package at.aggregate_service.controller;

import at.aggregate_service.dto.aggregate.CourseAggregate;
import at.aggregate_service.dto.aggregate.CourseAggregateList;
import at.aggregate_service.dto.aggregate.RatedCourseSort;
import at.aggregate_service.dto.course.Category;
import at.aggregate_service.dto.feedback.FeedbackSort;
import at.aggregate_service.service.AggregateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


@Tag(name = "CourseAggregateController", description = "REST API for working with modules")
@Slf4j
@RestController
@RequestMapping("/v1/course-aggregate")
@RequiredArgsConstructor
public class CourseAggregateController {

    private final AggregateService aggregateService;

    @Operation(
            summary = "${api.get-aggregate.summary}",
            description = "${api.get-aggregate.description}"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${api.response.getAggregateOk}"),
            @ApiResponse(
                    responseCode = "400",
                    description = "${api.response.badRequest}",
                    content = @Content(
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "${api.response.notFound}",
                    content = @Content(
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "${api.response.serviceUnavailable}",
                    content = @Content(
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            )
    })
    @GetMapping("/{id}")
    public Mono<CourseAggregate> getCourseAggregateInfo(@PathVariable("id")
                                                    @Positive(message = "Id can not be <= 0.")
                                                    Long courseId,
                                                        @RequestParam(value = "sortBy", defaultValue = "date_asc")
                                                    @NotBlank(message = "Sorting parameter can't be empty.")
                                                    String sortBy,
                                                        @RequestParam(value = "from", defaultValue = "0")
                                                    @PositiveOrZero(message = "Starting page cant be < 0.")
                                                    int from,
                                                        @RequestParam(value = "size", defaultValue = "10")
                                                    @Positive(message = "Page size can't be <= 0.")
                                                    int size) {
        log.info("Received request to GET info about course with ID={}. Sort: {}, from: {}, size: {}",
                courseId, sortBy, from, size);
        return aggregateService.getCourseAggregateInfo(courseId, FeedbackSort.fromString(sortBy), from, size);
    }

    @Operation(
            summary = "${api.get-aggregate-list.summary}",
            description = "${api.get-aggregate-list.description}"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${api.response.getAggregateListOk}"),
            @ApiResponse(
                    responseCode = "503",
                    description = "${api.response.serviceUnavailable}",
                    content = @Content(
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            )
    })
    @GetMapping
    public Mono<CourseAggregateList> getCoursesWithRatings(@RequestParam("category")
                                                       @NotBlank(message = "Category can't be empty.")
                                                       String category,
                                                           @RequestParam(value = "sortBy", defaultValue = "rate_desc")
                                                       @NotBlank(message = "Sorting parameter can't be empty.")
                                                       String sortBy) {
        log.info("Received request to GET info about courses for category: {}", category);
        return aggregateService.getCoursesWithRatings(Category.fromString(category), RatedCourseSort.fromString(sortBy));
    }
}
