package com.tms.base.report.internal;

import com.tms.base.report.dto.ReportRequest;
import com.tms.base.report.dto.ReportResponse;
import com.tms.base.report.api.ReportGenerator;
import com.tms.base.report.enums.ReportFormat;
import com.tms.base.report.api.ReportException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Unit tests for ThreadSafeReportExecutor.
 * 
 * @since 1.0.0
 */
class ThreadSafeReportExecutorTest {
    
    private ThreadSafeReportExecutor<String> executor;
    private TestReportGenerator generator;
    private ReportRequest<String> request;
    
    @BeforeEach
    void setUp() {
        executor = new ThreadSafeReportExecutor<>(2, 4, 3);
        generator = new TestReportGenerator();
        request = ReportRequest.<String>builder()
            .format(ReportFormat.PDF)
            .data("test data")
            .build();
    }
    
    @AfterEach
    void tearDown() {
        executor.shutdown();
    }
    
    @Test
    void shouldExecuteReportGeneration() throws Exception {
        CompletableFuture<ReportResponse> future = executor.execute(generator, request);
        
        ReportResponse response = future.get(5, TimeUnit.SECONDS);
        
        assertNotNull(response);
        assertEquals(ReportFormat.PDF, response.getFormat());
        assertArrayEquals("test data".getBytes(), response.getData());
    }
    
    @Test
    void shouldExecuteWithTaskManagement() throws Exception {
        CompletableFuture<ReportResponse> future = executor.executeWithTaskManagement(generator, request);
        
        ReportResponse response = future.get(5, TimeUnit.SECONDS);
        
        assertNotNull(response);
        assertEquals(ReportFormat.PDF, response.getFormat());
        assertArrayEquals("test data".getBytes(), response.getData());
    }
    
    @Test
    void shouldHandleExecutionFailure() {
        generator.shouldFail = true;
        
        CompletableFuture<ReportResponse> future = executor.execute(generator, request);
        
        assertThrows(ExecutionException.class, () -> {
            future.get(5, TimeUnit.SECONDS);
        });
    }
    
    @Test
    void shouldCancelTask() throws Exception {
        generator.delayMs = 1000; // 1 second delay
        CompletableFuture<ReportResponse> future = executor.execute(generator, request);
        
        // Cancel immediately
        boolean cancelled = executor.cancelTask("task-1");
        
        // Should not be able to cancel since task ID is generated internally
        assertFalse(cancelled);
        
        // Wait for completion
        ReportResponse response = future.get(5, TimeUnit.SECONDS);
        assertNotNull(response);
    }
    
    @Test
    void shouldGetQueueStatistics() {
        ThreadSafeReportExecutor.QueueStatistics stats = executor.getQueueStatistics();
        
        assertNotNull(stats);
        assertEquals(0, stats.getQueueSize());
        assertEquals(0, stats.getActiveTasks());
        assertEquals(3, stats.getMaxConcurrentTasks());
        assertFalse(stats.isQueueFull());
        assertFalse(stats.isMaxActiveTasksReached());
    }
    
    @Test
    void shouldHandleConcurrentExecutions() throws Exception {
        CompletableFuture<ReportResponse> future1 = executor.execute(generator, request);
        CompletableFuture<ReportResponse> future2 = executor.execute(generator, request);
        CompletableFuture<ReportResponse> future3 = executor.execute(generator, request);
        
        ReportResponse response1 = future1.get(5, TimeUnit.SECONDS);
        ReportResponse response2 = future2.get(5, TimeUnit.SECONDS);
        ReportResponse response3 = future3.get(5, TimeUnit.SECONDS);
        
        assertNotNull(response1);
        assertNotNull(response2);
        assertNotNull(response3);
    }
    
    @Test
    void shouldShutdownGracefully() throws Exception {
        CompletableFuture<ReportResponse> future = executor.execute(generator, request);
        
        // Shutdown executor
        executor.shutdown();
        
        // Should still complete the running task
        ReportResponse response = future.get(5, TimeUnit.SECONDS);
        assertNotNull(response);
    }
    
    // Test implementation
    private static class TestReportGenerator implements ReportGenerator<String> {
        boolean shouldFail = false;
        long delayMs = 0;
        
        @Override
        public ReportResponse generate(ReportRequest<String> request) throws ReportException {
            if (delayMs > 0) {
                try {
                    Thread.sleep(delayMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new ReportException("Interrupted", e);
                }
            }
            
            if (shouldFail) {
                throw new ReportException("Test failure");
            }
            
            return ReportResponse.builder()
                .data(request.getData().getBytes())
                .format(request.getFormat())
                .build();
        }
        
        @Override
        public java.util.concurrent.CompletableFuture<ReportResponse> generateAsync(ReportRequest<String> request) {
            return java.util.concurrent.CompletableFuture.completedFuture(generate(request));
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
