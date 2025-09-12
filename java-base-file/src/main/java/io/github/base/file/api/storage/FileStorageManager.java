package io.github.base.file.api.storage;

import io.github.base.file.api.model.FileMetadata;
import io.github.base.file.api.model.FileType;
import io.github.base.file.api.model.UploadResult;
import io.github.base.file.api.model.DownloadResult;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Manager interface for handling multiple file storage providers.
 *
 * <p>This interface provides a unified way to manage multiple storage providers
 * and route operations to the appropriate provider based on configuration or
 * provider selection criteria.
 *
 * <p>The manager supports:
 * <ul>
 *   <li>Multiple storage providers (local, S3, GCS, Azure, etc.)</li>
 *   <li>Provider selection based on file type, size, or other criteria</li>
 *   <li>Fallback mechanisms when primary provider fails</li>
 *   <li>Load balancing across providers</li>
 *   <li>Provider health monitoring</li>
 * </ul>
 *
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public interface FileStorageManager {

    /**
     * Gets the default file storage provider.
     *
     * @return the default FileStorage instance
     * @throws IllegalStateException if no default provider is configured
     */
    FileStorage getDefaultStorage();

    /**
     * Gets a specific file storage provider by name.
     *
     * @param providerName the name of the provider
     * @return the FileStorage instance for the specified provider
     * @throws IllegalArgumentException if providerName is null or empty
     * @throws IllegalStateException if provider is not found
     */
    FileStorage getStorage(String providerName);

    /**
     * Gets the best file storage provider for the given criteria.
     *
     * @param fileType the type of file to be stored
     * @param fileSize the size of the file in bytes
     * @param contentType the MIME type of the file
     * @return the best FileStorage instance for the criteria
     * @throws IllegalStateException if no suitable provider is found
     */
    FileStorage getBestStorage(FileType fileType, long fileSize, String contentType);

    /**
     * Gets all available storage providers.
     *
     * @return set of provider names
     */
    Set<String> getAvailableProviders();

    /**
     * Checks if a provider is available and healthy.
     *
     * @param providerName the name of the provider
     * @return true if provider is available and healthy, false otherwise
     */
    boolean isProviderHealthy(String providerName);

    /**
     * Gets the health status of all providers.
     *
     * @return map of provider names to their health status
     */
    Map<String, Boolean> getProviderHealthStatus();

    /**
     * Uploads a file using the default provider.
     *
     * @param fileId unique identifier for the file
     * @param file the file to upload
     * @param metadata file metadata including name, content type, size
     * @return upload result containing success status, file ID, and location
     */
    UploadResult upload(String fileId, File file, FileMetadata metadata);

    /**
     * Uploads byte array data using the default provider.
     *
     * @param fileId unique identifier for the file
     * @param data byte array data to upload
     * @param metadata file metadata including name, content type, size
     * @return upload result containing success status, file ID, and location
     */
    UploadResult upload(String fileId, byte[] data, FileMetadata metadata);

    /**
     * Uploads data from input stream using the default provider.
     *
     * @param fileId unique identifier for the file
     * @param inputStream input stream containing data to upload
     * @param metadata file metadata including name, content type, size
     * @return upload result containing success status, file ID, and location
     */
    UploadResult upload(String fileId, InputStream inputStream, FileMetadata metadata);

    /**
     * Uploads a file using a specific provider.
     *
     * @param providerName the name of the provider to use
     * @param fileId unique identifier for the file
     * @param file the file to upload
     * @param metadata file metadata including name, content type, size
     * @return upload result containing success status, file ID, and location
     */
    UploadResult upload(String providerName, String fileId, File file, FileMetadata metadata);

    /**
     * Uploads byte array data using a specific provider.
     *
     * @param providerName the name of the provider to use
     * @param fileId unique identifier for the file
     * @param data byte array data to upload
     * @param metadata file metadata including name, content type, size
     * @return upload result containing success status, file ID, and location
     */
    UploadResult upload(String providerName, String fileId, byte[] data, FileMetadata metadata);

    /**
     * Uploads data from input stream using a specific provider.
     *
     * @param providerName the name of the provider to use
     * @param fileId unique identifier for the file
     * @param inputStream input stream containing data to upload
     * @param metadata file metadata including name, content type, size
     * @return upload result containing success status, file ID, and location
     */
    UploadResult upload(String providerName, String fileId, InputStream inputStream, FileMetadata metadata);

    /**
     * Downloads a file using the default provider.
     *
     * @param fileId unique identifier for the file
     * @return download result containing the file and metadata
     */
    DownloadResult<File> downloadAsFile(String fileId);

    /**
     * Downloads a file as the specified type using the default provider.
     *
     * @param fileId unique identifier for the file
     * @param type the class type to download as
     * @param <T> the type to download as
     * @return download result containing the file content and metadata
     */
    <T> DownloadResult<T> download(String fileId, Class<T> type);

    /**
     * Downloads a file using a specific provider.
     *
     * @param providerName the name of the provider to use
     * @param fileId unique identifier for the file
     * @return download result containing the file and metadata
     */
    DownloadResult<File> downloadAsFile(String providerName, String fileId);

    /**
     * Downloads a file as the specified type using a specific provider.
     *
     * @param providerName the name of the provider to use
     * @param fileId unique identifier for the file
     * @param type the class type to download as
     * @param <T> the type to download as
     * @return download result containing the file content and metadata
     */
    <T> DownloadResult<T> download(String providerName, String fileId, Class<T> type);

    /**
     * Deletes a file using the default provider.
     *
     * @param fileId unique identifier for the file
     * @return true if file was deleted successfully, false if file not found
     */
    boolean delete(String fileId);

    /**
     * Deletes a file using a specific provider.
     *
     * @param providerName the name of the provider to use
     * @param fileId unique identifier for the file
     * @return true if file was deleted successfully, false if file not found
     */
    boolean delete(String providerName, String fileId);

    /**
     * Checks if a file exists using the default provider.
     *
     * @param fileId unique identifier for the file
     * @return true if file exists, false otherwise
     */
    boolean exists(String fileId);

    /**
     * Checks if a file exists using a specific provider.
     *
     * @param providerName the name of the provider to use
     * @param fileId unique identifier for the file
     * @return true if file exists, false otherwise
     */
    boolean exists(String providerName, String fileId);

    /**
     * Gets file metadata using the default provider.
     *
     * @param fileId unique identifier for the file
     * @return file metadata or null if file not found
     */
    FileMetadata getMetadata(String fileId);

    /**
     * Gets file metadata using a specific provider.
     *
     * @param providerName the name of the provider to use
     * @param fileId unique identifier for the file
     * @return file metadata or null if file not found
     */
    FileMetadata getMetadata(String providerName, String fileId);

    /**
     * Lists files using the default provider.
     *
     * @param prefix optional prefix to filter files (null for all files)
     * @param maxResults maximum number of results to return (0 for no limit)
     * @return list of file metadata
     */
    List<FileMetadata> listFiles(String prefix, int maxResults);

    /**
     * Lists files using a specific provider.
     *
     * @param providerName the name of the provider to use
     * @param prefix optional prefix to filter files (null for all files)
     * @param maxResults maximum number of results to return (0 for no limit)
     * @return list of file metadata
     */
    List<FileMetadata> listFiles(String providerName, String prefix, int maxResults);

    /**
     * Asynchronously uploads a file using the default provider.
     *
     * @param fileId unique identifier for the file
     * @param file the file to upload
     * @param metadata file metadata including name, content type, size
     * @return CompletableFuture containing upload result
     */
    CompletableFuture<UploadResult> uploadAsync(String fileId, File file, FileMetadata metadata);

    /**
     * Asynchronously uploads byte array data using the default provider.
     *
     * @param fileId unique identifier for the file
     * @param data byte array data to upload
     * @param metadata file metadata including name, content type, size
     * @return CompletableFuture containing upload result
     */
    CompletableFuture<UploadResult> uploadAsync(String fileId, byte[] data, FileMetadata metadata);

    /**
     * Asynchronously downloads a file using the default provider.
     *
     * @param fileId unique identifier for the file
     * @return CompletableFuture containing download result
     */
    CompletableFuture<DownloadResult<byte[]>> downloadAsync(String fileId);

    String getDefaultProvider();

    /**
     * Gets the default provider name.
     *
     * @return the name of the default provider
     */
    String getDefaultProviderName();

    /**
     * Sets the default provider name.
     *
     * @param providerName the name of the provider to set as default
     * @throws IllegalArgumentException if providerName is null or empty
     * @throws IllegalStateException if provider is not found
     */
    void setDefaultProvider(String providerName);
}
