You are a Senior Software Engineer + System Architect (or an AI coding agent acting like one).
Your task: design and implement a production-ready, enterprise-grade Spring Boot starter for report generation that provides concrete implementations and auto-configuration for PDF, Excel, Word, CSV, and demonstrates full runtime wiring (including ServiceLoader fallback).

📦 java-base-starter-report Design Plan

🎯 Overall Objectives
	• Spring Boot Starter Ecosystem
		○ Cung cấp auto-configuration + starter module.
		○ Sẵn sàng plug-and-play trong Spring Boot apps.
	• Concrete Implementations
		○ PDF generators (iText, Flying Saucer).
		○ Excel generators (Apache POI, EasyExcel).
		○ Word generators (Apache POI Word).
		○ CSV generators (custom implementation).
	• Advanced Features
		○ Streaming support cho large reports.
		○ Async operations với progress tracking.
		○ Template engines (Thymeleaf, FreeMarker, Mustache).
		○ Security integration (encryption, digital signature).

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

1. Report Generator Implementations
	• PDF: iText và Flying Saucer implementations.
	• Excel: Apache POI và EasyExcel implementations.
	• Word: Apache POI Word implementation.
	• CSV: Custom implementation với streaming support.

2. Template Engine Integration
	• Thymeleaf: Spring Boot integration.
	• FreeMarker: Standalone template engine.
	• Mustache: Logic-less template engine.

3. Service Layer
	• ReportService: Main sync service.
	• AsyncReportService: Async operations với progress tracking.
	• StreamingReportService: Streaming operations cho large reports.
	• ReportValidationService: Input validation.

4. Spring Boot Integration
	• Auto-configuration cho tất cả components.
	• Configuration properties binding.
	• Conditional beans based on classpath.
	• HTTP streaming với Spring MVC.

📖 Example Usage

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
	• Unit tests: tất cả implementations.
	• Integration tests: Spring Boot context loading.
	• Thread safety tests cho concurrent operations.
	• Streaming performance tests.
	• Code coverage ≥ 80% (Jacoco).
	• Backward compatibility check (Japicmp / Revapi).

✅ Standards
	• Java 21+
	• Javadoc đầy đủ cho public API.
	• Static utils: final class + private constructor.
	• Spring Boot best practices.
	• Follows SOLID, KISS, DRY.

🚀 Deliverables
	• java-base-starter-report: Spring Boot auto-config + PDF/Excel/Word/CSV generators.
	• README.md: mô tả design, usage, extensibility.
	• Sample demo app dùng multiple report formats và streaming.

## 📋 Checklist Triển Khai

### Phase 1: Setup Module Structure
- [ ] Tạo cấu trúc thư mục theo plan
- [ ] Setup pom.xml với Spring Boot dependencies
- [ ] Tạo package structure chuẩn

### Phase 2: Spring Boot AutoConfiguration
- [ ] `ReportAutoConfiguration` - Main auto-configuration
- [ ] `ReportProperties` - Configuration properties
- [ ] `ThreadPoolConfig` - Thread pool configuration
- [ ] `StreamingConfig` - Streaming configuration
- [ ] `ReportSecurityConfig` - Security configuration

### Phase 3: PDF Report Implementations
- [ ] `ItextPdfReportGenerator` - iText implementation
- [ ] `FlyingSaucerPdfGenerator` - Flying Saucer implementation
- [ ] `PdfReportProvider` - PDF provider

### Phase 4: Excel Report Implementations
- [ ] `PoiExcelReportGenerator` - Apache POI implementation
- [ ] `EasyExcelReportGenerator` - EasyExcel implementation
- [ ] `ExcelReportProvider` - Excel provider

### Phase 5: Template Engine Implementations
- [ ] `ThymeleafTemplateEngine` - Thymeleaf engine
- [ ] `FreemarkerTemplateEngine` - FreeMarker engine
- [ ] `MustacheTemplateEngine` - Mustache engine

### Phase 6: Serialization Implementations
- [ ] `JacksonSerializer` - Jackson serializer
- [ ] `GsonSerializer` - Gson serializer
- [ ] `DefaultSerializer` - Default serializer

### Phase 7: Service Layer
- [ ] `ReportService` - Main service
- [ ] `AsyncReportService` - Async service
- [ ] `StreamingReportService` - Streaming service
- [ ] `ReportValidationService` - Validation service

### Phase 8: Utilities & Security
- [ ] `ReportUtils` - Utility methods
- [ ] `FileUtils` - File utilities
- [ ] `ThreadUtils` - Thread utilities
- [ ] Security features (encryption, digital signature)

### Phase 9: Testing & Documentation
- [ ] Unit tests cho tất cả implementations
- [ ] Integration tests với Spring Boot
- [ ] Performance tests
- [ ] Usage examples

## 🚀 Bắt Đầu Triển Khai

### Bước 1: Tạo Module Structure
```bash
# Tạo thư mục java-base-starter-report
mkdir java-base-starter-report
cd java-base-starter-report

# Tạo cấu trúc thư mục
mkdir -p src/main/java/com/tms/base/starter/report/{autoconfig,pdf,excel,template,serializer,service,util}
mkdir -p src/main/resources/{META-INF/services,templates/{pdf,excel}}
mkdir -p src/test/java/com/tms/base/starter/report
```

### Bước 2: Setup pom.xml
Tạo file `pom.xml` với:
- Parent: `java-base-parent`
- Dependencies: `java-base-report`, Spring Boot, PDF/Excel libraries
- Packaging: `jar`
- Version: `1.0.0-SNAPSHOT`

### Bước 3: Triển Khai Configuration Properties

#### 3.1 ReportProperties
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
     * Streaming configuration.
     */
    @NestedConfigurationProperty
    private Streaming streaming = new Streaming();
    
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
    
    public static class Streaming {
        private boolean enabled = true;
        private String defaultMode = "CHUNKED";
        private int chunkSize = 8192;
        private int bufferSize = 65536;
        private int maxChunks = 1000;
        private long timeoutMs = 300000;
        private boolean enableCompression = true;
        private String compressionLevel = "MEDIUM";
        
        // Getters and setters...
    }
}
```

### Bước 4: Triển Khai AutoConfiguration

#### 4.1 ReportAutoConfiguration
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
    
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "base.report.streaming.enabled", havingValue = "true", matchIfMissing = true)
    public StreamingReportService streamingReportService(ReportManager reportManager, ReportProperties properties) {
        return new StreamingReportService(reportManager, properties);
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

### Bước 5: Triển Khai PDF Report Generators

#### 5.1 ItextPdfReportGenerator
```java
package com.tms.base.starter.report.pdf;

import com.tms.base.report.api.ReportGenerator;
import com.tms.base.report.dto.ReportRequest;
import com.tms.base.report.dto.ReportResponse;
import com.tms.base.report.enums.ReportFormat;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.CompletableFuture;

/**
 * iText-based PDF report generator.
 * 
 * @since 1.0.0
 */
@Component
public class ItextPdfReportGenerator<T> implements ReportGenerator<T> {
    
    private final String name = "itext-pdf-generator";
    
    @Override
    public ReportResponse generate(ReportRequest<T> request) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);
            
            // Add content based on template and data
            document.add(new Paragraph("Report: " + request.getTemplateName()));
            document.add(new Paragraph("Data: " + request.getData().toString()));
            
            document.close();
            
            return ReportResponse.builder()
                .data(outputStream.toByteArray())
                .format(ReportFormat.PDF)
                .fileName(request.getOutputFileName())
                .mimeType(ReportFormat.PDF.getMimeType())
                .build();
                
        } catch (Exception e) {
            throw new RuntimeException("PDF generation failed", e);
        }
    }
    
    @Override
    public CompletableFuture<ReportResponse> generateAsync(ReportRequest<T> request) {
        return CompletableFuture.supplyAsync(() -> generate(request));
    }
    
    @Override
    public boolean supports(ReportFormat format) {
        return ReportFormat.PDF.equals(format);
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public ReportFormat[] getSupportedFormats() {
        return new ReportFormat[]{ReportFormat.PDF};
    }
}
```

#### 5.2 PdfReportProvider
```java
package com.tms.base.starter.report.pdf;

import com.tms.base.report.api.ReportGenerator;
import com.tms.base.report.enums.ReportFormat;
import com.tms.base.report.spi.ReportProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * PDF report provider for ServiceLoader.
 * 
 * @since 1.0.0
 */
@Component
public class PdfReportProvider implements ReportProvider {
    
    @Autowired
    private ItextPdfReportGenerator<?> pdfGenerator;
    
    @Override
    public ReportGenerator<?> getGenerator() {
        return pdfGenerator;
    }
    
    @Override
    public ReportFormat[] getSupportedFormats() {
        return new ReportFormat[]{ReportFormat.PDF};
    }
    
    @Override
    public String getName() {
        return "pdf-provider";
    }
    
    @Override
    public int getPriority() {
        return 100;
    }
}
```

### Bước 6: Triển Khai Excel Report Generators

#### 6.1 PoiExcelReportGenerator
```java
package com.tms.base.starter.report.excel;

import com.tms.base.report.api.ReportGenerator;
import com.tms.base.report.dto.ReportRequest;
import com.tms.base.report.dto.ReportResponse;
import com.tms.base.report.enums.ReportFormat;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.CompletableFuture;

/**
 * Apache POI-based Excel report generator.
 * 
 * @since 1.0.0
 */
@Component
public class PoiExcelReportGenerator<T> implements ReportGenerator<T> {
    
    private final String name = "poi-excel-generator";
    
    @Override
    public ReportResponse generate(ReportRequest<T> request) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             Workbook workbook = new XSSFWorkbook()) {
            
            Sheet sheet = workbook.createSheet("Report");
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            Cell headerCell = headerRow.createCell(0);
            headerCell.setCellValue("Report: " + request.getTemplateName());
            
            // Create data row
            Row dataRow = sheet.createRow(1);
            Cell dataCell = dataRow.createCell(0);
            dataCell.setCellValue(request.getData().toString());
            
            workbook.write(outputStream);
            
            return ReportResponse.builder()
                .data(outputStream.toByteArray())
                .format(ReportFormat.EXCEL)
                .fileName(request.getOutputFileName())
                .mimeType(ReportFormat.EXCEL.getMimeType())
                .build();
                
        } catch (Exception e) {
            throw new RuntimeException("Excel generation failed", e);
        }
    }
    
    @Override
    public CompletableFuture<ReportResponse> generateAsync(ReportRequest<T> request) {
        return CompletableFuture.supplyAsync(() -> generate(request));
    }
    
    @Override
    public boolean supports(ReportFormat format) {
        return ReportFormat.EXCEL.equals(format);
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public ReportFormat[] getSupportedFormats() {
        return new ReportFormat[]{ReportFormat.EXCEL};
    }
}
```

### Bước 7: Triển Khai Template Engines

#### 7.1 ThymeleafTemplateEngine
```java
package com.tms.base.starter.report.template;

import com.tms.base.report.spi.TemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Thymeleaf template engine implementation.
 * 
 * @since 1.0.0
 */
@Component
public class ThymeleafTemplateEngine implements TemplateEngine {
    
    @Autowired
    private TemplateEngine thymeleafEngine;
    
    @Override
    public String process(String templateName, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        
        return thymeleafEngine.process(templateName, context);
    }
    
    @Override
    public String getName() {
        return "thymeleaf";
    }
    
    @Override
    public boolean supports(String templateType) {
        return "html".equalsIgnoreCase(templateType);
    }
}
```

### Bước 8: Triển Khai Service Layer

#### 8.1 ReportService
```java
package com.tms.base.starter.report.service;

import com.tms.base.report.api.ReportManager;
import com.tms.base.report.api.ReportGenerator;
import com.tms.base.report.dto.ReportRequest;
import com.tms.base.report.dto.ReportResponse;
import com.tms.base.report.enums.ReportFormat;
import com.tms.base.starter.report.autoconfig.ReportProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Main report service for synchronous operations.
 * 
 * @since 1.0.0
 */
@Service
public class ReportService {
    
    private final ReportManager reportManager;
    private final ReportProperties properties;
    
    @Autowired
    public ReportService(ReportManager reportManager, ReportProperties properties) {
        this.reportManager = reportManager;
        this.properties = properties;
    }
    
    /**
     * Generate report synchronously.
     * 
     * @param request Report generation request
     * @return Generated report response
     */
    public <T> ReportResponse generate(ReportRequest<T> request) {
        ReportFormat format = request.getFormat() != null ? 
            request.getFormat() : 
            ReportFormat.valueOf(properties.getDefaultFormat());
            
        ReportGenerator<T> generator = reportManager.getGenerator(format);
        return generator.generate(request);
    }
    
    /**
     * Generate report with default format.
     * 
     * @param templateName Template name
     * @param data Data to process
     * @return Generated report response
     */
    public <T> ReportResponse generate(String templateName, T data) {
        ReportRequest<T> request = ReportRequest.<T>builder()
            .format(ReportFormat.valueOf(properties.getDefaultFormat()))
            .templateName(templateName)
            .data(data)
            .build();
            
        return generate(request);
    }
}
```

#### 8.2 AsyncReportService
```java
package com.tms.base.starter.report.service;

import com.tms.base.report.api.ReportManager;
import com.tms.base.report.api.AsyncReportGenerator;
import com.tms.base.report.dto.ReportRequest;
import com.tms.base.report.dto.ReportResponse;
import com.tms.base.report.dto.ReportProgress;
import com.tms.base.report.enums.ReportFormat;
import com.tms.base.starter.report.autoconfig.ReportProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Async report service for asynchronous operations.
 * 
 * @since 1.0.0
 */
@Service
public class AsyncReportService {
    
    private final ReportManager reportManager;
    private final ReportProperties properties;
    
    @Autowired
    public AsyncReportService(ReportManager reportManager, ReportProperties properties) {
        this.reportManager = reportManager;
        this.properties = properties;
    }
    
    /**
     * Generate report asynchronously.
     * 
     * @param request Report generation request
     * @return CompletableFuture with report response
     */
    public <T> CompletableFuture<ReportResponse> generateAsync(ReportRequest<T> request) {
        ReportFormat format = request.getFormat() != null ? 
            request.getFormat() : 
            ReportFormat.valueOf(properties.getDefaultFormat());
            
        AsyncReportGenerator<T> generator = reportManager.getAsyncGenerator(format);
        return generator.generateAsync(request);
    }
    
    /**
     * Generate report with progress tracking.
     * 
     * @param request Report generation request
     * @param progressCallback Progress callback
     * @return CompletableFuture with report response
     */
    public <T> CompletableFuture<ReportResponse> generateWithProgress(
        ReportRequest<T> request, 
        Consumer<ReportProgress> progressCallback
    ) {
        ReportFormat format = request.getFormat() != null ? 
            request.getFormat() : 
            ReportFormat.valueOf(properties.getDefaultFormat());
            
        AsyncReportGenerator<T> generator = reportManager.getAsyncGenerator(format);
        return generator.generateWithProgress(request, progressCallback);
    }
}
```

#### 8.3 StreamingReportService
```java
package com.tms.base.starter.report.service;

import com.tms.base.report.api.ReportManager;
import com.tms.base.report.api.StreamingReportGenerator;
import com.tms.base.report.dto.ReportRequest;
import com.tms.base.report.dto.StreamingReportResponse;
import com.tms.base.report.dto.StreamingConfig;
import com.tms.base.report.dto.ReportProgress;
import com.tms.base.report.enums.ReportFormat;
import com.tms.base.report.enums.StreamingMode;
import com.tms.base.starter.report.autoconfig.ReportProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Streaming report service for large reports.
 * 
 * @since 1.0.0
 */
@Service
public class StreamingReportService {
    
    private final ReportManager reportManager;
    private final ReportProperties properties;
    
    @Autowired
    public StreamingReportService(ReportManager reportManager, ReportProperties properties) {
        this.reportManager = reportManager;
        this.properties = properties;
    }
    
    /**
     * Generate streaming report.
     * 
     * @param request Report generation request
     * @param config Streaming configuration
     * @return Streaming report response
     */
    public <T> StreamingReportResponse generateStreaming(ReportRequest<T> request, StreamingConfig config) {
        ReportFormat format = request.getFormat() != null ? 
            request.getFormat() : 
            ReportFormat.valueOf(properties.getDefaultFormat());
            
        StreamingReportGenerator<T> generator = reportManager.getStreamingGenerator(format);
        return generator.generateStreaming(request, config);
    }
    
    /**
     * Generate streaming report with progress tracking.
     * 
     * @param request Report generation request
     * @param config Streaming configuration
     * @param progressCallback Progress callback
     * @return Streaming report response
     */
    public <T> StreamingReportResponse generateStreamingWithProgress(
        ReportRequest<T> request, 
        StreamingConfig config,
        Consumer<ReportProgress> progressCallback
    ) {
        ReportFormat format = request.getFormat() != null ? 
            request.getFormat() : 
            ReportFormat.valueOf(properties.getDefaultFormat());
            
        StreamingReportGenerator<T> generator = reportManager.getStreamingGenerator(format);
        return generator.generateStreamingWithProgress(request, config, progressCallback);
    }
    
    /**
     * Generate streaming report asynchronously.
     * 
     * @param request Report generation request
     * @param config Streaming configuration
     * @return CompletableFuture with streaming response
     */
    public <T> CompletableFuture<StreamingReportResponse> generateStreamingAsync(
        ReportRequest<T> request, 
        StreamingConfig config
    ) {
        ReportFormat format = request.getFormat() != null ? 
            request.getFormat() : 
            ReportFormat.valueOf(properties.getDefaultFormat());
            
        StreamingReportGenerator<T> generator = reportManager.getStreamingGenerator(format);
        return generator.generateStreamingAsync(request, config);
    }
    
    /**
     * Create Spring MVC StreamingResponseBody for HTTP streaming.
     * 
     * @param request Report generation request
     * @param config Streaming configuration
     * @return StreamingResponseBody for HTTP response
     */
    public <T> StreamingResponseBody createStreamingResponseBody(
        ReportRequest<T> request, 
        StreamingConfig config
    ) {
        return new StreamingResponseBody() {
            @Override
            public void writeTo(OutputStream outputStream) throws IOException {
                StreamingReportResponse response = generateStreaming(request, config);
                
                try (var inputStream = response.getDataStream()) {
                    byte[] buffer = new byte[config.getBufferSize()];
                    int bytesRead;
                    
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                        outputStream.flush();
                    }
                }
            }
        };
    }
    
    /**
     * Create default streaming configuration.
     * 
     * @return Default streaming configuration
     */
    public StreamingConfig createDefaultStreamingConfig() {
        ReportProperties.Streaming streamingProps = properties.getStreaming();
        
        return StreamingConfig.builder()
            .mode(StreamingMode.valueOf(streamingProps.getDefaultMode()))
            .chunkSize(streamingProps.getChunkSize())
            .bufferSize(streamingProps.getBufferSize())
            .maxChunks(streamingProps.getMaxChunks())
            .timeoutMs(streamingProps.getTimeoutMs())
            .enableCompression(streamingProps.isEnableCompression())
            .compressionLevel(streamingProps.getCompressionLevel())
            .build();
    }
}
```

### Bước 9: Triển Khai Utilities

#### 9.1 ReportUtils
```java
package com.tms.base.starter.report.util;

import com.tms.base.report.enums.ReportFormat;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Utility methods for report operations.
 * 
 * @since 1.0.0
 */
@Component
public class ReportUtils {
    
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    
    /**
     * Generate unique report ID.
     * 
     * @return Unique report ID
     */
    public static String generateReportId() {
        return "REPORT_" + UUID.randomUUID().toString().replace("-", "");
    }
    
    /**
     * Generate timestamp-based filename.
     * 
     * @param baseName Base filename
     * @param format Report format
     * @return Generated filename
     */
    public static String generateFileName(String baseName, ReportFormat format) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        return String.format("%s_%s.%s", baseName, timestamp, format.getExtension());
    }
    
    /**
     * Validate report request.
     * 
     * @param request Report request
     * @return true if valid, false otherwise
     */
    public static boolean isValidRequest(Object request) {
        return request != null;
    }
}
```

### Bước 10: Triển Khai Security Features

#### 10.1 ReportSecurityConfig
```java
package com.tms.base.starter.report.autoconfig;

import com.tms.base.starter.report.service.ReportEncryptionService;
import com.tms.base.starter.report.service.ReportDigitalSignatureService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Security configuration for report module.
 * 
 * @since 1.0.0
 */
@Configuration
public class ReportSecurityConfig {
    
    @Bean
    @ConditionalOnProperty(name = "base.report.security.enableEncryption", havingValue = "true")
    public ReportEncryptionService reportEncryptionService(ReportProperties properties) {
        return new ReportEncryptionService(properties.getSecurity());
    }
    
    @Bean
    @ConditionalOnProperty(name = "base.report.security.enableDigitalSignature", havingValue = "true")
    public ReportDigitalSignatureService reportDigitalSignatureService(ReportProperties properties) {
        return new ReportDigitalSignatureService(properties.getSecurity());
    }
}
```

### Bước 11: Triển Khai Testing

#### 11.1 Integration Test
```java
package com.tms.base.starter.report;

import com.tms.base.report.dto.ReportRequest;
import com.tms.base.report.dto.ReportResponse;
import com.tms.base.report.dto.StreamingReportResponse;
import com.tms.base.report.dto.StreamingConfig;
import com.tms.base.report.enums.ReportFormat;
import com.tms.base.report.enums.StreamingMode;
import com.tms.base.starter.report.service.ReportService;
import com.tms.base.starter.report.service.AsyncReportService;
import com.tms.base.starter.report.service.StreamingReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for report module.
 * 
 * @since 1.0.0
 */
@SpringBootTest
@TestPropertySource(properties = {
    "base.report.pdf.enabled=true",
    "base.report.excel.enabled=true",
    "base.report.streaming.enabled=true"
})
class ReportIntegrationTest {
    
    @Autowired
    private ReportService reportService;
    
    @Autowired
    private AsyncReportService asyncReportService;
    
    @Autowired
    private StreamingReportService streamingReportService;
    
    @Test
    void shouldGeneratePdfReport() {
        ReportRequest<String> request = ReportRequest.<String>builder()
            .format(ReportFormat.PDF)
            .templateName("test-template")
            .data("test data")
            .build();
            
        ReportResponse response = reportService.generate(request);
        
        assertNotNull(response);
        assertEquals(ReportFormat.PDF, response.getFormat());
        assertNotNull(response.getData());
    }
    
    @Test
    void shouldGenerateExcelReport() {
        ReportRequest<String> request = ReportRequest.<String>builder()
            .format(ReportFormat.EXCEL)
            .templateName("test-template")
            .data("test data")
            .build();
            
        ReportResponse response = reportService.generate(request);
        
        assertNotNull(response);
        assertEquals(ReportFormat.EXCEL, response.getFormat());
        assertNotNull(response.getData());
    }
    
    @Test
    void shouldGenerateAsyncReport() {
        ReportRequest<String> request = ReportRequest.<String>builder()
            .format(ReportFormat.PDF)
            .templateName("test-template")
            .data("test data")
            .build();
            
        CompletableFuture<ReportResponse> future = asyncReportService.generateAsync(request);
        
        assertNotNull(future);
        assertTrue(future.isDone());
        
        ReportResponse response = future.join();
        assertNotNull(response);
        assertEquals(ReportFormat.PDF, response.getFormat());
    }
    
    @Test
    void shouldGenerateStreamingReport() {
        ReportRequest<String> request = ReportRequest.<String>builder()
            .format(ReportFormat.PDF)
            .templateName("test-template")
            .data("test data")
            .build();
            
        StreamingConfig config = StreamingConfig.builder()
            .mode(StreamingMode.CHUNKED)
            .chunkSize(1024)
            .bufferSize(8192)
            .build();
            
        StreamingReportResponse response = streamingReportService.generateStreaming(request, config);
        
        assertNotNull(response);
        assertEquals(ReportFormat.PDF, response.getFormat());
        assertEquals(StreamingMode.CHUNKED, response.getStreamingMode());
        assertNotNull(response.getDataStream());
    }
    
    @Test
    void shouldGenerateStreamingAsyncReport() {
        ReportRequest<String> request = ReportRequest.<String>builder()
            .format(ReportFormat.EXCEL)
            .templateName("test-template")
            .data("test data")
            .build();
            
        StreamingConfig config = StreamingConfig.builder()
            .mode(StreamingMode.BUFFERED)
            .chunkSize(2048)
            .bufferSize(16384)
            .build();
            
        CompletableFuture<StreamingReportResponse> future = 
            streamingReportService.generateStreamingAsync(request, config);
        
        assertNotNull(future);
        assertTrue(future.isDone());
        
        StreamingReportResponse response = future.join();
        assertNotNull(response);
        assertEquals(ReportFormat.EXCEL, response.getFormat());
        assertEquals(StreamingMode.BUFFERED, response.getStreamingMode());
    }
    
    @Test
    void shouldCreateStreamingResponseBody() throws IOException {
        ReportRequest<String> request = ReportRequest.<String>builder()
            .format(ReportFormat.PDF)
            .templateName("test-template")
            .data("test data")
            .build();
            
        StreamingConfig config = streamingReportService.createDefaultStreamingConfig();
        
        var responseBody = streamingReportService.createStreamingResponseBody(request, config);
        assertNotNull(responseBody);
        
        // Test streaming to output stream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        responseBody.writeTo(outputStream);
        
        assertTrue(outputStream.size() > 0);
    }
}
```

### Bước 12: Tạo META-INF/services

#### 12.1 ServiceLoader Configuration
Tạo file `src/main/resources/META-INF/services/com.tms.base.report.spi.ReportProvider`:
```
com.tms.base.starter.report.pdf.PdfReportProvider
com.tms.base.starter.report.excel.ExcelReportProvider
```

### Bước 13: Tạo Default Templates

#### 13.1 PDF Template
Tạo file `src/main/resources/templates/pdf/driver-report.html`:
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Driver Report</title>
</head>
<body>
    <h1>Driver Report</h1>
    <p>Generated on: <span th:text="${#dates.format(#dates.createNow(), 'dd/MM/yyyy HH:mm')}"></span></p>
    <div th:each="driver : ${drivers}">
        <h3 th:text="${driver.name}">Driver Name</h3>
        <p>License: <span th:text="${driver.licenseNumber}"></span></p>
        <p>Status: <span th:text="${driver.status}"></span></p>
    </div>
</body>
</html>
```

#### 13.2 Excel Template
Tạo file `src/main/resources/templates/excel/weekly-report.xlsx`:
```
# This would be an actual Excel file template
# For now, we'll generate programmatically
```

### Bước 14: Tạo Default Configuration

#### 14.1 application-report.yml
Tạo file `src/main/resources/application-report.yml`:
```yaml
base:
  report:
    default-format: PDF
    thread-pool:
      core-size: 5
      max-size: 20
      queue-capacity: 100
      keep-alive-seconds: 60
    pdf:
      enabled: true
      engine: itext
      template-path: classpath:templates/pdf/
      enable-compression: true
      compression-level: MEDIUM
    excel:
      enabled: true
      engine: poi
      template-path: classpath:templates/excel/
      enable-streaming: true
      max-rows-in-memory: 1000
    template:
      engine: thymeleaf
      cache-templates: true
      template-path: classpath:templates/
    streaming:
      enabled: true
      default-mode: CHUNKED
      chunk-size: 8192
      buffer-size: 65536
      max-chunks: 1000
      timeout-ms: 300000
      enable-compression: true
      compression-level: MEDIUM
    security:
      enable-encryption: false
      enable-digital-signature: false
```

### Bước 15: Triển Khai Streaming Examples

#### 15.1 Spring MVC Controller Example
```java
package com.tms.base.starter.report.controller;

import com.tms.base.report.dto.ReportRequest;
import com.tms.base.report.dto.StreamingConfig;
import com.tms.base.report.enums.ReportFormat;
import com.tms.base.report.enums.StreamingMode;
import com.tms.base.starter.report.service.StreamingReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.Map;

/**
 * REST controller for streaming report generation.
 * 
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/reports/streaming")
public class StreamingReportController {
    
    @Autowired
    private StreamingReportService streamingReportService;
    
    /**
     * Generate streaming PDF report.
     */
    @PostMapping("/pdf")
    public ResponseEntity<StreamingResponseBody> generateStreamingPdf(
        @RequestBody Map<String, Object> data,
        @RequestParam(defaultValue = "CHUNKED") String mode,
        @RequestParam(defaultValue = "8192") int chunkSize
    ) {
        ReportRequest<Map<String, Object>> request = ReportRequest.<Map<String, Object>>builder()
            .format(ReportFormat.PDF)
            .templateName("large-report")
            .data(data)
            .build();
            
        StreamingConfig config = StreamingConfig.builder()
            .mode(StreamingMode.valueOf(mode))
            .chunkSize(chunkSize)
            .bufferSize(chunkSize * 8)
            .build();
            
        StreamingResponseBody responseBody = streamingReportService.createStreamingResponseBody(request, config);
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf")
            .header("X-Streaming-Mode", mode)
            .body(responseBody);
    }
    
    /**
     * Generate streaming Excel report.
     */
    @PostMapping("/excel")
    public ResponseEntity<StreamingResponseBody> generateStreamingExcel(
        @RequestBody Map<String, Object> data,
        @RequestParam(defaultValue = "BUFFERED") String mode,
        @RequestParam(defaultValue = "16384") int bufferSize
    ) {
        ReportRequest<Map<String, Object>> request = ReportRequest.<Map<String, Object>>builder()
            .format(ReportFormat.EXCEL)
            .templateName("large-excel-report")
            .data(data)
            .build();
            
        StreamingConfig config = StreamingConfig.builder()
            .mode(StreamingMode.valueOf(mode))
            .chunkSize(4096)
            .bufferSize(bufferSize)
            .enableCompression(true)
            .build();
            
        StreamingResponseBody responseBody = streamingReportService.createStreamingResponseBody(request, config);
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.xlsx")
            .header("X-Streaming-Mode", mode)
            .body(responseBody);
    }
    
    /**
     * Generate reactive streaming report.
     */
    @PostMapping("/reactive")
    public ResponseEntity<StreamingResponseBody> generateReactiveStreaming(
        @RequestBody Map<String, Object> data
    ) {
        ReportRequest<Map<String, Object>> request = ReportRequest.<Map<String, Object>>builder()
            .format(ReportFormat.PDF)
            .templateName("reactive-report")
            .data(data)
            .build();
            
        StreamingConfig config = StreamingConfig.builder()
            .mode(StreamingMode.REACTIVE)
            .chunkSize(2048)
            .bufferSize(8192)
            .maxChunks(5000)
            .timeoutMs(600000) // 10 minutes
            .build();
            
        StreamingResponseBody responseBody = streamingReportService.createStreamingResponseBody(request, config);
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reactive-report.pdf")
            .header("X-Streaming-Mode", "REACTIVE")
            .body(responseBody);
    }
}
```

#### 15.2 Usage Examples
```java
// Basic streaming usage
@Service
public class LargeReportService {
    
    @Autowired
    private StreamingReportService streamingReportService;
    
    public StreamingReportResponse generateLargeDriverReport(List<Driver> drivers) {
        ReportRequest<List<Driver>> request = ReportRequest.<List<Driver>>builder()
            .format(ReportFormat.PDF)
            .templateName("large-driver-report")
            .data(drivers)
            .build();
            
        StreamingConfig config = StreamingConfig.builder()
            .mode(StreamingMode.CHUNKED)
            .chunkSize(8192)
            .bufferSize(65536)
            .enableCompression(true)
            .build();
            
        return streamingReportService.generateStreaming(request, config);
    }
    
    public CompletableFuture<StreamingReportResponse> generateAsyncLargeReport(
        List<Company> companies,
        Consumer<ReportProgress> progressCallback
    ) {
        ReportRequest<List<Company>> request = ReportRequest.<List<Company>>builder()
            .format(ReportFormat.EXCEL)
            .templateName("company-report")
            .data(companies)
            .build();
            
        StreamingConfig config = StreamingConfig.builder()
            .mode(StreamingMode.PAGINATED)
            .chunkSize(16384)
            .bufferSize(131072)
            .maxChunks(10000)
            .build();
            
        return streamingReportService.generateStreamingWithProgress(request, config, progressCallback);
    }
}
```

## ✅ Definition of Done

- [ ] Tất cả implementations được triển khai đầy đủ
- [ ] Spring Boot AutoConfiguration hoạt động
- [ ] PDF và Excel generation hoạt động
- [ ] Template engines integration hoạt động
- [ ] Async operations hoạt động
- [ ] **Streaming operations hoạt động**
- [ ] **HTTP streaming với Spring MVC hoạt động**
- [ ] **Multiple streaming modes được hỗ trợ**
- [ ] Security features hoạt động
- [ ] Integration tests pass
- [ ] **Streaming tests pass**
- [ ] Unit tests đạt coverage ≥ 80%
- [ ] Performance tests pass
- [ ] **Streaming performance tests pass**
- [ ] Documentation đầy đủ

## 🔄 Next Steps

Sau khi hoàn thành `java-base-starter-report`, có thể:
- Tích hợp với `java-base-file` cho file storage
- Tích hợp với `java-base-observability` cho monitoring
- Tích hợp với `java-base-security` cho authentication
- Tạo demo application

## 📚 References

- [Spring Boot AutoConfiguration](https://docs.spring.io/spring-boot/docs/current/reference/html/auto-configuration.html)
- [iText PDF Library](https://itextpdf.com/)
- [Apache POI](https://poi.apache.org/)
- [Thymeleaf Template Engine](https://www.thymeleaf.org/)
- [Spring Boot Testing](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-testing)
