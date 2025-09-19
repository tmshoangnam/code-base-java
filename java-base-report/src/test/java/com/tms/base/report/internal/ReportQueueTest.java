package com.tms.base.report.internal;

import com.tms.base.report.dto.ReportRequest;
import com.tms.base.report.enums.ReportFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ReportQueue.
 *
 * @since 1.0.0
 */
class ReportQueueTest {

    private ReportQueue reportQueue;
    private TestReportTask task1;
    private TestReportTask task2;

    @BeforeEach
    void setUp() {
        reportQueue = new ReportQueue(10, 5);
        task1 = new TestReportTask("task-1");
        task2 = new TestReportTask("task-2");
    }

    @Test
    void shouldAddTaskToQueue() {
        boolean added = reportQueue.addTask(task1);

        assertTrue(added);
        assertEquals(1, reportQueue.getQueueSize());
    }

    @Test
    void shouldNotAddTaskWhenQueueIsFull() {
        // Fill up the queue
        for (int i = 0; i < 10; i++) {
            reportQueue.addTask(new TestReportTask("task-" + i));
        }

        boolean added = reportQueue.addTask(task1);

        assertFalse(added);
        assertEquals(10, reportQueue.getQueueSize());
    }

    @Test
    void shouldTakeTaskFromQueue() {
        reportQueue.addTask(task1);

        ReportTask<?> taken = reportQueue.takeTask();

        assertNotNull(taken);
        assertEquals(task1, taken);
        assertEquals(0, reportQueue.getQueueSize());
    }

    @Test
    void shouldPollTaskFromQueue() {
        reportQueue.addTask(task1);

        ReportTask<?> polled = reportQueue.pollTask();

        assertNotNull(polled);
        assertEquals(task1, polled);
        assertEquals(0, reportQueue.getQueueSize());
    }

    @Test
    void shouldReturnNullWhenPollingEmptyQueue() {
        ReportTask<?> polled = reportQueue.pollTask();

        assertNull(polled);
    }

    @Test
    void shouldRegisterActiveTask() {
        boolean registered = reportQueue.registerActiveTask(task1);

        assertTrue(registered);
        assertEquals(1, reportQueue.getActiveTasksCount());
        assertEquals(task1, reportQueue.getActiveTask("task-1"));
    }

    @Test
    void shouldNotRegisterTaskWhenMaxActiveTasksReached() {
        // Fill up active tasks
        for (int i = 0; i < 5; i++) {
            reportQueue.registerActiveTask(new TestReportTask("task-" + i));
        }

        boolean registered = reportQueue.registerActiveTask(task1);

        assertFalse(registered);
        assertEquals(5, reportQueue.getActiveTasksCount());
    }

    @Test
    void shouldUnregisterActiveTask() {
        reportQueue.registerActiveTask(task1);
        assertEquals(1, reportQueue.getActiveTasksCount());

        ReportTask<?> removed = reportQueue.unregisterActiveTask("task-1");

        assertNotNull(removed);
        assertEquals(task1, removed);
        assertEquals(0, reportQueue.getActiveTasksCount());
    }

    @Test
    void shouldReturnNullWhenUnregisteringNonExistentTask() {
        ReportTask<?> removed = reportQueue.unregisterActiveTask("non-existent");

        assertNull(removed);
    }

    @Test
    void shouldGenerateUniqueTaskIds() {
        String id1 = reportQueue.generateTaskId();
        String id2 = reportQueue.generateTaskId();

        assertNotNull(id1);
        assertNotNull(id2);
        assertNotEquals(id1, id2);
        assertTrue(id1.startsWith("task-"));
        assertTrue(id2.startsWith("task-"));
    }

    @Test
    void shouldCheckQueueFull() {
        assertFalse(reportQueue.isQueueFull());

        // Fill up the queue
        for (int i = 0; i < 10; i++) {
            reportQueue.addTask(new TestReportTask("task-" + i));
        }

        assertTrue(reportQueue.isQueueFull());
    }

    @Test
    void shouldCheckMaxActiveTasksReached() {
        assertFalse(reportQueue.isMaxActiveTasksReached());

        // Fill up active tasks
        for (int i = 0; i < 5; i++) {
            reportQueue.registerActiveTask(new TestReportTask("task-" + i));
        }

        assertTrue(reportQueue.isMaxActiveTasksReached());
    }

    @Test
    void shouldClearAllTasks() {
        reportQueue.addTask(task1);
        reportQueue.addTask(task2);
        reportQueue.registerActiveTask(task1);

        reportQueue.clear();

        assertEquals(0, reportQueue.getQueueSize());
        assertEquals(0, reportQueue.getActiveTasksCount());
    }

    // Test implementation
    private static class TestReportTask extends ReportTask<String> {
        public TestReportTask(String taskId) {
            super(taskId,
                ReportRequest.<String>builder()
                    .format(ReportFormat.PDF)
                    .data("test")
                    .build(),
                new TestReportGenerator());
        }
    }

    private static class TestReportGenerator implements com.tms.base.report.api.ReportGenerator<String> {
        @Override
        public com.tms.base.report.dto.ReportResponse generate(com.tms.base.report.dto.ReportRequest<String> request) {
            return com.tms.base.report.dto.ReportResponse.builder()
                .data("test".getBytes())
                .format(request.getFormat())
                .build();
        }

        @Override
        public java.util.concurrent.CompletableFuture<com.tms.base.report.dto.ReportResponse> generateAsync(com.tms.base.report.dto.ReportRequest<String> request) {
            return java.util.concurrent.CompletableFuture.completedFuture(generate(request));
        }

        @Override
        public boolean supports(com.tms.base.report.enums.ReportFormat format) {
            return com.tms.base.report.enums.ReportFormat.PDF.equals(format);
        }

        @Override
        public String getName() {
            return "test-generator";
        }

        @Override
        public com.tms.base.report.enums.ReportFormat[] getSupportedFormats() {
            return new com.tms.base.report.enums.ReportFormat[]{com.tms.base.report.enums.ReportFormat.PDF};
        }
    }
}
