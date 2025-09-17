package io.github.base.starter.file.autoconfig;

import io.github.base.file.api.storage.FileStorage;
import io.github.base.file.api.storage.FileStorageManager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;

/**
 * Test for S3 configuration using YAML properties.
 *
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
@SpringBootTest(classes = {FileStorageAutoConfiguration.class})
@ActiveProfiles("s3-test")
class S3ConfigurationTest {

    @Autowired
    private FileStorageManager fileStorageManager;

    @Autowired
    private FileStorage fileStorage;

    @Test
    void shouldLoadS3ConfigurationFromYaml() {
        assertThat(fileStorageManager).isNotNull();
        assertThat(fileStorage).isNotNull();

        // Verify default provider is S3
        assertThat(fileStorageManager.getDefaultProvider()).isEqualTo("s3");

        // Verify S3 provider is available
        var availableProviders = fileStorageManager.getAvailableProviders();
        assertThat(availableProviders).contains("s3");

        // Verify we can get S3 storage without configuration error
        assertThatCode(() -> {
            FileStorage s3Storage = fileStorageManager.getStorage("s3");
            assertThat(s3Storage).isNotNull();
            assertThat(s3Storage.getProviderName()).isEqualTo("s3");
        }).doesNotThrowAnyException();
    }

    @Test
    void shouldNotThrowS3FileStorageRequiresConfigurationError() {
        // This is the main test - it should not throw the error anymore
        assertThatCode(() -> {
            FileStorage s3Storage = fileStorageManager.getStorage("s3");
            assertThat(s3Storage).isNotNull();
        }).doesNotThrowAnyException();
    }
}
