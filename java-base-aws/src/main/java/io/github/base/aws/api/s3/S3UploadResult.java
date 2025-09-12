package io.github.base.aws.api.s3;

import java.time.Instant;
import java.util.Objects;

/**
 * Immutable record representing the result of an S3 upload operation.
 * 
 * <p>This record contains information about the success or failure of an S3 upload
 * operation, including the bucket, key, ETag, and any error details.
 * 
 * @param success true if upload was successful, false otherwise
 * @param bucketName the name of the S3 bucket
 * @param key the S3 object key
 * @param etag the ETag of the uploaded object
 * @param versionId the version ID of the uploaded object (if versioning is enabled)
 * @param location the S3 URI of the uploaded object
 * @param errorMessage error message if upload failed, null if successful
 * @param errorCode error code if upload failed, null if successful
 * @param uploadedAt timestamp when the upload completed
 * @param metadata S3 metadata of the uploaded object
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public record S3UploadResult(
    boolean success,
    String bucketName,
    String key,
    String etag,
    String versionId,
    String location,
    String errorMessage,
    String errorCode,
    Instant uploadedAt,
    S3Metadata metadata
) {
    
    /**
     * Creates a successful S3 upload result.
     * 
     * @param bucketName the name of the S3 bucket
     * @param key the S3 object key
     * @param etag the ETag of the uploaded object
     * @param metadata S3 metadata of the uploaded object
     * @return successful S3UploadResult
     * @throws IllegalArgumentException if bucketName or key is null or empty
     */
    public static S3UploadResult success(String bucketName, String key, String etag, S3Metadata metadata) {
        if (bucketName == null || bucketName.trim().isEmpty()) {
            throw new IllegalArgumentException("Bucket name cannot be null or empty");
        }
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
        
        String location = "s3://" + bucketName.trim() + "/" + key.trim();
        return new S3UploadResult(
            true,
            bucketName.trim(),
            key.trim(),
            etag,
            null,
            location,
            null,
            null,
            Instant.now(),
            metadata
        );
    }
    
    /**
     * Creates a successful S3 upload result with version ID.
     * 
     * @param bucketName the name of the S3 bucket
     * @param key the S3 object key
     * @param etag the ETag of the uploaded object
     * @param versionId the version ID of the uploaded object
     * @param metadata S3 metadata of the uploaded object
     * @return successful S3UploadResult
     * @throws IllegalArgumentException if bucketName or key is null or empty
     */
    public static S3UploadResult success(String bucketName, String key, String etag, String versionId, S3Metadata metadata) {
        if (bucketName == null || bucketName.trim().isEmpty()) {
            throw new IllegalArgumentException("Bucket name cannot be null or empty");
        }
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
        
        String location = "s3://" + bucketName.trim() + "/" + key.trim();
        return new S3UploadResult(
            true,
            bucketName.trim(),
            key.trim(),
            etag,
            versionId,
            location,
            null,
            null,
            Instant.now(),
            metadata
        );
    }
    
    /**
     * Creates a successful S3 upload result with current timestamp.
     * 
     * @param bucketName the name of the S3 bucket
     * @param key the S3 object key
     * @param etag the ETag of the uploaded object
     * @return successful S3UploadResult
     * @throws IllegalArgumentException if bucketName or key is null or empty
     */
    public static S3UploadResult success(String bucketName, String key, String etag) {
        return success(bucketName, key, etag, null);
    }
    
    /**
     * Creates a failed S3 upload result.
     * 
     * @param bucketName the name of the S3 bucket
     * @param key the S3 object key
     * @param errorMessage error message describing the failure
     * @param errorCode error code for the failure
     * @return failed S3UploadResult
     * @throws IllegalArgumentException if bucketName or key is null or empty, or errorMessage is null or empty
     */
    public static S3UploadResult failure(String bucketName, String key, String errorMessage, String errorCode) {
        if (bucketName == null || bucketName.trim().isEmpty()) {
            throw new IllegalArgumentException("Bucket name cannot be null or empty");
        }
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
        if (errorMessage == null || errorMessage.trim().isEmpty()) {
            throw new IllegalArgumentException("Error message cannot be null or empty");
        }
        
        return new S3UploadResult(
            false,
            bucketName.trim(),
            key.trim(),
            null,
            null,
            null,
            errorMessage.trim(),
            errorCode,
            Instant.now(),
            null
        );
    }
    
    /**
     * Creates a failed S3 upload result with error code.
     * 
     * @param bucketName the name of the S3 bucket
     * @param key the S3 object key
     * @param errorMessage error message describing the failure
     * @return failed S3UploadResult
     * @throws IllegalArgumentException if bucketName or key is null or empty, or errorMessage is null or empty
     */
    public static S3UploadResult failure(String bucketName, String key, String errorMessage) {
        return failure(bucketName, key, errorMessage, null);
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
     * @return ETag or null if upload failed
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
     * Gets the S3 location URI.
     * 
     * @return S3 URI or null if upload failed
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
     * Gets the S3 metadata.
     * 
     * @return S3 metadata or null if upload failed
     */
    public S3Metadata getMetadata() {
        return metadata;
    }
    
    /**
     * Checks if versioning is enabled for this upload.
     * 
     * @return true if version ID is present, false otherwise
     */
    public boolean hasVersioning() {
        return versionId != null && !versionId.trim().isEmpty();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        S3UploadResult that = (S3UploadResult) obj;
        return success == that.success &&
               Objects.equals(bucketName, that.bucketName) &&
               Objects.equals(key, that.key) &&
               Objects.equals(etag, that.etag) &&
               Objects.equals(versionId, that.versionId) &&
               Objects.equals(location, that.location) &&
               Objects.equals(errorMessage, that.errorMessage) &&
               Objects.equals(errorCode, that.errorCode) &&
               Objects.equals(uploadedAt, that.uploadedAt) &&
               Objects.equals(metadata, that.metadata);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(success, bucketName, key, etag, versionId, location, errorMessage, errorCode, uploadedAt, metadata);
    }
    
    @Override
    public String toString() {
        return "S3UploadResult{" +
               "success=" + success +
               ", bucketName='" + bucketName + '\'' +
               ", key='" + key + '\'' +
               ", etag='" + etag + '\'' +
               ", versionId='" + versionId + '\'' +
               ", location='" + location + '\'' +
               ", errorMessage='" + errorMessage + '\'' +
               ", errorCode='" + errorCode + '\'' +
               ", uploadedAt=" + uploadedAt +
               ", metadata=" + metadata +
               '}';
    }
}
