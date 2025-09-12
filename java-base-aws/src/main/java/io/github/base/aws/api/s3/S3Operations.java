package io.github.base.aws.api.s3;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Contract for Amazon S3 operations.
 *
 * <p>This interface defines the standard operations for interacting with Amazon S3,
 * including file upload, download, deletion, and metadata management.
 *
 * <p>All operations are designed to be thread-safe and can be used in concurrent environments.
 *
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public interface S3Operations {

    /**
     * Uploads a file to S3.
     *
     * @param bucketName the name of the S3 bucket
     * @param key the S3 object key
     * @param file the file to upload
     * @param metadata S3 metadata including content type, tags, etc.
     * @return upload result containing success status, ETag, and location
     * @throws IllegalArgumentException if bucketName, key, or file is null or empty
     * @throws io.github.base.aws.api.common.AwsException if upload fails
     */
    S3UploadResult upload(String bucketName, String key, File file, S3Metadata metadata);

    /**
     * Uploads byte array data to S3.
     *
     * @param bucketName the name of the S3 bucket
     * @param key the S3 object key
     * @param data byte array data to upload
     * @param metadata S3 metadata including content type, tags, etc.
     * @return upload result containing success status, ETag, and location
     * @throws IllegalArgumentException if bucketName, key, or data is null or empty
     * @throws io.github.base.aws.api.common.AwsException if upload fails
     */
    S3UploadResult upload(String bucketName, String key, byte[] data, S3Metadata metadata);

    /**
     * Uploads data from input stream to S3.
     *
     * @param bucketName the name of the S3 bucket
     * @param key the S3 object key
     * @param inputStream input stream containing data to upload
     * @param metadata S3 metadata including content type, tags, etc.
     * @return upload result containing success status, ETag, and location
     * @throws IllegalArgumentException if bucketName, key, or inputStream is null or empty
     * @throws io.github.base.aws.api.common.AwsException if upload fails
     */
    S3UploadResult upload(String bucketName, String key, InputStream inputStream, S3Metadata metadata);

    /**
     * Downloads a file from S3.
     *
     * @param bucketName the name of the S3 bucket
     * @param key the S3 object key
     * @return download result containing the file and metadata
     * @throws IllegalArgumentException if bucketName or key is null or empty
     * @throws io.github.base.aws.api.common.AwsException if download fails
     */
    S3DownloadResult<File> download(String bucketName, String key);

    /**
     * Downloads data from S3 as the specified type.
     *
     * @param bucketName the name of the S3 bucket
     * @param key the S3 object key
     * @param type the class type to download as
     * @param <T> the type to download as
     * @return download result containing the file content and metadata
     * @throws IllegalArgumentException if bucketName or key is null or empty
     * @throws io.github.base.aws.api.common.AwsException if download fails
     */
    <T> S3DownloadResult<T> download(String bucketName, String key, Class<T> type);

    /**
     * Deletes an object from S3.
     *
     * @param bucketName the name of the S3 bucket
     * @param key the S3 object key
     * @return true if object was deleted successfully, false if object not found
     * @throws IllegalArgumentException if bucketName or key is null or empty
     * @throws io.github.base.aws.api.common.AwsException if deletion fails
     */
    boolean delete(String bucketName, String key);

    /**
     * Deletes multiple objects from S3.
     *
     * @param bucketName the name of the S3 bucket
     * @param keys list of S3 object keys to delete
     * @return map of keys to deletion success status
     * @throws IllegalArgumentException if bucketName is null or empty, or keys is null
     * @throws io.github.base.aws.api.common.AwsException if deletion fails
     */
    Map<String, Boolean> delete(String bucketName, List<String> keys);

    /**
     * Checks if an object exists in S3.
     *
     * @param bucketName the name of the S3 bucket
     * @param key the S3 object key
     * @return true if object exists, false otherwise
     * @throws IllegalArgumentException if bucketName or key is null or empty
     * @throws io.github.base.aws.api.common.AwsException if check fails
     */
    boolean exists(String bucketName, String key);

    /**
     * Gets object metadata from S3.
     *
     * @param bucketName the name of the S3 bucket
     * @param key the S3 object key
     * @return S3 metadata or null if object not found
     * @throws IllegalArgumentException if bucketName or key is null or empty
     * @throws io.github.base.aws.api.common.AwsException if metadata retrieval fails
     */
    S3Metadata getMetadata(String bucketName, String key);

    /**
     * Lists objects in S3 bucket.
     *
     * @param bucketName the name of the S3 bucket
     * @param prefix optional prefix to filter objects (null for all objects)
     * @param maxResults maximum number of results to return (0 for no limit)
     * @return list of S3 metadata
     * @throws IllegalArgumentException if bucketName is null or empty
     * @throws io.github.base.aws.api.common.AwsException if listing fails
     */
    List<S3Metadata> listObjects(String bucketName, String prefix, int maxResults);

    /**
     * Lists objects in S3 bucket with pagination.
     *
     * @param bucketName the name of the S3 bucket
     * @param prefix optional prefix to filter objects (null for all objects)
     * @param continuationToken token for pagination (null for first page)
     * @param maxResults maximum number of results to return
     * @return map containing object metadata list and next continuation token
     * @throws IllegalArgumentException if bucketName is null or empty
     * @throws io.github.base.aws.api.common.AwsException if listing fails
     */
    Map<String, Object> listObjects(String bucketName, String prefix, String continuationToken, int maxResults);

    /**
     * Generates a presigned URL for object access.
     *
     * @param bucketName the name of the S3 bucket
     * @param key the S3 object key
     * @param expirationMinutes URL expiration time in minutes
     * @return presigned URL or null if not supported
     * @throws IllegalArgumentException if bucketName or key is null or empty, or expirationMinutes <= 0
     * @throws io.github.base.aws.api.common.AwsException if URL generation fails
     */
    String generatePresignedUrl(String bucketName, String key, int expirationMinutes);

    /**
     * Generates a presigned URL for object upload.
     *
     * @param bucketName the name of the S3 bucket
     * @param key the S3 object key
     * @param expirationMinutes URL expiration time in minutes
     * @return presigned URL for upload or null if not supported
     * @throws IllegalArgumentException if bucketName or key is null or empty, or expirationMinutes <= 0
     * @throws io.github.base.aws.api.common.AwsException if URL generation fails
     */
    String generatePresignedUploadUrl(String bucketName, String key, int expirationMinutes);

    /**
     * Copies an object within S3.
     *
     * @param sourceBucket the source bucket name
     * @param sourceKey the source object key
     * @param destinationBucket the destination bucket name
     * @param destinationKey the destination object key
     * @return true if copy was successful, false otherwise
     * @throws IllegalArgumentException if any parameter is null or empty
     * @throws io.github.base.aws.api.common.AwsException if copy fails
     */
    boolean copyObject(String sourceBucket, String sourceKey, String destinationBucket, String destinationKey);

    /**
     * Asynchronously uploads a file to S3.
     *
     * @param bucketName the name of the S3 bucket
     * @param key the S3 object key
     * @param file the file to upload
     * @param metadata S3 metadata including content type, tags, etc.
     * @return CompletableFuture containing upload result
     */
    CompletableFuture<S3UploadResult> uploadAsync(String bucketName, String key, File file, S3Metadata metadata);

    /**
     * Asynchronously uploads byte array data to S3.
     *
     * @param bucketName the name of the S3 bucket
     * @param key the S3 object key
     * @param data byte array data to upload
     * @param metadata S3 metadata including content type, tags, etc.
     * @return CompletableFuture containing upload result
     */
    CompletableFuture<S3UploadResult> uploadAsync(String bucketName, String key, byte[] data, S3Metadata metadata);

    /**
     * Asynchronously downloads data from S3.
     *
     * @param bucketName the name of the S3 bucket
     * @param key the S3 object key
     * @return CompletableFuture containing download result
     */
    CompletableFuture<S3DownloadResult<byte[]>> downloadAsync(String bucketName, String key);

    /**
     * Gets the default bucket name.
     *
     * @return the default bucket name or null if not set
     */
    String getDefaultBucket();

    /**
     * Sets the default bucket name.
     *
     * @param bucketName the bucket name to set as default
     * @throws IllegalArgumentException if bucketName is null or empty
     */
    void setDefaultBucket(String bucketName);

    /**
     * Gets the S3 operations capabilities.
     *
     * @return map of capability flags
     */
    Map<String, Boolean> getCapabilities();
}
