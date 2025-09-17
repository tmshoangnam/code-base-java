package io.github.base.starter.file.autoconfig;

import io.github.base.file.api.storage.FileStorage;
import io.github.base.file.api.storage.FileStorageManager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import static org.assertj.core.api.Assertions.*;

/**
 * Debug test for S3 configuration issues.
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
@SpringBootTest(classes = {FileStorageAutoConfiguration.class})
@EnableConfigurationProperties(FileStorageProperties.class)
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
public class S3DebugTest {
    
    @Autowired
    private FileStorageManager fileStorageManager;
    
    @Autowired
    private FileStorageProperties properties;
    
    @Test
    void shouldDebugS3Configuration() {
        // Debug configuration
        System.out.println("=== S3 Configuration Debug ===");
        System.out.println("Default Provider: " + properties.getDefaultProvider());
        System.out.println("Providers: " + properties.getProviders());
        
        if (properties.getProviders() != null && properties.getProviders().containsKey("s3")) {
            var s3Config = properties.getProviders().get("s3");
            System.out.println("S3 Config Type: " + s3Config.getType());
            System.out.println("S3 Config Enabled: " + s3Config.isEnabled());
            System.out.println("S3 Config Priority: " + s3Config.getPriority());
            System.out.println("S3 Config Properties: " + s3Config.getProperties());
        } else {
            System.out.println("S3 Config: NOT FOUND");
        }
        
        // Debug available providers
        var availableProviders = fileStorageManager.getAvailableProviders();
        System.out.println("Available Providers: " + availableProviders);
        
        // Try to get S3 storage
        try {
            FileStorage s3Storage = fileStorageManager.getStorage("s3");
            System.out.println("S3 Storage: " + s3Storage);
            System.out.println("S3 Storage Provider: " + s3Storage.getProviderName());
        } catch (Exception e) {
            System.out.println("S3 Storage Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Try to get default storage
        try {
            FileStorage defaultStorage = fileStorageManager.getDefaultStorage();
            System.out.println("Default Storage: " + defaultStorage);
            System.out.println("Default Storage Provider: " + defaultStorage.getProviderName());
        } catch (Exception e) {
            System.out.println("Default Storage Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("=== End Debug ===");
    }
}
