package com.tms.base.report.dto;

import com.tms.base.report.enums.ReportFormat;
import java.util.Map;
import java.util.HashMap;

/**
 * Request DTO for report generation.
 * 
 * @param <T> Type of data to be processed
 * @since 1.0.0
 */
public class ReportRequest<T> {
    
    private ReportFormat format;
    private String templateName;
    private T data;
    private Map<String, Object> parameters;
    private String outputFileName;
    private Map<String, Object> metadata;
    private String requestId;
    private long timeoutMs = 300000; // 5 minutes default
    
    // Builder pattern
    public static <T> Builder<T> builder() {
        return new Builder<>();
    }
    
    public static class Builder<T> {
        private ReportRequest<T> request = new ReportRequest<>();
        
        public Builder<T> format(ReportFormat format) {
            request.format = format;
            return this;
        }
        
        public Builder<T> templateName(String templateName) {
            request.templateName = templateName;
            return this;
        }
        
        public Builder<T> data(T data) {
            request.data = data;
            return this;
        }
        
        public Builder<T> parameters(Map<String, Object> parameters) {
            request.parameters = parameters;
            return this;
        }
        
        public Builder<T> outputFileName(String outputFileName) {
            request.outputFileName = outputFileName;
            return this;
        }
        
        public Builder<T> metadata(Map<String, Object> metadata) {
            request.metadata = metadata;
            return this;
        }
        
        public Builder<T> requestId(String requestId) {
            request.requestId = requestId;
            return this;
        }
        
        public Builder<T> timeoutMs(long timeoutMs) {
            request.timeoutMs = timeoutMs;
            return this;
        }
        
        public ReportRequest<T> build() {
            if (request.parameters == null) {
                request.parameters = new HashMap<>();
            }
            if (request.metadata == null) {
                request.metadata = new HashMap<>();
            }
            if (request.requestId == null) {
                request.requestId = java.util.UUID.randomUUID().toString();
            }
            return request;
        }
    }
    
    // Getters and setters
    public ReportFormat getFormat() {
        return format;
    }
    
    public void setFormat(ReportFormat format) {
        this.format = format;
    }
    
    public String getTemplateName() {
        return templateName;
    }
    
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public Map<String, Object> getParameters() {
        return parameters;
    }
    
    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }
    
    public String getOutputFileName() {
        return outputFileName;
    }
    
    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }
    
    public Map<String, Object> getMetadata() {
        return metadata;
    }
    
    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
    
    public String getRequestId() {
        return requestId;
    }
    
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    
    public long getTimeoutMs() {
        return timeoutMs;
    }
    
    public void setTimeoutMs(long timeoutMs) {
        this.timeoutMs = timeoutMs;
    }
}
