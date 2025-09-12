package io.github.base.file.api.model;

/**
 * Enumeration of supported file types for storage operations.
 * 
 * <p>This enum defines the different ways files can be represented and handled
 * in the file storage system.
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public enum FileType {
    
    /**
     * Represents files as Java File objects.
     * Used for local file system operations and file-based storage.
     */
    FILE,
    
    /**
     * Represents files as byte arrays.
     * Used for in-memory operations and small file handling.
     */
    BYTE,
    
    /**
     * Represents files as input/output streams.
     * Used for streaming large files and network operations.
     */
    STREAM
}
