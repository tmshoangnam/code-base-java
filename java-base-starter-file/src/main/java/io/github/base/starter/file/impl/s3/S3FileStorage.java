package io.github.base.starter.file.impl.s3;

import io.github.base.file.api.model.FileMetadata;
import io.github.base.file.api.model.UploadResult;
import io.github.base.file.api.model.DownloadResult;
import io.github.base.file.api.exception.FileStorageException;
import io.github.base.file.api.exception.FileUploadException;
import io.github.base.file.api.exception.FileNotFoundException;
import io.github.base.aws.api.s3.S3Operations;
import io.github.base.aws.api.s3.S3Metadata;
import io.github.base.aws.api.s3.S3UploadResult;
import io.github.base.aws.api.s3.S3DownloadResult;
import io.github.base.aws.api.common.AwsException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

/**
 * S3 implementation of FileStorage interface.
 *
 * <p>This implementation provides file storage operations using AWS S3.
 * It supports all standard file operations including upload, download, delete,
 * and metadata management.
 *
 * <p>Features:
 * <ul>
 *   <li>File upload (File, byte[], InputStream)</li>
 *   <li>File download (as File, byte[], or custom type)</li>
 *   <li>File deletion and existence checking</li>
 *   <li>Metadata management</li>
 *   <li>File listing with pagination</li>
 *   <li>Presigned URL generation</li>
 *   <li>Asynchronous operations</li>
 * </ul>
 *
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public class S3FileStorage implements io.github.base.file.api.storage.FileStorage {

    private static final Logger logger = LoggerFactory.getLogger(S3FileStorage.class);

    private final S3Operations s3Operations;
    private final String bucketName;
    private final String keyPrefix;

    /**
     * Creates a new S3FileStorage instance.
     *
     * @param s3Operations S3 operations interface
     * @param bucketName S3 bucket name
     * @param keyPrefix optional key prefix for all files (null for no prefix)
     */
    public S3FileStorage(S3Operations s3Operations, String bucketName, String keyPrefix) {
        if (s3Operations == null) {
            throw new IllegalArgumentException("S3Operations cannot be null");
        }
        if (bucketName == null || bucketName.trim().isEmpty()) {
            throw new IllegalArgumentException("Bucket name cannot be null or empty");
        }

        this.s3Operations = s3Operations;
        this.bucketName = bucketName;
        this.keyPrefix = keyPrefix != null ? keyPrefix.trim() : null;
    }

    /**
     * Creates a new S3FileStorage instance without key prefix.
     *
     * @param s3Operations S3 operations interface
     * @param bucketName S3 bucket name
     */
    public S3FileStorage(S3Operations s3Operations, String bucketName) {
        this(s3Operations, bucketName, null);
    }

    @Override
    public UploadResult upload(String fileId, File file, FileMetadata metadata) throws FileStorageException {
        validateUploadParams(fileId, file, metadata);

        try {
            String s3Key = buildS3Key(fileId);
            S3Metadata s3Metadata = convertToS3Metadata(metadata);

            logger.debug("Uploading file to S3: {} -> s3://{}/{}", fileId, bucketName, s3Key);

            S3UploadResult result = s3Operations.upload(bucketName, s3Key, file, s3Metadata);

            if (result.isSuccess()) {
                logger.debug("Successfully uploaded file: {} (ETag: {})", fileId, result.getEtag());
                return new UploadResult(
                    true,
                    fileId,
                    result.getLocation(),
                    result.getEtag(),
                    result.getVersionId(),
                    Instant.now(),
                    metadata
                );
            } else {
                throw new FileUploadException(fileId, "S3 upload failed: " + result.getErrorMessage());
            }

        } catch (AwsException e) {
            logger.error("AWS error during file upload: {}", fileId, e);
            throw new FileUploadException(fileId, "AWS error during upload", e);
        } catch (Exception e) {
            logger.error("Unexpected error during file upload: {}", fileId, e);
            throw new FileUploadException(fileId, "Unexpected error during upload", e);
        }
    }

    @Override
    public UploadResult upload(String fileId, byte[] data, FileMetadata metadata) throws FileStorageException {
        validateUploadParams(fileId, data, metadata);

        try {
            String s3Key = buildS3Key(fileId);
            S3Metadata s3Metadata = convertToS3Metadata(metadata);

            logger.debug("Uploading byte array to S3: {} -> s3://{}/{}", fileId, bucketName, s3Key);

            S3UploadResult result = s3Operations.upload(bucketName, s3Key, data, s3Metadata);

            if (result.isSuccess()) {
                logger.debug("Successfully uploaded byte array: {} (ETag: {})", fileId, result.getEtag());
                return new UploadResult(
                    true,
                    fileId,
                    result.getLocation(),
                    result.getEtag(),
                    result.getVersionId(),
                    Instant.now(),
                    metadata
                );
            } else {
                throw new FileUploadException(fileId, "S3 upload failed: " + result.getErrorMessage());
            }

        } catch (AwsException e) {
            logger.error("AWS error during byte array upload: {}", fileId, e);
            throw new FileUploadException(fileId, "AWS error during upload", e);
        } catch (Exception e) {
            logger.error("Unexpected error during byte array upload: {}", fileId, e);
            throw new FileUploadException(fileId, "Unexpected error during upload", e);
        }
    }

    @Override
    public UploadResult upload(String fileId, InputStream inputStream, FileMetadata metadata) throws FileStorageException {
        validateUploadParams(fileId, inputStream, metadata);

        try {
            String s3Key = buildS3Key(fileId);
            S3Metadata s3Metadata = convertToS3Metadata(metadata);

            logger.debug("Uploading input stream to S3: {} -> s3://{}/{}", fileId, bucketName, s3Key);

            S3UploadResult result = s3Operations.upload(bucketName, s3Key, inputStream, s3Metadata);

            if (result.isSuccess()) {
                logger.debug("Successfully uploaded input stream: {} (ETag: {})", fileId, result.getEtag());
                return new UploadResult(
                    true,
                    fileId,
                    result.getLocation(),
                    result.getEtag(),
                    result.getVersionId(),
                    Instant.now(),
                    metadata
                );
            } else {
                throw new FileUploadException(fileId, "S3 upload failed: " + result.getErrorMessage());
            }

        } catch (AwsException e) {
            logger.error("AWS error during input stream upload: {}", fileId, e);
            throw new FileUploadException(fileId, "AWS error during upload", e);
        } catch (Exception e) {
            logger.error("Unexpected error during input stream upload: {}", fileId, e);
            throw new FileUploadException(fileId, "Unexpected error during upload", e);
        }
    }

    @Override
    public DownloadResult<File> downloadAsFile(String fileId) throws FileStorageException {
        validateFileId(fileId);

        try {
            String s3Key = buildS3Key(fileId);

            logger.debug("Downloading file from S3: s3://{}/{}", bucketName, s3Key);

            S3DownloadResult<File> result = s3Operations.download(bucketName, s3Key);

            if (result.isSuccess() && result.getContent() != null) {
                File file = result.getContent();
                FileMetadata metadata = convertFromS3Metadata(result.getMetadata());

                logger.debug("Successfully downloaded file: {} (size: {} bytes)", fileId, file.length());

                return DownloadResult.success(file, metadata);
            } else {
                throw new FileNotFoundException(fileId, "File not found in S3: " + result.getErrorMessage());
            }

        } catch (AwsException e) {
            logger.error("AWS error during file download: {}", fileId, e);
            throw new FileStorageException("AWS error during download", e);
        } catch (Exception e) {
            logger.error("Unexpected error during file download: {}", fileId, e);
            throw new FileStorageException("Unexpected error during download", e);
        }
    }

    @Override
    public <T> DownloadResult<T> download(String fileId, Class<T> type) throws FileStorageException {
        validateFileId(fileId);
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }

        try {
            String s3Key = buildS3Key(fileId);

            logger.debug("Downloading data from S3: s3://{}/{} as {}", bucketName, s3Key, type.getSimpleName());

            S3DownloadResult<T> result = s3Operations.download(bucketName, s3Key, type);

            if (result.isSuccess() && result.getContent() != null) {
                FileMetadata metadata = convertFromS3Metadata(result.getMetadata());

                logger.debug("Successfully downloaded data: {} (type: {})", fileId, type.getSimpleName());

                return DownloadResult.success(result.getContent(), metadata);
            } else {
                throw new FileNotFoundException(fileId, "File not found in S3: " + result.getErrorMessage());
            }

        } catch (AwsException e) {
            logger.error("AWS error during data download: {}", fileId, e);
            throw new FileStorageException("AWS error during download", e);
        } catch (Exception e) {
            logger.error("Unexpected error during data download: {}", fileId, e);
            throw new FileStorageException("Unexpected error during download", e);
        }
    }

    @Override
    public boolean delete(String fileId) throws FileStorageException {
        validateFileId(fileId);

        try {
            String s3Key = buildS3Key(fileId);

            logger.debug("Deleting file from S3: s3://{}/{}", bucketName, s3Key);

            boolean deleted = s3Operations.delete(bucketName, s3Key);

            if (deleted) {
                logger.debug("Successfully deleted file: {}", fileId);
            } else {
                logger.debug("File not found for deletion: {}", fileId);
            }

            return deleted;

        } catch (AwsException e) {
            logger.error("AWS error during file deletion: {}", fileId, e);
            throw new FileStorageException("AWS error during deletion", e);
        } catch (Exception e) {
            logger.error("Unexpected error during file deletion: {}", fileId, e);
            throw new FileStorageException("Unexpected error during deletion", e);
        }
    }

    @Override
    public boolean exists(String fileId) throws FileStorageException {
        validateFileId(fileId);

        try {
            String s3Key = buildS3Key(fileId);

            logger.debug("Checking file existence in S3: s3://{}/{}", bucketName, s3Key);

            boolean exists = s3Operations.exists(bucketName, s3Key);

            logger.debug("File existence check result: {} -> {}", fileId, exists);

            return exists;

        } catch (AwsException e) {
            logger.error("AWS error during file existence check: {}", fileId, e);
            throw new FileStorageException("AWS error during existence check", e);
        } catch (Exception e) {
            logger.error("Unexpected error during file existence check: {}", fileId, e);
            throw new FileStorageException("Unexpected error during existence check", e);
        }
    }

    @Override
    public FileMetadata getMetadata(String fileId) throws FileStorageException {
        validateFileId(fileId);

        try {
            String s3Key = buildS3Key(fileId);

            logger.debug("Getting file metadata from S3: s3://{}/{}", bucketName, s3Key);

            S3Metadata s3Metadata = s3Operations.getMetadata(bucketName, s3Key);

            if (s3Metadata != null) {
                FileMetadata metadata = convertFromS3Metadata(s3Metadata);
                logger.debug("Successfully retrieved metadata for file: {}", fileId);
                return metadata;
            } else {
                logger.debug("No metadata found for file: {}", fileId);
                return null;
            }

        } catch (AwsException e) {
            logger.error("AWS error during metadata retrieval: {}", fileId, e);
            throw new FileStorageException("AWS error during metadata retrieval", e);
        } catch (Exception e) {
            logger.error("Unexpected error during metadata retrieval: {}", fileId, e);
            throw new FileStorageException("Unexpected error during metadata retrieval", e);
        }
    }

    @Override
    public List<FileMetadata> listFiles(String prefix, int maxResults) throws FileStorageException {
        try {
            String s3Prefix = buildS3Key(prefix);

            logger.debug("Listing files in S3: s3://{}/{} (max: {})", bucketName, s3Prefix, maxResults);

            List<S3Metadata> s3MetadataList = s3Operations.listObjects(bucketName, s3Prefix, maxResults);

            List<FileMetadata> fileMetadataList = s3MetadataList.stream()
                .map(this::convertFromS3Metadata)
                .toList();

            logger.debug("Successfully listed {} files", fileMetadataList.size());

            return fileMetadataList;

        } catch (AwsException e) {
            logger.error("AWS error during file listing", e);
            throw new FileStorageException("AWS error during file listing", e);
        } catch (Exception e) {
            logger.error("Unexpected error during file listing", e);
            throw new FileStorageException("Unexpected error during file listing", e);
        }
    }

    @Override
    public Map<String, Object> listFiles(String prefix, String continuationToken, int maxResults) throws FileStorageException {
        try {
            String s3Prefix = buildS3Key(prefix);

            logger.debug("Listing files in S3 with pagination: s3://{}/{} (token: {}, max: {})",
                bucketName, s3Prefix, continuationToken, maxResults);

            Map<String, Object> result = s3Operations.listObjects(bucketName, s3Prefix, continuationToken, maxResults);

            // Convert S3Metadata list to FileMetadata list
            @SuppressWarnings("unchecked")
            List<S3Metadata> s3MetadataList = (List<S3Metadata>) result.get("objects");
            if (s3MetadataList != null) {
                List<FileMetadata> fileMetadataList = s3MetadataList.stream()
                    .map(this::convertFromS3Metadata)
                    .toList();
                result.put("objects", fileMetadataList);
            }

            logger.debug("Successfully listed files with pagination (count: {})",
                s3MetadataList != null ? s3MetadataList.size() : 0);

            return result;

        } catch (AwsException e) {
            logger.error("AWS error during paginated file listing", e);
            throw new FileStorageException("AWS error during paginated file listing", e);
        } catch (Exception e) {
            logger.error("Unexpected error during paginated file listing", e);
            throw new FileStorageException("Unexpected error during paginated file listing", e);
        }
    }

    @Override
    public String generatePresignedUrl(String fileId, int expirationMinutes) throws FileStorageException {
        validateFileId(fileId);
        if (expirationMinutes <= 0) {
            throw new IllegalArgumentException("Expiration minutes must be positive");
        }

        try {
            String s3Key = buildS3Key(fileId);

            logger.debug("Generating presigned URL for S3: s3://{}/{} (expires in {} minutes)",
                bucketName, s3Key, expirationMinutes);

            String presignedUrl = s3Operations.generatePresignedUrl(bucketName, s3Key, expirationMinutes);

            if (presignedUrl != null) {
                logger.debug("Successfully generated presigned URL for file: {}", fileId);
            } else {
                logger.debug("Presigned URL generation not supported for file: {}", fileId);
            }

            return presignedUrl;

        } catch (AwsException e) {
            logger.error("AWS error during presigned URL generation: {}", fileId, e);
            throw new FileStorageException("AWS error during presigned URL generation", e);
        } catch (Exception e) {
            logger.error("Unexpected error during presigned URL generation: {}", fileId, e);
            throw new FileStorageException("Unexpected error during presigned URL generation", e);
        }
    }

    @Override
    public CompletableFuture<UploadResult> uploadAsync(String fileId, File file, FileMetadata metadata) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return upload(fileId, file, metadata);
            } catch (FileStorageException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public CompletableFuture<UploadResult> uploadAsync(String fileId, byte[] data, FileMetadata metadata) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return upload(fileId, data, metadata);
            } catch (FileStorageException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public CompletableFuture<DownloadResult<byte[]>> downloadAsync(String fileId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return download(fileId, byte[].class);
            } catch (FileStorageException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public String getProviderName() {
        return "s3";
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
        capabilities.put("streaming", true);
        capabilities.put("concurrent", true);
        capabilities.put("replication", true);
        capabilities.put("encryption", true);
        capabilities.put("versioning", true);
        capabilities.put("presigned-urls", true);
        capabilities.put("async", true);
        return capabilities;
    }

    // Private helper methods

    private void validateFileId(String fileId) {
        if (fileId == null || fileId.trim().isEmpty()) {
            throw new IllegalArgumentException("File ID cannot be null or empty");
        }
    }

    private void validateUploadParams(String fileId, Object content, FileMetadata metadata) {
        validateFileId(fileId);
        if (content == null) {
            throw new IllegalArgumentException("Content cannot be null");
        }
        if (metadata == null) {
            throw new IllegalArgumentException("Metadata cannot be null");
        }
    }

    private String buildS3Key(String fileId) {
        if (keyPrefix != null && !keyPrefix.isEmpty()) {
            return keyPrefix.endsWith("/") ? keyPrefix + fileId : keyPrefix + "/" + fileId;
        }
        return fileId;
    }

    private S3Metadata convertToS3Metadata(FileMetadata metadata) {
        return S3Metadata.of(
            bucketName,
            buildS3Key(metadata.fileName()),
            metadata.contentType(),
            metadata.size()
        );
    }

    private FileMetadata convertFromS3Metadata(S3Metadata s3Metadata) {
        if (s3Metadata == null) {
            return null;
        }

        return FileMetadata.of(
            s3Metadata.key(),
            s3Metadata.contentType(),
            s3Metadata.contentLength(),
            s3Metadata.etag()
        );
    }
}
