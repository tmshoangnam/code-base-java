package io.github.base.aws.api.s3;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

/**
 * Immutable record representing S3 object metadata.
 * 
 * <p>This record contains essential information about an S3 object including
 * its key, content type, size, ETag, and other S3-specific metadata properties.
 * 
 * @param bucketName the name of the S3 bucket
 * @param key the S3 object key
 * @param contentType MIME type of the object content
 * @param contentLength the size of the object in bytes
 * @param etag the ETag of the object
 * @param lastModified timestamp when the object was last modified
 * @param storageClass the S3 storage class
 * @param tags map of object tags
 * @param metadata map of custom metadata
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public record S3Metadata(
    String bucketName,
    String key,
    String contentType,
    long contentLength,
    String etag,
    Instant lastModified,
    String storageClass,
    Map<String, String> tags,
    Map<String, String> metadata
) {
    
    /**
     * Creates an S3Metadata with basic information.
     * 
     * @param bucketName the name of the S3 bucket
     * @param key the S3 object key
     * @param contentType MIME type of the object content
     * @param contentLength the size of the object in bytes
     * @return new S3Metadata instance
     * @throws IllegalArgumentException if bucketName or key is null or empty, or contentLength < 0
     */
    public static S3Metadata of(String bucketName, String key, String contentType, long contentLength) {
        if (bucketName == null || bucketName.trim().isEmpty()) {
            throw new IllegalArgumentException("Bucket name cannot be null or empty");
        }
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
        if (contentLength < 0) {
            throw new IllegalArgumentException("Content length cannot be negative");
        }
        
        return new S3Metadata(
            bucketName.trim(),
            key.trim(),
            contentType,
            contentLength,
            null,
            Instant.now(),
            "STANDARD",
            Map.of(),
            Map.of()
        );
    }
    
    /**
     * Creates an S3Metadata with ETag and last modified timestamp.
     * 
     * @param bucketName the name of the S3 bucket
     * @param key the S3 object key
     * @param contentType MIME type of the object content
     * @param contentLength the size of the object in bytes
     * @param etag the ETag of the object
     * @param lastModified timestamp when the object was last modified
     * @return new S3Metadata instance
     * @throws IllegalArgumentException if bucketName or key is null or empty, or contentLength < 0
     */
    public static S3Metadata of(String bucketName, String key, String contentType, long contentLength, 
                               String etag, Instant lastModified) {
        if (bucketName == null || bucketName.trim().isEmpty()) {
            throw new IllegalArgumentException("Bucket name cannot be null or empty");
        }
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
        if (contentLength < 0) {
            throw new IllegalArgumentException("Content length cannot be negative");
        }
        
        return new S3Metadata(
            bucketName.trim(),
            key.trim(),
            contentType,
            contentLength,
            etag,
            lastModified != null ? lastModified : Instant.now(),
            "STANDARD",
            Map.of(),
            Map.of()
        );
    }
    
    /**
     * Creates a copy of this S3Metadata with updated tags.
     * 
     * @param tags new tags map
     * @return new S3Metadata instance with updated tags
     */
    public S3Metadata withTags(Map<String, String> tags) {
        return new S3Metadata(
            this.bucketName,
            this.key,
            this.contentType,
            this.contentLength,
            this.etag,
            this.lastModified,
            this.storageClass,
            tags != null ? Map.copyOf(tags) : Map.of(),
            this.metadata
        );
    }
    
    /**
     * Creates a copy of this S3Metadata with updated metadata.
     * 
     * @param metadata new metadata map
     * @return new S3Metadata instance with updated metadata
     */
    public S3Metadata withMetadata(Map<String, String> metadata) {
        return new S3Metadata(
            this.bucketName,
            this.key,
            this.contentType,
            this.contentLength,
            this.etag,
            this.lastModified,
            this.storageClass,
            this.tags,
            metadata != null ? Map.copyOf(metadata) : Map.of()
        );
    }
    
    /**
     * Creates a copy of this S3Metadata with updated storage class.
     * 
     * @param storageClass new storage class
     * @return new S3Metadata instance with updated storage class
     */
    public S3Metadata withStorageClass(String storageClass) {
        return new S3Metadata(
            this.bucketName,
            this.key,
            this.contentType,
            this.contentLength,
            this.etag,
            this.lastModified,
            storageClass != null ? storageClass : "STANDARD",
            this.tags,
            this.metadata
        );
    }
    
    /**
     * Gets a tag value by key.
     * 
     * @param key tag key
     * @return tag value or null if not found
     */
    public String getTag(String key) {
        return tags != null ? tags.get(key) : null;
    }
    
    /**
     * Checks if a tag exists.
     * 
     * @param key tag key
     * @return true if tag exists, false otherwise
     */
    public boolean hasTag(String key) {
        return tags != null && tags.containsKey(key);
    }
    
    /**
     * Gets a metadata value by key.
     * 
     * @param key metadata key
     * @return metadata value or null if not found
     */
    public String getMetadata(String key) {
        return metadata != null ? metadata.get(key) : null;
    }
    
    /**
     * Checks if a metadata key exists.
     * 
     * @param key metadata key
     * @return true if metadata key exists, false otherwise
     */
    public boolean hasMetadata(String key) {
        return metadata != null && metadata.containsKey(key);
    }
    
    /**
     * Gets the object key without the file extension.
     * 
     * @return the object key without extension
     */
    public String getKeyWithoutExtension() {
        if (key == null) {
            return "";
        }
        
        int lastDotIndex = key.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return key;
        }
        
        return key.substring(0, lastDotIndex);
    }
    
    /**
     * Gets the file extension from the object key.
     * 
     * @return the file extension (without dot) or empty string if no extension
     */
    public String getFileExtension() {
        if (key == null) {
            return "";
        }
        
        int lastDotIndex = key.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == key.length() - 1) {
            return "";
        }
        
        return key.substring(lastDotIndex + 1).toLowerCase();
    }
    
    /**
     * Gets the full S3 URI for this object.
     * 
     * @return the S3 URI (s3://bucket/key)
     */
    public String getS3Uri() {
        return "s3://" + bucketName + "/" + key;
    }
    
    /**
     * Gets the HTTPS URL for this object.
     * 
     * @param region the AWS region
     * @return the HTTPS URL or null if region is null or empty
     */
    public String getHttpsUrl(String region) {
        if (region == null || region.trim().isEmpty()) {
            return null;
        }
        
        return "https://" + bucketName + ".s3." + region.trim() + ".amazonaws.com/" + key;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        S3Metadata that = (S3Metadata) obj;
        return contentLength == that.contentLength &&
               Objects.equals(bucketName, that.bucketName) &&
               Objects.equals(key, that.key) &&
               Objects.equals(contentType, that.contentType) &&
               Objects.equals(etag, that.etag) &&
               Objects.equals(lastModified, that.lastModified) &&
               Objects.equals(storageClass, that.storageClass) &&
               Objects.equals(tags, that.tags) &&
               Objects.equals(metadata, that.metadata);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(bucketName, key, contentType, contentLength, etag, lastModified, storageClass, tags, metadata);
    }
    
    @Override
    public String toString() {
        return "S3Metadata{" +
               "bucketName='" + bucketName + '\'' +
               ", key='" + key + '\'' +
               ", contentType='" + contentType + '\'' +
               ", contentLength=" + contentLength +
               ", etag='" + etag + '\'' +
               ", lastModified=" + lastModified +
               ", storageClass='" + storageClass + '\'' +
               ", tags=" + tags +
               ", metadata=" + metadata +
               '}';
    }
}
