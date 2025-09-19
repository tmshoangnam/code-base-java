package com.tms.base.report.api;

/**
 * Exception thrown when report generation times out.
 * 
 * @since 1.0.0
 */
public class ReportTimeoutException extends ReportException {
    
    private final long timeoutMs;
    
    public ReportTimeoutException(String message, long timeoutMs) {
        super("TIMEOUT_ERROR", message, null);
        this.timeoutMs = timeoutMs;
    }
    
    public ReportTimeoutException(String message, long timeoutMs, Throwable cause) {
        super("TIMEOUT_ERROR", message, null, cause);
        this.timeoutMs = timeoutMs;
    }
    
    public ReportTimeoutException(String message, String reportId, long timeoutMs) {
        super("TIMEOUT_ERROR", message, reportId);
        this.timeoutMs = timeoutMs;
    }
    
    public ReportTimeoutException(String message, String reportId, long timeoutMs, Throwable cause) {
        super("TIMEOUT_ERROR", message, reportId, cause);
        this.timeoutMs = timeoutMs;
    }
    
    public long getTimeoutMs() {
        return timeoutMs;
    }
}
