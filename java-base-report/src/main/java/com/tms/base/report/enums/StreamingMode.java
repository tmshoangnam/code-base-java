package com.tms.base.report.enums;

/**
 * Supported streaming modes for report generation.
 * 
 * @since 1.0.0
 */
public enum StreamingMode {
    CHUNKED("chunked", "Chunked transfer encoding"),
    BUFFERED("buffered", "Buffered streaming with configurable buffer size"),
    REACTIVE("reactive", "Reactive streaming with backpressure"),
    PAGINATED("paginated", "Paginated streaming for large datasets"),
    CONTINUOUS("continuous", "Continuous streaming for real-time data");
    
    private final String value;
    private final String description;
    
    StreamingMode(String value, String description) {
        this.value = value;
        this.description = description;
    }
    
    public String getValue() {
        return value;
    }
    
    public String getDescription() {
        return description;
    }
}
