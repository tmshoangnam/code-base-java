package io.github.base.file.api.exception;

/**
 * Exception thrown when a file upload operation fails.
 * 
 * <p>This exception is thrown when there are issues during the upload process,
 * such as network failures, storage quota exceeded, or invalid file data.
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public class FileUploadException extends FileStorageException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Constructs a new FileUploadException with the specified file ID.
     * 
     * @param fileId the ID of the file that failed to upload
     */
    public FileUploadException(String fileId) {
        super("File upload failed: " + fileId, "UPLOAD_FAILED", fileId);
    }
    
    /**
     * Constructs a new FileUploadException with the specified file ID and detail message.
     * 
     * @param fileId the ID of the file that failed to upload
     * @param message the detail message
     */
    public FileUploadException(String fileId, String message) {
        super(message, "UPLOAD_FAILED", fileId);
    }
    
    /**
     * Constructs a new FileUploadException with the specified file ID, detail message, and cause.
     * 
     * @param fileId the ID of the file that failed to upload
     * @param message the detail message
     * @param cause the cause
     */
    public FileUploadException(String fileId, String message, Throwable cause) {
        super(message, cause, "UPLOAD_FAILED", fileId);
    }
    
    /**
     * Constructs a new FileUploadException with the specified file ID and cause.
     * 
     * @param fileId the ID of the file that failed to upload
     * @param cause the cause
     */
    public FileUploadException(String fileId, Throwable cause) {
        super("File upload failed: " + fileId, cause, "UPLOAD_FAILED", fileId);
    }
}
