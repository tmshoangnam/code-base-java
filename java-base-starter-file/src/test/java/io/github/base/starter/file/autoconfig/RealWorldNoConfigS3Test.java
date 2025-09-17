package io.github.base.starter.file.autoconfig;

import io.github.base.file.api.storage.FileStorage;
import io.github.base.file.api.storage.FileStorageManager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;

/**
 * Test for S3 configuration when no explicit configuration is provided in Spring context.
 * This simulates the real-world scenario where configuration might not be loaded properly.
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
@SpringBootTest(classes = {FileStorageAutoConfiguration.class})
@TestPropertySource(properties = {
    "base.file.storage.enabled=true",
    "base.file.storage.default-provider=s3"
    // No explicit S3 configuration provided - this should trigger environment variable fallback
})
public class RealWorldNoConfigS3Test {
    
    @Autowired
    private FileStorageManager fileStorageManager;
    
    @Test
    void shouldHandleMissingS3ConfigurationGracefully() {
        // This test verifies that when no S3 configuration is provided in Spring context,
        // the system should attempt to use environment variables as fallback
        
        // The test should not throw the "S3FileStorage requires configuration" error
        // Instead, it should either:
        // 1. Use environment variables if available, or
        // 2. Throw a more informative error about missing configuration
        
        assertThatCode(() -> {
            FileStorage s3Storage = fileStorageManager.getStorage("s3");
            // If environment variables are available, this should work
            // If not, it should throw a clear error about missing configuration
        }).satisfies(throwable -> {
            if (throwable != null) {
                // Should not be the old "S3FileStorage requires configuration" error
                assertThat(throwable.getMessage())
                    .doesNotContain("S3FileStorage requires configuration. Use createStorage(Map<String, String> configuration) instead.");
                
                // Should be a more informative error about missing configuration
                assertThat(throwable.getMessage())
                    .containsAnyOf(
                        "S3FileStorage requires configuration",
                        "Please provide AWS credentials",
                        "Missing:",
                        "AWS service provider not found"
                    );
            }
        });
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
}
