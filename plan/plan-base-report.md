# Java Base Report Module Implementation Guide

## Tóm tắt vấn đề

Triển khai hệ thống báo cáo modular cho Java Spring Boot với 2 module chính:
- **java-base-report**: Core contracts và interfaces (framework-agnostic)
- **java-base-starter-report**: Spring Boot integration và concrete implementations

## Dữ liệu quan sát được

### Yêu cầu chức năng
- Xuất báo cáo PDF và Excel
- Hỗ trợ đa luồng (thread-safe)
- Cấu hình linh hoạt cho nhiều loại file
- Tách biệt rõ ràng giữa report generation và file storage

### Kiến trúc hiện tại
```
java-base-parent
├── java-base-bom (pom)
├── java-base-core (jar)
├── java-base-cache (jar)
├── java-base-observability (jar)
├── java-base-security (jar)
├── java-base-file (jar)              <-- File storage contracts
├── java-base-aws (jar)               <-- AWS service contracts
├── java-base-report (jar)            <-- Report generation contracts
└── java-base-starter-report (jar)    <-- Spring Boot integration + all providers
```

## Phân tích & Lập luận

### 1. Thiết kế Module Architecture

**Nguyên tắc thiết kế:**
- **Separation of Concerns**: Core module chỉ chứa contracts, không phụ thuộc vào framework
- **Dependency Inversion**: Core module phụ thuộc vào abstractions, không phụ thuộc vào concrete implementations
- **Single Responsibility**: Mỗi module có trách nhiệm rõ ràng
- **Open/Closed Principle**: Dễ mở rộng thêm format mới mà không sửa code cũ

### 2. Thread Safety Strategy

**Vấn đề đa luồng:**
- Report generation có thể tốn thời gian (I/O operations)
- Cần đảm bảo thread-safe khi xử lý concurrent requests
- Template engine và file generation cần thread-safe

**Giải pháp:**
- Stateless design cho report generators
- Thread-safe template engines
- Connection pooling cho external services
- Immutable configuration objects

## Nguyên nhân gốc (Root Cause)

Yêu cầu modular design xuất phát từ:
1. **Reusability**: Sử dụng ở nhiều project khác nhau
2. **Maintainability**: Dễ bảo trì và mở rộng
3. **Testability**: Test độc lập từng module
4. **Performance**: Xử lý đa luồng hiệu quả

## Ảnh hưởng (Impact)

### Tích cực
- **Modularity**: Tách biệt rõ ràng các chức năng
- **Flexibility**: Sử dụng report generation mà không cần file storage
- **Testability**: Dễ test từng module riêng biệt
- **Scalability**: Dễ mở rộng thêm format khác
- **Reusability**: Sử dụng ở nhiều project

### Rủi ro
- **Complexity**: Tăng độ phức tạp khi thiết kế
- **Learning Curve**: Team cần thời gian học cách sử dụng

## Giải pháp & Hành động khắc phục

### 1. Cấu trúc Module Chi Tiết

#### Module: java-base-report (jar)
```
java-base-report/
├── src/main/java/
│   └── com/tms/base/report/
│       ├── api/
│       │   ├── ReportGenerator.java              // Core sync API
│       │   ├── AsyncReportGenerator.java         // Async API
│       │   ├── ReportManager.java                // Registry for named generators
│       │   ├── ReportException.java              // Runtime exceptions
│       │   ├── ReportConfig.java                 // Configuration
│       │   └── ReportRequest.java                // Request DTO
│       ├── spi/
│       │   ├── ReportProvider.java               // ServiceLoader SPI
│       │   ├── TemplateEngine.java               // Template engine SPI
│       │   └── Serializer.java                   // Serialization SPI
│       ├── enums/
│       │   ├── ReportFormat.java                 // PDF, EXCEL, WORD, CSV
│       │   ├── ReportStatus.java                 // SUCCESS, FAILED, PROCESSING
│       │   └── TemplateType.java                 // HTML, XML, JSON
│       ├── dto/
│       │   ├── ReportResponse.java               // Response DTO
│       │   ├── ReportMetadata.java               // Metadata
│       │   └── ReportProgress.java               // Progress tracking
│       ├── internal/
│       │   ├── ThreadSafeReportExecutor.java     // Thread-safe execution
│       │   ├── ReportTask.java                   // Task wrapper
│       │   └── ReportQueue.java                  // Queue management
│       └── ReportServices.java                   // ServiceLoader helper
├── src/main/resources/
│   └── META-INF/
│       └── services/
│           └── com.tms.base.report.spi.ReportProvider
└── pom.xml
```

#### Module: java-base-starter-report (jar)
```
java-base-starter-report/
├── src/main/java/
│   └── com/tms/base/starter/report/
│       ├── autoconfig/
│       │   ├── ReportAutoConfiguration.java      // Auto-configuration
│       │   ├── ReportProperties.java             // Configuration properties
│       │   ├── ThreadPoolConfig.java             // Thread pool configuration
│       │   └── ReportSecurityConfig.java         // Security configuration
│       ├── pdf/
│       │   ├── ItextPdfReportGenerator.java      // iText implementation
│       │   ├── FlyingSaucerPdfGenerator.java     // Flying Saucer implementation
│       │   └── PdfReportProvider.java            // PDF provider
│       ├── excel/
│       │   ├── PoiExcelReportGenerator.java      // Apache POI implementation
│       │   ├── EasyExcelReportGenerator.java     // EasyExcel implementation
│       │   └── ExcelReportProvider.java          // Excel provider
│       ├── template/
│       │   ├── ThymeleafTemplateEngine.java      // Thymeleaf engine
│       │   ├── FreemarkerTemplateEngine.java     // FreeMarker engine
│       │   └── MustacheTemplateEngine.java       // Mustache engine
│       ├── serializer/
│       │   ├── JacksonSerializer.java            // Jackson serializer
│       │   ├── GsonSerializer.java               // Gson serializer
│       │   └── DefaultSerializer.java            // Default serializer
│       ├── service/
│       │   ├── ReportService.java                // Main service
│       │   ├── AsyncReportService.java           // Async service
│       │   └── ReportValidationService.java      // Validation service
│       └── util/
│           ├── ReportUtils.java                  // Utility methods
│           ├── FileUtils.java                    // File utilities
│           └── ThreadUtils.java                  // Thread utilities
├── src/main/resources/
│   ├── META-INF/
│   │   └── services/
│   │       └── com.tms.base.report.spi.ReportProvider
│   ├── templates/
│   │   ├── pdf/
│   │   │   ├── driver-report.html
│   │   │   └── company-report.html
│   │   └── excel/
│   │       ├── weekly-report.xlsx
│   │       └── monthly-report.xlsx
│   └── application-report.yml                    // Default configuration
└── pom.xml
```

### 2. Core Interfaces Implementation

#### ReportGenerator.java
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

#### AsyncReportGenerator.java
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

#### ReportManager.java
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
     * Get generator by name.
     * 
     * @param name Generator name
     * @return Report generator
     * @throws ReportException if generator not found
     */
    <T> ReportGenerator<T> getGenerator(String name) throws ReportException;
    
    /**
     * Get all registered generators.
     * 
     * @return Map of generator name to generator instance
     */
    Map<String, ReportGenerator<?>> getAllGenerators();
    
    /**
     * Register a generator.
     * 
     * @param generator Report generator
     */
    void registerGenerator(ReportGenerator<?> generator);
    
    /**
     * Unregister a generator.
     * 
     * @param name Generator name
     */
    void unregisterGenerator(String name);
}
```

### 3. Thread Safety Implementation

#### ThreadSafeReportExecutor.java
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

### 4. Configuration Properties

#### ReportProperties.java
```java
package com.tms.base.starter.report.autoconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import java.util.Map;
import java.util.HashMap;

/**
 * Configuration properties for report module.
 * 
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "base.report")
public class ReportProperties {
    
    /**
     * Default report format.
     */
    private String defaultFormat = "PDF";
    
    /**
     * Thread pool configuration.
     */
    @NestedConfigurationProperty
    private ThreadPool threadPool = new ThreadPool();
    
    /**
     * PDF configuration.
     */
    @NestedConfigurationProperty
    private Pdf pdf = new Pdf();
    
    /**
     * Excel configuration.
     */
    @NestedConfigurationProperty
    private Excel excel = new Excel();
    
    /**
     * Template configuration.
     */
    @NestedConfigurationProperty
    private Template template = new Template();
    
    /**
     * Security configuration.
     */
    @NestedConfigurationProperty
    private Security security = new Security();
    
    /**
     * Custom configurations per format.
     */
    private Map<String, Object> custom = new HashMap<>();
    
    // Getters and setters...
    
    public static class ThreadPool {
        private int coreSize = 5;
        private int maxSize = 20;
        private int queueCapacity = 100;
        private long keepAliveSeconds = 60;
        
        // Getters and setters...
    }
    
    public static class Pdf {
        private String engine = "itext";
        private String templatePath = "classpath:templates/pdf/";
        private boolean enableCompression = true;
        private String compressionLevel = "MEDIUM";
        
        // Getters and setters...
    }
    
    public static class Excel {
        private String engine = "poi";
        private String templatePath = "classpath:templates/excel/";
        private boolean enableStreaming = true;
        private int maxRowsInMemory = 1000;
        
        // Getters and setters...
    }
    
    public static class Template {
        private String engine = "thymeleaf";
        private String cacheTemplates = "true";
        private String templatePath = "classpath:templates/";
        
        // Getters and setters...
    }
    
    public static class Security {
        private boolean enableEncryption = false;
        private String encryptionKey;
        private boolean enableDigitalSignature = false;
        private String signatureKey;
        
        // Getters and setters...
    }
}
```

### 5. Spring Boot Auto-Configuration

#### ReportAutoConfiguration.java
```java
package com.tms.base.starter.report.autoconfig;

import com.tms.base.report.api.ReportManager;
import com.tms.base.report.api.ReportGenerator;
import com.tms.base.report.spi.ReportProvider;
import com.tms.base.starter.report.service.ReportService;
import com.tms.base.starter.report.service.AsyncReportService;
import com.tms.base.starter.report.pdf.PdfReportProvider;
import com.tms.base.starter.report.excel.ExcelReportProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import java.util.List;

/**
 * Auto-configuration for report module.
 * 
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties(ReportProperties.class)
@ConditionalOnClass(ReportGenerator.class)
public class ReportAutoConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    public ReportManager reportManager(List<ReportProvider> providers) {
        return new DefaultReportManager(providers);
    }
    
    @Bean
    @ConditionalOnMissingBean
    public ReportService reportService(ReportManager reportManager, ReportProperties properties) {
        return new ReportService(reportManager, properties);
    }
    
    @Bean
    @ConditionalOnMissingBean
    public AsyncReportService asyncReportService(ReportManager reportManager, ReportProperties properties) {
        return new AsyncReportService(reportManager, properties);
    }
    
    @Configuration
    @ConditionalOnClass(name = "com.itextpdf.kernel.pdf.PdfDocument")
    @ConditionalOnProperty(name = "base.report.pdf.enabled", havingValue = "true", matchIfMissing = true)
    static class PdfConfiguration {
        
        @Bean
        @ConditionalOnMissingBean
        public PdfReportProvider pdfReportProvider(ReportProperties properties) {
            return new PdfReportProvider(properties.getPdf());
        }
    }
    
    @Configuration
    @ConditionalOnClass(name = "org.apache.poi.ss.usermodel.Workbook")
    @ConditionalOnProperty(name = "base.report.excel.enabled", havingValue = "true", matchIfMissing = true)
    static class ExcelConfiguration {
        
        @Bean
        @ConditionalOnMissingBean
        public ExcelReportProvider excelReportProvider(ReportProperties properties) {
            return new ExcelReportProvider(properties.getExcel());
        }
    }
}
```

### 6. Usage Examples

#### Basic Usage
```java
@Service
public class DriverReportService {
    
    @Autowired
    private ReportService reportService;
    
    public ReportResponse generateDriverReport(DriverReportData data) {
        ReportRequest<DriverReportData> request = ReportRequest.<DriverReportData>builder()
            .format(ReportFormat.PDF)
            .templateName("driver-report")
            .data(data)
            .build();
            
        return reportService.generate(request);
    }
}
```

#### Async Usage
```java
@Service
public class CompanyReportService {
    
    @Autowired
    private AsyncReportService asyncReportService;
    
    public CompletableFuture<ReportResponse> generateMonthlyReport(MonthlyReportData data) {
        ReportRequest<MonthlyReportData> request = ReportRequest.<MonthlyReportData>builder()
            .format(ReportFormat.EXCEL)
            .templateName("monthly-report")
            .data(data)
            .build();
            
        return asyncReportService.generateAsync(request);
    }
}
```

#### Progress Tracking
```java
@Service
public class LargeReportService {
    
    @Autowired
    private AsyncReportService asyncReportService;
    
    public CompletableFuture<ReportResponse> generateLargeReport(LargeReportData data) {
        ReportRequest<LargeReportData> request = ReportRequest.<LargeReportData>builder()
            .format(ReportFormat.EXCEL)
            .templateName("large-report")
            .data(data)
            .build();
            
        return asyncReportService.generateWithProgress(request, progress -> {
            log.info("Report generation progress: {}%", progress.getPercentage());
        });
    }
}
```

### 7. Maven Dependencies

#### java-base-report/pom.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>com.tms.base</groupId>
        <artifactId>java-base-parent</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>
    
    <artifactId>java-base-report</artifactId>
    <packaging>jar</packaging>
    
    <dependencies>
        <dependency>
            <groupId>com.tms.base</groupId>
            <artifactId>java-base-core</artifactId>
        </dependency>
    </dependencies>
</project>
```

#### java-base-starter-report/pom.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>com.tms.base</groupId>
        <artifactId>java-base-parent</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>
    
    <artifactId>java-base-starter-report</artifactId>
    <packaging>jar</packaging>
    
    <dependencies>
        <dependency>
            <groupId>com.tms.base</groupId>
            <artifactId>java-base-report</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-autoconfigure</artifactId>
        </dependency>
        
        <!-- PDF Generation -->
        <dependency>
            <groupId>com.itextpdf</groupId>
            <artifactId>itext7-core</artifactId>
            <version>7.2.5</version>
        </dependency>
        
        <dependency>
            <groupId>org.xhtmlrenderer</groupId>
            <artifactId>flying-saucer-pdf</artifactId>
            <version>9.1.22</version>
        </dependency>
        
        <!-- Excel Generation -->
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi-ooxml</artifactId>
            <version>5.2.4</version>
        </dependency>
        
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>easyexcel</artifactId>
            <version>3.3.2</version>
        </dependency>
        
        <!-- Template Engines -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.freemarker</groupId>
            <artifactId>freemarker</artifactId>
        </dependency>
        
        <dependency>
            <groupId>com.github.spullara.mustache.java</groupId>
            <artifactId>compiler</artifactId>
            <version>0.9.10</version>
        </dependency>
        
        <!-- Serialization -->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
        </dependency>
        
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
        </dependency>
    </dependencies>
</project>
```

### 8. Testing Strategy

#### Unit Tests
```java
@ExtendWith(MockitoExtension.class)
class ReportServiceTest {
    
    @Mock
    private ReportManager reportManager;
    
    @Mock
    private ReportGenerator<TestData> reportGenerator;
    
    @InjectMocks
    private ReportService reportService;
    
    @Test
    void shouldGenerateReportSuccessfully() {
        // Given
        ReportRequest<TestData> request = createTestRequest();
        ReportResponse expectedResponse = createTestResponse();
        
        when(reportManager.getGenerator(ReportFormat.PDF))
            .thenReturn(reportGenerator);
        when(reportGenerator.generate(request))
            .thenReturn(expectedResponse);
        
        // When
        ReportResponse actualResponse = reportService.generate(request);
        
        // Then
        assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(reportGenerator).generate(request);
    }
}
```

#### Integration Tests
```java
@SpringBootTest
@TestPropertySource(properties = {
    "base.report.pdf.enabled=true",
    "base.report.excel.enabled=true"
})
class ReportIntegrationTest {
    
    @Autowired
    private ReportService reportService;
    
    @Test
    void shouldGeneratePdfReport() {
        // Test PDF generation
    }
    
    @Test
    void shouldGenerateExcelReport() {
        // Test Excel generation
    }
    
    @Test
    void shouldHandleConcurrentRequests() {
        // Test thread safety
    }
}
```

### 9. Security Considerations

#### ReportSecurityConfig.java
```java
@Configuration
@EnableWebSecurity
public class ReportSecurityConfig {
    
    @Bean
    public ReportSecurityFilter reportSecurityFilter() {
        return new ReportSecurityFilter();
    }
    
    @Bean
    public ReportEncryptionService reportEncryptionService() {
        return new ReportEncryptionService();
    }
    
    @Bean
    public ReportDigitalSignatureService reportDigitalSignatureService() {
        return new ReportDigitalSignatureService();
    }
}
```

### 10. Performance Monitoring

#### ReportMetrics.java
```java
@Component
public class ReportMetrics {
    
    private final MeterRegistry meterRegistry;
    private final Counter reportGenerationCounter;
    private final Timer reportGenerationTimer;
    private final Gauge activeReportGauge;
    
    public ReportMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.reportGenerationCounter = Counter.builder("report.generation.total")
            .description("Total number of report generations")
            .register(meterRegistry);
        this.reportGenerationTimer = Timer.builder("report.generation.duration")
            .description("Report generation duration")
            .register(meterRegistry);
        this.activeReportGauge = Gauge.builder("report.generation.active")
            .description("Number of active report generations")
            .register(meterRegistry, this, ReportMetrics::getActiveRepo