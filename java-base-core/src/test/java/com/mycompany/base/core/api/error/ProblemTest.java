package com.mycompany.base.core.api.error;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProblemTest {

    @Test
    void testProblemCreation() {
        Instant timestamp = Instant.now();
        Map<String, Object> extensions = Map.of("field", "email", "reason", "Invalid format");

        Problem problem = new Problem(
                "https://api.example.com/problems/validation-error",
                "Validation Failed",
                400,
                "The request body contains invalid data",
                "/api/users/123",
                "req-12345",
                timestamp,
                extensions
        );

        assertThat(problem.type()).isEqualTo("https://api.example.com/problems/validation-error");
        assertThat(problem.title()).isEqualTo("Validation Failed");
        assertThat(problem.status()).isEqualTo(400);
        assertThat(problem.detail()).isEqualTo("The request body contains invalid data");
        assertThat(problem.instance()).isEqualTo("/api/users/123");
        assertThat(problem.correlationId()).isEqualTo("req-12345");
        assertThat(problem.timestamp()).isEqualTo(timestamp);
        assertThat(problem.extensions()).isEqualTo(extensions);
    }

    @Test
    void testProblemCreationWithoutExtensions() {
        Instant timestamp = Instant.now();

        Problem problem = new Problem(
                "https://api.example.com/problems/validation-error",
                "Validation Failed",
                400,
                "The request body contains invalid data",
                "/api/users/123",
                "req-12345",
                timestamp
        );

        assertThat(problem.type()).isEqualTo("https://api.example.com/problems/validation-error");
        assertThat(problem.title()).isEqualTo("Validation Failed");
        assertThat(problem.status()).isEqualTo(400);
        assertThat(problem.detail()).isEqualTo("The request body contains invalid data");
        assertThat(problem.instance()).isEqualTo("/api/users/123");
        assertThat(problem.correlationId()).isEqualTo("req-12345");
        assertThat(problem.timestamp()).isEqualTo(timestamp);
        assertThat(problem.extensions()).isEmpty();
    }

    @Test
    void testProblemCreationWithCurrentTimestamp() {
        Problem problem = new Problem(
                "https://api.example.com/problems/validation-error",
                "Validation Failed",
                400,
                "The request body contains invalid data",
                "/api/users/123",
                "req-12345"
        );

        assertThat(problem.type()).isEqualTo("https://api.example.com/problems/validation-error");
        assertThat(problem.title()).isEqualTo("Validation Failed");
        assertThat(problem.status()).isEqualTo(400);
        assertThat(problem.detail()).isEqualTo("The request body contains invalid data");
        assertThat(problem.instance()).isEqualTo("/api/users/123");
        assertThat(problem.correlationId()).isEqualTo("req-12345");
        assertThat(problem.timestamp()).isNotNull();
        assertThat(problem.extensions()).isEmpty();
    }

    @Test
    void testProblemValidationWithNullType() {
        assertThatThrownBy(() -> new Problem(null, "Title", 400, "Detail", "/instance", "correlation", Instant.now()))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("type cannot be null");
    }

    @Test
    void testProblemValidationWithEmptyType() {
        assertThatThrownBy(() -> new Problem("", "Title", 400, "Detail", "/instance", "correlation", Instant.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("type cannot be empty");
    }

    @Test
    void testProblemValidationWithBlankType() {
        assertThatThrownBy(() -> new Problem("   ", "Title", 400, "Detail", "/instance", "correlation", Instant.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("type cannot be empty");
    }

    @Test
    void testProblemValidationWithNullTitle() {
        assertThatThrownBy(() -> new Problem("type", null, 400, "Detail", "/instance", "correlation", Instant.now()))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("title cannot be null");
    }

    @Test
    void testProblemValidationWithEmptyTitle() {
        assertThatThrownBy(() -> new Problem("type", "", 400, "Detail", "/instance", "correlation", Instant.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("title cannot be empty");
    }

    @Test
    void testProblemValidationWithNullDetail() {
        assertThatThrownBy(() -> new Problem("type", "Title", 400, null, "/instance", "correlation", Instant.now()))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("detail cannot be null");
    }

    @Test
    void testProblemValidationWithEmptyDetail() {
        assertThatThrownBy(() -> new Problem("type", "Title", 400, "", "/instance", "correlation", Instant.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("detail cannot be empty");
    }

    @Test
    void testProblemValidationWithNullCorrelationId() {
        assertThatThrownBy(() -> new Problem("type", "Title", 400, "Detail", "/instance", null, Instant.now()))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("correlationId cannot be null");
    }

    @Test
    void testProblemValidationWithEmptyCorrelationId() {
        assertThatThrownBy(() -> new Problem("type", "Title", 400, "Detail", "/instance", "", Instant.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("correlationId cannot be empty");
    }

    @Test
    void testProblemValidationWithNullTimestamp() {
        assertThatThrownBy(() -> new Problem("type", "Title", 400, "Detail", "/instance", "correlation", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("timestamp cannot be null");
    }

    @Test
    void testProblemValidationWithInvalidStatus() {
        assertThatThrownBy(() -> new Problem("type", "Title", 99, "Detail", "/instance", "correlation", Instant.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("status must be between 100 and 599");
    }

    @Test
    void testProblemValidationWithInvalidStatusHigh() {
        assertThatThrownBy(() -> new Problem("type", "Title", 600, "Detail", "/instance", "correlation", Instant.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("status must be between 100 and 599");
    }
}
