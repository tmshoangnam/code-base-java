package io.github.base.starter.file.constants;

/**
 * Constants for file storage services.
 * 
 * <p>This class contains common constants used across file storage
 * implementations including version numbers, provider names, and
 * default configuration values.
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public final class FileStorageConstants {
    
    /**
     * Current version of the file storage module.
     */
    public static final String VERSION = "1.0.5-SNAPSHOT";
    
    /**
     * Default local provider name.
     */
    public static final String LOCAL_PROVIDER_NAME = "local";
    
    /**
     * Default S3 provider name.
     */
    public static final String S3_PROVIDER_NAME = "s3";
    
    /**
     * Default GCS provider name.
     */
    public static final String GCS_PROVIDER_NAME = "gcs";
    
    /**
     * Default Azure provider name.
     */
    public static final String AZURE_PROVIDER_NAME = "azure";
    
    /**
     * Default AWS region.
     */
    public static final String DEFAULT_AWS_REGION = "us-east-1";
    
    /**
     * Default local storage base path.
     */
    public static final String DEFAULT_LOCAL_BASE_PATH = System.getProperty("java.io.tmpdir") + "/file-storage";
    
    /**
     * Default cache TTL in seconds.
     */
    public static final long DEFAULT_CACHE_TTL_SECONDS = 300;
    
    /**
     * Default maximum cache size.
     */
    public static final int DEFAULT_MAX_CACHE_SIZE = 1000;
    
    /**
     * Default local provider priority.
     */
    public static final int DEFAULT_LOCAL_PRIORITY = 100;
    
    /**
     * Default cloud provider priority.
     */
    public static final int DEFAULT_CLOUD_PRIORITY = 50;
    
    /**
     * Private constructor to prevent instantiation.
     */
    private FileStorageConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
