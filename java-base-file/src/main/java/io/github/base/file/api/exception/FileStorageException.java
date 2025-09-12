package io.github.base.file.api.exception;

/**
 * Base exception for all file storage related errors.
 * 
 * <p>This exception serves as the root of the file storage exception hierarchy
 * and provides common functionality for all file storage exceptions.
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public class FileStorageException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    private final String errorCode;
    private final String fileId;
    
    /**
     * Constructs a new FileStorageException with the specified detail message.
     * 
     * @param message the detail message
     */
    public FileStorageException(String message) {
        super(message);
        this.errorCode = null;
        this.fileId = null;
    }
    
    /**
     * Constructs a new FileStorageException with the specified detail message and cause.
     * 
     * @param message the detail message
     * @param cause the cause
     */
    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = null;
        this.fileId = null;
    }
    
    /**
     * Constructs a new FileStorageException with the specified detail message, error code, and file ID.
     * 
     * @param message the detail message
     * @param errorCode the error code
     * @param fileId the file ID related to the error
     */
    public FileStorageException(String message, String errorCode, String fileId) {
        super(message);
        this.errorCode = errorCode;
        this.fileId = fileId;
    }
    
    /**
     * Constructs a new FileStorageException with the specified detail message, cause, error code, and file ID.
     * 
     * @param message the detail message
     * @param cause the cause
     * @param errorCode the error code
     * @param fileId the file ID related to the error
     */
    public FileStorageException(String message, Throwable cause, String errorCode, String fileId) {
        super(message, cause);
        this.errorCode = errorCode;
        this.fileId = fileId;
    }
    
    /**
     * Gets the error code.
     * 
     * @return the error code or null if not specified
     */
    public String getErrorCode() {
        return errorCode;
    }
    
    /**
     * Gets the file ID related to the error.
     * 
     * @return the file ID or null if not specified
     */
    public String getFileId() {
        return fileId;
    }
    
    /**
     * Checks if this exception has an error code.
     * 
     * @return true if error code is present, false otherwise
     */
    public boolean hasErrorCode() {
        return errorCode != null && !errorCode.trim().isEmpty();
    }
    
    /**
     * Checks if this exception has a file ID.
     * 
     * @return true if file ID is present, false otherwise
     */
    public boolean hasFileId() {
        return fileId != null && !fileId.trim().isEmpty();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(": ").append(getMessage());
        
        if (hasErrorCode()) {
            sb.append(" [errorCode=").append(errorCode).append("]");
        }
        
        if (hasFileId()) {
            sb.append(" [fileId=").append(fileId).append("]");
        }
        
        if (getCause() != null) {
            sb.append(" [caused by: ").append(getCause().getClass().getSimpleName()).append("]");
        }
        
        return sb.toString();
    }
}
