package com.tms.base.report.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for StreamingMode enum.
 * 
 * @since 1.0.0
 */
class StreamingModeTest {
    
    @Test
    void shouldHaveCorrectValues() {
        assertEquals("chunked", StreamingMode.CHUNKED.getValue());
        assertEquals("buffered", StreamingMode.BUFFERED.getValue());
        assertEquals("reactive", StreamingMode.REACTIVE.getValue());
        assertEquals("paginated", StreamingMode.PAGINATED.getValue());
        assertEquals("continuous", StreamingMode.CONTINUOUS.getValue());
    }
    
    @Test
    void shouldHaveCorrectDescriptions() {
        assertEquals("Chunked transfer encoding", StreamingMode.CHUNKED.getDescription());
        assertEquals("Buffered streaming with configurable buffer size", StreamingMode.BUFFERED.getDescription());
        assertEquals("Reactive streaming with backpressure", StreamingMode.REACTIVE.getDescription());
        assertEquals("Paginated streaming for large datasets", StreamingMode.PAGINATED.getDescription());
        assertEquals("Continuous streaming for real-time data", StreamingMode.CONTINUOUS.getDescription());
    }
    
    @Test
    void shouldHaveAllStreamingModes() {
        StreamingMode[] modes = StreamingMode.values();
        assertEquals(5, modes.length);
        
        assertTrue(java.util.Arrays.asList(modes).contains(StreamingMode.CHUNKED));
        assertTrue(java.util.Arrays.asList(modes).contains(StreamingMode.BUFFERED));
        assertTrue(java.util.Arrays.asList(modes).contains(StreamingMode.REACTIVE));
        assertTrue(java.util.Arrays.asList(modes).contains(StreamingMode.PAGINATED));
        assertTrue(java.util.Arrays.asList(modes).contains(StreamingMode.CONTINUOUS));
    }
}
