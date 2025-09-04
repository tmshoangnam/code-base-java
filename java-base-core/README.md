# java-base-core

Enterprise-grade foundation library for Java applications providing common utilities, resilience patterns, and error handling capabilities.

## Overview

`java-base-core` is a framework-agnostic foundation library designed for enterprise Java applications. It provides essential utilities and patterns that are commonly needed across different projects while maintaining minimal dependencies and clean architecture principles.

## Features

### 🛡️ Resilience Patterns
- **Circuit Breaker** - Prevents cascading failures
- **Retry** - Automatic retry with configurable strategies  
- **Time Limiter** - Timeout protection for operations
- **Bulkhead** - Resource isolation and concurrency control

### 🚨 Error Handling
- **BusinessException** - For business rule violations
- **ServiceException** - For infrastructure failures
- **Problem** - RFC 7807 compliant problem model
- **ProblemMapper** - SPI for exception-to-problem mapping

### 📝 Structured Logging
- **StructuredLogger** - MDC-based structured logging
- **CorrelationIdGenerator** - Unique correlation ID generation
- **Thread-safe operations** with proper cleanup

### 🔧 Common Utilities
- **StringUtils** - String manipulation and validation
- **DateUtils** - Date/time operations using Java 8+ Time API
- **JsonUtils** - JSON serialization/deserialization
- **ValidationUtils** - Jakarta Bean Validation utilities

## Design Principles

- **Framework-agnostic** - No Spring or other framework dependencies
- **Thread-safe** - All public methods are thread-safe
- **Null-safe** - Proper null handling throughout
- **Minimal dependencies** - Only essential libraries included
- **Backward compatible** - Stable API with semantic versioning

## Dependencies

### Runtime Dependencies
- `org.slf4j:slf4j-api` - Logging API
- `jakarta.validation:jakarta.validation-api` - Bean validation
- `io.github.resilience4j:*` - Resilience patterns
- `com.fasterxml.jackson.core:*` - JSON processing (optional)

### Test Dependencies
- `org.junit.jupiter:junit-jupiter` - Testing framework
- `org.assertj:assertj-core` - Assertions
- `org.mockito:mockito-core` - Mocking framework

## Quick Start

### Maven Dependency

```xml
<dependency>
    <groupId>io.github.tms-hoangnam</groupId>
    <artifactId>java-base-core</artifactId>
    <version>1.0.1-SNAPSHOT</version>
</dependency>
```

### Basic Usage Examples

#### Resilience Patterns

```java
import com.mycompany.base.core.api.resilience.ResilienceFactories;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;

// Create circuit breaker
CircuitBreaker cb = ResilienceFactories.defaultCircuitBreakerRegistry()
                                       .circuitBreaker("payment-service");

// Decorate operation
Runnable decorated = CircuitBreaker.decorateRunnable(cb, () -> {
    // External API call
    paymentService.processPayment();
});
decorated.run();
```

#### Error Handling

```java
import com.mycompany.base.core.api.error.BusinessException;
import com.mycompany.base.core.api.error.Problem;

// Business rule violation
throw new BusinessException("INSUFFICIENT_BALANCE", 
    "Account balance is insufficient for this transaction");

// Create problem object
Problem problem = new Problem(
    "https://api.example.com/problems/validation-error",
    "Validation Failed",
    400,
    "The request body contains invalid data",
    "/api/users/123",
    "req-12345",
    Instant.now()
);
```

#### Structured Logging

```java
import com.mycompany.base.core.api.logging.StructuredLogger;
import com.mycompany.base.core.api.logging.CorrelationIdGenerator;

// Generate correlation ID
String correlationId = CorrelationIdGenerator.generate("payment");

// Log business event
StructuredLogger.logBusinessEvent(correlationId, "PAYMENT_PROCESSED", 
    Map.of("amount", 99.99, "currency", "USD"));

// Log error
StructuredLogger.logError(correlationId, "Payment failed", exception, 
    Map.of("orderId", "order123"));
```

#### Common Utilities

```java


// String operations
if(StringUtils.isBlank(userInput)){
        throw new IllegalArgumentException("Input cannot be empty");
        }

// Date operations
        String timestamp=DateUtils.formatCurrentTime("yyyy-MM-dd HH:mm:ss");

// JSON operations
        String json=JsonUtils.toJson(user);
        User user=JsonUtils.fromJson(json,User.class);

// Validation
        ValidationUtils.validate(userDto);
```

## Package Structure

```
com.mycompany.base.core.api
├── error/           # Exception hierarchy and error handling
├── logging/         # Structured logging utilities
├── resilience/      # Resilience pattern factories
└── util/           # Common utility classes
```

## API Documentation

### Resilience Layer

#### ResilienceFactories
Factory class for creating Resilience4j registries with default configurations.

```java
// Create registries
CircuitBreakerRegistry cbRegistry = ResilienceFactories.defaultCircuitBreakerRegistry();
RetryRegistry retryRegistry = ResilienceFactories.defaultRetryRegistry();
TimeLimiterRegistry tlRegistry = ResilienceFactories.defaultTimeLimiterRegistry();
BulkheadRegistry bhRegistry = ResilienceFactories.defaultBulkheadRegistry();
```

### Error Handling

#### BusinessException
Base class for business-related exceptions.

```java
// Simple business exception
throw new BusinessException("USER_NOT_FOUND", "User does not exist");

// With context
throw new BusinessException("INSUFFICIENT_BALANCE", 
    "Account balance is insufficient",
    Map.of("currentBalance", 100.0, "requiredAmount", 150.0));
```

#### ServiceException
Base class for service-related exceptions.

```java
// Service failure
throw new ServiceException("DATABASE_CONNECTION_FAILED", 
    "Unable to connect to the database");

// With context
throw new ServiceException("EXTERNAL_API_TIMEOUT", 
    "Payment service did not respond within timeout",
    Map.of("service", "payment-service", "timeout", "30s"));
```

#### Problem
RFC 7807 compliant problem model.

```java
Problem problem = new Problem(
    "https://api.example.com/problems/validation-error",
    "Validation Failed",
    400,
    "The request body contains invalid data",
    "/api/users/123",
    "req-12345",
    Instant.now(),
    Map.of("field", "email", "reason", "Invalid format")
);
```

### Logging

#### StructuredLogger
Utility for structured logging using MDC.

```java
// Business event
StructuredLogger.logBusinessEvent(correlationId, "USER_LOGIN", 
    Map.of("userId", "user123", "ipAddress", "192.168.1.1"));

// Error logging
StructuredLogger.logError(correlationId, "Failed to process payment", 
    exception, Map.of("orderId", "order456", "amount", 99.99));

// Info logging
StructuredLogger.logInfo(correlationId, "Operation completed successfully", 
    Map.of("duration", "150ms"));

// Warning logging
StructuredLogger.logWarning(correlationId, "Rate limit approaching", 
    Map.of("currentRate", 90, "limit", 100));
```

#### CorrelationIdGenerator
Utility for generating unique correlation IDs.

```java
// Default format: req-20231201-143022-abc123def456
String correlationId = CorrelationIdGenerator.generate();

// Custom prefix: payment-20231201-143022-abc123def456
String correlationId = CorrelationIdGenerator.generate("payment");

// Custom prefix and length: api-20231201-143022-abc12345
String correlationId = CorrelationIdGenerator.generate("api", 8);

// Simple format: test-abc123def456
String correlationId = CorrelationIdGenerator.generateSimple("test");
```

### Utilities

#### StringUtils
String manipulation and validation utilities.

```java
// Check if string is blank
if (StringUtils.isBlank(input)) {
    // Handle empty input
}

// Check if string has text
if (StringUtils.hasText(input)) {
    // Process non-empty input
}

// Default value for blank strings
String result = StringUtils.defaultIfBlank(input, "defaultValue");

// Trim operations
String trimmed = StringUtils.trimToNull(input);  // Returns null if empty
String trimmed = StringUtils.trimToEmpty(input); // Returns empty string if null
```

#### DateUtils
Date and time operations using Java 8+ Time API.

```java
// Format current time
String timestamp = DateUtils.formatCurrentTime("yyyy-MM-dd HH:mm:ss");
String date = DateUtils.formatCurrentDate("yyyy-MM-dd");

// Format specific dates
String formatted = DateUtils.formatDateTime(dateTime, "yyyy-MM-dd HH:mm:ss");
String formatted = DateUtils.formatDate(date, "yyyy-MM-dd");

// Parse dates
LocalDate date = DateUtils.parseDate("2023-12-01", "yyyy-MM-dd");
LocalDateTime dateTime = DateUtils.parseDateTime("2023-12-01 14:30:45", "yyyy-MM-dd HH:mm:ss");

// Timezone conversions
ZonedDateTime utcTime = DateUtils.toUtc(localDateTime);
ZonedDateTime nyTime = DateUtils.toTimezone(localDateTime, ZoneId.of("America/New_York"));

// Instant conversions
LocalDateTime dateTime = DateUtils.fromInstant(instant);
Instant instant = DateUtils.toInstant(localDateTime);

// ISO format
String iso = DateUtils.formatAsIso(instant);
Instant instant = DateUtils.parseIso("2023-12-01T14:30:45Z");
```

#### JsonUtils
JSON serialization and deserialization using Jackson.

```java
// Serialize to JSON
String json = JsonUtils.toJson(object);
String prettyJson = JsonUtils.toPrettyJson(object);

// Safe serialization (returns null on error)
String json = JsonUtils.toJsonSafe(object);
String prettyJson = JsonUtils.toPrettyJsonSafe(object);

// Deserialize from JSON
MyObject obj = JsonUtils.fromJson(json, MyObject.class);

// Safe deserialization (returns null on error)
MyObject obj = JsonUtils.fromJsonSafe(json, MyObject.class);

// Validate JSON
boolean isValid = JsonUtils.isValidJson(jsonString);

// Get ObjectMapper for advanced usage
ObjectMapper mapper = JsonUtils.getObjectMapper();
```

#### ValidationUtils
Jakarta Bean Validation utilities.

```java
// Validate object and throw exception on failure
ValidationUtils.validate(userDto);

// Validate object and get violations
Set<ConstraintViolation<UserDto>> violations = ValidationUtils.validateAndGet(userDto);
if (!violations.isEmpty()) {
    // Handle validation errors
}

// Validate specific property
ValidationUtils.validateProperty(userDto, "email");
Set<ConstraintViolation<UserDto>> violations = ValidationUtils.validateProperty(userDto, "email");

// Check if object is valid
boolean isValid = ValidationUtils.isValid(userDto);
boolean isPropertyValid = ValidationUtils.isPropertyValid(userDto, "email");

// Get Validator for advanced usage
Validator validator = ValidationUtils.getValidator();
```

## Testing

The library includes comprehensive unit tests with 80%+ code coverage. Run tests using:

```bash
mvn test
```

Generate test coverage report:

```bash
mvn jacoco:report
```

## Building

Build the library:

```bash
mvn clean compile
```

Run tests:

```bash
mvn test
```

Package the library:

```bash
mvn package
```

## Versioning

This project follows [Semantic Versioning](https://semver.org/):
- **MAJOR** version for incompatible API changes
- **MINOR** version for backwards-compatible functionality additions
- **PATCH** version for backwards-compatible bug fixes

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass
6. Submit a pull request

## Support

For questions, issues, or contributions, please:
- Open an issue on GitHub
- Contact the development team
- Check the documentation

---

**Note**: This library is designed to be framework-agnostic and focuses on providing essential utilities for enterprise Java applications. For Spring Boot integration, see the `java-base-starter` module.
