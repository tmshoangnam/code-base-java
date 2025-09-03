Complete Plan & Examples (English Standardized)
1. Objective

Provide a foundation layer with:

Resilience patterns (circuit breaker, retry, timeout, bulkhead).

Error handling contracts (exception hierarchy, problem model).

Structured logging utilities (MDC, correlation ID).

Common utilities (validation, string helpers).

No Spring dependency.

java-base-core = pure Java library.

java-base-starter = Spring Boot integration.

2. Dependencies
Runtime (core library)

Should be managed centrally in BOM:

<dependency>
  <groupId>org.slf4j</groupId>
  <artifactId>slf4j-api</artifactId>
</dependency>

<dependency>
  <groupId>jakarta.validation</groupId>
  <artifactId>jakarta.validation-api</artifactId>
</dependency>

<dependency>
  <groupId>io.github.resilience4j</groupId>
  <artifactId>resilience4j-circuitbreaker</artifactId>
</dependency>
<dependency>
  <groupId>io.github.resilience4j</groupId>
  <artifactId>resilience4j-retry</artifactId>
</dependency>
<dependency>
  <groupId>io.github.resilience4j</groupId>
  <artifactId>resilience4j-timelimiter</artifactId>
</dependency>
<dependency>
  <groupId>io.github.resilience4j</groupId>
  <artifactId>resilience4j-bulkhead</artifactId>
</dependency>

Test only

Also managed via BOM:

<dependency>
  <groupId>org.junit.jupiter</groupId>
  <artifactId>junit-jupiter</artifactId>
  <scope>test</scope>
</dependency>

<dependency>
  <groupId>org.assertj</groupId>
  <artifactId>assertj-core</artifactId>
  <scope>test</scope>
</dependency>

<dependency>
  <groupId>org.mockito</groupId>
  <artifactId>mockito-junit-jupiter</artifactId>
  <scope>test</scope>
</dependency>

Excluded from core

No Spring (spring-boot-starter-*).

No logging implementation (Logback, Log4j2).

No Actuator, Micrometer, OpenTelemetry.

No Spring ProblemDetail.

3. Package Structure
com.mycompany.base.core
 ├── api
 │    ├── error       # Problem model, exceptions, mappers
 │    ├── resilience  # Resilience factories, contracts
 │    ├── logging     # Structured logging utilities
 │    └── util        # Validation and helpers
 ├── internal         # Non-public APIs
 └── CoreServices.java (ServiceLoader support)

4. Code Examples
4.1 Resilience Utilities
package com.mycompany.base.core.api.resilience;

import io.github.resilience4j.circuitbreaker.*;
import io.github.resilience4j.retry.*;
import io.github.resilience4j.timelimiter.*;
import io.github.resilience4j.bulkhead.*;

/**
 * Factory class for creating Resilience4j registries
 * with default configurations.
 *
 * NOTE:
 * - Application code should not instantiate CircuitBreaker or Retry directly.
 * - Always fetch from a registry to simplify lifecycle management.
 *
 * @since 1.0.0
 */
public final class ResilienceFactories {

    private ResilienceFactories() {}

    public static CircuitBreakerRegistry defaultCircuitBreakerRegistry() {
        return CircuitBreakerRegistry.ofDefaults();
    }

    public static RetryRegistry defaultRetryRegistry() {
        return RetryRegistry.ofDefaults();
    }

    public static TimeLimiterRegistry defaultTimeLimiterRegistry() {
        return TimeLimiterRegistry.ofDefaults();
    }

    public static BulkheadRegistry defaultBulkheadRegistry() {
        return BulkheadRegistry.ofDefaults();
    }
}


Usage example:

CircuitBreaker cb = ResilienceFactories.defaultCircuitBreakerRegistry()
                                       .circuitBreaker("payment-service");

Runnable decorated = CircuitBreaker.decorateRunnable(cb, () -> {
    // External API call
});
decorated.run();

4.2 Error Handling
package com.mycompany.base.core.api.error;

/**
 * SPI for mapping exceptions to Problem objects (RFC 7807 style).
 *
 * NOTE:
 * - Core only defines the contract.
 * - Actual Spring integration is implemented in java-base-starter.
 */
public interface ProblemMapper {

    Problem mapBusinessException(BusinessException ex, String correlationId);

    Problem mapServiceUnavailable(Throwable ex, String correlationId);
}

package com.mycompany.base.core.api.error;

/**
 * Base class for business-related exceptions.
 *
 * Usage:
 * throw new BusinessException("USER_NOT_FOUND", "User does not exist");
 */
public class BusinessException extends RuntimeException {
    private final String errorCode;

    public BusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}

package com.mycompany.base.core.api.error;

import java.time.Instant;

/**
 * Problem model aligned with RFC 7807.
 * Pure Java POJO without Spring dependency.
 */
public record Problem(
    String type,
    String title,
    int status,
    String detail,
    String instance,
    String correlationId,
    Instant timestamp
) {}

4.3 Structured Logging
package com.mycompany.base.core.api.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import java.util.Map;

/**
 * Utility for structured logging using MDC and correlation IDs.
 *
 * NOTE:
 * - Core does not enforce JSON formatting (starter decides).
 * - Always clear MDC to prevent context leakage across threads.
 *
 * @since 1.0.0
 */
public final class StructuredLogger {
    private static final Logger logger = LoggerFactory.getLogger(StructuredLogger.class);

    private StructuredLogger() {}

    public static void logBusinessEvent(
        String correlationId,
        String event,
        Map<String, Object> context
    ) {
        try {
            MDC.put("correlationId", correlationId);
            MDC.put("event", event);
            context.forEach((k, v) -> MDC.put(k, String.valueOf(v)));
            logger.info("Business event: {}", event);
        } finally {
            MDC.clear();
        }
    }

    public static void logError(
        String correlationId,
        String message,
        Throwable throwable,
        Map<String, Object> context
    ) {
        try {
            MDC.put("correlationId", correlationId);
            MDC.put("error", "true");
            context.forEach((k, v) -> MDC.put(k, String.valueOf(v)));
            logger.error(message, throwable);
        } finally {
            MDC.clear();
        }
    }
}

4.4 Validation Utilities
package com.mycompany.base.core.api.util;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;

/**
 * Utility class for Jakarta Bean Validation.
 *
 * Usage:
 * ValidationUtils.validate(userDto);
 */
public final class ValidationUtils {
    private static final Validator VALIDATOR =
        Validation.buildDefaultValidatorFactory().getValidator();

    private ValidationUtils() {}

    public static <T> void validate(T obj) {
        Set<ConstraintViolation<T>> violations = VALIDATOR.validate(obj);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException("Validation failed: " + violations);
        }
    }
}

package com.mycompany.base.core.api.util;

/**
 * Simple String helper utilities.
 *
 * NOTE:
 * - Avoid duplicating Apache Commons Lang.
 * - Keep only lightweight helpers frequently needed.
 */
public final class StringUtils {
    private StringUtils() {}

    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static String defaultIfBlank(String str, String defaultStr) {
        return isBlank(str) ? defaultStr : str;
    }
}

5. Documentation & Coding Standards

Class-level Javadoc: Purpose, usage guidelines, design notes.

Method-level Javadoc: Parameters, return values, side effects.

Use NOTE: for important constraints.

Use Usage: for quick example snippets.

Tag public API with @since for version tracking.

package-info.java: Each package explains its purpose.

6. Quality & Testing

Unit tests in core (Resilience, MDC, Validation).

Integration tests only in starter.

Jacoco ≥ 80% coverage.

PIT Mutation Testing for resilience & exception handling.

Revapi/Japicmp for backward compatibility checks.

✅ With this structure, java-base-core is:

Lightweight (no Spring).

Enterprise-ready (contracts, SPI, resilience patterns).

Safe for reuse (dependencies managed via BOM, clear boundaries).