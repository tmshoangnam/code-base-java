package io.github.base.file.api.model;

import java.time.Instant;
import java.util.Objects;

/**
 * Immutable record representing the result of a file download operation.
 * 
 * <p>This record contains information about the success or failure of a download
 * operation, including the file content, metadata, and any error details.
 * 
 * @param success true if download was successful, false otherwise
 * @param content the downloaded file content (type depends on download method)
 * @param metadata file metadata of the downloaded file
 * @param errorMessage error message if download failed, null if successful
 * @param errorCode error code if download failed, null if successful
 * @param downloadedAt timestamp when the download completed
 * 
 * @param <T> the type of content (File, byte[], InputStream, etc.)
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public record DownloadResult<T>(
    boolean success,
    T content,
    FileMetadata metadata,
    String errorMessage,
    String errorCode,
    Instant downloadedAt
) {
    
    /**
     * Creates a successful download result.
     * 
     * @param content the downloaded file content
     * @param metadata file metadata of the downloaded file
     * @return successful DownloadResult
     * @throws IllegalArgumentException if content is null
     */
    public static <T> DownloadResult<T> success(T content, FileMetadata metadata) {
        if (content == null) {
            throw new IllegalArgumentException("Content cannot be null");
        }
        
        return new DownloadResult<>(
            true,
            content,
            metadata,
            null,
            null,
            Instant.now()
        );
    }
    
    /**
     * Creates a successful download result with current timestamp.
     * 
     * @param content the downloaded file content
     * @return successful DownloadResult
     * @throws IllegalArgumentException if content is null
     */
    public static <T> DownloadResult<T> success(T content) {
        return success(content, null);
    }
    
    /**
     * Creates a failed download result.
     * 
     * @param errorMessage error message describing the failure
     * @param errorCode error code for the failure
     * @return failed DownloadResult
     * @throws IllegalArgumentException if errorMessage is null or empty
     */
    public static <T> DownloadResult<T> failure(String errorMessage, String errorCode) {
        if (errorMessage == null || errorMessage.trim().isEmpty()) {
            throw new IllegalArgumentException("Error message cannot be null or empty");
        }
        
        return new DownloadResult<>(
            false,
            null,
            null,
            errorMessage.trim(),
            errorCode,
            Instant.now()
        );
    }
    
    /**
     * Creates a failed download result with error code.
     * 
     * @param errorMessage error message describing the failure
     * @return failed DownloadResult
     * @throws IllegalArgumentException if errorMessage is null or empty
     */
    public static <T> DownloadResult<T> failure(String errorMessage) {
        return failure(errorMessage, null);
    }
    
    /**
     * Checks if the download was successful.
     * 
     * @return true if download was successful, false otherwise
     */
    public boolean isSuccess() {
        return success;
    }
    
    /**
     * Checks if the download failed.
     * 
     * @return true if download failed, false otherwise
     */
    public boolean isFailure() {
        return !success;
    }
    
    /**
     * Gets the downloaded content.
     * 
     * @return content or null if download failed
     */
    public T getContent() {
        return content;
    }
    
    /**
     * Gets the file metadata.
     * 
     * @return file metadata or null if download failed
     */
    public FileMetadata getMetadata() {
        return metadata;
    }
    
    /**
     * Gets the error message.
     * 
     * @return error message or null if download was successful
     */
    public String getErrorMessage() {
        return errorMessage;
    }
    
    /**
     * Gets the error code.
     * 
     * @return error code or null if download was successful
     */
    public String getErrorCode() {
        return errorCode;
    }
    
    /**
     * Gets the download timestamp.
     * 
     * @return timestamp when download completed
     */
    public Instant getDownloadedAt() {
        return downloadedAt;
    }
    
    /**
     * Checks if the content is available.
     * 
     * @return true if content is not null, false otherwise
     */
    public boolean hasContent() {
        return content != null;
    }
    
    /**
     * Checks if metadata is available.
     * 
     * @return true if metadata is not null, false otherwise
     */
    public boolean hasMetadata() {
        return metadata != null;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DownloadResult<?> that = (DownloadResult<?>) obj;
        return success == that.success &&
               Objects.equals(content, that.content) &&
               Objects.equals(metadata, that.metadata) &&
               Objects.equals(errorMessage, that.errorMessage) &&
               Objects.equals(errorCode, that.errorCode) &&
               Objects.equals(downloadedAt, that.downloadedAt);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(success, content, metadata, errorMessage, errorCode, downloadedAt);
    }
    
    @Override
    public String toString() {
        return "DownloadResult{" +
               "success=" + success +
               ", content=" + content +
               ", metadata=" + metadata +
               ", errorMessage='" + errorMessage + '\'' +
               ", errorCode='" + errorCode + '\'' +
               ", downloadedAt=" + downloadedAt +
               '}';
    }
}
