package com.tms.base.report.dto;

import com.tms.base.report.enums.ReportFormat;
import com.tms.base.report.enums.StreamingMode;
import java.io.InputStream;
import java.util.Map;
import java.util.HashMap;
import java.util.function.Consumer;

/**
 * Response DTO for streaming report generation.
 * 
 * @since 1.0.0
 */
public class StreamingReportResponse {
    
    private ReportFormat format;
    private String fileName;
    private String mimeType;
    private StreamingMode streamingMode;
    private InputStream dataStream;
    private long totalSize;
    private int chunkSize;
    private Map<String, Object> metadata;
    private Consumer<byte[]> chunkConsumer;
    private Runnable onComplete;
    private Runnable onError;
    private String requestId;
    
    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private StreamingReportResponse response = new StreamingReportResponse();
        
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
        
        public Builder streamingMode(StreamingMode streamingMode) {
            response.streamingMode = streamingMode;
            return this;
        }
        
        public Builder dataStream(InputStream dataStream) {
            response.dataStream = dataStream;
            return this;
        }
        
        public Builder totalSize(long totalSize) {
            response.totalSize = totalSize;
            return this;
        }
        
        public Builder chunkSize(int chunkSize) {
            response.chunkSize = chunkSize;
            return this;
        }
        
        public Builder metadata(Map<String, Object> metadata) {
            response.metadata = metadata;
            return this;
        }
        
        public Builder chunkConsumer(Consumer<byte[]> chunkConsumer) {
            response.chunkConsumer = chunkConsumer;
            return this;
        }
        
        public Builder onComplete(Runnable onComplete) {
            response.onComplete = onComplete;
            return this;
        }
        
        public Builder onError(Runnable onError) {
            response.onError = onError;
            return this;
        }
        
        public Builder requestId(String requestId) {
            response.requestId = requestId;
            return this;
        }
        
        public StreamingReportResponse build() {
            if (response.metadata == null) {
                response.metadata = new HashMap<>();
            }
            if (response.requestId == null) {
                response.requestId = java.util.UUID.randomUUID().toString();
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
    
    public StreamingMode getStreamingMode() {
        return streamingMode;
    }
    
    public void setStreamingMode(StreamingMode streamingMode) {
        this.streamingMode = streamingMode;
    }
    
    public InputStream getDataStream() {
        return dataStream;
    }
    
    public void setDataStream(InputStream dataStream) {
        this.dataStream = dataStream;
    }
    
    public long getTotalSize() {
        return totalSize;
    }
    
    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }
    
    public int getChunkSize() {
        return chunkSize;
    }
    
    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }
    
    public Map<String, Object> getMetadata() {
        return metadata;
    }
    
    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
    
    public Consumer<byte[]> getChunkConsumer() {
        return chunkConsumer;
    }
    
    public void setChunkConsumer(Consumer<byte[]> chunkConsumer) {
        this.chunkConsumer = chunkConsumer;
    }
    
    public Runnable getOnComplete() {
        return onComplete;
    }
    
    public void setOnComplete(Runnable onComplete) {
        this.onComplete = onComplete;
    }
    
    public Runnable getOnError() {
        return onError;
    }
    
    public void setOnError(Runnable onError) {
        this.onError = onError;
    }
    
    public String getRequestId() {
        return requestId;
    }
    
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
