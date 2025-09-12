package io.github.base.file.api.storage;

import io.github.base.file.api.model.FileMetadata;
import io.github.base.file.api.model.FileType;
import io.github.base.file.api.model.UploadResult;
import io.github.base.file.api.model.DownloadResult;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Core file storage contract providing abstraction for file operations.
 * This interface defines the standard operations for file storage across different providers.
 * 
 * <p>Supported file types:
 * <ul>
 *   <li>{@link FileType#FILE} - Java File objects</li>
 *   <li>{@link FileType#BYTE} - Byte arrays</li>
 *   <li>{@link FileType#STREAM} - InputStream/OutputStream</li>
 * </ul>
 * 
 * <p>All operations are designed to be thread-safe and can be used in concurrent environments.
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public interface FileStorage {
    
    /**
     * Uploads a file to storage.
     * 
     * @param fileId unique identifier for the file
     * @param file the file to upload
     * @param metadata file metadata including name, content type, size
     * @return upload result containing success status, file ID, and location
     * @throws IllegalArgumentException if fileId is null or empty, or file is null
     * @throws io.github.base.file.api.exception.FileUploadException if upload fails
     */
    UploadResult upload(String fileId, File file, FileMetadata metadata);
    
    /**
     * Uploads byte array data to storage.
     * 
     * @param fileId unique identifier for the file
     * @param data byte array data to upload
     * @param metadata file metadata including name, content type, size
     * @return upload result containing success status, file ID, and location
     * @throws IllegalArgumentException if fileId is null or empty, or data is null
     * @throws io.github.base.file.api.exception.FileUploadException if upload fails
     */
    UploadResult upload(String fileId, byte[] data, FileMetadata metadata);
    
    /**
     * Uploads data from input stream to storage.
     * 
     * @param fileId unique identifier for the file
     * @param inputStream input stream containing data to upload
     * @param metadata file metadata including name, content type, size
     * @return upload result containing success status, file ID, and location
     * @throws IllegalArgumentException if fileId is null or empty, or inputStream is null
     * @throws io.github.base.file.api.exception.FileUploadException if upload fails
     */
    UploadResult upload(String fileId, InputStream inputStream, FileMetadata metadata);
    
    /**
     * Downloads a file from storage as a File object.
     * 
     * @param fileId unique identifier for the file
     * @return download result containing the file and metadata
     * @throws IllegalArgumentException if fileId is null or empty
     * @throws io.github.base.file.api.exception.FileNotFoundException if file not found
     */
    DownloadResult<File> downloadAsFile(String fileId);
    
    /**
     * Downloads a file from storage as the specified type.
     * 
     * @param fileId unique identifier for the file
     * @param type the class type to download as
     * @param <T> the type to download as
     * @return download result containing the file content and metadata
     * @throws IllegalArgumentException if fileId is null or empty
     * @throws io.github.base.file.api.exception.FileNotFoundException if file not found
     */
    <T> DownloadResult<T> download(String fileId, Class<T> type);
    
    /**
     * Deletes a file from storage.
     * 
     * @param fileId unique identifier for the file
     * @return true if file was deleted successfully, false if file not found
     * @throws IllegalArgumentException if fileId is null or empty
     */
    boolean delete(String fileId);
    
    /**
     * Checks if a file exists in storage.
     * 
     * @param fileId unique identifier for the file
     * @return true if file exists, false otherwise
     * @throws IllegalArgumentException if fileId is null or empty
     */
    boolean exists(String fileId);
    
    /**
     * Gets file metadata without downloading the file.
     * 
     * @param fileId unique identifier for the file
     * @return file metadata or null if file not found
     * @throws IllegalArgumentException if fileId is null or empty
     */
    FileMetadata getMetadata(String fileId);
    
    /**
     * Lists files in storage with optional filtering.
     * 
     * @param prefix optional prefix to filter files (null for all files)
     * @param maxResults maximum number of results to return (0 for no limit)
     * @return list of file metadata
     */
    List<FileMetadata> listFiles(String prefix, int maxResults);
    
    /**
     * Lists files in storage with pagination.
     * 
     * @param prefix optional prefix to filter files (null for all files)
     * @param continuationToken token for pagination (null for first page)
     * @param maxResults maximum number of results to return
     * @return map containing file metadata list and next continuation token
     */
    Map<String, Object> listFiles(String prefix, String continuationToken, int maxResults);
    
    /**
     * Generates a presigned URL for file access.
     * 
     * @param fileId unique identifier for the file
     * @param expirationMinutes URL expiration time in minutes
     * @return presigned URL or null if not supported by provider
     * @throws IllegalArgumentException if fileId is null or empty, or expirationMinutes <= 0
     */
    String generatePresignedUrl(String fileId, int expirationMinutes);
    
    /**
     * Asynchronously uploads a file to storage.
     * 
     * @param fileId unique identifier for the file
     * @param file the file to upload
     * @param metadata file metadata including name, content type, size
     * @return CompletableFuture containing upload result
     */
    CompletableFuture<UploadResult> uploadAsync(String fileId, File file, FileMetadata metadata);
    
    /**
     * Asynchronously uploads byte array data to storage.
     * 
     * @param fileId unique identifier for the file
     * @param data byte array data to upload
     * @param metadata file metadata including name, content type, size
     * @return CompletableFuture containing upload result
     */
    CompletableFuture<UploadResult> uploadAsync(String fileId, byte[] data, FileMetadata metadata);
    
    /**
     * Asynchronously downloads a file from storage.
     * 
     * @param fileId unique identifier for the file
     * @return CompletableFuture containing download result
     */
    CompletableFuture<DownloadResult<byte[]>> downloadAsync(String fileId);
    
    /**
     * Gets the storage provider name.
     * 
     * @return provider name (e.g., "local", "s3", "gcs", "azure")
     */
    String getProviderName();
    
    /**
     * Gets the storage provider capabilities.
     * 
     * @return map of capability flags
     */
    Map<String, Boolean> getCapabilities();
}
