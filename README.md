# Course Aggregate Service

The Course Aggregate Service is part of the E-Learning Platform microservices ecosystem.
It aggregates and exposes comprehensive course information — combining course details, user feedback, and enrollment statistics from other services — to provide a unified view for clients.

## Related Services

| Service                                                                 | Description                       |
|-------------------------------------------------------------------------|-----------------------------------|
| [Course Service](https://github.com/annasergeevaGIT/course-service)     | Handles courses                   |
| [Feedback Service](https://github.com/annasergeevaGIT/feedback-service) | Manages user feedback             |
| [Enrollment Service](https://github.com/annasergeevaGIT/enrollment-service)                       | Aggregates course and review data |
| [Gateway Service](../gateway-service)                                   | Routes requests to microservices  |
| [Config Server](../config-server)                                       | Centralized configuration storage |

## Overview

The Aggregate Service acts as a read-only composition layer.
It communicates with multiple microservices — such as Course, Feedback, and Enrollment — to build rich aggregated responses for front-end clients or other services.

It does not store data locally but relies on external service clients and asynchronous communication for data retrieval.

## Key Responsibilities

- Aggregate data from multiple microservices
- Expose unified API endpoints for course information
- Provide precomputed metrics like average ratings and enrollment counts
- Apply resilience patterns (CircuitBreaker, Retry) for external service calls
- Enable observability and monitoring through Micrometer and Prometheus

## Functionality

- Retrieve combined data for courses (details, feedbacks, enrollments)
- Fetch aggregated ratings from the Feedback Service
- Collect student enrollment information
- Expose a single API endpoint for clients instead of multiple service calls
- Provide resilience via Resilience4j
- Fully containerized and CI/CD-ready with GitHub Actions

## Endpoints

| Method  | Endpoint                        | Description                            |
|---------|---------------------------------|----------------------------------------|
| `GET`  | `/v1/courses/aggregate/{courseId}`                 | Get aggregated course details including rating and enrollment info                 |
| `GET`   | `/v1/courses/aggregate`            | Get paginated list of all aggregated course summaries                     |
| `GET`   | `/v1/courses/aggregate/ratings`              | Get average ratings for multiple courses      |
| `GET`   | `/actuator/health` | Health check endpoint |


## Tech Stack

- **Java 21**
- **Spring Boot 3**
- **Spring WebFlux / WebClient**
- **Spring Cloud OpenFeign (for service clients)**
- **Resilience4j (Circuit Breaker / Retry)**
- **Micrometer / Prometheus**
- **Lombok**
- **WebTestClient / JUnit 5 (testing)**
- **MapStruct (DTO mapping)**
- **Eureka Discovery**
- **Docker**
- **GitHub Actions (CI/CD)**

## Build & Run

```bash
./gradlew clean bootBuildImage
docker-compose up -d
