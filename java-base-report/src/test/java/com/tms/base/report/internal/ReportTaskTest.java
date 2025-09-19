package com.tms.base.report.internal;

import com.tms.base.report.dto.ReportRequest;
import com.tms.base.report.dto.ReportResponse;
import com.tms.base.report.api.ReportGenerator;
import com.tms.base.report.enums.ReportFormat;
import com.tms.base.report.api.ReportException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Unit tests for ReportTask.
 *
 * @since 1.0.0
 */
class ReportTaskTest {

    private TestReportGenerator generator;
    private ReportRequest<String> request;

    @BeforeEach
    void setUp() {
        generator = new TestReportGenerator();
        request = ReportRequest.<String>builder()
            .format(ReportFormat.PDF)
            .data("test data")
            .build();
    }

    @Test
    void shouldCreateTaskWithCorrectProperties() {
        ReportTask<String> task = new ReportTask<>("task-123", request, generator);

        assertEquals("task-123", task.getTaskId());
        assertEquals(request, task.getRequest());
        assertEquals(generator, task.getGenerator());
        assertNotNull(task.getFuture());
        assertNotNull(task.getCreatedAt());
        assertFalse(task.isCancelled());
        assertFalse(task.isCompleted());
    }

    @Test
    void shouldExecuteTaskSuccessfully() throws Exception {
        ReportTask<String> task = new ReportTask<>("task-123", request, generator);

        CompletableFuture<ReportResponse> future = task.execute();

        assertTrue(task.isCompleted());
        assertNotNull(task.getStartedAt());
        assertNotNull(task.getCompletedAt());

        ReportResponse response = future.get();
        assertNotNull(response);
        assertEquals(ReportFormat.PDF, response.getFormat());
    }

    @Test
    void shouldHandleTaskExecutionFailure() {
        generator.shouldFail = true;
        ReportTask<String> task = new ReportTask<>("task-123", request, generator);

        CompletableFuture<ReportResponse> future = task.execute();

        assertTrue(task.isCompleted());
        assertNotNull(task.getStartedAt());
        assertNotNull(task.getCompletedAt());

        assertThrows(ExecutionException.class, future::get);
    }

    @Test
    void shouldCancelTaskSuccessfully() {
        ReportTask<String> task = new ReportTask<>("task-123", request, generator);

        boolean cancelled = task.cancel();

        assertTrue(cancelled);
        assertTrue(task.isCancelled());
    }

    @Test
    void shouldNotCancelTaskTwice() {
        ReportTask<String> task = new ReportTask<>("task-123", request, generator);

        boolean cancelled1 = task.cancel();
        boolean cancelled2 = task.cancel();

        assertTrue(cancelled1);
        assertFalse(cancelled2);
        assertTrue(task.isCancelled());
    }

    @Test
    void shouldNotExecuteCancelledTask() {
        ReportTask<String> task = new ReportTask<>("task-123", request, generator);
        task.cancel();

        CompletableFuture<ReportResponse> future = task.execute();

        assertTrue(task.isCompleted());
        assertThrows(Exception.class, future::get);
    }

    // Test implementation
    private static class TestReportGenerator implements ReportGenerator<String> {
        boolean shouldFail = false;

        @Override
        public ReportResponse generate(ReportRequest<String> request) throws ReportException {
            if (shouldFail) {
                throw new ReportException("Test failure");
            }
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
}
