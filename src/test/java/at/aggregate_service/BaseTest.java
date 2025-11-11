package at.aggregate_service;

import at.aggregate_service.dto.aggregate.RatedCourseSort;
import at.aggregate_service.props.ExternalServiceProps;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.util.ResourceUtils;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

import static at.aggregate_service.dto.course.Category.ENGINEERING;
import static at.aggregate_service.testutil.TestConstants.*;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@ActiveProfiles("test")
@Import(TestWebClientConfig.class)
@SpringBootTest
public abstract class BaseTest {

    private static final int DELAY = 1500;
    public static final int PORT = 4567;
    private static final String RETRIES_SCENARIO = "Retries Scenario";
    private static final String FAILURE_STEP_1 = "Failure step 1";
    private static final String FAILURE_STEP_2 = "Failure step 2";
    private static final String SUCCESS_STEP = "Success step";

    @Autowired
    private ExternalServiceProps props;
    @Autowired
    protected CircuitBreakerRegistry circuitBreakerRegistry;

    @RegisterExtension
    protected static WireMockExtension wiremock = WireMockExtension.newInstance().options(wireMockConfig().port(PORT)).build();

    @DynamicPropertySource
    static void applyProperties(DynamicPropertyRegistry registry) {
        registry.add("external.course-service-url", () -> "http://localhost:" + PORT);
        registry.add("external.feedback-service-url", () -> "http://localhost:" + PORT);
        registry.add("external.retry-backoff", () -> "50ms");
        registry.add("external.retry-count", () -> "3");
        registry.add("external.default-timeout", () -> "1s");
        registry.add("resilience4j.circuitbreaker.configs.default.minimum-number-of-calls", () -> "6");
        registry.add("eureka.client.enabled", () -> "false");
        registry.add("spring.cloud.loadbalancer.enabled", () -> "false");
    }

    @BeforeEach
    void reset() {
        circuitBreakerRegistry.getAllCircuitBreakers().forEach(CircuitBreaker::reset);
    }

    protected void stubForCourseRatingsRetriesFailure() {
        stubForRetriesAndFailure(getCourseRatingsUrl(), "wiremock/ratings-response.json", WireMock::post);
    }

    protected void stubForCourseRatingsRetriesSuccess() {
        stubForRetriesAndSuccess(getCourseRatingsUrl(), "wiremock/ratings-response.json", WireMock::post);
    }

    protected void stubForCourseRatings500Error() {
        stubFor500Error(getCourseRatingsUrl(), WireMock::post);
    }

    protected void stubForCourseRatingsTimeout() {
        stubForTimeout(getCourseRatingsUrl(), WireMock::post);
    }

    protected void stubForCorrectCourseRatingsResponse() {
        stubForCorrectResponse(getCourseRatingsUrl(), WireMock::post, "wiremock/ratings-response.json");
    }

    protected void stubForEmptyCourseRatingsResponse() {
        stubForCorrectResponse(getCourseRatingsUrl(), WireMock::post, "wiremock/ratings-empty-response.json");
    }

    protected void stubForRatedFeedbacksListRetriesSuccess() {
        stubForRetriesAndSuccess(getFeedbacksOfCourseOneUrl(), "wiremock/feedbacks-response.json", WireMock::get);
    }

    protected void stubForRatedFeedbacksListRetriesFailure() {
        stubForRetriesAndFailure(getFeedbacksOfCourseOneUrl(), "wiremock/feedbacks-response.json", WireMock::get);
    }

    protected void stubForRatedFeedbacksList500Error() {
        stubFor500Error(getFeedbacksOfCourseOneUrl(), WireMock::get);
    }

    protected void stubForRatedFeedbacksListTimeout() {
        stubForTimeout(getFeedbacksOfCourseOneUrl(), WireMock::get);
    }

    protected void stubForCorrectRatedFeedbacksList() {
        stubForCorrectResponse(getFeedbacksOfCourseOneUrl(), WireMock::get, "wiremock/feedbacks-response.json");
    }

    protected void stubForCourseListWithRetriesAndSuccess() {
        stubForRetriesAndSuccess(getCourseListUrl(), "wiremock/course-list-response.json", WireMock::get);
    }

    protected void stubForCourseListWithRetriesAndFailure() {
        stubForRetriesAndFailure(getCourseListUrl(), "wiremock/course-list-response.json", WireMock::get);
    }

    protected void stubForCourseList500Error() {
        stubFor500Error(getCourseListUrl(), WireMock::get);
    }

    protected void stubForCourseListTimeout() {
        stubForTimeout(getCourseListUrl(), WireMock::get);
    }

    protected void stubForCorrectCourseListResponse() {
        stubForCorrectResponse(getCourseListUrl(), WireMock::get, "wiremock/course-list-response.json");
    }

    protected void stubForEmptyCourseListResponse() {
        stubForCorrectResponse(getCourseListUrl(), WireMock::get, "wiremock/course-list-empty-response.json");
    }

    protected void stubForCourseWithRetriesAndSuccess() {
        stubForRetriesAndSuccess(getCourseUrl(), "wiremock/course-response.json", WireMock::get);
    }

    protected void stubForCourseWithRetriesAndFailure() {
        stubForRetriesAndFailure(getCourseUrl(), "wiremock/course-response.json", WireMock::get);
    }

    protected void stubForCourse500Error() {
        stubFor500Error(getCourseUrl(), WireMock::get);
    }

    protected void stubForCourseTimeout() {
        stubForTimeout(getCourseUrl(), WireMock::get);
    }

    protected void stubForCourseNotFound() {
        stubFor404Error(getCourseUrl(), WireMock::get);
    }

    protected void stubForCorrectCourseResponse() {
        stubForCorrectResponse(getCourseUrl(), WireMock::get, "wiremock/course-response.json");
    }

    protected String getCourseListUrl() {
        return props.getCoursePath() + "?category=" + ENGINEERING + "&sort=" + RatedCourseSort.DATE_DESC;
    }

    protected String getFeedbacksOfCourseOneUrl() {
        return props.getCourseFeedbacksPath() + "/" + COURSE_ONE_ID + "?from=0&size=10&sortBy=DATE_ASC";
    }

    protected String getCourseUrl() {
        return props.getCoursePath() + "/" + COURSE_ONE_ID;
    }

    protected String getCourseRatingsUrl() {
        return props.getCourseRatingsPath();
    }

    private void stubForRetriesAndSuccess(String url, String filePath, Function<String, MappingBuilder> function) {
        wiremock.resetScenarios();
        wiremock.stubFor(function.apply(url)
                .inScenario(RETRIES_SCENARIO)
                .whenScenarioStateIs(Scenario.STARTED)
                .willSetStateTo(FAILURE_STEP_1)
                .willReturn(serviceUnavailable()));
        var response = readFileToString(filePath);

        wiremock.stubFor(function.apply(url)
                .inScenario(RETRIES_SCENARIO)
                .whenScenarioStateIs(FAILURE_STEP_1)
                .willSetStateTo(SUCCESS_STEP)
                .willReturn(okJson(response).withFixedDelay(DELAY)));

        wiremock.stubFor(function.apply(url)
                .inScenario(RETRIES_SCENARIO)
                .whenScenarioStateIs(SUCCESS_STEP)
                .willReturn(okJson(response)));
    }

    private void stubForRetriesAndFailure(String url, String filePath, Function<String, MappingBuilder> function) {
        wiremock.resetScenarios();
        wiremock.stubFor(function.apply(url)
                .inScenario(RETRIES_SCENARIO)
                .whenScenarioStateIs(Scenario.STARTED)
                .willSetStateTo(FAILURE_STEP_1)
                .willReturn(serviceUnavailable()));
        var response = readFileToString(filePath);

        wiremock.stubFor(function.apply(url)
                .inScenario(RETRIES_SCENARIO)
                .whenScenarioStateIs(FAILURE_STEP_1)
                .willSetStateTo(FAILURE_STEP_2)
                .willReturn(okJson(response).withFixedDelay(DELAY)));

        wiremock.stubFor(function.apply(url)
                .inScenario(RETRIES_SCENARIO)
                .whenScenarioStateIs(FAILURE_STEP_2)
                .willReturn(serviceUnavailable()));
    }

    private void stubForTimeout(String url, Function<String, MappingBuilder> function) {
        wiremock.stubFor(function.apply(url).willReturn(aResponse().withFixedDelay(DELAY)));
    }

    private void stubFor500Error(String url, Function<String, MappingBuilder> function) {
        wiremock.stubFor(function.apply(url).willReturn(serverError()));
    }

    private void stubFor404Error(String url, Function<String, MappingBuilder> function) {
        wiremock.stubFor(function.apply(url).willReturn(notFound()));
    }

    private void stubForCorrectResponse(String url, Function<String, MappingBuilder> function, String filePath) {
        var response = readFileToString(filePath);
        wiremock.stubFor(function.apply(url).willReturn(okJson(response)));
    }

    private String readFileToString(String filePath) {
        try {
            Path path = ResourceUtils.getFile("classpath:" + filePath).toPath();
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}