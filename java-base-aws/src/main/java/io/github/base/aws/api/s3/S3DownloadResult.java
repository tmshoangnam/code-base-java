package io.github.base.aws.api.s3;

import java.time.Instant;
import java.util.Objects;

/**
 * Immutable record representing the result of an S3 download operation.
 * 
 * <p>This record contains information about the success or failure of an S3 download
 * operation, including the downloaded content, metadata, and any error details.
 * 
 * @param success true if download was successful, false otherwise
 * @param content the downloaded content (type depends on download method)
 * @param bucketName the name of the S3 bucket
 * @param key the S3 object key
 * @param etag the ETag of the downloaded object
 * @param versionId the version ID of the downloaded object (if versioning is enabled)
 * @param metadata S3 metadata of the downloaded object
 * @param errorMessage error message if download failed, null if successful
 * @param errorCode error code if download failed, null if successful
 * @param downloadedAt timestamp when the download completed
 * 
 * @param <T> the type of content (File, byte[], InputStream, etc.)
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public record S3DownloadResult<T>(
    boolean success,
    T content,
    String bucketName,
    String key,
    String etag,
    String versionId,
    S3Metadata metadata,
    String errorMessage,
    String errorCode,
    Instant downloadedAt
) {
    
    /**
     * Creates a successful S3 download result.
     * 
     * @param content the downloaded content
     * @param bucketName the name of the S3 bucket
     * @param key the S3 object key
     * @param etag the ETag of the downloaded object
     * @param metadata S3 metadata of the downloaded object
     * @return successful S3DownloadResult
     * @throws IllegalArgumentException if content is null, or bucketName or key is null or empty
     */
    public static <T> S3DownloadResult<T> success(T content, String bucketName, String key, String etag, S3Metadata metadata) {
        if (content == null) {
            throw new IllegalArgumentException("Content cannot be null");
        }
        if (bucketName == null || bucketName.trim().isEmpty()) {
            throw new IllegalArgumentException("Bucket name cannot be null or empty");
        }
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
        
        return new S3DownloadResult<>(
            true,
            content,
            bucketName.trim(),
            key.trim(),
            etag,
            null,
            metadata,
            null,
            null,
            Instant.now()
        );
    }
    
    /**
     * Creates a successful S3 download result with version ID.
     * 
     * @param content the downloaded content
     * @param bucketName the name of the S3 bucket
     * @param key the S3 object key
     * @param etag the ETag of the downloaded object
     * @param versionId the version ID of the downloaded object
     * @param metadata S3 metadata of the downloaded object
     * @return successful S3DownloadResult
     * @throws IllegalArgumentException if content is null, or bucketName or key is null or empty
     */
    public static <T> S3DownloadResult<T> success(T content, String bucketName, String key, String etag, String versionId, S3Metadata metadata) {
        if (content == null) {
            throw new IllegalArgumentException("Content cannot be null");
        }
        if (bucketName == null || bucketName.trim().isEmpty()) {
            throw new IllegalArgumentException("Bucket name cannot be null or empty");
        }
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
        
        return new S3DownloadResult<>(
            true,
            content,
            bucketName.trim(),
            key.trim(),
            etag,
            versionId,
            metadata,
            null,
            null,
            Instant.now()
        );
    }
    
    /**
     * Creates a successful S3 download result with current timestamp.
     * 
     * @param content the downloaded content
     * @param bucketName the name of the S3 bucket
     * @param key the S3 object key
     * @param etag the ETag of the downloaded object
     * @return successful S3DownloadResult
     * @throws IllegalArgumentException if content is null, or bucketName or key is null or empty
     */
    public static <T> S3DownloadResult<T> success(T content, String bucketName, String key, String etag) {
        return success(content, bucketName, key, etag, null);
    }
    
    /**
     * Creates a failed S3 download result.
     * 
     * @param bucketName the name of the S3 bucket
     * @param key the S3 object key
     * @param errorMessage error message describing the failure
     * @param errorCode error code for the failure
     * @return failed S3DownloadResult
     * @throws IllegalArgumentException if bucketName or key is null or empty, or errorMessage is null or empty
     */
    public static <T> S3DownloadResult<T> failure(String bucketName, String key, String errorMessage, String errorCode) {
        if (bucketName == null || bucketName.trim().isEmpty()) {
            throw new IllegalArgumentException("Bucket name cannot be null or empty");
        }
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
        if (errorMessage == null || errorMessage.trim().isEmpty()) {
            throw new IllegalArgumentException("Error message cannot be null or empty");
        }
        
        return new S3DownloadResult<>(
            false,
            null,
            bucketName.trim(),
            key.trim(),
            null,
            null,
            null,
            errorMessage.trim(),
            errorCode,
            Instant.now()
        );
    }
    
    /**
     * Creates a failed S3 download result with error code.
     * 
     * @param bucketName the name of the S3 bucket
     * @param key the S3 object key
     * @param errorMessage error message describing the failure
     * @return failed S3DownloadResult
     * @throws IllegalArgumentException if bucketName or key is null or empty, or errorMessage is null or empty
     */
    public static <T> S3DownloadResult<T> failure(String bucketName, String key, String errorMessage) {
        return failure(bucketName, key, errorMessage, null);
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
     * Gets the bucket name.
     * 
     * @return bucket name
     */
    public String getBucketName() {
        return bucketName;
    }
    
    /**
     * Gets the object key.
     * 
     * @return object key
     */
    public String getKey() {
        return key;
    }
    
    /**
     * Gets the ETag.
     * 
     * @return ETag or null if download failed
     */
    public String getEtag() {
        return etag;
    }
    
    /**
     * Gets the version ID.
     * 
     * @return version ID or null if not available
     */
    public String getVersionId() {
        return versionId;
    }
    
    /**
     * Gets the S3 metadata.
     * 
     * @return S3 metadata or null if download failed
     */
    public S3Metadata getMetadata() {
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
    
    /**
     * Checks if versioning is enabled for this download.
     * 
     * @return true if version ID is present, false otherwise
     */
    public boolean hasVersioning() {
        return versionId != null && !versionId.trim().isEmpty();
    }
    
    /**
     * Gets the S3 URI for this object.
     * 
     * @return the S3 URI (s3://bucket/key)
     */
    public String getS3Uri() {
        return "s3://" + bucketName + "/" + key;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        S3DownloadResult<?> that = (S3DownloadResult<?>) obj;
        return success == that.success &&
               Objects.equals(content, that.content) &&
               Objects.equals(bucketName, that.bucketName) &&
               Objects.equals(key, that.key) &&
               Objects.equals(etag, that.etag) &&
               Objects.equals(versionId, that.versionId) &&
               Objects.equals(metadata, that.metadata) &&
               Objects.equals(errorMessage, that.errorMessage) &&
               Objects.equals(errorCode, that.errorCode) &&
               Objects.equals(downloadedAt, that.downloadedAt);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(success, content, bucketName, key, etag, versionId, metadata, errorMessage, errorCode, downloadedAt);
    }
    
    @Override
    public String toString() {
        return "S3DownloadResult{" +
               "success=" + success +
               ", content=" + content +
               ", bucketName='" + bucketName + '\'' +
               ", key='" + key + '\'' +
               ", etag='" + etag + '\'' +
               ", versionId='" + versionId + '\'' +
               ", metadata=" + metadata +
               ", errorMessage='" + errorMessage + '\'' +
               ", errorCode='" + errorCode + '\'' +
               ", downloadedAt=" + downloadedAt +
               '}';
    }
}
