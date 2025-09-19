package com.tms.base.report.api;

import com.tms.base.report.dto.ReportRequest;
import com.tms.base.report.dto.ReportResponse;
import com.tms.base.report.enums.ReportFormat;
import java.util.concurrent.CompletableFuture;

/**
 * Core interface for report generation.
 * Thread-safe and framework-agnostic.
 * 
 * @param <T> Type of data to be processed
 * @since 1.0.0
 */
public interface ReportGenerator<T> {
    
    /**
     * Generate report synchronously.
     * 
     * @param request Report generation request
     * @return Generated report response
     * @throws ReportException if generation fails
     */
    ReportResponse generate(ReportRequest<T> request) throws ReportException;
    
    /**
     * Generate report asynchronously.
     * 
     * @param request Report generation request
     * @return CompletableFuture with report response
     */
    CompletableFuture<ReportResponse> generateAsync(ReportRequest<T> request);
    
    /**
     * Check if this generator supports the given format.
     * 
     * @param format Report format
     * @return true if supported, false otherwise
     */
    boolean supports(ReportFormat format);
    
    /**
     * Get generator name.
     * 
     * @return Generator name
     */
    String getName();
    
    /**
     * Get supported formats.
     * 
     * @return Array of supported formats
     */
    ReportFormat[] getSupportedFormats();
}
