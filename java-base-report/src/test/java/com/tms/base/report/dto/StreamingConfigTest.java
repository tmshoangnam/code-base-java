package com.tms.base.report.dto;

import com.tms.base.report.enums.StreamingMode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;
import java.util.HashMap;

/**
 * Unit tests for StreamingConfig DTO.
 * 
 * @since 1.0.0
 */
class StreamingConfigTest {
    
    @Test
    void shouldBuildConfigWithAllFields() {
        Map<String, Object> custom = new HashMap<>();
        custom.put("custom1", "value1");
        
        StreamingConfig config = StreamingConfig.builder()
            .mode(StreamingMode.REACTIVE)
            .chunkSize(16384)
            .bufferSize(131072)
            .maxChunks(2000)
            .timeoutMs(600000L)
            .enableCompression(false)
            .compressionLevel("HIGH")
            .custom(custom)
            .build();
        
        assertEquals(StreamingMode.REACTIVE, config.getMode());
        assertEquals(16384, config.getChunkSize());
        assertEquals(131072, config.getBufferSize());
        assertEquals(2000, config.getMaxChunks());
        assertEquals(600000L, config.getTimeoutMs());
        assertFalse(config.isEnableCompression());
        assertEquals("HIGH", config.getCompressionLevel());
        assertEquals(custom, config.getCustom());
    }
    
    @Test
    void shouldBuildConfigWithMinimalFields() {
        StreamingConfig config = StreamingConfig.builder().build();
        
        assertEquals(StreamingMode.CHUNKED, config.getMode()); // default
        assertEquals(8192, config.getChunkSize()); // default
        assertEquals(65536, config.getBufferSize()); // default
        assertEquals(1000, config.getMaxChunks()); // default
        assertEquals(300000L, config.getTimeoutMs()); // default
        assertTrue(config.isEnableCompression()); // default
        assertEquals("MEDIUM", config.getCompressionLevel()); // default
        assertNotNull(config.getCustom());
        assertTrue(config.getCustom().isEmpty());
    }
    
    @Test
    void shouldHaveCorrectDefaultValues() {
        StreamingConfig config = new StreamingConfig();
        
        assertEquals(StreamingMode.CHUNKED, config.getMode());
        assertEquals(8192, config.getChunkSize());
        assertEquals(65536, config.getBufferSize());
        assertEquals(1000, config.getMaxChunks());
        assertEquals(300000L, config.getTimeoutMs());
        assertTrue(config.isEnableCompression());
        assertEquals("MEDIUM", config.getCompressionLevel());
        assertNotNull(config.getCustom());
    }
    
    @Test
    void shouldAllowCustomConfiguration() {
        StreamingConfig config = StreamingConfig.builder()
            .mode(StreamingMode.PAGINATED)
            .chunkSize(4096)
            .bufferSize(32768)
            .maxChunks(500)
            .timeoutMs(120000L)
            .enableCompression(true)
            .compressionLevel("LOW")
            .build();
        
        assertEquals(StreamingMode.PAGINATED, config.getMode());
        assertEquals(4096, config.getChunkSize());
        assertEquals(32768, config.getBufferSize());
        assertEquals(500, config.getMaxChunks());
        assertEquals(120000L, config.getTimeoutMs());
        assertTrue(config.isEnableCompression());
        assertEquals("LOW", config.getCompressionLevel());
    }
}
