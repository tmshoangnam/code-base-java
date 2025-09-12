package io.github.base.file.api.model;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

/**
 * Immutable record representing file metadata.
 * 
 * <p>This record contains essential information about a file including
 * its name, content type, size, and other metadata properties.
 * 
 * @param fileName the original file name
 * @param contentType MIME type of the file content
 * @param size file size in bytes
 * @param createdAt timestamp when the file was created
 * @param lastModified timestamp when the file was last modified
 * @param checksum file checksum for integrity verification
 * @param properties additional custom properties
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public record FileMetadata(
    String fileName,
    String contentType,
    long size,
    Instant createdAt,
    Instant lastModified,
    String checksum,
    Map<String, String> properties
) {
    
    /**
     * Creates a FileMetadata with current timestamp for creation and modification.
     * 
     * @param fileName the original file name
     * @param contentType MIME type of the file content
     * @param size file size in bytes
     * @return new FileMetadata instance
     * @throws IllegalArgumentException if fileName is null or empty, or size < 0
     */
    public static FileMetadata of(String fileName, String contentType, long size) {
        return of(fileName, contentType, size, null, null);
    }
    
    /**
     * Creates a FileMetadata with specified timestamps.
     * 
     * @param fileName the original file name
     * @param contentType MIME type of the file content
     * @param size file size in bytes
     * @param createdAt timestamp when the file was created
     * @param lastModified timestamp when the file was last modified
     * @return new FileMetadata instance
     * @throws IllegalArgumentException if fileName is null or empty, or size < 0
     */
    public static FileMetadata of(String fileName, String contentType, long size, 
                                 Instant createdAt, Instant lastModified) {
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("File name cannot be null or empty");
        }
        if (size < 0) {
            throw new IllegalArgumentException("File size cannot be negative");
        }
        
        Instant now = Instant.now();
        return new FileMetadata(
            fileName.trim(),
            contentType,
            size,
            createdAt != null ? createdAt : now,
            lastModified != null ? lastModified : now,
            null,
            Map.of()
        );
    }
    
    /**
     * Creates a FileMetadata with checksum.
     * 
     * @param fileName the original file name
     * @param contentType MIME type of the file content
     * @param size file size in bytes
     * @param checksum file checksum for integrity verification
     * @return new FileMetadata instance
     * @throws IllegalArgumentException if fileName is null or empty, or size < 0
     */
    public static FileMetadata of(String fileName, String contentType, long size, String checksum) {
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("File name cannot be null or empty");
        }
        if (size < 0) {
            throw new IllegalArgumentException("File size cannot be negative");
        }
        
        Instant now = Instant.now();
        return new FileMetadata(
            fileName.trim(),
            contentType,
            size,
            now,
            now,
            checksum,
            Map.of()
        );
    }
    
    /**
     * Creates a copy of this FileMetadata with updated properties.
     * 
     * @param properties new properties map
     * @return new FileMetadata instance with updated properties
     */
    public FileMetadata withProperties(Map<String, String> properties) {
        return new FileMetadata(
            this.fileName,
            this.contentType,
            this.size,
            this.createdAt,
            this.lastModified,
            this.checksum,
            properties != null ? Map.copyOf(properties) : Map.of()
        );
    }
    
    /**
     * Creates a copy of this FileMetadata with updated checksum.
     * 
     * @param checksum new checksum value
     * @return new FileMetadata instance with updated checksum
     */
    public FileMetadata withChecksum(String checksum) {
        return new FileMetadata(
            this.fileName,
            this.contentType,
            this.size,
            this.createdAt,
            this.lastModified,
            checksum,
            this.properties
        );
    }
    
    /**
     * Gets a property value by key.
     * 
     * @param key property key
     * @return property value or null if not found
     */
    public String getProperty(String key) {
        return properties != null ? properties.get(key) : null;
    }
    
    /**
     * Checks if a property exists.
     * 
     * @param key property key
     * @return true if property exists, false otherwise
     */
    public boolean hasProperty(String key) {
        return properties != null && properties.containsKey(key);
    }
    
    /**
     * Gets the file extension from the file name.
     * 
     * @return file extension (without dot) or empty string if no extension
     */
    public String getFileExtension() {
        if (fileName == null) {
            return "";
        }
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(lastDotIndex + 1).toLowerCase();
    }
    
    /**
     * Checks if the file has a specific extension.
     * 
     * @param extension extension to check (case-insensitive)
     * @return true if file has the specified extension, false otherwise
     */
    public boolean hasExtension(String extension) {
        if (extension == null || extension.trim().isEmpty()) {
            return false;
        }
        return getFileExtension().equals(extension.toLowerCase().trim());
    }
    
    /**
     * Gets the file name without extension.
     * 
     * @return file name without extension
     */
    public String getFileNameWithoutExtension() {
        if (fileName == null) {
            return "";
        }
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return fileName;
        }
        return fileName.substring(0, lastDotIndex);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        FileMetadata that = (FileMetadata) obj;
        return size == that.size &&
               Objects.equals(fileName, that.fileName) &&
               Objects.equals(contentType, that.contentType) &&
               Objects.equals(createdAt, that.createdAt) &&
               Objects.equals(lastModified, that.lastModified) &&
               Objects.equals(checksum, that.checksum) &&
               Objects.equals(properties, that.properties);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(fileName, contentType, size, createdAt, lastModified, checksum, properties);
    }
    
    @Override
    public String toString() {
        return "FileMetadata{" +
               "fileName='" + fileName + '\'' +
               ", contentType='" + contentType + '\'' +
               ", size=" + size +
               ", createdAt=" + createdAt +
               ", lastModified=" + lastModified +
               ", checksum='" + checksum + '\'' +
               ", properties=" + properties +
               '}';
    }
}
