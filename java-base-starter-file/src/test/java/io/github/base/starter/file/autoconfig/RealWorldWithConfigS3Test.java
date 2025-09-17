package io.github.base.starter.file.autoconfig;

import io.github.base.file.api.storage.FileStorage;
import io.github.base.file.api.storage.FileStorageManager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;

/**
 * Test for S3 configuration when explicit configuration is provided in Spring context.
 * This simulates the real-world scenario where configuration is properly loaded from application.yml.
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
    "base.file.storage.providers.s3.properties.bucket-name=logidaily-bucket--apne1-az4--x-s3",
    "base.file.storage.providers.s3.properties.region=ap-northeast-1",
    "base.file.storage.providers.s3.properties.access-key-id=AKIAQIUTXSYUMUR65VUK",
    "base.file.storage.providers.s3.properties.secret-access-key=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"
})
public class RealWorldWithConfigS3Test {
    
    @Autowired
    private FileStorageManager fileStorageManager;
    
    @Autowired
    private FileStorage fileStorage;
    
    @Test
    void shouldNotThrowS3FileStorageRequiresConfigurationError() {
        // This is the main test - it should not throw the error anymore
        assertThatCode(() -> {
            FileStorage s3Storage = fileStorageManager.getStorage("s3");
            assertThat(s3Storage).isNotNull();
            assertThat(s3Storage.getProviderName()).isEqualTo("s3");
        }).doesNotThrowAnyException();
    }
    
    @Test
    void shouldCreateFileStorageSuccessfully() {
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
    void shouldSupportS3Capabilities() {
        FileStorage s3Storage = fileStorageManager.getStorage("s3");
        var capabilities = s3Storage.getCapabilities();
        
        assertThat(capabilities).isNotNull();
        assertThat(capabilities).containsKey("upload");
        assertThat(capabilities).containsKey("download");
        assertThat(capabilities).containsKey("delete");
        assertThat(capabilities).containsKey("exists");
        assertThat(capabilities).containsKey("metadata");
        assertThat(capabilities).containsKey("list");
        assertThat(capabilities).containsKey("presigned-urls");
    }
}
