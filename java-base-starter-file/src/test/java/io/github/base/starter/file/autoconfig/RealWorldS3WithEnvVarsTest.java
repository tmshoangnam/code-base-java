package io.github.base.starter.file.autoconfig;

import io.github.base.file.api.storage.FileStorage;
import io.github.base.file.api.storage.FileStorageManager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;

/**
 * Test for S3 configuration when environment variables are not set.
 * This simulates the real-world scenario where Spring Boot cannot resolve ${AWS_S3_BUCKET_NAME:...}
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
    "base.file.storage.providers.s3.properties.bucket-name=${AWS_S3_BUCKET_NAME:logidaily-bucket--apne1-az4--x-s3}",
    "base.file.storage.providers.s3.properties.region=${AWS_REGION:ap-northeast-1}",
    "base.file.storage.providers.s3.properties.access-key-id=${AWS_ACCESS_KEY_ID:AKIAQIUTXSYUMUR65VUK}",
    "base.file.storage.providers.s3.properties.secret-access-key=${AWS_SECRET_ACCESS_KEY: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}"
})
public class RealWorldS3WithEnvVarsTest {
    
    @Autowired
    private FileStorageManager fileStorageManager;
    
    @Autowired
    private FileStorage fileStorage;
    
    @Test
    void shouldNotFallbackToLocalWhenS3ConfigIsValid() {
        // This test verifies that when S3 configuration is valid (even with env var placeholders),
        // it should not fallback to Local storage
        
        assertThatCode(() -> {
            FileStorage s3Storage = fileStorageManager.getStorage("s3");
            assertThat(s3Storage).isNotNull();
            assertThat(s3Storage.getProviderName()).isEqualTo("s3");
        }).doesNotThrowAnyException();
    }
    
    @Test
    void shouldUseS3AsDefaultProvider() {
        assertThat(fileStorage).isNotNull();
        assertThat(fileStorage.getProviderName()).isEqualTo("s3");
    }
    
    @Test
    void shouldHaveCorrectDefaultProvider() {
        assertThat(fileStorageManager.getDefaultProvider()).isEqualTo("s3");
    }
    
    @Test
    void shouldHaveS3ProviderAvailable() {
        var availableProviders = fileStorageManager.getAvailableProviders();
        assertThat(availableProviders).contains("s3");
    }
    
    @Test
    void shouldNotHaveLocalAsDefaultWhenS3IsConfigured() {
        // When S3 is properly configured, it should not fallback to local
        assertThat(fileStorageManager.getDefaultProvider()).isNotEqualTo("local");
    }
}
