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
