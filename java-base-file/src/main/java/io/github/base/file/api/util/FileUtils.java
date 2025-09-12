package io.github.base.file.api.util;

import io.github.base.file.api.model.FileMetadata;
import io.github.base.file.api.model.FileType;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

/**
 * Utility class for file operations and validation.
 *
 * <p>This class provides common file operations, validation methods, and
 * utility functions that are used across the file storage system.
 *
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public final class FileUtils {

    private static final String DEFAULT_CHECKSUM_ALGORITHM = "SHA-256";
    private static final int BUFFER_SIZE = 8192;

    private FileUtils() {
        // Utility class - prevent instantiation
    }

    /**
     * Generates a unique file ID based on timestamp and random data.
     *
     * @return a unique file ID
     */
    public static String generateFileId() {
        return "file_" + System.currentTimeMillis() + "_" + (int) (Math.random() * 10000);
    }

    /**
     * Generates a file ID with a custom prefix.
     *
     * @param prefix the prefix for the file ID
     * @return a unique file ID with the specified prefix
     * @throws IllegalArgumentException if prefix is null or empty
     */
    public static String generateFileId(String prefix) {
        if (prefix == null || prefix.trim().isEmpty()) {
            throw new IllegalArgumentException("Prefix cannot be null or empty");
        }
        return prefix.trim() + "_" + System.currentTimeMillis() + "_" + (int) (Math.random() * 10000);
    }

    /**
     * Calculates the SHA-256 checksum of a file.
     *
     * @param file the file to calculate checksum for
     * @return the SHA-256 checksum as a hexadecimal string
     * @throws IllegalArgumentException if file is null or does not exist
     * @throws RuntimeException if checksum calculation fails
     */
    public static String calculateChecksum(File file) {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        if (!file.exists()) {
            throw new IllegalArgumentException("File does not exist: " + file.getAbsolutePath());
        }

        try {
            return calculateChecksum(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to calculate checksum for file: " + file.getAbsolutePath(), e);
        }
    }

    /**
     * Calculates the SHA-256 checksum of byte array data.
     *
     * @param data the data to calculate checksum for
     * @return the SHA-256 checksum as a hexadecimal string
     * @throws IllegalArgumentException if data is null
     * @throws RuntimeException if checksum calculation fails
     */
    public static String calculateChecksum(byte[] data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }

        try {
            MessageDigest digest = MessageDigest.getInstance(DEFAULT_CHECKSUM_ALGORITHM);
            byte[] hash = digest.digest(data);
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    /**
     * Calculates the SHA-256 checksum of input stream data.
     *
     * @param inputStream the input stream to calculate checksum for
     * @return the SHA-256 checksum as a hexadecimal string
     * @throws IllegalArgumentException if inputStream is null
     * @throws RuntimeException if checksum calculation fails
     */
    public static String calculateChecksum(InputStream inputStream) {
        if (inputStream == null) {
            throw new IllegalArgumentException("InputStream cannot be null");
        }

        try {
            MessageDigest digest = MessageDigest.getInstance(DEFAULT_CHECKSUM_ALGORITHM);
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }

            byte[] hash = digest.digest();
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        } catch (IOException e) {
            throw new RuntimeException("Failed to calculate checksum from input stream", e);
        }
    }

    /**
     * Creates FileMetadata from a File object.
     *
     * @param file the file to create metadata for
     * @return FileMetadata instance
     * @throws IllegalArgumentException if file is null or does not exist
     * @throws RuntimeException if metadata creation fails
     */
    public static FileMetadata createMetadata(File file) {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        if (!file.exists()) {
            throw new IllegalArgumentException("File does not exist: " + file.getAbsolutePath());
        }

        try {
            String fileName = file.getName();
            String contentType = Files.probeContentType(file.toPath());
            long size = file.length();
            Instant lastModified = Instant.ofEpochMilli(file.lastModified());

            return FileMetadata.of(fileName, contentType, size, lastModified, lastModified);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create metadata for file: " + file.getAbsolutePath(), e);
        }
    }

    /**
     * Creates FileMetadata from byte array data.
     *
     * @param data the data to create metadata for
     * @param fileName the name of the file
     * @param contentType the MIME type of the content
     * @return FileMetadata instance
     * @throws IllegalArgumentException if data is null, or fileName is null or empty
     */
    public static FileMetadata createMetadata(byte[] data, String fileName, String contentType) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("File name cannot be null or empty");
        }

        return FileMetadata.of(fileName.trim(), contentType, data.length);
    }

    /**
     * Creates FileMetadata from input stream data.
     *
     * @param inputStream the input stream to create metadata for
     * @param fileName the name of the file
     * @param contentType the MIME type of the content
     * @param size the size of the data in bytes
     * @return FileMetadata instance
     * @throws IllegalArgumentException if inputStream is null, or fileName is null or empty, or size < 0
     */
    public static FileMetadata createMetadata(InputStream inputStream, String fileName, String contentType, long size) {
        if (inputStream == null) {
            throw new IllegalArgumentException("InputStream cannot be null");
        }
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("File name cannot be null or empty");
        }
        if (size < 0) {
            throw new IllegalArgumentException("Size cannot be negative");
        }

        return FileMetadata.of(fileName.trim(), contentType, size);
    }

    /**
     * Validates a file ID.
     *
     * @param fileId the file ID to validate
     * @return true if file ID is valid, false otherwise
     */
    public static boolean isValidFileId(String fileId) {
        return fileId != null && !fileId.trim().isEmpty() && fileId.length() <= 255;
    }

    /**
     * Validates a file name.
     *
     * @param fileName the file name to validate
     * @return true if file name is valid, false otherwise
     */
    public static boolean isValidFileName(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return false;
        }

        String trimmed = fileName.trim();
        if (trimmed.length() > 255) {
            return false;
        }

        // Check for invalid characters
        return !trimmed.contains("..") &&
               !trimmed.contains("/") &&
               !trimmed.contains("\\") &&
               !trimmed.contains(":") &&
               !trimmed.contains("*") &&
               !trimmed.contains("?") &&
               !trimmed.contains("\"") &&
               !trimmed.contains("<") &&
               !trimmed.contains(">") &&
               !trimmed.contains("|");
    }

    /**
     * Gets the file extension from a file name.
     *
     * @param fileName the file name
     * @return the file extension (without dot) or empty string if no extension
     */
    public static String getFileExtension(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return "";
        }

        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
            return "";
        }

        return fileName.substring(lastDotIndex + 1).toLowerCase();
    }

    /**
     * Gets the file name without extension.
     *
     * @param fileName the file name
     * @return the file name without extension
     */
    public static String getFileNameWithoutExtension(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return "";
        }

        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return fileName;
        }

        return fileName.substring(0, lastDotIndex);
    }

    /**
     * Determines the file type based on the input object.
     *
     * @param input the input object
     * @return the corresponding FileType
     * @throws IllegalArgumentException if input is null or unsupported type
     */
    public static FileType determineFileType(Object input) {
        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }

        if (input instanceof File) {
            return FileType.FILE;
        } else if (input instanceof byte[]) {
            return FileType.BYTE;
        } else if (input instanceof InputStream) {
            return FileType.STREAM;
        } else {
            throw new IllegalArgumentException("Unsupported input type: " + input.getClass().getName());
        }
    }

    /**
     * Converts bytes to hexadecimal string.
     *
     * @param bytes the bytes to convert
     * @return hexadecimal string representation
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    /**
     * Gets the default checksum algorithm.
     *
     * @return the default checksum algorithm name
     */
    public static String getDefaultChecksumAlgorithm() {
        return DEFAULT_CHECKSUM_ALGORITHM;
    }

    /**
     * Gets the default buffer size for I/O operations.
     *
     * @return the default buffer size in bytes
     */
    public static int getDefaultBufferSize() {
        return BUFFER_SIZE;
    }
}
