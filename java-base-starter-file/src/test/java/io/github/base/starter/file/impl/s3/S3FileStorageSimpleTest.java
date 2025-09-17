package io.github.base.starter.file.impl.s3;

import io.github.base.file.api.model.FileMetadata;
import io.github.base.file.api.model.UploadResult;
import io.github.base.aws.api.s3.S3Operations;
import io.github.base.aws.api.s3.S3Metadata;
import io.github.base.aws.api.s3.S3UploadResult;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.time.Instant;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Simple unit tests for S3FileStorage.
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("S3FileStorage Simple Tests")
class S3FileStorageSimpleTest {
    
    @Mock
    private S3Operations s3Operations;
    
    private S3FileStorage s3FileStorage;
    private static final String BUCKET_NAME = "test-bucket";
    private static final String KEY_PREFIX = "uploads";
    private static final String FILE_ID = "test-file.txt";
    private static final String S3_KEY = "uploads/test-file.txt";
    
    @BeforeEach
    void setUp() {
        s3FileStorage = new S3FileStorage(s3Operations, BUCKET_NAME, KEY_PREFIX);
    }
    
    @Test
    @DisplayName("Should upload file successfully")
    void shouldUploadFileSuccessfully() throws Exception {
        // Given
        File file = new File("test-file.txt");
        FileMetadata metadata = FileMetadata.of("test-file.txt", "text/plain", 1024L);
        S3Metadata s3Metadata = S3Metadata.of(BUCKET_NAME, S3_KEY, "text/plain", 1024L);
        S3UploadResult successResult = S3UploadResult.success(BUCKET_NAME, S3_KEY, "etag123", s3Metadata);
        
        when(s3Operations.upload(eq(BUCKET_NAME), eq(S3_KEY), eq(file), any(S3Metadata.class)))
            .thenReturn(successResult);
        
        // When
        UploadResult result = s3FileStorage.upload(FILE_ID, file, metadata);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getFileId()).isEqualTo(FILE_ID);
        assertThat(result.getLocation()).isEqualTo("s3://test-bucket/uploads/test-file.txt");
        assertThat(result.getMetadata()).isEqualTo(metadata);
        
        verify(s3Operations).upload(eq(BUCKET_NAME), eq(S3_KEY), eq(file), any(S3Metadata.class));
    }
    
    @Test
    @DisplayName("Should upload byte array successfully")
    void shouldUploadByteArraySuccessfully() throws Exception {
        // Given
        byte[] data = "test content".getBytes();
        FileMetadata metadata = FileMetadata.of("test-file.txt", "text/plain", data.length);
        S3Metadata s3Metadata = S3Metadata.of(BUCKET_NAME, S3_KEY, "text/plain", data.length);
        S3UploadResult successResult = S3UploadResult.success(BUCKET_NAME, S3_KEY, "etag123", s3Metadata);
        
        when(s3Operations.upload(eq(BUCKET_NAME), eq(S3_KEY), eq(data), any(S3Metadata.class)))
            .thenReturn(successResult);
        
        // When
        UploadResult result = s3FileStorage.upload(FILE_ID, data, metadata);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getFileId()).isEqualTo(FILE_ID);
        
        verify(s3Operations).upload(eq(BUCKET_NAME), eq(S3_KEY), eq(data), any(S3Metadata.class));
    }
    
    @Test
    @DisplayName("Should return correct provider name")
    void shouldReturnCorrectProviderName() {
        // When
        String providerName = s3FileStorage.getProviderName();
        
        // Then
        assertThat(providerName).isEqualTo("s3");
    }
    
    @Test
    @DisplayName("Should return correct capabilities")
    void shouldReturnCorrectCapabilities() {
        // When
        var capabilities = s3FileStorage.getCapabilities();
        
        // Then
        assertThat(capabilities).isNotNull();
        assertThat(capabilities).containsEntry("upload", true);
        assertThat(capabilities).containsEntry("download", true);
        assertThat(capabilities).containsEntry("delete", true);
        assertThat(capabilities).containsEntry("exists", true);
        assertThat(capabilities).containsEntry("metadata", true);
        assertThat(capabilities).containsEntry("list", true);
        assertThat(capabilities).containsEntry("streaming", true);
        assertThat(capabilities).containsEntry("concurrent", true);
        assertThat(capabilities).containsEntry("replication", true);
        assertThat(capabilities).containsEntry("encryption", true);
        assertThat(capabilities).containsEntry("versioning", true);
        assertThat(capabilities).containsEntry("presigned-urls", true);
        assertThat(capabilities).containsEntry("async", true);
    }
}
