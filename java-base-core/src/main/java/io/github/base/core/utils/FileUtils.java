package io.github.base.core.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Utility class for file and I/O operations.
 * 
 * <p>This class provides safe file operations with proper resource management
 * and UTF-8 encoding. It includes helpers for reading, writing, and temporary
 * file operations commonly needed in enterprise applications.
 * 
 * <p><strong>Design Principles:</strong>
 * <ul>
 *   <li>Safe resource management with try-with-resources</li>
 *   <li>UTF-8 encoding by default</li>
 *   <li>Null-safe operations where appropriate</li>
 *   <li>Minimal dependencies - pure Java implementation</li>
 * </ul>
 * 
 * <p><strong>Example Usage:</strong>
 * <pre>{@code
 * // Read file content
 * String content = FileUtils.readFile("config.properties");
 * 
 * // Write file content
 * FileUtils.writeFile("output.txt", "Hello World");
 * 
 * // Safe resource handling
 * FileUtils.withResource(new FileInputStream("data.txt"), inputStream -> {
 *     // Process input stream
 * });
 * }</pre>
 * 
 * @since 1.0.0
 * @author java-base-core
 */
public final class FileUtils {
    
    private static final String UTF_8 = StandardCharsets.UTF_8.name();
    
    private FileUtils() {
        // Utility class - prevent instantiation
    }
    
    /**
     * Reads the entire content of a file as a string using UTF-8 encoding.
     * 
     * @param filePath the path to the file
     * @return the file content as a string, or null if file doesn't exist or cannot be read
     */
    public static String readFile(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return null;
        }
        
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                return null;
            }
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return null;
        }
    }
    
    /**
     * Reads the entire content of a file as a string using the specified encoding.
     * 
     * @param filePath the path to the file
     * @param encoding the character encoding
     * @return the file content as a string, or null if file doesn't exist or cannot be read
     */
    public static String readFile(String filePath, String encoding) {
        if (StringUtils.isBlank(filePath) || StringUtils.isBlank(encoding)) {
            return null;
        }
        
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                return null;
            }
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return null;
        }
    }
    
    /**
     * Writes content to a file using UTF-8 encoding.
     * 
     * @param filePath the path to the file
     * @param content the content to write
     * @return true if successful, false otherwise
     */
    public static boolean writeFile(String filePath, String content) {
        if (StringUtils.isBlank(filePath)) {
            return false;
        }
        
        try {
            Path path = Paths.get(filePath);
            Files.createDirectories(path.getParent());
            Files.writeString(path, content != null ? content : "", StandardCharsets.UTF_8);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    /**
     * Writes content to a file using the specified encoding.
     * 
     * @param filePath the path to the file
     * @param content the content to write
     * @param encoding the character encoding
     * @return true if successful, false otherwise
     */
    public static boolean writeFile(String filePath, String content, String encoding) {
        if (StringUtils.isBlank(filePath) || StringUtils.isBlank(encoding)) {
            return false;
        }
        
        try {
            Path path = Paths.get(filePath);
            Files.createDirectories(path.getParent());
            Files.writeString(path, content != null ? content : "", StandardCharsets.UTF_8);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    /**
     * Creates a temporary file with the specified prefix and suffix.
     * 
     * @param prefix the prefix for the temporary file name
     * @param suffix the suffix for the temporary file name
     * @return the path to the created temporary file, or null if creation fails
     */
    public static String createTempFile(String prefix, String suffix) {
        try {
            Path tempFile = Files.createTempFile(prefix, suffix);
            return tempFile.toString();
        } catch (IOException e) {
            return null;
        }
    }
    
    /**
     * Creates a temporary file with the specified prefix.
     * 
     * @param prefix the prefix for the temporary file name
     * @return the path to the created temporary file, or null if creation fails
     */
    public static String createTempFile(String prefix) {
        return createTempFile(prefix, ".tmp");
    }
    
    /**
     * Creates a temporary file with default prefix.
     * 
     * @return the path to the created temporary file, or null if creation fails
     */
    public static String createTempFile() {
        return createTempFile("java-base-", ".tmp");
    }
    
    /**
     * Safely executes a consumer with an AutoCloseable resource, ensuring proper cleanup.
     * 
     * @param resource the resource to use
     * @param consumer the consumer to execute with the resource
     * @param <T> the type of the resource
     * @return true if successful, false if an exception occurs
     */
    public static <T extends AutoCloseable> boolean withResource(T resource, Consumer<T> consumer) {
        if (resource == null || consumer == null) {
            return false;
        }
        
        try (T r = resource) {
            consumer.accept(r);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Safely executes a consumer with an InputStream, ensuring proper cleanup.
     * 
     * @param inputStream the input stream to use
     * @param consumer the consumer to execute with the input stream
     * @return true if successful, false if an exception occurs
     */
    public static boolean withInputStream(InputStream inputStream, Consumer<InputStream> consumer) {
        return withResource(inputStream, consumer);
    }
    
    /**
     * Safely executes a consumer with an OutputStream, ensuring proper cleanup.
     * 
     * @param outputStream the output stream to use
     * @param consumer the consumer to execute with the output stream
     * @return true if successful, false if an exception occurs
     */
    public static boolean withOutputStream(OutputStream outputStream, Consumer<OutputStream> consumer) {
        return withResource(outputStream, consumer);
    }
    
    /**
     * Safely executes a consumer with a Reader, ensuring proper cleanup.
     * 
     * @param reader the reader to use
     * @param consumer the consumer to execute with the reader
     * @return true if successful, false if an exception occurs
     */
    public static boolean withReader(Reader reader, Consumer<Reader> consumer) {
        return withResource(reader, consumer);
    }
    
    /**
     * Safely executes a consumer with a Writer, ensuring proper cleanup.
     * 
     * @param writer the writer to use
     * @param consumer the consumer to execute with the writer
     * @return true if successful, false if an exception occurs
     */
    public static boolean withWriter(Writer writer, Consumer<Writer> consumer) {
        return withResource(writer, consumer);
    }
    
    /**
     * Checks if a file exists.
     * 
     * @param filePath the path to the file
     * @return true if the file exists, false otherwise
     */
    public static boolean exists(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return false;
        }
        
        try {
            return Files.exists(Paths.get(filePath));
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Deletes a file if it exists.
     * 
     * @param filePath the path to the file
     * @return true if the file was deleted or didn't exist, false if deletion failed
     */
    public static boolean delete(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return true;
        }
        
        try {
            Path path = Paths.get(filePath);
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            return false;
        }
    }
    
    /**
     * Gets the file size in bytes.
     * 
     * @param filePath the path to the file
     * @return the file size in bytes, or -1 if file doesn't exist or cannot be read
     */
    public static long getFileSize(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return -1;
        }
        
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                return -1;
            }
            return Files.size(path);
        } catch (IOException e) {
            return -1;
        }
    }
}
