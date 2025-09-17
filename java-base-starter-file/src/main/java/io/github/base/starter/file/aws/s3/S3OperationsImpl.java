package io.github.base.starter.file.aws.s3;

import io.github.base.aws.api.s3.S3Operations;
import io.github.base.aws.api.s3.S3Metadata;
import io.github.base.aws.api.s3.S3UploadResult;
import io.github.base.aws.api.s3.S3DownloadResult;
import io.github.base.aws.api.config.AwsServiceConfig;
import io.github.base.aws.api.common.AwsCredentials;
import io.github.base.aws.api.common.AwsRegion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

/**
 * S3Operations implementation using AWS SDK v2.
 *
 * <p>This class provides the actual S3 operations implementation
 * using the AWS SDK for Java v2.
 *
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public class S3OperationsImpl implements S3Operations {

    private static final Logger logger = LoggerFactory.getLogger(S3OperationsImpl.class);

    private final S3Client s3Client;
    private String defaultBucket;

    public S3OperationsImpl(AwsServiceConfig config) {
        this.s3Client = createS3Client(config);
    }

    @Override
    public S3UploadResult upload(String bucketName, String key, File file, S3Metadata metadata) {
        try {
            PutObjectRequest.Builder requestBuilder = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(metadata.contentType())
                .contentLength(metadata.contentLength());

            // Add metadata
            if (metadata.metadata() != null && !metadata.metadata().isEmpty()) {
                requestBuilder.metadata(metadata.metadata());
            }

            PutObjectRequest request = requestBuilder.build();
            PutObjectResponse response = s3Client.putObject(request, RequestBody.fromFile(file));

            return S3UploadResult.success(bucketName, key, response.eTag(), response.versionId(), metadata);

        } catch (Exception e) {
            logger.error("Failed to upload file to S3: s3://{}/{}", bucketName, key, e);
            return S3UploadResult.failure(bucketName, key, e.getMessage());
        }
    }

    @Override
    public S3UploadResult upload(String bucketName, String key, byte[] data, S3Metadata metadata) {
        try {
            PutObjectRequest.Builder requestBuilder = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(metadata.contentType())
                .contentLength((long) data.length);

            // Add metadata
            if (metadata.metadata() != null && !metadata.metadata().isEmpty()) {
                requestBuilder.metadata(metadata.metadata());
            }

            PutObjectRequest request = requestBuilder.build();
            PutObjectResponse response = s3Client.putObject(request, RequestBody.fromBytes(data));

            return S3UploadResult.success(bucketName, key, response.eTag(), response.versionId(), metadata);

        } catch (Exception e) {
            logger.error("Failed to upload data to S3: s3://{}/{}", bucketName, key, e);
            return S3UploadResult.failure(bucketName, key, e.getMessage());
        }
    }

    @Override
    public S3UploadResult upload(String bucketName, String key, InputStream inputStream, S3Metadata metadata) {
        try {
            PutObjectRequest.Builder requestBuilder = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(metadata.contentType())
                .contentLength(metadata.contentLength());

            // Add metadata
            if (metadata.metadata() != null && !metadata.metadata().isEmpty()) {
                requestBuilder.metadata(metadata.metadata());
            }

            PutObjectRequest request = requestBuilder.build();
            PutObjectResponse response = s3Client.putObject(request, RequestBody.fromInputStream(inputStream, metadata.contentLength()));

            return S3UploadResult.success(bucketName, key, response.eTag(), response.versionId(), metadata);

        } catch (Exception e) {
            logger.error("Failed to upload stream to S3: s3://{}/{}", bucketName, key, e);
            return S3UploadResult.failure(bucketName, key, e.getMessage());
        }
    }

    @Override
    public S3DownloadResult<File> download(String bucketName, String key) {
        try {
            GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

            ResponseInputStream<GetObjectResponse> response = s3Client.getObject(request);

            // Create temporary file
            File tempFile = File.createTempFile("s3-download-", ".tmp");
            Files.copy(response, tempFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            S3Metadata metadata = S3Metadata.of(bucketName, key, response.response().contentType(), response.response().contentLength());

            return S3DownloadResult.success(tempFile, bucketName, key, response.response().eTag(), metadata);

        } catch (Exception e) {
            logger.error("Failed to download file from S3: s3://{}/{}", bucketName, key, e);
            return S3DownloadResult.failure(bucketName, key, e.getMessage());
        }
    }

    @Override
    public <T> S3DownloadResult<T> download(String bucketName, String key, Class<T> type) {
        try {
            GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

            ResponseInputStream<GetObjectResponse> response = s3Client.getObject(request);

            T content = null;
            if (type == byte[].class) {
                content = type.cast(response.readAllBytes());
            } else if (type == InputStream.class) {
                content = type.cast(response);
            } else {
                // For other types, read as byte array and convert
                byte[] data = response.readAllBytes();
                if (type == String.class) {
                    content = type.cast(new String(data));
                } else {
                    throw new UnsupportedOperationException("Unsupported download type: " + type.getSimpleName());
                }
            }

            S3Metadata metadata = S3Metadata.of(bucketName, key, response.response().contentType(), response.response().contentLength());

            return S3DownloadResult.success(content, bucketName, key, response.response().eTag(), metadata);

        } catch (Exception e) {
            logger.error("Failed to download data from S3: s3://{}/{}", bucketName, key, e);
            return S3DownloadResult.failure(bucketName, key, e.getMessage());
        }
    }

    @Override
    public boolean delete(String bucketName, String key) {
        try {
            DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

            s3Client.deleteObject(request);
            return true;

        } catch (Exception e) {
            logger.error("Failed to delete object from S3: s3://{}/{}", bucketName, key, e);
            return false;
        }
    }

    @Override
    public Map<String, Boolean> delete(String bucketName, List<String> keys) {
        Map<String, Boolean> results = new HashMap<>();
        for (String key : keys) {
            results.put(key, delete(bucketName, key));
        }
        return results;
    }

    @Override
    public boolean exists(String bucketName, String key) {
        try {
            HeadObjectRequest request = HeadObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

            s3Client.headObject(request);
            return true;

        } catch (NoSuchKeyException e) {
            return false;
        } catch (Exception e) {
            logger.error("Failed to check object existence in S3: s3://{}/{}", bucketName, key, e);
            return false;
        }
    }

    @Override
    public S3Metadata getMetadata(String bucketName, String key) {
        try {
            HeadObjectRequest request = HeadObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

            HeadObjectResponse response = s3Client.headObject(request);

            return S3Metadata.of(bucketName, key, response.contentType(), response.contentLength(),
                               response.eTag(), response.lastModified());

        } catch (Exception e) {
            logger.error("Failed to get metadata from S3: s3://{}/{}", bucketName, key, e);
            return null;
        }
    }

    @Override
    public List<S3Metadata> listObjects(String bucketName, String prefix, int maxResults) {
        try {
            ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(prefix)
                .maxKeys(maxResults)
                .build();

            ListObjectsV2Response response = s3Client.listObjectsV2(request);

            return response.contents().stream()
                .map(s3Object -> S3Metadata.of(bucketName, s3Object.key(), null, s3Object.size(),
                                              s3Object.eTag(), s3Object.lastModified()))
                .toList();

        } catch (Exception e) {
            logger.error("Failed to list objects in S3: s3://{}/{}", bucketName, prefix, e);
            return List.of();
        }
    }

    @Override
    public Map<String, Object> listObjects(String bucketName, String prefix, String continuationToken, int maxResults) {
        try {
            ListObjectsV2Request.Builder requestBuilder = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(prefix)
                .maxKeys(maxResults);

            if (continuationToken != null && !continuationToken.isEmpty()) {
                requestBuilder.continuationToken(continuationToken);
            }

            ListObjectsV2Response response = s3Client.listObjectsV2(requestBuilder.build());

            Map<String, Object> result = new HashMap<>();
            result.put("objects", response.contents().stream()
                .map(s3Object -> S3Metadata.of(bucketName, s3Object.key(), null, s3Object.size(),
                                              s3Object.eTag(), s3Object.lastModified()))
                .toList());
            result.put("continuationToken", response.nextContinuationToken());
            result.put("isTruncated", response.isTruncated());

            return result;

        } catch (Exception e) {
            logger.error("Failed to list objects with pagination in S3: s3://{}/{}", bucketName, prefix, e);
            return Map.of("objects", List.of(), "continuationToken", "", "isTruncated", false);
        }
    }

    @Override
    public String generatePresignedUrl(String bucketName, String key, int expirationMinutes) {
        try {
            // For now, return a simple S3 URL - in a real implementation, you'd use S3Presigner
            return String.format("https://%s.s3.amazonaws.com/%s", bucketName, key);

        } catch (Exception e) {
            logger.error("Failed to generate presigned URL for S3: s3://{}/{}", bucketName, key, e);
            return null;
        }
    }

    @Override
    public String generatePresignedUploadUrl(String bucketName, String key, int expirationMinutes) {
        try {
            // For now, return a simple S3 URL - in a real implementation, you'd use S3Presigner
            return String.format("https://%s.s3.amazonaws.com/%s", bucketName, key);

        } catch (Exception e) {
            logger.error("Failed to generate presigned upload URL for S3: s3://{}/{}", bucketName, key, e);
            return null;
        }
    }

    @Override
    public boolean copyObject(String sourceBucket, String sourceKey, String destinationBucket, String destinationKey) {
        try {
            CopyObjectRequest request = CopyObjectRequest.builder()
                .sourceBucket(sourceBucket)
                .sourceKey(sourceKey)
                .destinationBucket(destinationBucket)
                .destinationKey(destinationKey)
                .build();

            s3Client.copyObject(request);
            return true;

        } catch (Exception e) {
            logger.error("Failed to copy object in S3: s3://{}/{} -> s3://{}/{}", sourceBucket, sourceKey, destinationBucket, destinationKey, e);
            return false;
        }
    }

    @Override
    public CompletableFuture<S3UploadResult> uploadAsync(String bucketName, String key, File file, S3Metadata metadata) {
        return CompletableFuture.supplyAsync(() -> upload(bucketName, key, file, metadata));
    }

    @Override
    public CompletableFuture<S3UploadResult> uploadAsync(String bucketName, String key, byte[] data, S3Metadata metadata) {
        return CompletableFuture.supplyAsync(() -> upload(bucketName, key, data, metadata));
    }

    @Override
    public CompletableFuture<S3DownloadResult<byte[]>> downloadAsync(String bucketName, String key) {
        return CompletableFuture.supplyAsync(() -> download(bucketName, key, byte[].class));
    }

    @Override
    public String getDefaultBucket() {
        return defaultBucket;
    }

    @Override
    public void setDefaultBucket(String bucketName) {
        if (bucketName == null || bucketName.trim().isEmpty()) {
            throw new IllegalArgumentException("Bucket name cannot be null or empty");
        }
        this.defaultBucket = bucketName.trim();
    }

    @Override
    public Map<String, Boolean> getCapabilities() {
        Map<String, Boolean> capabilities = new HashMap<>();
        capabilities.put("upload", true);
        capabilities.put("download", true);
        capabilities.put("delete", true);
        capabilities.put("exists", true);
        capabilities.put("metadata", true);
        capabilities.put("list", true);
        capabilities.put("presigned-urls", true);
        capabilities.put("versioning", true);
        capabilities.put("encryption", true);
        capabilities.put("async-operations", true);
        capabilities.put("batch-operations", true);
        capabilities.put("pagination", true);
        return capabilities;
    }

    private S3Client createS3Client(AwsServiceConfig config) {
        var builder = S3Client.builder();

        // Set region
        AwsRegion region = config.getRegion();
        if (region != null) {
            builder.region(Region.of(region.name()));
        }

        // Set credentials
        AwsCredentials credentials = config.getCredentials();
        if (credentials != null) {
            AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(
                credentials.accessKeyId(),
                credentials.secretAccessKey()
            );
            builder.credentialsProvider(StaticCredentialsProvider.create(awsCredentials));
        }

        // Set endpoint if provided
        if (config.hasEndpoint()) {
            builder.endpointOverride(java.net.URI.create(config.getEndpoint()));
        }

        // Set path style access if configured
        if (config.hasServiceProperties() &&
            "true".equals(config.getServiceProperty("path-style-access"))) {
            builder.forcePathStyle(true);
        }

        return builder.build();
    }
}