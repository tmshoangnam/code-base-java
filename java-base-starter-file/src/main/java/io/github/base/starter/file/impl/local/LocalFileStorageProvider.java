package io.github.base.starter.file.impl.local;

import io.github.base.file.api.storage.FileStorage;
import io.github.base.file.spi.FileStorageProvider;
import io.github.base.starter.file.constants.FileStorageConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Local file storage provider implementation.
 * 
 * <p>This provider implements file storage using the local filesystem.
 * It supports all standard file operations and provides a simple
 * local storage solution for development and testing.
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public class LocalFileStorageProvider implements FileStorageProvider {
    
    private static final Logger logger = LoggerFactory.getLogger(LocalFileStorageProvider.class);
    
    private static final String PROVIDER_NAME = FileStorageConstants.LOCAL_PROVIDER_NAME;
    private static final String BASE_PATH_PROPERTY = "base-path";
    private static final String DEFAULT_BASE_PATH = FileStorageConstants.DEFAULT_LOCAL_BASE_PATH;
    
    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }
    
    @Override
    public String getProviderDescription() {
        return "Local filesystem storage provider";
    }
    
    @Override
    public String getProviderVersion() {
        return FileStorageConstants.VERSION;
    }
    
    @Override
    public boolean isAvailable() {
        try {
            // Check if we can create a temporary file
            Path tempFile = Files.createTempFile("file-storage-test", ".tmp");
            Files.deleteIfExists(tempFile);
            return true;
        } catch (Exception e) {
            logger.warn("Local file storage provider is not available", e);
            return false;
        }
    }
    
    @Override
    public int getPriority() {
        return FileStorageConstants.DEFAULT_LOCAL_PRIORITY; // High priority for local storage
    }
    
    @Override
    public Map<String, String> getRequiredConfiguration() {
        return Map.of(
            BASE_PATH_PROPERTY, "Base path for file storage (e.g., /tmp/files)"
        );
    }
    
    @Override
    public Map<String, String> getOptionalConfiguration() {
        return Map.of(
            "create-directories", "Whether to create directories automatically (default: true)",
            "file-permissions", "File permissions (default: 644)",
            "directory-permissions", "Directory permissions (default: 755)"
        );
    }
    
    @Override
    public FileStorage createStorage() {
        String basePath = DEFAULT_BASE_PATH;
        boolean createDirectories = true;
        String filePermissions = "644";
        String directoryPermissions = "755";
        
        logger.info("Creating local file storage with base path: {}", basePath);
        
        return new LocalFileStorage(
            Paths.get(basePath),
            createDirectories,
            filePermissions,
            directoryPermissions
        );
    }
    
    @Override
    public Map<String, Boolean> getCapabilities() {
        return Map.of(
            "upload", true,
            "download", true,
            "delete", true,
            "exists", true,
            "metadata", true,
            "list", true,
            "streaming", true,
            "concurrent", true
        );
    }
    
    @Override
    public Map<String, String> getLimitations() {
        return Map.of(
            "max-file-size", "Limited by available disk space",
            "max-files", "Limited by available disk space",
            "replication", "false",
            "encryption", "false",
            "versioning", "false"
        );
    }
    
    @Override
    public List<String> validateConfiguration(Map<String, String> configuration) {
        List<String> errors = new ArrayList<>();
        
        if (configuration == null) {
            return errors; // Use defaults
        }
        
        String basePath = configuration.get(BASE_PATH_PROPERTY);
        if (basePath != null) {
            try {
                Path path = Paths.get(basePath);
                if (!Files.exists(path)) {
                    // Check if parent directory exists and is writable
                    Path parent = path.getParent();
                    if (parent == null || !Files.exists(parent)) {
                        errors.add("Base path parent directory does not exist: " + basePath);
                    } else if (!Files.isWritable(parent)) {
                        errors.add("Base path parent directory is not writable: " + basePath);
                    }
                } else if (!Files.isDirectory(path)) {
                    errors.add("Base path is not a directory: " + basePath);
                } else if (!Files.isWritable(path)) {
                    errors.add("Base path is not writable: " + basePath);
                }
            } catch (Exception e) {
                errors.add("Invalid base path: " + basePath + " - " + e.getMessage());
            }
        }
        
        // Validate permissions
        String filePermissions = configuration.get("file-permissions");
        if (filePermissions != null && !isValidPermissions(filePermissions)) {
            errors.add("Invalid file permissions: " + filePermissions);
        }
        
        String directoryPermissions = configuration.get("directory-permissions");
        if (directoryPermissions != null && !isValidPermissions(directoryPermissions)) {
            errors.add("Invalid directory permissions: " + directoryPermissions);
        }
        
        return errors;
    }
    
    /**
     * Validates Unix-style permissions string.
     * 
     * @param permissions the permissions string
     * @return true if valid
     */
    private boolean isValidPermissions(String permissions) {
        if (permissions == null || permissions.length() != 3) {
            return false;
        }
        
        try {
            for (char c : permissions.toCharArray()) {
                int digit = Character.digit(c, 8);
                if (digit < 0 || digit > 7) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
