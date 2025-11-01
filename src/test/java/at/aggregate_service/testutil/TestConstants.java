package at.aggregate_service.testutil;

import at.aggregate_service.dto.course.Category;
import at.aggregate_service.dto.course.Difficulty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TestConstants {
    public static final String BASE_URL = "/v1/course-aggregate";
    public static final Long COURSE_ONE_ID = 1L;
    public static final String COURSE_ONE_NAME = "One";
    public static final String COURSE_ONE_DESCRIPTION = "Nice Item One";
    public static final BigDecimal COURSE_ONE_PRICE = BigDecimal.valueOf(10.1);
    public static final Category COURSE_ONE_CATEGORY = Category.ENGINEERING;
    public static final long COURSE_ONE_DURATION = 1000;
    public static final Difficulty COURSE_ONE_DIFFICULTY = Difficulty.BEGINNER;
    public static final String COURSE_ONE_IMAGE_URL = "http://images.com/one.png";
    public static final LocalDateTime COURSE_ONE_UPDATED_AT = LocalDateTime.parse("2024-03-20T11:01:30.043025");
    public static final LocalDateTime COURSE_ONE_CREATED_AT = LocalDateTime.parse("2024-03-20T11:01:30.042833");

    public static final Long COURSE_TWO_ID = 2L;
    public static final String COURSE_TWO_NAME = "Two";
    public static final String COURSE_TWO_DESCRIPTION = "Nice Item Two";
    public static final BigDecimal COURSE_TWO_PRICE = BigDecimal.valueOf(10.1);
    public static final Category COURSE_TWO_CATEGORY = Category.ENGINEERING;
    public static final long COURSE_TWO_DURATION = 1000;
    public static final Difficulty COURSE_TWO_DIFFICULTY = Difficulty.BEGINNER;
    public static final String COURSE_TWO_IMAGE_URL = "http://images.com/two.png";
    public static final LocalDateTime COURSE_TWO_UPDATED_AT = LocalDateTime.parse("2024-03-20T11:01:34.574555");
    public static final LocalDateTime COURSE_TWO_CREATED_AT = LocalDateTime.parse("2024-03-20T11:01:34.574508");

    public static final Long COURSE_THREE_ID = 3L;
    public static final String COURSE_THREE_NAME = "Three";
    public static final String COURSE_THREE_DESCRIPTION = "Nice Item Three";
    public static final BigDecimal COURSE_THREE_PRICE = BigDecimal.valueOf(10.1);
    public static final Category COURSE_THREE_CATEGORY = Category.ENGINEERING;
    public static final long COURSE_THREE_DURATION = 1000;
    public static final Difficulty COURSE_THREE_DIFFICULTY = Difficulty.BEGINNER;
    public static final String COURSE_THREE_IMAGE_URL = "http://images.com/three.png";
    public static final LocalDateTime COURSE_THREE_UPDATED_AT = LocalDateTime.parse("2024-03-20T11:01:37.942606");
    public static final LocalDateTime COURSE_THREE_CREATED_AT = LocalDateTime.parse("2024-03-20T11:01:37.942576");

    public static final String MODULE_ONE_NAME = "module one";
    public static final String MODULE_TWO_NAME = "module two";
    public static final int MODULE_ONE_DURATION = 10;
    public static final int MODULE_TWO_DURATION = 20;

    public static final Float COURSE_ONE_WILSON_SCORE = 0.17042015f;
    public static final Float COURSE_ONE_AVG_STARS = 3.0f;

    public static final Float COURSE_TWO_WILSON_SCORE = 0.2065433f;
    public static final Float COURSE_TWO_AVG_STARS = 5.0f;

    public static final Float COURSE_THREE_WILSON_SCORE = 0.2065433f;
    public static final Float COURSE_THREE_AVG_STARS = 5.0f;

    public static final String ALEX = "Alex";
    public static final LocalDateTime ALEX_FEEDBACK_DATE = LocalDateTime.parse("2024-03-20T11:01:48.178558");
    public static final Integer ALEX_RATE = 5;

    public static final String JOHN = "John";
    public static final LocalDateTime JOHN_FEEDBACK_DATE = LocalDateTime.parse("2024-03-20T11:02:17.491651");
    public static final Integer JOHN_RATE = 4;

    public static final String MICHAEL = "Michael";
    public static final LocalDateTime MICHAEL_FEEDBACK_DATE = LocalDateTime.parse("2024-03-20T11:02:21.307757");
    public static final Integer MICHAEL_RATE = 3;

    public static final String MAX = "Max";
    public static final LocalDateTime MAX_FEEDBACK_DATE = LocalDateTime.parse("2024-03-20T11:02:25.373142");
    public static final Integer MAX_RATE = 2;

    public static final String PHILL = "Phill";
    public static final LocalDateTime PHILL_FEEDBACK_DATE = LocalDateTime.parse("2024-03-20T11:02:29.873426");
    public static final Integer PHILL_RATE = 1;

    public static final String COURSE_BACKEND = "courseBackend";
    public static final String FEEDBACK_BACKEND = "feedbackBackend";
}
