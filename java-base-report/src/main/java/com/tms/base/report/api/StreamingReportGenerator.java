package com.tms.base.report.api;

import com.tms.base.report.dto.ReportRequest;
import com.tms.base.report.dto.StreamingReportResponse;
import com.tms.base.report.dto.StreamingConfig;
import com.tms.base.report.dto.ReportProgress;
import com.tms.base.report.enums.StreamingMode;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Streaming report generator for large reports.
 * Supports chunked streaming and real-time data processing.
 * 
 * @param <T> Type of data to be processed
 * @since 1.0.0
 */
public interface StreamingReportGenerator<T> extends ReportGenerator<T> {
    
    /**
     * Generate report with streaming response.
     * 
     * @param request Report generation request
     * @param config Streaming configuration
     * @return Streaming report response
     */
    StreamingReportResponse generateStreaming(ReportRequest<T> request, StreamingConfig config);
    
    /**
     * Generate report with streaming and progress callback.
     * 
     * @param request Report generation request
     * @param config Streaming configuration
     * @param progressCallback Progress callback function
     * @return Streaming report response
     */
    StreamingReportResponse generateStreamingWithProgress(
        ReportRequest<T> request, 
        StreamingConfig config,
        Consumer<ReportProgress> progressCallback
    );
    
    /**
     * Generate report as streaming async.
     * 
     * @param request Report generation request
     * @param config Streaming configuration
     * @return CompletableFuture with streaming response
     */
    CompletableFuture<StreamingReportResponse> generateStreamingAsync(
        ReportRequest<T> request, 
        StreamingConfig config
    );
    
    /**
     * Check if this generator supports streaming mode.
     * 
     * @param mode Streaming mode
     * @return true if supported, false otherwise
     */
    boolean supportsStreaming(StreamingMode mode);
    
    /**
     * Get supported streaming modes.
     * 
     * @return Array of supported streaming modes
     */
    StreamingMode[] getSupportedStreamingModes();
    
    /**
     * Get maximum chunk size for streaming.
     * 
     * @return Maximum chunk size in bytes
     */
    int getMaxChunkSize();
    
    /**
     * Get recommended buffer size for streaming.
     * 
     * @return Recommended buffer size in bytes
     */
    int getRecommendedBufferSize();
}
