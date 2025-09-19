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
 * @param <T> Type of data to be processed
 * @since 1.0.0
 */
public class ThreadSafeReportExecutor<T> {
    
    private final ExecutorService executorService;
    private final AtomicLong taskCounter = new AtomicLong(0);
    private final int maxConcurrentTasks;
    private final ReportQueue reportQueue;
    
    public ThreadSafeReportExecutor(int corePoolSize, int maxPoolSize, int maxConcurrentTasks) {
        this.maxConcurrentTasks = maxConcurrentTasks;
        this.reportQueue = new ReportQueue(maxConcurrentTasks * 2, maxConcurrentTasks);
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
        String taskId = reportQueue.generateTaskId();
        ReportTask<T> task = new ReportTask<>(taskId, request, generator);
        
        if (!reportQueue.addTask(task)) {
            return CompletableFuture.failedFuture(
                new IllegalStateException("Report queue is full")
            );
        }
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (!reportQueue.registerActiveTask(task)) {
                    throw new IllegalStateException("Max active tasks reached");
                }
                
                ReportResponse response = generator.generate(request);
                reportQueue.unregisterActiveTask(taskId);
                return response;
            } catch (Exception e) {
                reportQueue.unregisterActiveTask(taskId);
                throw new RuntimeException("Report generation failed", e);
            }
        }, executorService);
    }
    
    /**
     * Execute report generation with task management.
     * 
     * @param generator Report generator
     * @param request Report request
     * @return CompletableFuture with result
     */
    public CompletableFuture<ReportResponse> executeWithTaskManagement(
        ReportGenerator<T> generator, 
        ReportRequest<T> request
    ) {
        String taskId = reportQueue.generateTaskId();
        ReportTask<T> task = new ReportTask<>(taskId, request, generator);
        
        if (!reportQueue.addTask(task)) {
            return CompletableFuture.failedFuture(
                new IllegalStateException("Report queue is full")
            );
        }
        
        return task.execute();
    }
    
    /**
     * Cancel task by ID.
     * 
     * @param taskId Task ID
     * @return true if cancelled successfully
     */
    public boolean cancelTask(String taskId) {
        ReportTask<?> task = reportQueue.getActiveTask(taskId);
        if (task != null) {
            return task.cancel();
        }
        return false;
    }
    
    /**
     * Get queue statistics.
     * 
     * @return Queue statistics
     */
    public QueueStatistics getQueueStatistics() {
        return new QueueStatistics(
            reportQueue.getQueueSize(),
            reportQueue.getActiveTasksCount(),
            maxConcurrentTasks,
            reportQueue.isQueueFull(),
            reportQueue.isMaxActiveTasksReached()
        );
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
    
    /**
     * Queue statistics.
     */
    public static class QueueStatistics {
        private final int queueSize;
        private final int activeTasks;
        private final int maxConcurrentTasks;
        private final boolean queueFull;
        private final boolean maxActiveTasksReached;
        
        public QueueStatistics(int queueSize, int activeTasks, int maxConcurrentTasks, 
                             boolean queueFull, boolean maxActiveTasksReached) {
            this.queueSize = queueSize;
            this.activeTasks = activeTasks;
            this.maxConcurrentTasks = maxConcurrentTasks;
            this.queueFull = queueFull;
            this.maxActiveTasksReached = maxActiveTasksReached;
        }
        
        public int getQueueSize() { return queueSize; }
        public int getActiveTasks() { return activeTasks; }
        public int getMaxConcurrentTasks() { return maxConcurrentTasks; }
        public boolean isQueueFull() { return queueFull; }
        public boolean isMaxActiveTasksReached() { return maxActiveTasksReached; }
    }
}
