package at.aggregate_service.client;

import at.aggregate_service.dto.aggregate.RatedCourseSort;
import at.aggregate_service.dto.course.Category;
import at.aggregate_service.dto.course.Course;
import at.aggregate_service.exception.CourseAggregateException;
import at.aggregate_service.props.ExternalServiceProps;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import static at.aggregate_service.dto.aggregate.RatedCourseSort.*;


@Component
public class CourseClient extends BaseClient {

    private static final EnumSet<RatedCourseSort> SUPPORTED_SORTS = EnumSet.of(AZ, ZA, DATE_ASC, DATE_DESC, PRICE_ASC, PRICE_DESC);

    private static final String COURSE_BACKEND = "courseBackend";
    private final WebClient webClient;

    public CourseClient(WebClient.Builder clientBuilder,
                        ExternalServiceProps props) {
        super(props);
        this.webClient = clientBuilder
                .baseUrl(props.getCourseServiceUrl())
                .build();
    }

    @CircuitBreaker(name = COURSE_BACKEND)
    @Retry(name = COURSE_BACKEND)
    public Mono<Course> getCourse(Long courseId) {
        var mono = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(props.getCoursePath())
                        .path("/{courseId}")
                        .build(courseId))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        Mono.error(new CourseAggregateException("Course Service unavailable", HttpStatus.SERVICE_UNAVAILABLE)))
                .bodyToMono(Course.class);

        return applyTimeoutAndHandleExceptions(mono);
    }

    @CircuitBreaker(name = COURSE_BACKEND)
    @Retry(name = COURSE_BACKEND)
    public Mono<List<Course>> getCoursesForCategory(Category category, RatedCourseSort sort) {
        var mono = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(props.getCoursePath())
                        .queryParam("category", category)
                        .queryParamIfPresent("sort",
                                Optional.of(sort).filter(this::supported))
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        Mono.error(new CourseAggregateException("Course Service unavailable", HttpStatus.SERVICE_UNAVAILABLE)))
                .bodyToMono(new ParameterizedTypeReference<List<Course>>() {
                });

        return applyTimeoutAndHandleExceptions(mono);
    }

    private boolean supported(RatedCourseSort sort) {
        return SUPPORTED_SORTS.contains(sort);
    }
}
