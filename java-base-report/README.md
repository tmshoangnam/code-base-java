# java-base-report

Enterprise-grade report generation foundation library for Java applications.

## Overview

`java-base-report` is a pure Java library that provides a contract-first design for report generation. It defines abstractions, SPI interfaces, and utilities for generating reports in various formats (PDF, Excel, Word, CSV, HTML) without any external dependencies.

## Features

- **Contract-First Design**: Clean abstractions separated from implementations
- **Multiple Report Formats**: Support for PDF, Excel, Word, CSV, and HTML
- **Async & Streaming**: Asynchronous report generation with progress tracking and streaming support
- **Thread-Safe**: All components are designed for concurrent usage
- **SPI Integration**: ServiceLoader-based provider discovery
- **Framework Agnostic**: No Spring or other framework dependencies
- **Enterprise Ready**: Built for scalability and maintainability

## Architecture

### Core Interfaces

- `ReportGenerator<T>`: Core synchronous report generation
- `AsyncReportGenerator<T>`: Asynchronous generation with progress tracking
- `StreamingReportGenerator<T>`: Streaming generation for large reports
- `ReportManager`: Registry for managing generators

### Data Transfer Objects

- `ReportRequest<T>`: Request DTO with builder pattern
- `ReportResponse`: Response DTO with metadata
- `StreamingReportResponse`: Streaming response with callbacks
- `ReportMetadata`: Report metadata and information
- `ReportProgress`: Progress tracking for async operations
- `StreamingConfig`: Configuration for streaming operations

### Enums

- `ReportFormat`: Supported report formats (PDF, EXCEL, WORD, CSV, HTML)
- `ReportStatus`: Report generation status
- `TemplateType`: Template engine types
- `StreamingMode`: Streaming modes (CHUNKED, BUFFERED, REACTIVE, etc.)

### SPI Interfaces

- `ReportProvider`: ServiceLoader SPI for report generators
- `TemplateEngine`: Template processing SPI
- `Serializer`: Data serialization SPI

### Internal Components

- `ThreadSafeReportExecutor`: Thread-safe execution with configurable thread pools
- `StreamingReportExecutor`: Streaming execution for large reports
- `ReportTask`: Task wrapper for report generation
- `ReportQueue`: Queue management for tasks

## Usage

### Basic Usage

```java
// Get provider via ServiceLoader
ReportProvider provider = ReportServices.getProvider("pdf");
ReportGenerator<String> generator = provider.getGenerator();

// Create request
ReportRequest<String> request = ReportRequest.<String>builder()
    .format(ReportFormat.PDF)
    .templateName("report-template")
    .data("report data")
    .build();

// Generate report
ReportResponse response = generator.generate(request);
```

### Async Usage

```java
AsyncReportGenerator<String> asyncGenerator = provider.getAsyncGenerator();

CompletableFuture<ReportResponse> future = asyncGenerator.generateWithProgress(
    request, 
    progress -> System.out.println("Progress: " + progress.getPercentage() + "%")
);

ReportResponse response = future.get();
```

### Streaming Usage

```java
StreamingReportGenerator<String> streamingGenerator = provider.getStreamingGenerator();

StreamingConfig config = StreamingConfig.builder()
    .mode(StreamingMode.CHUNKED)
    .chunkSize(8192)
    .build();

StreamingReportResponse streamingResponse = streamingGenerator.generateStreaming(request, config);
```

### Using ReportManager

```java
ReportManager manager = new DefaultReportManager();

// Register generators
manager.registerGenerator(pdfGenerator);
manager.registerAsyncGenerator(asyncPdfGenerator);
manager.registerStreamingGenerator(streamingPdfGenerator);

// Get generators
ReportGenerator<String> generator = manager.getGenerator(ReportFormat.PDF);
AsyncReportGenerator<String> asyncGenerator = manager.getAsyncGenerator(ReportFormat.PDF);
StreamingReportGenerator<String> streamingGenerator = manager.getStreamingGenerator(ReportFormat.PDF);
```

## ServiceLoader Integration

The library uses Java's ServiceLoader mechanism for provider discovery. Providers are automatically discovered from the classpath.

### META-INF/services Configuration

```
# com.tms.base.report.spi.ReportProvider
com.example.PdfReportProvider
com.example.ExcelReportProvider

# com.tms.base.report.spi.TemplateEngine
com.example.ThymeleafTemplateEngine
com.example.FreemarkerTemplateEngine

# com.tms.base.report.spi.Serializer
com.example.JacksonSerializer
com.example.GsonSerializer
```

## Thread Safety

All public interfaces and implementations are thread-safe:

- `ReportManager` uses concurrent collections and read-write locks
- `ThreadSafeReportExecutor` manages thread pools safely
- `ReportQueue` uses concurrent data structures
- All DTOs are immutable or properly synchronized

## Error Handling

The library provides comprehensive error handling:

- `ReportException`: Base exception for all report errors
- `ReportGenerationException`: Generation-specific errors
- `ReportValidationException`: Validation errors
- `ReportTimeoutException`: Timeout errors with timeout information

## Testing

The library includes comprehensive unit tests:

- Interface contract tests
- Thread safety tests
- Exception handling tests
- Builder pattern tests

Run tests with:

```bash
mvn test
```

## Dependencies

- **SLF4J API**: For logging (no implementation required)
- **JUnit 5**: For testing
- **AssertJ**: For fluent assertions
- **Mockito**: For mocking in tests

## Building

```bash
mvn clean compile
mvn test
mvn package
```

## Integration with Spring Boot

This library is designed to work with `java-base-starter-report` which provides:

- Spring Boot AutoConfiguration
- Concrete implementations (PDF, Excel, Word, CSV)
- Template engine integrations
- Security features
- Performance monitoring

## License

This project is part of the TMS Base Libraries suite.

## Contributing

Please follow the engineering guidelines defined in the parent project for:

- Code style and formatting
- Testing requirements
- Documentation standards
- Backward compatibility

## Version

Current version: 1.0.5-SNAPSHOT
