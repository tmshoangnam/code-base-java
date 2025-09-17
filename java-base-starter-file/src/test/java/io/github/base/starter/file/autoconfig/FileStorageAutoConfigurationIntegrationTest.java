package io.github.base.starter.file.autoconfig;

import io.github.base.file.api.storage.FileStorage;
import io.github.base.file.api.storage.FileStorageManager;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;

/**
 * Integration test for FileStorageAutoConfiguration with S3.
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
@SpringBootTest(classes = {FileStorageAutoConfiguration.class})
@TestPropertySource(properties = {
    "base.file.storage.enabled=true",
    "base.file.storage.default-provider=s3",
    "base.file.storage.providers.s3.type=s3",
    "base.file.storage.providers.s3.enabled=true",
    "base.file.storage.providers.s3.priority=100",
    "base.file.storage.providers.s3.properties.bucket-name=test-bucket",
    "base.file.storage.providers.s3.properties.region=us-east-1",
    "base.file.storage.providers.s3.properties.access-key-id=test-access-key",
    "base.file.storage.providers.s3.properties.secret-access-key=test-secret-key"
})
public class FileStorageAutoConfigurationIntegrationTest {
    
    @Autowired
    private FileStorageManager fileStorageManager;
    
    @Autowired
    private FileStorage fileStorage;
    
    @BeforeEach
    void setUp() {
        // Clear any caches
    }
    
    @AfterEach
    void tearDown() {
        // Cleanup
    }
    
    @Test
    void shouldCreateFileStorageManager() {
        assertThat(fileStorageManager).isNotNull();
    }
    
    @Test
    void shouldCreateFileStorage() {
        assertThat(fileStorage).isNotNull();
    }
    
    @Test
    void shouldHaveS3AsDefaultProvider() {
        assertThat(fileStorageManager.getDefaultProvider()).isEqualTo("s3");
    }
    
    @Test
    void shouldHaveS3ProviderAvailable() {
        var availableProviders = fileStorageManager.getAvailableProviders();
        assertThat(availableProviders).contains("s3");
    }
    
    @Test
    void shouldGetS3Storage() {
        FileStorage s3Storage = fileStorageManager.getStorage("s3");
        assertThat(s3Storage).isNotNull();
        assertThat(s3Storage.getProviderName()).isEqualTo("s3");
    }
    
    @Test
    void shouldNotThrowExceptionWhenGettingS3Storage() {
        // This test verifies that the "S3FileStorage requires configuration" error is fixed
        assertThatCode(() -> {
            FileStorage s3Storage = fileStorageManager.getStorage("s3");
            assertThat(s3Storage).isNotNull();
        }).doesNotThrowAnyException();
    }
}