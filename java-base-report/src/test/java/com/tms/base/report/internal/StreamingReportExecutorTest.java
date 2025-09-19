package com.tms.base.report.internal;

import com.tms.base.report.dto.ReportRequest;
import com.tms.base.report.dto.StreamingReportResponse;
import com.tms.base.report.dto.StreamingConfig;
import com.tms.base.report.api.StreamingReportGenerator;
import com.tms.base.report.enums.ReportFormat;
import com.tms.base.report.enums.StreamingMode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Unit tests for StreamingReportExecutor.
 *
 * @since 1.0.0
 */
class StreamingReportExecutorTest {

    private StreamingReportExecutor<String> executor;
    private TestStreamingReportGenerator generator;
    private ReportRequest<String> request;
    private StreamingConfig config;

    @BeforeEach
    void setUp() {
        executor = new StreamingReportExecutor<>();
        generator = new TestStreamingReportGenerator();
        request = ReportRequest.<String>builder()
            .format(ReportFormat.PDF)
            .data("test data")
            .build();
        config = StreamingConfig.builder()
            .mode(StreamingMode.CHUNKED)
            .chunkSize(1024)
            .build();
    }

    @AfterEach
    void tearDown() {
        executor.shutdown();
    }

    @Test
    void shouldExecuteStreamingGeneration() throws Exception {
        CompletableFuture<StreamingReportResponse> future = executor.executeStreaming(generator, request, config);

        StreamingReportResponse response = future.get(5, TimeUnit.SECONDS);

        assertNotNull(response);
        assertEquals(ReportFormat.PDF, response.getFormat());
        assertNotNull(response.getDataStream());
    }

    @Test
    void shouldExecuteStreamingWithProgress() throws Exception {
        AtomicInteger progressCount = new AtomicInteger(0);

        CompletableFuture<StreamingReportResponse> future = executor.executeStreamingWithProgress(
            generator, request, config, progress -> progressCount.incrementAndGet()
        );

        StreamingReportResponse response = future.get(5, TimeUnit.SECONDS);

        assertNotNull(response);
        assertEquals(ReportFormat.PDF, response.getFormat());
        assertNotNull(response.getDataStream());
    }

    @Test
    void shouldHandleStreamingFailure() {
        generator.shouldFail = true;

        CompletableFuture<StreamingReportResponse> future = executor.executeStreaming(generator, request, config);

        assertThrows(ExecutionException.class, () -> {
            future.get(5, TimeUnit.SECONDS);
        });
    }

    @Test
    void shouldCreateStreamingInputStream() throws Exception {
        byte[] data = "test streaming data".getBytes();
        InputStream stream = executor.createStreamingInputStream(data, config);

        assertNotNull(stream);

        byte[] buffer = new byte[1024];
        int bytesRead = stream.read(buffer);

        assertTrue(bytesRead > 0);
        assertTrue(bytesRead <= data.length);
    }

    @Test
    void shouldCreateStreamingInputStreamWithCallbacks() throws Exception {
        byte[] data = "test streaming data".getBytes();
        AtomicInteger chunkCount = new AtomicInteger(0);
        AtomicBoolean completed = new AtomicBoolean(false);
        AtomicBoolean error = new AtomicBoolean(false);

        InputStream stream = executor.createStreamingInputStreamWithCallbacks(
            data, config,
            chunk -> chunkCount.incrementAndGet(),
            () -> completed.set(true),
            () -> error.set(true)
        );

        assertNotNull(stream);

        byte[] buffer = new byte[1024];
        int totalBytes = 0;
        int bytesRead;

        while ((bytesRead = stream.read(buffer)) != -1) {
            totalBytes += bytesRead;
        }

        assertEquals(data.length, totalBytes);
        assertTrue(chunkCount.get() > 0);
        assertTrue(completed.get());
        assertFalse(error.get());
    }

    @Test
    void shouldHandleCallbackErrors() {
        byte[] data = "test streaming data".getBytes();
        AtomicBoolean error = new AtomicBoolean(false);

        InputStream stream = executor.createStreamingInputStreamWithCallbacks(
            data, config,
            chunk -> { throw new RuntimeException("Callback error"); },
            () -> {},
            () -> error.set(true)
        );

        assertNotNull(stream);

        // Reading should trigger the error callback
        byte[] buffer = new byte[1024];
        assertThrows(RuntimeException.class, () -> {
            stream.read(buffer);
        });

        assertTrue(error.get());
    }

    @Test
    void shouldShutdownGracefully() throws Exception {
        CompletableFuture<StreamingReportResponse> future = executor.executeStreaming(generator, request, config);

        // Shutdown executor
        executor.shutdown();

        // Should still complete the running task
        StreamingReportResponse response = future.get(5, TimeUnit.SECONDS);
        assertNotNull(response);
    }

    @Test
    void shouldNotExecuteAfterShutdown() {
        executor.shutdown();

        CompletableFuture<StreamingReportResponse> future = executor.executeStreaming(generator, request, config);

        assertThrows(ExecutionException.class, () -> {
            future.get(5, TimeUnit.SECONDS);
        });
    }

    // Test implementation
    private static class TestStreamingReportGenerator implements StreamingReportGenerator<String> {
        boolean shouldFail = false;

        @Override
        public com.tms.base.report.dto.ReportResponse generate(com.tms.base.report.dto.ReportRequest<String> request) {
            return com.tms.base.report.dto.ReportResponse.builder()
                .data(request.getData().getBytes())
                .format(request.getFormat())
                .build();
        }

        @Override
        public java.util.concurrent.CompletableFuture<com.tms.base.report.dto.ReportResponse> generateAsync(com.tms.base.report.dto.ReportRequest<String> request) {
            return java.util.concurrent.CompletableFuture.completedFuture(generate(request));
        }

        @Override
        public StreamingReportResponse generateStreaming(ReportRequest<String> request, StreamingConfig config) {
            if (shouldFail) {
                throw new RuntimeException("Streaming generation failed");
            }

            return StreamingReportResponse.builder()
                .format(request.getFormat())
                .dataStream(new ByteArrayInputStream(request.getData().getBytes()))
                .streamingMode(config.getMode())
                .chunkSize(config.getChunkSize())
                .build();
        }

        @Override
        public StreamingReportResponse generateStreamingWithProgress(
            ReportRequest<String> request,
            StreamingConfig config,
            java.util.function.Consumer<com.tms.base.report.dto.ReportProgress> progressCallback
        ) {
            if (shouldFail) {
                throw new RuntimeException("Streaming generation failed");
            }

            // Simulate progress updates
            if (progressCallback != null) {
                progressCallback.accept(com.tms.base.report.dto.ReportProgress.builder()
                    .requestId(request.getRequestId())
                    .percentage(50)
                    .build());
            }

            return generateStreaming(request, config);
        }

        @Override
        public java.util.concurrent.CompletableFuture<StreamingReportResponse> generateStreamingAsync(
            ReportRequest<String> request,
            StreamingConfig config
        ) {
            return java.util.concurrent.CompletableFuture.completedFuture(generateStreaming(request, config));
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
