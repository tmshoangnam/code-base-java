package com.tms.base.report.dto;

import com.tms.base.report.enums.StreamingMode;
import java.util.Map;
import java.util.HashMap;

/**
 * Configuration for streaming report generation.
 * 
 * @since 1.0.0
 */
public class StreamingConfig {
    
    private StreamingMode mode = StreamingMode.CHUNKED;
    private int chunkSize = 8192; // 8KB default
    private int bufferSize = 65536; // 64KB default
    private int maxChunks = 1000;
    private long timeoutMs = 300000; // 5 minutes
    private boolean enableCompression = true;
    private String compressionLevel = "MEDIUM";
    private Map<String, Object> custom = new HashMap<>();
    
    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private StreamingConfig config = new StreamingConfig();
        
        public Builder mode(StreamingMode mode) {
            config.mode = mode;
            return this;
        }
        
        public Builder chunkSize(int chunkSize) {
            config.chunkSize = chunkSize;
            return this;
        }
        
        public Builder bufferSize(int bufferSize) {
            config.bufferSize = bufferSize;
            return this;
        }
        
        public Builder maxChunks(int maxChunks) {
            config.maxChunks = maxChunks;
            return this;
        }
        
        public Builder timeoutMs(long timeoutMs) {
            config.timeoutMs = timeoutMs;
            return this;
        }
        
        public Builder enableCompression(boolean enableCompression) {
            config.enableCompression = enableCompression;
            return this;
        }
        
        public Builder compressionLevel(String compressionLevel) {
            config.compressionLevel = compressionLevel;
            return this;
        }
        
        public Builder custom(Map<String, Object> custom) {
            config.custom = custom;
            return this;
        }
        
        public StreamingConfig build() {
            return config;
        }
    }
    
    // Getters and setters
    public StreamingMode getMode() {
        return mode;
    }
    
    public void setMode(StreamingMode mode) {
        this.mode = mode;
    }
    
    public int getChunkSize() {
        return chunkSize;
    }
    
    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }
    
    public int getBufferSize() {
        return bufferSize;
    }
    
    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }
    
    public int getMaxChunks() {
        return maxChunks;
    }
    
    public void setMaxChunks(int maxChunks) {
        this.maxChunks = maxChunks;
    }
    
    public long getTimeoutMs() {
        return timeoutMs;
    }
    
    public void setTimeoutMs(long timeoutMs) {
        this.timeoutMs = timeoutMs;
    }
    
    public boolean isEnableCompression() {
        return enableCompression;
    }
    
    public void setEnableCompression(boolean enableCompression) {
        this.enableCompression = enableCompression;
    }
    
    public String getCompressionLevel() {
        return compressionLevel;
    }
    
    public void setCompressionLevel(String compressionLevel) {
        this.compressionLevel = compressionLevel;
    }
    
    public Map<String, Object> getCustom() {
        return custom;
    }
    
    public void setCustom(Map<String, Object> custom) {
        this.custom = custom;
    }
}
