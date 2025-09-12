package io.github.base.file.api.model;

import java.time.Instant;
import java.util.Objects;

/**
 * Immutable record representing the result of a file upload operation.
 * 
 * <p>This record contains information about the success or failure of an upload
 * operation, including the file ID, location, and any error details.
 * 
 * @param success true if upload was successful, false otherwise
 * @param fileId unique identifier for the uploaded file
 * @param location storage location or URL of the uploaded file
 * @param errorMessage error message if upload failed, null if successful
 * @param errorCode error code if upload failed, null if successful
 * @param uploadedAt timestamp when the upload completed
 * @param metadata file metadata of the uploaded file
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public record UploadResult(
    boolean success,
    String fileId,
    String location,
    String errorMessage,
    String errorCode,
    Instant uploadedAt,
    FileMetadata metadata
) {
    
    /**
     * Creates a successful upload result.
     * 
     * @param fileId unique identifier for the uploaded file
     * @param location storage location or URL of the uploaded file
     * @param metadata file metadata of the uploaded file
     * @return successful UploadResult
     * @throws IllegalArgumentException if fileId is null or empty, or location is null or empty
     */
    public static UploadResult success(String fileId, String location, FileMetadata metadata) {
        if (fileId == null || fileId.trim().isEmpty()) {
            throw new IllegalArgumentException("File ID cannot be null or empty");
        }
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Location cannot be null or empty");
        }
        
        return new UploadResult(
            true,
            fileId.trim(),
            location.trim(),
            null,
            null,
            Instant.now(),
            metadata
        );
    }
    
    /**
     * Creates a successful upload result with current timestamp.
     * 
     * @param fileId unique identifier for the uploaded file
     * @param location storage location or URL of the uploaded file
     * @return successful UploadResult
     * @throws IllegalArgumentException if fileId is null or empty, or location is null or empty
     */
    public static UploadResult success(String fileId, String location) {
        return success(fileId, location, null);
    }
    
    /**
     * Creates a failed upload result.
     * 
     * @param fileId unique identifier for the file that failed to upload
     * @param errorMessage error message describing the failure
     * @param errorCode error code for the failure
     * @return failed UploadResult
     * @throws IllegalArgumentException if fileId is null or empty, or errorMessage is null or empty
     */
    public static UploadResult failure(String fileId, String errorMessage, String errorCode) {
        if (fileId == null || fileId.trim().isEmpty()) {
            throw new IllegalArgumentException("File ID cannot be null or empty");
        }
        if (errorMessage == null || errorMessage.trim().isEmpty()) {
            throw new IllegalArgumentException("Error message cannot be null or empty");
        }
        
        return new UploadResult(
            false,
            fileId.trim(),
            null,
            errorMessage.trim(),
            errorCode,
            Instant.now(),
            null
        );
    }
    
    /**
     * Creates a failed upload result with error code.
     * 
     * @param fileId unique identifier for the file that failed to upload
     * @param errorMessage error message describing the failure
     * @return failed UploadResult
     * @throws IllegalArgumentException if fileId is null or empty, or errorMessage is null or empty
     */
    public static UploadResult failure(String fileId, String errorMessage) {
        return failure(fileId, errorMessage, null);
    }
    
    /**
     * Checks if the upload was successful.
     * 
     * @return true if upload was successful, false otherwise
     */
    public boolean isSuccess() {
        return success;
    }
    
    /**
     * Checks if the upload failed.
     * 
     * @return true if upload failed, false otherwise
     */
    public boolean isFailure() {
        return !success;
    }
    
    /**
     * Gets the file ID.
     * 
     * @return file ID
     */
    public String getFileId() {
        return fileId;
    }
    
    /**
     * Gets the storage location or URL.
     * 
     * @return location or null if upload failed
     */
    public String getLocation() {
        return location;
    }
    
    /**
     * Gets the error message.
     * 
     * @return error message or null if upload was successful
     */
    public String getErrorMessage() {
        return errorMessage;
    }
    
    /**
     * Gets the error code.
     * 
     * @return error code or null if upload was successful
     */
    public String getErrorCode() {
        return errorCode;
    }
    
    /**
     * Gets the upload timestamp.
     * 
     * @return timestamp when upload completed
     */
    public Instant getUploadedAt() {
        return uploadedAt;
    }
    
    /**
     * Gets the file metadata.
     * 
     * @return file metadata or null if upload failed
     */
    public FileMetadata getMetadata() {
        return metadata;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UploadResult that = (UploadResult) obj;
        return success == that.success &&
               Objects.equals(fileId, that.fileId) &&
               Objects.equals(location, that.location) &&
               Objects.equals(errorMessage, that.errorMessage) &&
               Objects.equals(errorCode, that.errorCode) &&
               Objects.equals(uploadedAt, that.uploadedAt) &&
               Objects.equals(metadata, that.metadata);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(success, fileId, location, errorMessage, errorCode, uploadedAt, metadata);
    }
    
    @Override
    public String toString() {
        return "UploadResult{" +
               "success=" + success +
               ", fileId='" + fileId + '\'' +
               ", location='" + location + '\'' +
               ", errorMessage='" + errorMessage + '\'' +
               ", errorCode='" + errorCode + '\'' +
               ", uploadedAt=" + uploadedAt +
               ", metadata=" + metadata +
               '}';
    }
}
