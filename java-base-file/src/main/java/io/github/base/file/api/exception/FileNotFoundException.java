package io.github.base.file.api.exception;

/**
 * Exception thrown when a requested file is not found in storage.
 * 
 * <p>This exception is thrown when attempting to download, delete, or access
 * metadata for a file that does not exist in the storage system.
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public class FileNotFoundException extends FileStorageException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Constructs a new FileNotFoundException with the specified file ID.
     * 
     * @param fileId the ID of the file that was not found
     */
    public FileNotFoundException(String fileId) {
        super("File not found: " + fileId, "FILE_NOT_FOUND", fileId);
    }
    
    /**
     * Constructs a new FileNotFoundException with the specified file ID and detail message.
     * 
     * @param fileId the ID of the file that was not found
     * @param message the detail message
     */
    public FileNotFoundException(String fileId, String message) {
        super(message, "FILE_NOT_FOUND", fileId);
    }
    
    /**
     * Constructs a new FileNotFoundException with the specified file ID, detail message, and cause.
     * 
     * @param fileId the ID of the file that was not found
     * @param message the detail message
     * @param cause the cause
     */
    public FileNotFoundException(String fileId, String message, Throwable cause) {
        super(message, cause, "FILE_NOT_FOUND", fileId);
    }
}
