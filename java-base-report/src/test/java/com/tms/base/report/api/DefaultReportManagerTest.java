package com.tms.base.report.api;

import com.tms.base.report.dto.ReportRequest;
import com.tms.base.report.dto.ReportResponse;
import com.tms.base.report.enums.ReportFormat;
import com.tms.base.report.enums.StreamingMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.CompletableFuture;

/**
 * Unit tests for DefaultReportManager.
 * 
 * @since 1.0.0
 */
class DefaultReportManagerTest {
    
    private DefaultReportManager reportManager;
    private TestReportGenerator testGenerator;
    private TestAsyncReportGenerator testAsyncGenerator;
    private TestStreamingReportGenerator testStreamingGenerator;
    
    @BeforeEach
    void setUp() {
        reportManager = new DefaultReportManager();
        testGenerator = new TestReportGenerator();
        testAsyncGenerator = new TestAsyncReportGenerator();
        testStreamingGenerator = new TestStreamingReportGenerator();
    }
    
    @Test
    void shouldRegisterAndRetrieveGenerator() throws Exception {
        reportManager.registerGenerator(testGenerator);
        
        ReportGenerator<String> retrieved = reportManager.getGenerator(ReportFormat.PDF);
        assertNotNull(retrieved);
        assertEquals("test-generator", retrieved.getName());
        
        ReportGenerator<String> byName = reportManager.getGenerator("test-generator");
        assertNotNull(byName);
        assertEquals("test-generator", byName.getName());
    }
    
    @Test
    void shouldRegisterAndRetrieveAsyncGenerator() throws Exception {
        reportManager.registerAsyncGenerator(testAsyncGenerator);
        
        AsyncReportGenerator<String> retrieved = reportManager.getAsyncGenerator(ReportFormat.PDF);
        assertNotNull(retrieved);
        assertEquals("test-async-generator", retrieved.getName());
        
        AsyncReportGenerator<String> byName = reportManager.getAsyncGenerator("test-async-generator");
        assertNotNull(byName);
        assertEquals("test-async-generator", byName.getName());
    }
    
    @Test
    void shouldRegisterAndRetrieveStreamingGenerator() throws Exception {
        reportManager.registerStreamingGenerator(testStreamingGenerator);
        
        StreamingReportGenerator<String> retrieved = reportManager.getStreamingGenerator(ReportFormat.PDF);
        assertNotNull(retrieved);
        assertEquals("test-streaming-generator", retrieved.getName());
        
        StreamingReportGenerator<String> byName = reportManager.getStreamingGenerator("test-streaming-generator");
        assertNotNull(byName);
        assertEquals("test-streaming-generator", byName.getName());
    }
    
    @Test
    void shouldThrowExceptionWhenGeneratorNotFound() {
        assertThrows(ReportException.class, () -> {
            reportManager.getGenerator(ReportFormat.PDF);
        });
        
        assertThrows(ReportException.class, () -> {
            reportManager.getGenerator("non-existent");
        });
    }
    
    @Test
    void shouldCheckStreamingSupport() {
        assertFalse(reportManager.isStreamingSupported(ReportFormat.PDF));
        
        reportManager.registerStreamingGenerator(testStreamingGenerator);
        
        assertTrue(reportManager.isStreamingSupported(ReportFormat.PDF));
    }
    
    @Test
    void shouldGetSupportedStreamingModes() {
        reportManager.registerStreamingGenerator(testStreamingGenerator);
        
        StreamingMode[] modes = reportManager.getSupportedStreamingModes(ReportFormat.PDF);
        assertNotNull(modes);
        assertEquals(1, modes.length);
        assertEquals(StreamingMode.CHUNKED, modes[0]);
    }
    
    @Test
    void shouldUnregisterGenerator() throws Exception {
        reportManager.registerGenerator(testGenerator);
        assertNotNull(reportManager.getGenerator(ReportFormat.PDF));
        
        reportManager.unregisterGenerator("test-generator");
        
        assertThrows(ReportException.class, () -> {
            reportManager.getGenerator(ReportFormat.PDF);
        });
    }
    
    @Test
    void shouldGetAllGenerators() {
        reportManager.registerGenerator(testGenerator);
        reportManager.registerAsyncGenerator(testAsyncGenerator);
        reportManager.registerStreamingGenerator(testStreamingGenerator);
        
        assertEquals(1, reportManager.getAllGenerators().size());
        assertEquals(1, reportManager.getAllAsyncGenerators().size());
        assertEquals(1, reportManager.getAllStreamingGenerators().size());
    }
    
    // Test implementations
    private static class TestReportGenerator implements ReportGenerator<String> {
        @Override
        public ReportResponse generate(ReportRequest<String> request) {
            return ReportResponse.builder()
                .data("test data".getBytes())
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
    
    private static class TestAsyncReportGenerator implements AsyncReportGenerator<String> {
        @Override
        public ReportResponse generate(ReportRequest<String> request) {
            return ReportResponse.builder()
                .data("test data".getBytes())
                .format(request.getFormat())
                .build();
        }
        
        @Override
        public CompletableFuture<ReportResponse> generateAsync(ReportRequest<String> request) {
            return CompletableFuture.completedFuture(generate(request));
        }
        
        @Override
        public CompletableFuture<ReportResponse> generateWithProgress(
            ReportRequest<String> request, 
            java.util.function.Consumer<com.tms.base.report.dto.ReportProgress> progressCallback
        ) {
            return CompletableFuture.completedFuture(generate(request));
        }
        
        @Override
        public boolean cancel(String requestId) {
            return false;
        }
        
        @Override
        public com.tms.base.report.dto.ReportProgress getProgress(String requestId) {
            return com.tms.base.report.dto.ReportProgress.builder()
                .requestId(requestId)
                .percentage(100)
                .build();
        }
        
        @Override
        public boolean supports(ReportFormat format) {
            return ReportFormat.PDF.equals(format);
        }
        
        @Override
        public String getName() {
            return "test-async-generator";
        }
        
        @Override
        public ReportFormat[] getSupportedFormats() {
            return new ReportFormat[]{ReportFormat.PDF};
        }
    }
    
    private static class TestStreamingReportGenerator implements StreamingReportGenerator<String> {
        @Override
        public ReportResponse generate(ReportRequest<String> request) {
            return ReportResponse.builder()
                .data("test data".getBytes())
                .format(request.getFormat())
                .build();
        }
        
        @Override
        public CompletableFuture<ReportResponse> generateAsync(ReportRequest<String> request) {
            return CompletableFuture.completedFuture(generate(request));
        }
        
        @Override
        public com.tms.base.report.dto.StreamingReportResponse generateStreaming(
            ReportRequest<String> request, 
            com.tms.base.report.dto.StreamingConfig config
        ) {
            return com.tms.base.report.dto.StreamingReportResponse.builder()
                .format(request.getFormat())
                .dataStream(new java.io.ByteArrayInputStream("test data".getBytes()))
                .build();
        }
        
        @Override
        public com.tms.base.report.dto.StreamingReportResponse generateStreamingWithProgress(
            ReportRequest<String> request, 
            com.tms.base.report.dto.StreamingConfig config,
            java.util.function.Consumer<com.tms.base.report.dto.ReportProgress> progressCallback
        ) {
            return generateStreaming(request, config);
        }
        
        @Override
        public CompletableFuture<com.tms.base.report.dto.StreamingReportResponse> generateStreamingAsync(
            ReportRequest<String> request, 
            com.tms.base.report.dto.StreamingConfig config
        ) {
            return CompletableFuture.completedFuture(generateStreaming(request, config));
        }
        
        @Override
        public boolean supportsStreaming(StreamingMode mode) {
            return StreamingMode.CHUNKED.equals(mode);
        }
        
        @Override
        public StreamingMode[] getSupportedStreamingModes() {
            return new StreamingMode[]{StreamingMode.CHUNKED};
        }
        
        @Override
        public int getMaxChunkSize() {
            return 8192;
        }
        
        @Override
        public int getRecommendedBufferSize() {
            return 65536;
        }
        
        @Override
        public boolean supports(ReportFormat format) {
            return ReportFormat.PDF.equals(format);
        }
        
        @Override
        public String getName() {
            return "test-streaming-generator";
        }
        
        @Override
        public ReportFormat[] getSupportedFormats() {
            return new ReportFormat[]{ReportFormat.PDF};
        }
    }
}
