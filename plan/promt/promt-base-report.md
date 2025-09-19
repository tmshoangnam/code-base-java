You are a Senior Software Engineer + System Architect (or an AI coding agent acting like one).
Your task: design and implement a production-ready, enterprise-grade report generation foundation split into two modules:
	• java-base-report — pure Java library (jar) that defines report generation contracts, SPI, and utilities (no Spring, no external dependencies).
	• java-base-starter-report — Spring Boot starter that provides concrete report generator implementations and auto-configuration for PDF, Excel, Word, CSV, and demonstrates full runtime wiring (including ServiceLoader fallback).

📦 java-base-report Design Plan

🎯 Overall Objectives
	• Spring Boot Starter Ecosystem
		○ Cung cấp auto-configuration + starter module.
		○ Sẵn sàng plug-and-play trong Spring Boot apps.
	• Contract-First Design
		○ java-base-report định nghĩa contracts & SPI (không phụ thuộc Spring).
		○ java-base-starter-report mới chứa implementation (PDF, Excel, Word, CSV).
	• Scalability & Maintainability
		○ Dễ mở rộng (custom report generators, new formats).
		○ Backward-compatible API.
		○ Đảm bảo dùng dài hạn trong các enterprise projects.
	• Thread Safety & Performance
		○ Thread-safe report generation.
		○ Streaming support cho large reports.
		○ Async operations với progress tracking.

📂 Module Structure

java-base-parent
├── java-base-bom (pom)
├── java-base-core (jar)
├── java-base-cache (jar)
├── java-base-observability (jar)
├── java-base-security (jar)
├── java-base-file (jar)              <-- File storage contracts
├── java-base-aws (jar)               <-- AWS service contracts
├── java-base-report (jar)            <-- Report generation contracts
└── java-base-starter-report (jar)    <-- Spring Boot auto-config & impl

📦 java-base-report (jar, framework-agnostic)
Top-level package: com.tms.base.report

com.tms.base.report
 ├─ api
 │   ├─ ReportGenerator.java              // Core sync API
 │   ├─ AsyncReportGenerator.java         // Async API với progress tracking
 │   ├─ StreamingReportGenerator.java     // Streaming API cho large reports
 │   ├─ ReportManager.java                // Registry cho named generators
 │   └─ ReportException.java              // Runtime exceptions
 ├─ dto
 │   ├─ ReportRequest.java                // Request DTO
 │   ├─ ReportResponse.java               // Response DTO
 │   ├─ StreamingReportResponse.java      // Streaming response DTO
 │   ├─ ReportMetadata.java               // Metadata
 │   ├─ ReportProgress.java               // Progress tracking
 │   └─ StreamingConfig.java              // Streaming configuration
 ├─ enums
 │   ├─ ReportFormat.java                 // PDF, EXCEL, WORD, CSV, HTML
 │   ├─ ReportStatus.java                 // SUCCESS, FAILED, PROCESSING
 │   ├─ TemplateType.java                 // HTML, XML, JSON
 │   └─ StreamingMode.java                // CHUNKED, BUFFERED, REACTIVE, PAGINATED, CONTINUOUS
 ├─ spi
 │   ├─ ReportProvider.java               // ServiceLoader SPI
 │   ├─ TemplateEngine.java               // Template engine SPI
 │   └─ Serializer.java                   // Serialization SPI
 ├─ internal
 │   ├─ ThreadSafeReportExecutor.java     // Thread-safe execution
 │   ├─ StreamingReportExecutor.java      // Streaming execution
 │   ├─ ReportTask.java                   // Task wrapper
 │   └─ ReportQueue.java                  // Queue management
 └─ ReportServices.java                  // ServiceLoader helper

✅ Characteristics:
	• Pure Java, không Spring, không external dependencies.
	• Định nghĩa chuẩn về ReportGenerator, AsyncReportGenerator, StreamingReportGenerator.
	• SPI để load provider (PDF, Excel, Word, CSV…) bằng ServiceLoader.
	• Support multiple report formats và streaming modes.
	• Thread-safe và performance-optimized.

📦 java-base-starter-report (Spring Boot starter)
Top-level package: com.tms.base.starter.report

com.tms.base.starter.report
 ├─ autoconfig
 │   ├─ ReportAutoConfiguration.java      // Auto-configuration
 │   ├─ ReportProperties.java             // Configuration properties
 │   ├─ ThreadPoolConfig.java             // Thread pool configuration
 │   └─ ReportSecurityConfig.java         // Security configuration
 ├─ impl
 │   ├─ pdf
 │   │   ├─ ItextPdfReportGenerator.java  // iText implementation
 │   │   ├─ FlyingSaucerPdfGenerator.java // Flying Saucer implementation
 │   │   └─ PdfReportProvider.java        // PDF provider
 │   ├─ excel
 │   │   ├─ PoiExcelReportGenerator.java  // Apache POI implementation
 │   │   ├─ EasyExcelReportGenerator.java // EasyExcel implementation
 │   │   └─ ExcelReportProvider.java      // Excel provider
 │   ├─ word
 │   │   ├─ PoiWordReportGenerator.java   // Apache POI Word implementation
 │   │   └─ WordReportProvider.java       // Word provider
 │   └─ csv
 │       ├─ CsvReportGenerator.java       // CSV implementation
 │       └─ CsvReportProvider.java        // CSV provider
 ├─ template
 │   ├─ ThymeleafTemplateEngine.java      // Thymeleaf engine
 │   ├─ FreemarkerTemplateEngine.java     // FreeMarker engine
 │   └─ MustacheTemplateEngine.java       // Mustache engine
 ├─ serializer
 │   ├─ JacksonSerializer.java            // Jackson serializer
 │   ├─ GsonSerializer.java               // Gson serializer
 │   └─ DefaultSerializer.java            // Default serializer
 ├─ service
 │   ├─ ReportService.java                // Main service
 │   ├─ AsyncReportService.java           // Async service
 │   ├─ StreamingReportService.java       // Streaming service
 │   └─ ReportValidationService.java      // Validation service
 ├─ util
 │   ├─ ReportUtils.java                  // Utility methods
 │   ├─ FileUtils.java                    // File utilities
 │   └─ ThreadUtils.java                  // Thread utilities
 └─ resources
     ├─ META-INF/services/com.tms.base.report.spi.ReportProvider
     ├─ templates/pdf/
     ├─ templates/excel/
     └─ application-report.yml

✅ Characteristics:
	• Auto-config cho ReportService, AsyncReportService, StreamingReportService.
	• Hỗ trợ chọn format và engine qua application.yml:

base.report.default-format: PDF
base.report.pdf.enabled: true
base.report.pdf.engine: itext
base.report.excel.enabled: true
base.report.excel.engine: poi
base.report.streaming.enabled: true
base.report.streaming.default-mode: CHUNKED
	• Tích hợp với java-base-core (resilience, logging).
	• Tích hợp với java-base-cache (template caching).
	• Tích hợp với java-base-security (access control, encryption).
	• Tích hợp với java-base-observability (metrics, tracing).

🧩 Key Components

1. Report Generation Contract
	• ReportGenerator: core interface cho sync operations (generate, supports, getName).
	• AsyncReportGenerator: async operations với progress tracking và cancellation.
	• StreamingReportGenerator: streaming operations cho large reports.
	• Support cho 5 report formats: PDF, EXCEL, WORD, CSV, HTML.

2. Report Data Transfer Objects
	• ReportRequest/ReportResponse: standard request/response cho sync operations.
	• StreamingReportResponse: streaming response với InputStream và callbacks.
	• ReportMetadata: chứa thông tin report (format, template, parameters).
	• ReportProgress: progress tracking cho async operations.

3. Report Provider SPI
	• ReportProvider: loadable via ServiceLoader.
	• Trả về implementation cho ReportGenerator, AsyncReportGenerator, StreamingReportGenerator.
	• Support cho PDF (iText, Flying Saucer), Excel (POI, EasyExcel), Word, CSV.

4. Integration Layer
	• Decorator pattern cho resilience, logging, caching, security.
	• Tích hợp với existing base modules.
	• Spring Boot auto-configuration.
	• HTTP streaming với Spring MVC.

📖 Example Usage

Core (framework-agnostic):

ReportProvider provider = ReportServices.findProvider("pdf");
ReportGenerator<String> generator = provider.getGenerator();

ReportRequest<String> request = ReportRequest.<String>builder()
    .format(ReportFormat.PDF)
    .templateName("driver-report")
    .data("test data")
    .build();

ReportResponse response = generator.generate(request);

Spring Boot App:

base.report.default-format: PDF
base.report.pdf.enabled: true
base.report.streaming.enabled: true

@RestController
class ReportController {
    private final ReportService reportService;
    private final StreamingReportService streamingReportService;
    
    @PostMapping("/reports/pdf")
    ResponseEntity<byte[]> generatePdfReport(@RequestBody ReportData data) {
        ReportRequest<ReportData> request = ReportRequest.<ReportData>builder()
            .format(ReportFormat.PDF)
            .templateName("company-report")
            .data(data)
            .build();
            
        ReportResponse response = reportService.generate(request);
        return ResponseEntity.ok(response.getData());
    }
    
    @PostMapping("/reports/streaming/pdf")
    ResponseEntity<StreamingResponseBody> generateStreamingPdf(@RequestBody ReportData data) {
        StreamingConfig config = StreamingConfig.builder()
            .mode(StreamingMode.CHUNKED)
            .chunkSize(8192)
            .build();
            
        StreamingResponseBody responseBody = streamingReportService.createStreamingResponseBody(request, config);
        return ResponseEntity.ok(responseBody);
    }
}

🧪 Testing & Quality
	• Unit tests (java-base-report): contracts, SPI loading, exceptions.
	• Integration tests (java-base-starter-report): PDF, Excel, Word, CSV generators.
	• Thread safety tests cho concurrent operations.
	• Streaming performance tests.
	• Code coverage ≥ 80% (Jacoco).
	• Backward compatibility check (Japicmp / Revapi).

✅ Standards
	• Java 21+
	• Javadoc đầy đủ cho public API.
	• Static utils: final class + private constructor.
	• Không phụ thuộc Spring trong core.
	• Package spi chỉ để load provider.
	• Follows SOLID, KISS, DRY.

🚀 Deliverables
	• java-base-report: abstraction + SPI.
	• java-base-starter-report: Spring Boot auto-config + PDF/Excel/Word/CSV generators.
	• README.md: mô tả design, usage, extensibility.
	• Sample demo app dùng multiple report formats và streaming.

## 📋 Checklist Triển Khai

### Phase 1: Setup Module Structure
- [ ] Tạo cấu trúc thư mục theo plan
- [ ] Setup pom.xml với dependencies cơ bản
- [ ] Tạo package structure chuẩn

### Phase 2: Core Interfaces & Enums
- [ ] `ReportGenerator<T>` - Core sync API
- [ ] `AsyncReportGenerator<T>` - Async API với progress tracking
- [ ] `StreamingReportGenerator<T>` - Streaming API cho large reports
- [ ] `ReportManager` - Registry cho named generators
- [ ] `ReportException` - Runtime exceptions
- [ ] `ReportConfig` - Configuration
- [ ] `ReportRequest<T>` - Request DTO
- [ ] Enums: `ReportFormat`, `ReportStatus`, `TemplateType`, `StreamingMode`

### Phase 3: SPI Interfaces
- [ ] `ReportProvider` - ServiceLoader SPI
- [ ] `TemplateEngine` - Template engine SPI
- [ ] `Serializer` - Serialization SPI

### Phase 4: DTOs & Internal Classes
- [ ] `ReportResponse` - Response DTO
- [ ] `StreamingReportResponse` - Streaming response DTO
- [ ] `ReportMetadata` - Metadata
- [ ] `ReportProgress` - Progress tracking
- [ ] `StreamingConfig` - Streaming configuration
- [ ] `ThreadSafeReportExecutor` - Thread-safe execution
- [ ] `StreamingReportExecutor` - Streaming execution
- [ ] `ReportTask` - Task wrapper
- [ ] `ReportQueue` - Queue management

### Phase 5: ServiceLoader Integration
- [ ] `ReportServices` - ServiceLoader helper
- [ ] META-INF/services configuration

### Phase 6: Testing
- [ ] Unit tests cho tất cả interfaces
- [ ] Mock implementations
- [ ] Thread safety tests

## 🚀 Bắt Đầu Triển Khai

### Bước 1: Tạo Module Structure
```bash
# Tạo thư mục java-base-report
mkdir java-base-report
cd java-base-report

# Tạo cấu trúc thư mục
mkdir -p src/main/java/com/tms/base/report/{api,spi,enums,dto,internal}
mkdir -p src/main/resources/META-INF/services
mkdir -p src/test/java/com/tms/base/report
```

### Bước 2: Setup pom.xml
Tạo file `pom.xml` với:
- Parent: `java-base-parent`
- Dependencies: `java-base-core`
- Packaging: `jar`
- Version: `1.0.0-SNAPSHOT`

### Bước 3: Triển Khai Core Interfaces

#### 3.1 ReportGenerator Interface
```java
package com.tms.base.report.api;

import com.tms.base.report.dto.ReportRequest;
import com.tms.base.report.dto.ReportResponse;
import com.tms.base.report.enums.ReportFormat;
import java.util.concurrent.CompletableFuture;

/**
 * Core interface for report generation.
 * Thread-safe and framework-agnostic.
 * 
 * @param <T> Type of data to be processed
 * @since 1.0.0
 */
public interface ReportGenerator<T> {
    
    /**
     * Generate report synchronously.
     * 
     * @param request Report generation request
     * @return Generated report response
     * @throws ReportException if generation fails
     */
    ReportResponse generate(ReportRequest<T> request) throws ReportException;
    
    /**
     * Generate report asynchronously.
     * 
     * @param request Report generation request
     * @return CompletableFuture with report response
     */
    CompletableFuture<ReportResponse> generateAsync(ReportRequest<T> request);
    
    /**
     * Check if this generator supports the given format.
     * 
     * @param format Report format
     * @return true if supported, false otherwise
     */
    boolean supports(ReportFormat format);
    
    /**
     * Get generator name.
     * 
     * @return Generator name
     */
    String getName();
    
    /**
     * Get supported formats.
     * 
     * @return Array of supported formats
     */
    ReportFormat[] getSupportedFormats();
}
```

#### 3.2 AsyncReportGenerator Interface
```java
package com.tms.base.report.api;

import com.tms.base.report.dto.ReportRequest;
import com.tms.base.report.dto.ReportResponse;
import com.tms.base.report.dto.ReportProgress;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Enhanced async report generator with progress tracking.
 * 
 * @param <T> Type of data to be processed
 * @since 1.0.0
 */
public interface AsyncReportGenerator<T> extends ReportGenerator<T> {
    
    /**
     * Generate report with progress callback.
     * 
     * @param request Report generation request
     * @param progressCallback Progress callback function
     * @return CompletableFuture with report response
     */
    CompletableFuture<ReportResponse> generateWithProgress(
        ReportRequest<T> request, 
        Consumer<ReportProgress> progressCallback
    );
    
    /**
     * Cancel ongoing report generation.
     * 
     * @param requestId Request ID to cancel
     * @return true if cancelled successfully
     */
    boolean cancel(String requestId);
    
    /**
     * Get progress of ongoing generation.
     * 
     * @param requestId Request ID
     * @return Progress information
     */
    ReportProgress getProgress(String requestId);
}
```

#### 3.3 StreamingReportGenerator Interface
```java
package com.tms.base.report.api;

import com.tms.base.report.dto.ReportRequest;
import com.tms.base.report.dto.StreamingReportResponse;
import com.tms.base.report.dto.StreamingConfig;
import com.tms.base.report.enums.StreamingMode;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Streaming report generator for large reports.
 * Supports chunked streaming and real-time data processing.
 * 
 * @param <T> Type of data to be processed
 * @since 1.0.0
 */
public interface StreamingReportGenerator<T> extends ReportGenerator<T> {
    
    /**
     * Generate report with streaming response.
     * 
     * @param request Report generation request
     * @param config Streaming configuration
     * @return Streaming report response
     */
    StreamingReportResponse generateStreaming(ReportRequest<T> request, StreamingConfig config);
    
    /**
     * Generate report with streaming and progress callback.
     * 
     * @param request Report generation request
     * @param config Streaming configuration
     * @param progressCallback Progress callback function
     * @return Streaming report response
     */
    StreamingReportResponse generateStreamingWithProgress(
        ReportRequest<T> request, 
        StreamingConfig config,
        Consumer<ReportProgress> progressCallback
    );
    
    /**
     * Generate report as streaming async.
     * 
     * @param request Report generation request
     * @param config Streaming configuration
     * @return CompletableFuture with streaming response
     */
    CompletableFuture<StreamingReportResponse> generateStreamingAsync(
        ReportRequest<T> request, 
        StreamingConfig config
    );
    
    /**
     * Check if this generator supports streaming mode.
     * 
     * @param mode Streaming mode
     * @return true if supported, false otherwise
     */
    boolean supportsStreaming(StreamingMode mode);
    
    /**
     * Get supported streaming modes.
     * 
     * @return Array of supported streaming modes
     */
    StreamingMode[] getSupportedStreamingModes();
    
    /**
     * Get maximum chunk size for streaming.
     * 
     * @return Maximum chunk size in bytes
     */
    int getMaxChunkSize();
    
    /**
     * Get recommended buffer size for streaming.
     * 
     * @return Recommended buffer size in bytes
     */
    int getRecommendedBufferSize();
}
```

### Bước 4: Triển Khai Enums

#### 4.1 ReportFormat Enum
```java
package com.tms.base.report.enums;

/**
 * Supported report formats.
 * 
 * @since 1.0.0
 */
public enum ReportFormat {
    PDF("pdf", "application/pdf"),
    EXCEL("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    WORD("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    CSV("csv", "text/csv"),
    HTML("html", "text/html");
    
    private final String extension;
    private final String mimeType;
    
    ReportFormat(String extension, String mimeType) {
        this.extension = extension;
        this.mimeType = mimeType;
    }
    
    public String getExtension() {
        return extension;
    }
    
    public String getMimeType() {
        return mimeType;
    }
}
```

#### 4.2 StreamingMode Enum
```java
package com.tms.base.report.enums;

/**
 * Supported streaming modes for report generation.
 * 
 * @since 1.0.0
 */
public enum StreamingMode {
    CHUNKED("chunked", "Chunked transfer encoding"),
    BUFFERED("buffered", "Buffered streaming with configurable buffer size"),
    REACTIVE("reactive", "Reactive streaming with backpressure"),
    PAGINATED("paginated", "Paginated streaming for large datasets"),
    CONTINUOUS("continuous", "Continuous streaming for real-time data");
    
    private final String value;
    private final String description;
    
    StreamingMode(String value, String description) {
        this.value = value;
        this.description = description;
    }
    
    public String getValue() {
        return value;
    }
    
    public String getDescription() {
        return description;
    }
}
```

### Bước 5: Triển Khai DTOs

#### 5.1 StreamingReportResponse DTO
```java
package com.tms.base.report.dto;

import com.tms.base.report.enums.ReportFormat;
import com.tms.base.report.enums.StreamingMode;
import java.io.InputStream;
import java.util.Map;
import java.util.HashMap;
import java.util.function.Consumer;

/**
 * Response DTO for streaming report generation.
 * 
 * @since 1.0.0
 */
public class StreamingReportResponse {
    
    private ReportFormat format;
    private String fileName;
    private String mimeType;
    private StreamingMode streamingMode;
    private InputStream dataStream;
    private long totalSize;
    private int chunkSize;
    private Map<String, Object> metadata;
    private Consumer<byte[]> chunkConsumer;
    private Runnable onComplete;
    private Runnable onError;
    
    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private StreamingReportResponse response = new StreamingReportResponse();
        
        public Builder format(ReportFormat format) {
            response.format = format;
            return this;
        }
        
        public Builder fileName(String fileName) {
            response.fileName = fileName;
            return this;
        }
        
        public Builder mimeType(String mimeType) {
            response.mimeType = mimeType;
            return this;
        }
        
        public Builder streamingMode(StreamingMode streamingMode) {
            response.streamingMode = streamingMode;
            return this;
        }
        
        public Builder dataStream(InputStream dataStream) {
            response.dataStream = dataStream;
            return this;
        }
        
        public Builder totalSize(long totalSize) {
            response.totalSize = totalSize;
            return this;
        }
        
        public Builder chunkSize(int chunkSize) {
            response.chunkSize = chunkSize;
            return this;
        }
        
        public Builder metadata(Map<String, Object> metadata) {
            response.metadata = metadata;
            return this;
        }
        
        public Builder chunkConsumer(Consumer<byte[]> chunkConsumer) {
            response.chunkConsumer = chunkConsumer;
            return this;
        }
        
        public Builder onComplete(Runnable onComplete) {
            response.onComplete = onComplete;
            return this;
        }
        
        public Builder onError(Runnable onError) {
            response.onError = onError;
            return this;
        }
        
        public StreamingReportResponse build() {
            if (response.metadata == null) {
                response.metadata = new HashMap<>();
            }
            return response;
        }
    }
    
    // Getters and setters...
    public ReportFormat getFormat() { return format; }
    public String getFileName() { return fileName; }
    public String getMimeType() { return mimeType; }
    public StreamingMode getStreamingMode() { return streamingMode; }
    public InputStream getDataStream() { return dataStream; }
    public long getTotalSize() { return totalSize; }
    public int getChunkSize() { return chunkSize; }
    public Map<String, Object> getMetadata() { return metadata; }
    public Consumer<byte[]> getChunkConsumer() { return chunkConsumer; }
    public Runnable getOnComplete() { return onComplete; }
    public Runnable getOnError() { return onError; }
}
```

#### 5.2 StreamingConfig DTO
```java
package com.tms.base.report.dto;

import com.tms.base.report.enums.StreamingMode;
import java.util.Map;
import java.util.HashMap;

/**
 * Configuration for streaming report generation.
 * 
 * @since 1.0.0
 */
public class StreamingConfig {
    
    private StreamingMode mode = StreamingMode.CHUNKED;
    private int chunkSize = 8192; // 8KB default
    private int bufferSize = 65536; // 64KB default
    private int maxChunks = 1000;
    private long timeoutMs = 300000; // 5 minutes
    private boolean enableCompression = true;
    private String compressionLevel = "MEDIUM";
    private Map<String, Object> custom = new HashMap<>();
    
    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private StreamingConfig config = new StreamingConfig();
        
        public Builder mode(StreamingMode mode) {
            config.mode = mode;
            return this;
        }
        
        public Builder chunkSize(int chunkSize) {
            config.chunkSize = chunkSize;
            return this;
        }
        
        public Builder bufferSize(int bufferSize) {
            config.bufferSize = bufferSize;
            return this;
        }
        
        public Builder maxChunks(int maxChunks) {
            config.maxChunks = maxChunks;
            return this;
        }
        
        public Builder timeoutMs(long timeoutMs) {
            config.timeoutMs = timeoutMs;
            return this;
        }
        
        public Builder enableCompression(boolean enableCompression) {
            config.enableCompression = enableCompression;
            return this;
        }
        
        public Builder compressionLevel(String compressionLevel) {
            config.compressionLevel = compressionLevel;
            return this;
        }
        
        public Builder custom(Map<String, Object> custom) {
            config.custom = custom;
            return this;
        }
        
        public StreamingConfig build() {
            return config;
        }
    }
    
    // Getters and setters...
    public StreamingMode getMode() { return mode; }
    public int getChunkSize() { return chunkSize; }
    public int getBufferSize() { return bufferSize; }
    public int getMaxChunks() { return maxChunks; }
    public long getTimeoutMs() { return timeoutMs; }
    public boolean isEnableCompression() { return enableCompression; }
    public String getCompressionLevel() { return compressionLevel; }
    public Map<String, Object> getCustom() { return custom; }
}
```

#### 5.3 ReportRequest DTO
```java
package com.tms.base.report.dto;

import com.tms.base.report.enums.ReportFormat;
import java.util.Map;
import java.util.HashMap;

/**
 * Request DTO for report generation.
 * 
 * @param <T> Type of data to be processed
 * @since 1.0.0
 */
public class ReportRequest<T> {
    
    private ReportFormat format;
    private String templateName;
    private T data;
    private Map<String, Object> parameters;
    private String outputFileName;
    private Map<String, Object> metadata;
    
    // Builder pattern
    public static <T> Builder<T> builder() {
        return new Builder<>();
    }
    
    public static class Builder<T> {
        private ReportRequest<T> request = new ReportRequest<>();
        
        public Builder<T> format(ReportFormat format) {
            request.format = format;
            return this;
        }
        
        public Builder<T> templateName(String templateName) {
            request.templateName = templateName;
            return this;
        }
        
        public Builder<T> data(T data) {
            request.data = data;
            return this;
        }
        
        public Builder<T> parameters(Map<String, Object> parameters) {
            request.parameters = parameters;
            return this;
        }
        
        public Builder<T> outputFileName(String outputFileName) {
            request.outputFileName = outputFileName;
            return this;
        }
        
        public Builder<T> metadata(Map<String, Object> metadata) {
            request.metadata = metadata;
            return this;
        }
        
        public ReportRequest<T> build() {
            if (request.parameters == null) {
                request.parameters = new HashMap<>();
            }
            if (request.metadata == null) {
                request.metadata = new HashMap<>();
            }
            return request;
        }
    }
    
    // Getters and setters...
}
```

### Bước 6: Triển Khai Exception Classes

#### 6.1 ReportException
```java
package com.tms.base.report.api;

/**
 * Base exception for report generation errors.
 * 
 * @since 1.0.0
 */
public class ReportException extends RuntimeException {
    
    private final String errorCode;
    private final String reportId;
    
    public ReportException(String message) {
        super(message);
        this.errorCode = "REPORT_ERROR";
        this.reportId = null;
    }
    
    public ReportException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "REPORT_ERROR";
        this.reportId = null;
    }
    
    public ReportException(String errorCode, String message, String reportId) {
        super(message);
        this.errorCode = errorCode;
        this.reportId = reportId;
    }
    
    public ReportException(String errorCode, String message, String reportId, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.reportId = reportId;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public String getReportId() {
        return reportId;
    }
}
```

### Bước 7: Triển Khai ReportManager

#### 7.1 ReportManager Interface
```java
package com.tms.base.report.api;

import com.tms.base.report.enums.ReportFormat;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Registry for report generators.
 * Thread-safe and supports multiple generators.
 * 
 * @since 1.0.0
 */
public interface ReportManager {
    
    /**
     * Get generator by format.
     * 
     * @param format Report format
     * @return Report generator
     * @throws ReportException if generator not found
     */
    <T> ReportGenerator<T> getGenerator(ReportFormat format) throws ReportException;
    
    /**
     * Get async generator by format.
     * 
     * @param format Report format
     * @return Async report generator
     * @throws ReportException if generator not found
     */
    <T> AsyncReportGenerator<T> getAsyncGenerator(ReportFormat format) throws ReportException;
    
    /**
     * Get streaming generator by format.
     * 
     * @param format Report format
     * @return Streaming report generator
     * @throws ReportException if generator not found
     */
    <T> StreamingReportGenerator<T> getStreamingGenerator(ReportFormat format) throws ReportException;
    
    /**
     * Get generator by name.
     * 
     * @param name Generator name
     * @return Report generator
     * @throws ReportException if generator not found
     */
    <T> ReportGenerator<T> getGenerator(String name) throws ReportException;
    
    /**
     * Get async generator by name.
     * 
     * @param name Generator name
     * @return Async report generator
     * @throws ReportException if generator not found
     */
    <T> AsyncReportGenerator<T> getAsyncGenerator(String name) throws ReportException;
    
    /**
     * Get streaming generator by name.
     * 
     * @param name Generator name
     * @return Streaming report generator
     * @throws ReportException if generator not found
     */
    <T> StreamingReportGenerator<T> getStreamingGenerator(String name) throws ReportException;
    
    /**
     * Get all registered generators.
     * 
     * @return Map of generator name to generator instance
     */
    Map<String, ReportGenerator<?>> getAllGenerators();
    
    /**
     * Get all registered async generators.
     * 
     * @return Map of generator name to async generator instance
     */
    Map<String, AsyncReportGenerator<?>> getAllAsyncGenerators();
    
    /**
     * Get all registered streaming generators.
     * 
     * @return Map of generator name to streaming generator instance
     */
    Map<String, StreamingReportGenerator<?>> getAllStreamingGenerators();
    
    /**
     * Register a generator.
     * 
     * @param generator Report generator
     */
    void registerGenerator(ReportGenerator<?> generator);
    
    /**
     * Register an async generator.
     * 
     * @param generator Async report generator
     */
    void registerAsyncGenerator(AsyncReportGenerator<?> generator);
    
    /**
     * Register a streaming generator.
     * 
     * @param generator Streaming report generator
     */
    void registerStreamingGenerator(StreamingReportGenerator<?> generator);
    
    /**
     * Unregister a generator.
     * 
     * @param name Generator name
     */
    void unregisterGenerator(String name);
    
    /**
     * Check if streaming is supported for format.
     * 
     * @param format Report format
     * @return true if streaming is supported
     */
    boolean isStreamingSupported(ReportFormat format);
    
    /**
     * Get supported streaming modes for format.
     * 
     * @param format Report format
     * @return Array of supported streaming modes
     */
    com.tms.base.report.enums.StreamingMode[] getSupportedStreamingModes(ReportFormat format);
}
```

### Bước 8: Triển Khai SPI Interfaces

#### 8.1 ReportProvider SPI
```java
package com.tms.base.report.spi;

import com.tms.base.report.api.ReportGenerator;
import com.tms.base.report.enums.ReportFormat;

/**
 * Service Provider Interface for report generators.
 * 
 * @since 1.0.0
 */
public interface ReportProvider {
    
    /**
     * Get the report generator.
     * 
     * @return Report generator instance
     */
    ReportGenerator<?> getGenerator();
    
    /**
     * Get supported formats.
     * 
     * @return Array of supported formats
     */
    ReportFormat[] getSupportedFormats();
    
    /**
     * Get provider name.
     * 
     * @return Provider name
     */
    String getName();
    
    /**
     * Get provider priority (higher number = higher priority).
     * 
     * @return Priority value
     */
    default int getPriority() {
        return 0;
    }
}
```

### Bước 9: Triển Khai Thread Safety

#### 9.1 ThreadSafeReportExecutor
```java
package com.tms.base.report.internal;

import com.tms.base.report.dto.ReportRequest;
import com.tms.base.report.dto.ReportResponse;
import com.tms.base.report.api.ReportGenerator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Thread-safe report executor with configurable thread pool.
 * 
 * @since 1.0.0
 */
public class ThreadSafeReportExecutor<T> {
    
    private final ExecutorService executorService;
    private final AtomicLong taskCounter = new AtomicLong(0);
    private final int maxConcurrentTasks;
    
    public ThreadSafeReportExecutor(int corePoolSize, int maxPoolSize, int maxConcurrentTasks) {
        this.maxConcurrentTasks = maxConcurrentTasks;
        this.executorService = new ThreadPoolExecutor(
            corePoolSize,
            maxPoolSize,
            60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(maxConcurrentTasks),
            r -> new Thread(r, "report-generator-" + taskCounter.incrementAndGet()),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
    
    /**
     * Execute report generation in thread pool.
     * 
     * @param generator Report generator
     * @param request Report request
     * @return CompletableFuture with result
     */
    public CompletableFuture<ReportResponse> execute(
        ReportGenerator<T> generator, 
        ReportRequest<T> request
    ) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return generator.generate(request);
            } catch (Exception e) {
                throw new RuntimeException("Report generation failed", e);
            }
        }, executorService);
    }
    
    /**
     * Shutdown executor gracefully.
     */
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
```

### Bước 10: Triển Khai ServiceLoader Integration

#### 10.1 ReportServices Helper
```java
package com.tms.base.report;

import com.tms.base.report.spi.ReportProvider;
import java.util.ServiceLoader;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * ServiceLoader helper for report providers.
 * 
 * @since 1.0.0
 */
public class ReportServices {
    
    private static final ServiceLoader<ReportProvider> PROVIDERS = 
        ServiceLoader.load(ReportProvider.class);
    
    /**
     * Get all available report providers.
     * 
     * @return List of report providers sorted by priority
     */
    public static List<ReportProvider> getProviders() {
        List<ReportProvider> providers = new ArrayList<>();
        for (ReportProvider provider : PROVIDERS) {
            providers.add(provider);
        }
        providers.sort(Comparator.comparingInt(ReportProvider::getPriority).reversed());
        return providers;
    }
    
    /**
     * Get provider by name.
     * 
     * @param name Provider name
     * @return Report provider or null if not found
     */
    public static ReportProvider getProvider(String name) {
        return getProviders().stream()
            .filter(provider -> provider.getName().equals(name))
            .findFirst()
            .orElse(null);
    }
}
```

### Bước 11: Tạo META-INF/services

#### 11.1 ServiceLoader Configuration
Tạo file `src/main/resources/META-INF/services/com.tms.base.report.spi.ReportProvider`:
```
# This file will be populated by concrete implementations
# in the starter module
```

### Bước 12: Triển Khai Unit Tests

#### 12.1 ReportGenerator Test
```java
package com.tms.base.report.api;

import com.tms.base.report.dto.ReportRequest;
import com.tms.base.report.dto.ReportResponse;
import com.tms.base.report.enums.ReportFormat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ReportGenerator interface.
 * 
 * @since 1.0.0
 */
class ReportGeneratorTest {
    
    private TestReportGenerator generator;
    
    @BeforeEach
    void setUp() {
        generator = new TestReportGenerator();
    }
    
    @Test
    void shouldSupportPdfFormat() {
        assertTrue(generator.supports(ReportFormat.PDF));
    }
    
    @Test
    void shouldNotSupportExcelFormat() {
        assertFalse(generator.supports(ReportFormat.EXCEL));
    }
    
    @Test
    void shouldGenerateReportSuccessfully() throws Exception {
        ReportRequest<String> request = ReportRequest.<String>builder()
            .format(ReportFormat.PDF)
            .data("test data")
            .build();
            
        ReportResponse response = generator.generate(request);
        
        assertNotNull(response);
        assertEquals("test data", response.getData());
    }
    
    @Test
    void shouldGenerateAsyncReport() {
        ReportRequest<String> request = ReportRequest.<String>builder()
            .format(ReportFormat.PDF)
            .data("test data")
            .build();
            
        CompletableFuture<ReportResponse> future = generator.generateAsync(request);
        
        assertNotNull(future);
        assertTrue(future.isDone());
    }
    
    // Test implementation
    private static class TestReportGenerator implements ReportGenerator<String> {
        
        @Override
        public ReportResponse generate(ReportRequest<String> request) {
            return ReportResponse.builder()
                .data(request.getData())
                .format(request.getFormat())
                .build();
        }
        
        @Override
        public CompletableFuture<ReportResponse> generateAsync(ReportRequest<String> request) {
            return CompletableFuture.completedFuture(generate(request));
        }
        
        @Override
        public boolean supports(ReportFormat format) {
            return ReportFormat.PDF.equals(format);
        }
        
        @Override
        public String getName() {
            return "test-generator";
        }
        
        @Override
        public ReportFormat[] getSupportedFormats() {
            return new ReportFormat[]{ReportFormat.PDF};
        }
    }
}
```

## ✅ Definition of Done

- [ ] Tất cả interfaces được implement đầy đủ
- [ ] Unit tests đạt coverage ≥ 80%
- [ ] Thread safety được đảm bảo
- [ ] ServiceLoader integration hoạt động
- [ ] Javadoc đầy đủ cho public APIs
- [ ] Checkstyle và SpotBugs pass
- [ ] Module build thành công

## 🔄 Next Steps

Sau khi hoàn thành `java-base-report`, chuyển sang triển khai `java-base-starter-report` với:
- Spring Boot AutoConfiguration
- Concrete implementations (PDF, Excel)
- Template engines integration
- Security features
- Performance monitoring

## 📚 References

- [ServiceLoader Documentation](https://docs.oracle.com/javase/8/docs/api/java/util/ServiceLoader.html)
- [CompletableFuture Guide](https://www.baeldung.com/java-completablefuture)
- [Thread Safety Best Practices](https://docs.oracle.com/javase/tutorial/essential/concurrency/)
