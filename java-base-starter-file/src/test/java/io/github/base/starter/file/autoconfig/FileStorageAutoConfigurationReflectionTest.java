package io.github.base.starter.file.autoconfig;

import io.github.base.starter.file.impl.s3.S3FileStorageProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

/**
 * Test to verify reflection logic in FileStorageAutoConfiguration.
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
@DisplayName("FileStorageAutoConfiguration Reflection Tests")
class FileStorageAutoConfigurationReflectionTest {
    
    @Test
    @DisplayName("Should find createStorage(Map) method in S3FileStorageProvider")
    void shouldFindCreateStorageWithMapMethodInS3Provider() throws Exception {
        // Given
        S3FileStorageProvider provider = new S3FileStorageProvider();
        
        // When
        Method createStorageWithMap = provider.getClass().getMethod("createStorage", Map.class);
        
        // Then
        assertThat(createStorageWithMap).isNotNull();
        assertThat(createStorageWithMap.getName()).isEqualTo("createStorage");
        assertThat(createStorageWithMap.getParameterTypes()).hasSize(1);
        assertThat(createStorageWithMap.getParameterTypes()[0]).isEqualTo(Map.class);
    }
    
    @Test
    @DisplayName("Should find createStorage() method without parameters in S3FileStorageProvider")
    void shouldFindCreateStorageWithoutParametersInS3Provider() throws Exception {
        // Given
        S3FileStorageProvider provider = new S3FileStorageProvider();
        
        // When
        Method createStorageWithoutParams = provider.getClass().getMethod("createStorage");
        
        // Then
        assertThat(createStorageWithoutParams).isNotNull();
        assertThat(createStorageWithoutParams.getName()).isEqualTo("createStorage");
        assertThat(createStorageWithoutParams.getParameterTypes()).isEmpty();
    }
    
    @Test
    @DisplayName("Should have both createStorage() and createStorage(Map) methods in S3FileStorageProvider")
    void shouldHaveBothCreateStorageMethodsInS3Provider() throws Exception {
        // Given
        S3FileStorageProvider provider = new S3FileStorageProvider();
        
        // When
        Method createStorageWithoutParams = provider.getClass().getMethod("createStorage");
        Method createStorageWithMap = provider.getClass().getMethod("createStorage", Map.class);
        
        // Then
        assertThat(createStorageWithoutParams).isNotNull();
        assertThat(createStorageWithMap).isNotNull();
        assertThat(createStorageWithoutParams.getName()).isEqualTo("createStorage");
        assertThat(createStorageWithMap.getName()).isEqualTo("createStorage");
    }
}
