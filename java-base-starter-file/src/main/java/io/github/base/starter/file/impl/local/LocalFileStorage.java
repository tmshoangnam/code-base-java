package io.github.base.starter.file.impl.local;

import io.github.base.file.api.exception.FileStorageException;
import io.github.base.file.api.exception.FileNotFoundException;
import io.github.base.file.api.exception.FileUploadException;
import io.github.base.file.api.model.DownloadResult;
import io.github.base.file.api.model.FileMetadata;
import io.github.base.file.api.model.UploadResult;
import io.github.base.file.api.storage.FileStorage;
import io.github.base.starter.file.constants.FileStorageConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Local file storage implementation.
 * 
 * <p>This implementation provides file storage using the local filesystem.
 * It supports all standard file operations including upload, download, delete,
 * exists checks, metadata retrieval, and file listing.
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public class LocalFileStorage implements FileStorage {
    
    private static final Logger logger = LoggerFactory.getLogger(LocalFileStorage.class);
    
    private final Path basePath;
    private final boolean createDirectories;
    private final String filePermissions;
    private final String directoryPermissions;
    
    /**
     * Creates a new local file storage instance.
     * 
     * @param basePath the base path for file storage
     * @param createDirectories whether to create directories automatically
     * @param filePermissions the file permissions
     * @param directoryPermissions the directory permissions
     */
    public LocalFileStorage(Path basePath, boolean createDirectories, String filePermissions, String directoryPermissions) {
        this.basePath = basePath;
        this.createDirectories = createDirectories;
        this.filePermissions = filePermissions;
        this.directoryPermissions = directoryPermissions;
        
        if (createDirectories) {
            try {
                Files.createDirectories(basePath);
                logger.info("Created base directory: {}", basePath);
            } catch (IOException e) {
                logger.error("Failed to create base directory: {}", basePath, e);
                throw new FileStorageException("Failed to create base directory: " + basePath, e);
            }
        }
    }
    
    @Override
    public UploadResult upload(String fileId, byte[] content, FileMetadata metadata) throws FileStorageException {
        try {
            Path filePath = getFilePath(fileId);
            ensureParentDirectoryExists(filePath);
            
            Files.write(filePath, content);
            setFilePermissions(filePath, filePermissions);
            
            logger.debug("Uploaded file: {} ({} bytes)", fileId, content.length);
            
            return new UploadResult(true, fileId, filePath.toString(), null, null, java.time.Instant.now(), metadata);
            
        } catch (IOException e) {
            logger.error("Failed to upload file: {}", fileId, e);
            throw new FileUploadException("Failed to upload file: " + fileId, e);
        }
    }
    
    @Override
    public UploadResult upload(String fileId, InputStream content, FileMetadata metadata) throws FileStorageException {
        try {
            Path filePath = getFilePath(fileId);
            ensureParentDirectoryExists(filePath);
            
            Files.copy(content, filePath, StandardCopyOption.REPLACE_EXISTING);
            setFilePermissions(filePath, filePermissions);
            
            logger.debug("Uploaded file: {} ({} bytes)", fileId, metadata.size());
            
            return new UploadResult(true, fileId, filePath.toString(), null, null, java.time.Instant.now(), metadata);
            
        } catch (IOException e) {
            logger.error("Failed to upload file: {}", fileId, e);
            throw new FileUploadException("Failed to upload file: " + fileId, e);
        }
    }
    
    @Override
    public UploadResult upload(String fileId, File content, FileMetadata metadata) throws FileStorageException {
        try {
            Path filePath = getFilePath(fileId);
            ensureParentDirectoryExists(filePath);
            
            Files.copy(content.toPath(), filePath, StandardCopyOption.REPLACE_EXISTING);
            setFilePermissions(filePath, filePermissions);
            
            logger.debug("Uploaded file: {} ({} bytes)", fileId, content.length());
            
            return new UploadResult(true, fileId, filePath.toString(), null, null, java.time.Instant.now(), metadata);
            
        } catch (IOException e) {
            logger.error("Failed to upload file: {}", fileId, e);
            throw new FileUploadException("Failed to upload file: " + fileId, e);
        }
    }
    
    @Override
    public DownloadResult<File> downloadAsFile(String fileId) throws FileStorageException {
        try {
            Path filePath = getFilePath(fileId);
            
            if (!Files.exists(filePath)) {
                throw new FileNotFoundException("File not found: " + fileId);
            }
            
            if (!Files.isRegularFile(filePath)) {
                throw new FileStorageException("Path is not a file: " + fileId);
            }
            
            File file = filePath.toFile();
            FileMetadata metadata = getFileMetadata(filePath);
            
            logger.debug("Downloaded file: {}", fileId);
            
            return new DownloadResult<>(true, file, metadata, null, null, java.time.Instant.now());
            
        } catch (IOException e) {
            logger.error("Failed to download file: {}", fileId, e);
            throw new FileStorageException("Failed to download file: " + fileId, e);
        }
    }
    
    @Override
    public <T> DownloadResult<T> download(String fileId, Class<T> type) throws FileStorageException {
        try {
            Path filePath = getFilePath(fileId);
            
            if (!Files.exists(filePath)) {
                throw new FileNotFoundException("File not found: " + fileId);
            }
            
            if (!Files.isRegularFile(filePath)) {
                throw new FileStorageException("Path is not a file: " + fileId);
            }
            
            T content = null;
            if (type == byte[].class) {
                content = type.cast(Files.readAllBytes(filePath));
            } else if (type == InputStream.class) {
                content = type.cast(new FileInputStream(filePath.toFile()));
            } else if (type == File.class) {
                content = type.cast(filePath.toFile());
            } else {
                throw new FileStorageException("Unsupported content type: " + type.getName());
            }
            
            FileMetadata metadata = getFileMetadata(filePath);
            
            logger.debug("Downloaded file: {}", fileId);
            
            return new DownloadResult<>(true, content, metadata, null, null, java.time.Instant.now());
            
        } catch (IOException e) {
            logger.error("Failed to download file: {}", fileId, e);
            throw new FileStorageException("Failed to download file: " + fileId, e);
        }
    }
    
    @Override
    public boolean delete(String fileId) throws FileStorageException {
        try {
            Path filePath = getFilePath(fileId);
            
            if (!Files.exists(filePath)) {
                logger.debug("File not found for deletion: {}", fileId);
                return false;
            }
            
            boolean deleted = Files.deleteIfExists(filePath);
            
            if (deleted) {
                logger.debug("Deleted file: {}", fileId);
            }
            
            return deleted;
            
        } catch (IOException e) {
            logger.error("Failed to delete file: {}", fileId, e);
            throw new FileStorageException("Failed to delete file: " + fileId, e);
        }
    }
    
    @Override
    public boolean exists(String fileId) throws FileStorageException {
        try {
            Path filePath = getFilePath(fileId);
            return Files.exists(filePath) && Files.isRegularFile(filePath);
        } catch (Exception e) {
            logger.error("Failed to check file existence: {}", fileId, e);
            throw new FileStorageException("Failed to check file existence: " + fileId, e);
        }
    }
    
    @Override
    public FileMetadata getMetadata(String fileId) throws FileStorageException {
        try {
            Path filePath = getFilePath(fileId);
            
            if (!Files.exists(filePath) || !Files.isRegularFile(filePath)) {
                return null;
            }
            
            return getFileMetadata(filePath);
            
        } catch (IOException e) {
            logger.error("Failed to get file metadata: {}", fileId, e);
            throw new FileStorageException("Failed to get file metadata: " + fileId, e);
        }
    }
    
    @Override
    public List<FileMetadata> listFiles(String prefix, int maxResults) throws FileStorageException {
        try {
            List<FileMetadata> files = new ArrayList<>();
            Path searchPath = prefix != null ? basePath.resolve(prefix) : basePath;
            
            if (!Files.exists(searchPath)) {
                return files;
            }
            
            try (Stream<Path> stream = Files.walk(searchPath)) {
                stream.filter(Files::isRegularFile)
                    .limit(maxResults > 0 ? maxResults : Long.MAX_VALUE)
                    .forEach(filePath -> {
                        try {
                            FileMetadata metadata = getFileMetadata(filePath);
                            files.add(metadata);
                        } catch (IOException e) {
                            logger.warn("Failed to get metadata for file: {}", filePath, e);
                        }
                    });
            }
            
            logger.debug("Listed {} files with prefix: {}", files.size(), prefix);
            return files;
            
        } catch (IOException e) {
            logger.error("Failed to list files with prefix: {}", prefix, e);
            throw new FileStorageException("Failed to list files with prefix: " + prefix, e);
        }
    }
    
    @Override
    public Map<String, Object> listFiles(String prefix, String continuationToken, int maxResults) throws FileStorageException {
        // Simple implementation - no pagination support for local storage
        List<FileMetadata> files = listFiles(prefix, maxResults);
        return Map.of(
            "files", files,
            "nextToken", null
        );
    }
    
    @Override
    public String generatePresignedUrl(String fileId, int expirationMinutes) {
        // Local storage doesn't support presigned URLs
        return null;
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
        return FileStorageConstants.LOCAL_PROVIDER_NAME;
    }
    
    @Override
    public Map<String, Boolean> getCapabilities() {
        return Map.of(
            "presigned-urls", false,
            "async-operations", true,
            "metadata", true,
            "list-files", true,
            "checksum", false
        );
    }
    
    /**
     * Gets the file path for a given file ID.
     * 
     * @param fileId the file ID
     * @return the file path
     */
    private Path getFilePath(String fileId) {
        return basePath.resolve(fileId);
    }
    
    /**
     * Ensures the parent directory of the given path exists.
     * 
     * @param filePath the file path
     * @throws IOException if the directory cannot be created
     */
    private void ensureParentDirectoryExists(Path filePath) throws IOException {
        if (createDirectories) {
            Path parent = filePath.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
                setDirectoryPermissions(parent, directoryPermissions);
            }
        }
    }
    
    /**
     * Sets file permissions on the given path.
     * 
     * @param path the file path
     * @param permissions the permissions string
     */
    private void setFilePermissions(Path path, String permissions) {
        // Implementation would set file permissions based on the permissions string
        // This is a simplified version - in a real implementation, you would
        // parse the permissions string and set the appropriate file attributes
        logger.debug("Setting file permissions {} on {}", permissions, path);
    }
    
    /**
     * Sets directory permissions on the given path.
     * 
     * @param path the directory path
     * @param permissions the permissions string
     */
    private void setDirectoryPermissions(Path path, String permissions) {
        // Implementation would set directory permissions based on the permissions string
        // This is a simplified version - in a real implementation, you would
        // parse the permissions string and set the appropriate file attributes
        logger.debug("Setting directory permissions {} on {}", permissions, path);
    }
    
    /**
     * Gets file metadata from the given path.
     * 
     * @param filePath the file path
     * @return the file metadata
     * @throws IOException if the metadata cannot be retrieved
     */
    private FileMetadata getFileMetadata(Path filePath) throws IOException {
        BasicFileAttributes attrs = Files.readAttributes(filePath, BasicFileAttributes.class);
        
        String fileName = filePath.getFileName().toString();
        String contentType = getContentType(fileName);
        long size = attrs.size();
        Instant creationTime = attrs.creationTime().toInstant();
        Instant lastModifiedTime = attrs.lastModifiedTime().toInstant();
        
        return new FileMetadata(fileName, contentType, size, creationTime, lastModifiedTime, null, Map.of());
    }
    
    /**
     * Gets the content type for a file based on its name.
     * 
     * @param fileName the file name
     * @return the content type
     */
    private String getContentType(String fileName) {
        String extension = getFileExtension(fileName);
        return switch (extension.toLowerCase()) {
            case "txt" -> "text/plain";
            case "html", "htm" -> "text/html";
            case "css" -> "text/css";
            case "js" -> "application/javascript";
            case "json" -> "application/json";
            case "xml" -> "application/xml";
            case "pdf" -> "application/pdf";
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "svg" -> "image/svg+xml";
            case "zip" -> "application/zip";
            case "tar" -> "application/x-tar";
            case "gz" -> "application/gzip";
            default -> "application/octet-stream";
        };
    }
    
    /**
     * Gets the file extension from a file name.
     * 
     * @param fileName the file name
     * @return the file extension (without the dot)
     */
    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        return lastDotIndex > 0 ? fileName.substring(lastDotIndex + 1) : "";
    }
}
