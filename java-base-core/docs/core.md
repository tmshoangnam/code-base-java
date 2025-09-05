# 📚 Java Base Core Documentation

## Overview

`java-base-core` is a comprehensive foundation library for Java applications providing essential utilities, exception handling, and common data models. It follows enterprise best practices and provides a clean, consistent API for common operations.

## Features

### 🛠️ Utilities
- **StringUtils** - String manipulation and validation
- **DateTimeUtils** - Date and time operations using Java 8+ Time API
- **JsonUtils** - JSON serialization/deserialization using Jackson
- **CollectionUtils** - Collection and map operations with null-safety
- **FileUtils** - File and I/O operations with proper resource management

### 🚨 Exception Handling
- **BaseException** - Base class for all custom exceptions
- **BusinessException** - For business rule violations
- **ValidationException** - For validation errors and constraint violations
- **SystemException** - For system-level errors and infrastructure failures
- **ErrorCodeRegistry** - Centralized error code management

### 📦 Data Models
- **CommonResponse** - Standard API response model with status, message, code, data, and metadata

### 🧪 Test Utilities
- **AssertUtils** - Custom assertion utilities for testing
- **FixtureUtils** - Test data generation utilities

## Quick Start

### Maven Dependency

```xml
<dependency>
    <groupId>io.github.tmshoangnam</groupId>
    <artifactId>java-base-core</artifactId>
    <version>1.0.4-SNAPSHOT</version>
</dependency>
```

### Basic Usage

#### String Operations

```java
import io.github.base.core.utils.StringUtils;

// Check if string is blank
if (StringUtils.isBlank(userInput)) {
    throw new IllegalArgumentException("Input cannot be empty");
}

// Default value for blank strings
String result = StringUtils.defaultIfBlank(input, "defaultValue");

// Trim operations
String trimmed = StringUtils.trimToNull(input);  // Returns null if empty
String trimmed = StringUtils.trimToEmpty(input); // Returns empty string if null
```

#### Date and Time Operations

```java
import io.github.base.core.utils.DateTimeUtils;

// Format current time
String timestamp = DateTimeUtils.formatCurrentTime("yyyy-MM-dd HH:mm:ss");
String date = DateTimeUtils.formatCurrentDate("yyyy-MM-dd");

// Parse dates
LocalDate date = DateTimeUtils.parseDate("2023-12-01", "yyyy-MM-dd");
LocalDateTime dateTime = DateTimeUtils.parseDateTime("2023-12-01 14:30:45", "yyyy-MM-dd HH:mm:ss");

// Timezone conversions
ZonedDateTime utcTime = DateTimeUtils.toUtc(LocalDateTime.now());
ZonedDateTime nyTime = DateTimeUtils.toTimezone(LocalDateTime.now(), ZoneId.of("America/New_York"));
```

#### JSON Operations

```java
import io.github.base.core.utils.JsonUtils;

// Serialize to JSON
String json = JsonUtils.toJson(user);
String prettyJson = JsonUtils.toPrettyJson(user);

// Safe serialization (returns null on error)
String json = JsonUtils.toJsonSafe(user);
String prettyJson = JsonUtils.toPrettyJsonSafe(user);

// Deserialize from JSON
User user = JsonUtils.fromJson(json, User.class);

// Safe deserialization (returns null on error)
User user = JsonUtils.fromJsonSafe(json, User.class);

// Validate JSON
boolean isValid = JsonUtils.isValidJson(jsonString);
```

#### Collection Operations

```java
import io.github.base.core.utils.CollectionUtils;

// Null-safe operations
List<String> safeList = CollectionUtils.emptyIfNull(possiblyNullList);
Map<String, User> userMap = CollectionUtils.toMap(users, User::getId);

// Convert between collection types
Set<String> set = CollectionUtils.toSet(list);
List<String> list = CollectionUtils.toList(set);

// Grouping and partitioning
Map<String, List<User>> grouped = CollectionUtils.groupBy(users, User::getDepartment);
Map<Boolean, List<User>> partitioned = CollectionUtils.partition(users, User::isActive);

// Flatten nested collections
List<String> flattened = CollectionUtils.flatten(nestedCollections);

// Merge maps
Map<String, String> merged = CollectionUtils.merge(map1, map2);
Map<String, String> mergedWithResolver = CollectionUtils.merge(map1, map2, v -> "resolved_" + v);
```

#### File Operations

```java


// Read file content
String content=FileUtils.readFile("config.properties");

// Write file content
        FileUtils.writeFile("output.txt","Hello World");

// Create temporary files
        String tempFile=FileUtils.createTempFile("prefix",".txt");
        String tempFile2=FileUtils.createTempFile("prefix");
        String tempFile3=FileUtils.createTempFile();

// Safe resource handling
        FileUtils.withResource(new FileInputStream("data.txt"),inputStream->{
        // Process input stream
        });

// Check file existence
        boolean exists=FileUtils.exists("file.txt");

// Delete file
        boolean deleted=FileUtils.delete("file.txt");

// Get file size
        long size=FileUtils.getFileSize("file.txt");
```

#### Exception Handling

```java
import io.github.base.core.exception.*;

// Register error codes
ErrorCodeRegistry.register("USER_NOT_FOUND", "User does not exist");
ErrorCodeRegistry.register("INSUFFICIENT_BALANCE", "Account balance is insufficient");

// Business rule violation
throw new BusinessException("INSUFFICIENT_BALANCE", 
    "Account balance is insufficient for this transaction",
    Map.of("currentBalance", 100.0, "requiredAmount", 150.0));

// Validation error
throw new ValidationException("INVALID_EMAIL", 
    "Email format is invalid",
    Map.of("field", "email", "value", "invalid-email"));

// System error
throw new SystemException("DATABASE_CONNECTION_FAILED", 
    "Unable to connect to the database",
    Map.of("database", "users", "host", "localhost"));

// Check if error code exists
boolean exists = ErrorCodeRegistry.exists("USER_NOT_FOUND");
String description = ErrorCodeRegistry.getDescription("USER_NOT_FOUND");
```

#### Common Response Model

```java
import io.github.base.core.model.CommonResponse;

// Success response
CommonResponse<User> response = CommonResponse.success(user);
CommonResponse<User> response = CommonResponse.success(user, "User created successfully");

// Error response
CommonResponse<Void> response = CommonResponse.error("USER_NOT_FOUND", "User does not exist");
CommonResponse<Void> response = CommonResponse.error("VALIDATION_ERROR", "Validation failed", 
    Map.of("field", "email", "reason", "Invalid format"));

// Warning response
CommonResponse<String> response = CommonResponse.warning("partial_data", "Some data may be incomplete");

// Custom response with builder
CommonResponse<String> response = CommonResponse.<String>builder()
    .status("success")
    .message("Operation completed")
    .code("OK")
    .data("result")
    .metadata(Map.of("processingTime", "150ms"))
    .build();

// Check response type
if (response.isSuccess()) {
    // Handle success
} else if (response.isError()) {
    // Handle error
} else if (response.isWarning()) {
    // Handle warning
}
```

#### Test Utilities

```java
import io.github.base.core.test.*;

// Custom assertions
AssertUtils.assertNotEmpty(collection, "Collection should not be empty");
AssertUtils.assertValidEmail("user@example.com", "Email should be valid");
AssertUtils.assertBetween(age, 18, 65, "Age should be between 18 and 65");
AssertUtils.assertMatches(phone, "\\(\\d{3}\\) \\d{3}-\\d{4}", "Phone should match format");
AssertUtils.assertNotBlank(name, "Name should not be blank");
AssertUtils.assertInstanceOf(obj, String.class, "Object should be String");

// Test data generation
String email = FixtureUtils.randomEmail();
String name = FixtureUtils.randomName();
String phone = FixtureUtils.randomPhoneNumber();
String address = FixtureUtils.randomStreetAddress();
String city = FixtureUtils.randomCity();
String state = FixtureUtils.randomState();
String zipCode = FixtureUtils.randomZipCode();

LocalDate birthDate = FixtureUtils.randomBirthDate();
LocalDateTime dateTime = FixtureUtils.randomDateTime();

int age = FixtureUtils.randomInt(18, 65);
double salary = FixtureUtils.randomDouble(30000, 100000);
boolean isActive = FixtureUtils.randomBoolean();

String randomString = FixtureUtils.randomString(10);
String uuid = FixtureUtils.randomUuid();

// Generate collections
List<String> names = FixtureUtils.randomList(5, FixtureUtils::randomName);
Set<String> emails = FixtureUtils.randomSet(3, FixtureUtils::randomEmail);

// Generate test objects
User user = FixtureUtils.createUser();
List<User> users = FixtureUtils.createUsers(10);
```

## Best Practices

### 1. Error Handling
- Use specific exception types (BusinessException, ValidationException, SystemException)
- Register error codes in ErrorCodeRegistry for consistency
- Include meaningful context information in exceptions
- Use CommonResponse for API responses

### 2. Utility Usage
- Use null-safe operations from CollectionUtils
- Prefer DateTimeUtils over manual date formatting
- Use JsonUtils for JSON operations instead of manual parsing
- Use FileUtils for file operations with proper resource management

### 3. Testing
- Use AssertUtils for custom assertions
- Use FixtureUtils for generating test data
- Write descriptive test names and assertions
- Use meaningful test data that reflects real-world scenarios

### 4. Performance
- Use static imports for utility methods
- Prefer immutable objects where possible
- Use appropriate collection types for your use case
- Handle resources properly with try-with-resources or FileUtils

## Migration Guide

### From Old Package Structure
If you're migrating from the old package structure (`com.mycompany.base.core.api`), update your imports:

```java
// Old imports
import com.mycompany.base.core.api.util.StringUtils;
import com.mycompany.base.core.api.error.BusinessException;

// New imports
import io.github.base.core.utils.StringUtils;
import io.github.base.core.exception.BusinessException;
```

### Breaking Changes
- Package names have changed from `com.mycompany.base.core.api` to `io.github.base.core`
- Some method signatures may have changed for better consistency
- New utility classes have been added (CollectionUtils, FileUtils)
- New exception types have been added (ValidationException, SystemException)

## Contributing

1. Follow the existing code style and patterns
2. Add comprehensive tests for new functionality
3. Update documentation for new features
4. Ensure all tests pass and coverage is maintained
5. Follow semantic versioning for releases

## License

This project is licensed under the MIT License - see the LICENSE file for details.
