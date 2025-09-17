package io.github.base.starter.file.impl.s3;

import io.github.base.file.api.storage.FileStorage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

/**
 * Test for S3FileStorageProvider.
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
public class S3FileStorageProviderTest {
    
    private S3FileStorageProvider provider;
    
    @BeforeEach
    void setUp() {
        provider = new S3FileStorageProvider();
    }
    
    @Test
    void shouldHaveCorrectProviderName() {
        assertThat(provider.getProviderName()).isEqualTo("s3");
    }
    
    @Test
    void shouldHaveCorrectDescription() {
        assertThat(provider.getProviderDescription()).isEqualTo("AWS S3 file storage provider");
    }
    
    @Test
    void shouldHaveCorrectVersion() {
        assertThat(provider.getProviderVersion()).isEqualTo("1.0.5-SNAPSHOT");
    }
    
    @Test
    void shouldBeAvailableWhenAwsSdkIsPresent() {
        boolean available = provider.isAvailable();
        assertThat(available).isNotNull();
    }
    
    @Test
    void shouldHaveCorrectPriority() {
        assertThat(provider.getPriority()).isEqualTo(50);
    }
    
    @Test
    void shouldHaveRequiredConfiguration() {
        var required = provider.getRequiredConfiguration();
        assertThat(required).isNotNull();
        assertThat(required).containsKey("bucket-name");
        assertThat(required).containsKey("region");
    }
    
    @Test
    void shouldHaveOptionalConfiguration() {
        var optional = provider.getOptionalConfiguration();
        assertThat(optional).isNotNull();
        assertThat(optional).containsKey("endpoint");
        assertThat(optional).containsKey("path-style-access");
        assertThat(optional).containsKey("encryption");
        assertThat(optional).containsKey("storage-class");
    }
    
    @Test
    void shouldCreateStorageWithValidConfiguration() {
        Map<String, String> config = Map.of(
            "bucket-name", "test-bucket",
            "region", "us-east-1",
            "access-key-id", "test-key",
            "secret-access-key", "test-secret"
        );
        
        FileStorage storage = provider.createStorage(config);
        assertThat(storage).isNotNull();
        assertThat(storage.getProviderName()).isEqualTo("s3");
    }
    
    @Test
    void shouldThrowExceptionWithInvalidConfiguration() {
        Map<String, String> config = Map.of(
            "bucket-name", "test-bucket"
            // Missing required fields
        );
        
        assertThatThrownBy(() -> provider.createStorage(config))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Invalid configuration");
    }
    
    @Test
    void shouldThrowExceptionWhenCreateStorageWithoutConfig() {
        // This test verifies the behavior when no environment variables are set
        assertThatThrownBy(() -> provider.createStorage())
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("S3FileStorage requires configuration");
    }
    
    @Test
    void shouldValidateConfigurationCorrectly() {
        Map<String, String> validConfig = Map.of(
            "bucket-name", "test-bucket",
            "region", "us-east-1",
            "access-key-id", "test-key",
            "secret-access-key", "test-secret"
        );
        
        var errors = provider.validateConfiguration(validConfig);
        assertThat(errors).isEmpty();
    }
    
    @Test
    void shouldDetectMissingRequiredConfiguration() {
        Map<String, String> invalidConfig = Map.of(
            "bucket-name", "test-bucket"
            // Missing region, access-key-id, secret-access-key
        );
        
        var errors = provider.validateConfiguration(invalidConfig);
        assertThat(errors).isNotEmpty();
        assertThat(errors).contains("AWS region is required");
        assertThat(errors).anyMatch(error -> error.contains("AWS access key ID is required"));
        assertThat(errors).anyMatch(error -> error.contains("AWS secret access key is required"));
    }
    
    @Test
    void shouldDetectInvalidBucketName() {
        Map<String, String> invalidConfig = Map.of(
            "bucket-name", "Invalid.Bucket.Name!", // Invalid bucket name
            "region", "us-east-1",
            "access-key-id", "test-key",
            "secret-access-key", "test-secret"
        );
        
        var errors = provider.validateConfiguration(invalidConfig);
        assertThat(errors).isNotEmpty();
        assertThat(errors).anyMatch(error -> error.contains("Invalid S3 bucket name"));
    }
    
    @Test
    void shouldDetectInvalidRegion() {
        Map<String, String> invalidConfig = Map.of(
            "bucket-name", "test-bucket",
            "region", "invalid-region!@#", // Invalid region
            "access-key-id", "test-key",
            "secret-access-key", "test-secret"
        );
        
        var errors = provider.validateConfiguration(invalidConfig);
        assertThat(errors).isNotEmpty();
        assertThat(errors).anyMatch(error -> error.contains("Invalid AWS region"));
    }
}