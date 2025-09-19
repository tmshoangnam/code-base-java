package com.tms.base.report.dto;

import com.tms.base.report.enums.ReportFormat;
import com.tms.base.report.enums.ReportStatus;
import java.util.Map;
import java.util.HashMap;

/**
 * Response DTO for report generation.
 * 
 * @since 1.0.0
 */
public class ReportResponse {
    
    private ReportFormat format;
    private String fileName;
    private String mimeType;
    private byte[] data;
    private ReportStatus status;
    private String message;
    private long size;
    private long generationTimeMs;
    private Map<String, Object> metadata;
    private String requestId;
    
    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private ReportResponse response = new ReportResponse();
        
        public Builder format(ReportFormat format) {
            response.format = format;
            return this;
        }
        
        public Builder fileName(String fileName) {
            response.fileName = fileName;
            return this;
        }
        
        public Builder mimeType(String mimeType) {
            response.mimeType = mimeType;
            return this;
        }
        
        public Builder data(byte[] data) {
            response.data = data;
            return this;
        }
        
        public Builder status(ReportStatus status) {
            response.status = status;
            return this;
        }
        
        public Builder message(String message) {
            response.message = message;
            return this;
        }
        
        public Builder size(long size) {
            response.size = size;
            return this;
        }
        
        public Builder generationTimeMs(long generationTimeMs) {
            response.generationTimeMs = generationTimeMs;
            return this;
        }
        
        public Builder metadata(Map<String, Object> metadata) {
            response.metadata = metadata;
            return this;
        }
        
        public Builder requestId(String requestId) {
            response.requestId = requestId;
            return this;
        }
        
        public ReportResponse build() {
            if (response.metadata == null) {
                response.metadata = new HashMap<>();
            }
            if (response.status == null) {
                response.status = ReportStatus.SUCCESS;
            }
            if (response.data != null) {
                response.size = response.data.length;
            }
            return response;
        }
    }
    
    // Getters and setters
    public ReportFormat getFormat() {
        return format;
    }
    
    public void setFormat(ReportFormat format) {
        this.format = format;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public String getMimeType() {
        return mimeType;
    }
    
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
    
    public byte[] getData() {
        return data;
    }
    
    public void setData(byte[] data) {
        this.data = data;
    }
    
    public ReportStatus getStatus() {
        return status;
    }
    
    public void setStatus(ReportStatus status) {
        this.status = status;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public long getSize() {
        return size;
    }
    
    public void setSize(long size) {
        this.size = size;
    }
    
    public long getGenerationTimeMs() {
        return generationTimeMs;
    }
    
    public void setGenerationTimeMs(long generationTimeMs) {
        this.generationTimeMs = generationTimeMs;
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
}
