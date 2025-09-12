package io.github.base.file.api.util;

import io.github.base.file.api.model.FileMetadata;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for validating FileMetadata objects.
 *
 * <p>This class provides validation methods for FileMetadata objects to ensure
 * they meet the required criteria for file storage operations.
 *
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public final class FileMetadataValidator {

    private static final long MAX_FILE_SIZE = 5L * 1024 * 1024 * 1024; // 5GB
    private static final int MAX_FILE_NAME_LENGTH = 255;
    private static final int MAX_CONTENT_TYPE_LENGTH = 100;

    private FileMetadataValidator() {
        // Utility class - prevent instantiation
    }

    /**
     * Validates a FileMetadata object.
     *
     * @param metadata the metadata to validate
     * @return list of validation errors (empty if valid)
     * @throws IllegalArgumentException if metadata is null
     */
    public static List<String> validate(FileMetadata metadata) {
        if (metadata == null) {
            throw new IllegalArgumentException("Metadata cannot be null");
        }

        List<String> errors = new ArrayList<>();

        // Validate file name
        if (metadata.fileName() == null || metadata.fileName().trim().isEmpty()) {
            errors.add("File name cannot be null or empty");
        } else if (metadata.fileName().length() > MAX_FILE_NAME_LENGTH) {
            errors.add("File name cannot exceed " + MAX_FILE_NAME_LENGTH + " characters");
        } else if (!FileUtils.isValidFileName(metadata.fileName())) {
            errors.add("File name contains invalid characters");
        }

        // Validate content type
        if (metadata.contentType() != null && metadata.contentType().length() > MAX_CONTENT_TYPE_LENGTH) {
            errors.add("Content type cannot exceed " + MAX_CONTENT_TYPE_LENGTH + " characters");
        }

        // Validate file size
        if (metadata.size() < 0) {
            errors.add("File size cannot be negative");
        } else if (metadata.size() > MAX_FILE_SIZE) {
            errors.add("File size cannot exceed " + MAX_FILE_SIZE + " bytes");
        }

        // Validate timestamps
        if (metadata.createdAt() == null) {
            errors.add("Created at timestamp cannot be null");
        }

        if (metadata.lastModified() == null) {
            errors.add("Last modified timestamp cannot be null");
        }

        if (metadata.createdAt() != null && metadata.lastModified() != null && metadata.createdAt().isAfter(metadata.lastModified())) {
                errors.add("Created at timestamp cannot be after last modified timestamp");
            }


        // Validate checksum
        if (metadata.checksum() != null && metadata.checksum().trim().isEmpty()) {
            errors.add("Checksum cannot be empty if provided");
        }

        // Validate properties
        if (metadata.properties() != null) {
            for (var entry : metadata.properties().entrySet()) {
                if (entry.getKey() == null || entry.getKey().trim().isEmpty()) {
                    errors.add("Property key cannot be null or empty");
                }
                if (entry.getValue() == null) {
                    errors.add("Property value cannot be null for key: " + entry.getKey());
                }
            }
        }

        return errors;
    }

    /**
     * Validates a FileMetadata object and throws an exception if invalid.
     *
     * @param metadata the metadata to validate
     * @throws IllegalArgumentException if metadata is invalid
     */
    public static void validateOrThrow(FileMetadata metadata) {
        List<String> errors = validate(metadata);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Invalid metadata: " + String.join(", ", errors));
        }
    }

    /**
     * Checks if a FileMetadata object is valid.
     *
     * @param metadata the metadata to check
     * @return true if valid, false otherwise
     */
    public static boolean isValid(FileMetadata metadata) {
        if (metadata == null) {
            return false;
        }

        return validate(metadata).isEmpty();
    }

    /**
     * Validates file name.
     *
     * @param fileName the file name to validate
     * @return list of validation errors (empty if valid)
     */
    public static List<String> validateFileName(String fileName) {
        List<String> errors = new ArrayList<>();

        if (fileName == null || fileName.trim().isEmpty()) {
            errors.add("File name cannot be null or empty");
        } else if (fileName.length() > MAX_FILE_NAME_LENGTH) {
            errors.add("File name cannot exceed " + MAX_FILE_NAME_LENGTH + " characters");
        } else if (!FileUtils.isValidFileName(fileName)) {
            errors.add("File name contains invalid characters");
        }

        return errors;
    }

    /**
     * Validates content type.
     *
     * @param contentType the content type to validate
     * @return list of validation errors (empty if valid)
     */
    public static List<String> validateContentType(String contentType) {
        List<String> errors = new ArrayList<>();

        if (contentType != null) {
            if (contentType.length() > MAX_CONTENT_TYPE_LENGTH) {
                errors.add("Content type cannot exceed " + MAX_CONTENT_TYPE_LENGTH + " characters");
            }

            // Basic MIME type validation
            if (!contentType.matches("^[a-zA-Z0-9][a-zA-Z0-9!#$&^_-]*/[a-zA-Z0-9][a-zA-Z0-9!#$&^_-]*$")) {
                errors.add("Invalid content type format");
            }
        }

        return errors;
    }

    /**
     * Validates file size.
     *
     * @param size the file size to validate
     * @return list of validation errors (empty if valid)
     */
    public static List<String> validateFileSize(long size) {
        List<String> errors = new ArrayList<>();

        if (size < 0) {
            errors.add("File size cannot be negative");
        } else if (size > MAX_FILE_SIZE) {
            errors.add("File size cannot exceed " + MAX_FILE_SIZE + " bytes");
        }

        return errors;
    }

    /**
     * Validates checksum.
     *
     * @param checksum the checksum to validate
     * @return list of validation errors (empty if valid)
     */
    public static List<String> validateChecksum(String checksum) {
        List<String> errors = new ArrayList<>();

        if (checksum != null) {
            if (checksum.trim().isEmpty()) {
                errors.add("Checksum cannot be empty if provided");
            } else if (!checksum.matches("^[a-fA-F0-9]+$")) {
                errors.add("Checksum must contain only hexadecimal characters");
            }
        }

        return errors;
    }

    /**
     * Gets the maximum allowed file size.
     *
     * @return maximum file size in bytes
     */
    public static long getMaxFileSize() {
        return MAX_FILE_SIZE;
    }

    /**
     * Gets the maximum allowed file name length.
     *
     * @return maximum file name length in characters
     */
    public static int getMaxFileNameLength() {
        return MAX_FILE_NAME_LENGTH;
    }

    /**
     * Gets the maximum allowed content type length.
     *
     * @return maximum content type length in characters
     */
    public static int getMaxContentTypeLength() {
        return MAX_CONTENT_TYPE_LENGTH;
    }
}
