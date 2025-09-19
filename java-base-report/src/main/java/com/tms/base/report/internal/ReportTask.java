package com.tms.base.report.internal;

import com.tms.base.report.dto.ReportRequest;
import com.tms.base.report.dto.ReportResponse;
import com.tms.base.report.api.ReportGenerator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.time.LocalDateTime;

/**
 * Task wrapper for report generation.
 * 
 * @param <T> Type of data to be processed
 * @since 1.0.0
 */
public class ReportTask<T> {
    
    private final String taskId;
    private final ReportRequest<T> request;
    private final ReportGenerator<T> generator;
    private final CompletableFuture<ReportResponse> future;
    private final AtomicBoolean cancelled = new AtomicBoolean(false);
    private final LocalDateTime createdAt;
    private volatile LocalDateTime startedAt;
    private volatile LocalDateTime completedAt;
    
    public ReportTask(String taskId, ReportRequest<T> request, ReportGenerator<T> generator) {
        this.taskId = taskId;
        this.request = request;
        this.generator = generator;
        this.createdAt = LocalDateTime.now();
        this.future = new CompletableFuture<>();
    }
    
    /**
     * Execute the report generation task.
     * 
     * @return CompletableFuture with result
     */
    public CompletableFuture<ReportResponse> execute() {
        if (cancelled.get()) {
            future.completeExceptionally(new IllegalStateException("Task already cancelled"));
            return future;
        }
        
        startedAt = LocalDateTime.now();
        
        try {
            ReportResponse response = generator.generate(request);
            completedAt = LocalDateTime.now();
            future.complete(response);
        } catch (Exception e) {
            completedAt = LocalDateTime.now();
            future.completeExceptionally(e);
        }
        
        return future;
    }
    
    /**
     * Cancel the task.
     * 
     * @return true if cancelled successfully
     */
    public boolean cancel() {
        if (cancelled.compareAndSet(false, true)) {
            if (!future.isDone()) {
                future.cancel(true);
            }
            return true;
        }
        return false;
    }
    
    /**
     * Check if task is cancelled.
     * 
     * @return true if cancelled
     */
    public boolean isCancelled() {
        return cancelled.get();
    }
    
    /**
     * Check if task is completed.
     * 
     * @return true if completed
     */
    public boolean isCompleted() {
        return future.isDone();
    }
    
    // Getters
    public String getTaskId() {
        return taskId;
    }
    
    public ReportRequest<T> getRequest() {
        return request;
    }
    
    public ReportGenerator<T> getGenerator() {
        return generator;
    }
    
    public CompletableFuture<ReportResponse> getFuture() {
        return future;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getStartedAt() {
        return startedAt;
    }
    
    public LocalDateTime getCompletedAt() {
        return completedAt;
    }
}
