package io.github.base.starter.file.decorators;

import io.github.base.file.api.exception.FileStorageException;
import io.github.base.file.api.model.DownloadResult;
import io.github.base.file.api.model.FileMetadata;
import io.github.base.file.api.model.UploadResult;
import io.github.base.file.api.storage.FileStorage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link CachedFileStorage}.
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CachedFileStorage Tests")
class CachedFileStorageTest {

    @Mock
    private FileStorage delegate;

    private CachedFileStorage cachedFileStorage;

    @BeforeEach
    void setUp() {
        cachedFileStorage = new CachedFileStorage(delegate);
    }

    @Nested
    @DisplayName("Upload Operations Tests")
    class UploadOperationsTests {

        @Test
        @DisplayName("Should cache metadata after successful file upload")
        void shouldCacheMetadataAfterSuccessfulFileUpload() throws FileStorageException {
            // Given
            String fileId = "test-file.txt";
            File file = new File("test.txt");
            FileMetadata metadata = FileMetadata.of("test.txt", "text/plain", 1024L);
            UploadResult uploadResult = UploadResult.success(fileId, "test-url", metadata);

            when(delegate.upload(fileId, file, metadata)).thenReturn(uploadResult);

            // When
            UploadResult result = cachedFileStorage.upload(fileId, file, metadata);

            // Then
            assertThat(result).isEqualTo(uploadResult);
            assertThat(cachedFileStorage.exists(fileId)).isTrue();
            assertThat(cachedFileStorage.getMetadata(fileId)).isEqualTo(metadata);
        }

        @Test
        @DisplayName("Should cache metadata after successful byte array upload")
        void shouldCacheMetadataAfterSuccessfulByteArrayUpload() throws FileStorageException {
            // Given
            String fileId = "test-file.txt";
            byte[] data = "test content".getBytes();
            FileMetadata metadata = FileMetadata.of("test.txt", "text/plain", data.length);
            UploadResult uploadResult = UploadResult.success(fileId, "test-url", metadata);

            when(delegate.upload(fileId, data, metadata)).thenReturn(uploadResult);

            // When
            UploadResult result = cachedFileStorage.upload(fileId, data, metadata);

            // Then
            assertThat(result).isEqualTo(uploadResult);
            assertThat(cachedFileStorage.exists(fileId)).isTrue();
            assertThat(cachedFileStorage.getMetadata(fileId)).isEqualTo(metadata);
        }

        @Test
        @DisplayName("Should cache metadata after successful input stream upload")
        void shouldCacheMetadataAfterSuccessfulInputStreamUpload() throws FileStorageException {
            // Given
            String fileId = "test-file.txt";
            InputStream inputStream = new java.io.ByteArrayInputStream("test content".getBytes());
            FileMetadata metadata = FileMetadata.of("test.txt", "text/plain", 12L);
            UploadResult uploadResult = UploadResult.success(fileId, "test-url", metadata);

            when(delegate.upload(fileId, inputStream, metadata)).thenReturn(uploadResult);

            // When
            UploadResult result = cachedFileStorage.upload(fileId, inputStream, metadata);

            // Then
            assertThat(result).isEqualTo(uploadResult);
            assertThat(cachedFileStorage.exists(fileId)).isTrue();
            assertThat(cachedFileStorage.getMetadata(fileId)).isEqualTo(metadata);
        }

        @Test
        @DisplayName("Should not cache metadata after failed upload")
        void shouldNotCacheMetadataAfterFailedUpload() throws FileStorageException {
            // Given
            String fileId = "test-file.txt";
            File file = new File("test.txt");
            FileMetadata metadata = FileMetadata.of("test.txt", "text/plain", 1024L);
            UploadResult uploadResult = UploadResult.failure(fileId, "Upload failed");

            when(delegate.upload(fileId, file, metadata)).thenReturn(uploadResult);

            // When
            UploadResult result = cachedFileStorage.upload(fileId, file, metadata);

            // Then
            assertThat(result).isEqualTo(uploadResult);
            // Should not be cached - verify that exists and getMetadata call delegate
            when(delegate.exists(fileId)).thenReturn(false);
            when(delegate.getMetadata(fileId)).thenReturn(null);
            
            assertThat(cachedFileStorage.exists(fileId)).isFalse();
            assertThat(cachedFileStorage.getMetadata(fileId)).isNull();
        }
    }

    @Nested
    @DisplayName("Download Operations Tests")
    class DownloadOperationsTests {

        @Test
        @DisplayName("Should delegate download operations")
        void shouldDelegateDownloadOperations() throws FileStorageException {
            // Given
            String fileId = "test-file.txt";
            File expectedFile = new File("test.txt");
            DownloadResult<File> expectedResult = DownloadResult.success(expectedFile);

            when(delegate.downloadAsFile(fileId)).thenReturn(expectedResult);

            // When
            DownloadResult<File> result = cachedFileStorage.downloadAsFile(fileId);

            // Then
            assertThat(result).isEqualTo(expectedResult);
            verify(delegate).downloadAsFile(fileId);
        }

        @Test
        @DisplayName("Should delegate generic download operations")
        void shouldDelegateGenericDownloadOperations() throws FileStorageException {
            // Given
            String fileId = "test-file.txt";
            byte[] expectedData = "test content".getBytes();
            DownloadResult<byte[]> expectedResult = DownloadResult.success(expectedData);

            when(delegate.download(fileId, byte[].class)).thenReturn(expectedResult);

            // When
            DownloadResult<byte[]> result = cachedFileStorage.download(fileId, byte[].class);

            // Then
            assertThat(result).isEqualTo(expectedResult);
            verify(delegate).download(fileId, byte[].class);
        }
    }

    @Nested
    @DisplayName("Delete Operations Tests")
    class DeleteOperationsTests {

        @Test
        @DisplayName("Should remove from cache after successful delete")
        void shouldRemoveFromCacheAfterSuccessfulDelete() throws FileStorageException {
            // Given
            String fileId = "test-file.txt";
            FileMetadata metadata = FileMetadata.of("test.txt", "text/plain", 1024L);
            
            // First upload to cache
            UploadResult uploadResult = UploadResult.success(fileId, "test-url", metadata);
            when(delegate.upload(anyString(), any(File.class), any(FileMetadata.class))).thenReturn(uploadResult);
            cachedFileStorage.upload(fileId, new File("test.txt"), metadata);
            
            // Then delete
            when(delegate.delete(fileId)).thenReturn(true);

            // When
            boolean result = cachedFileStorage.delete(fileId);

            // Then
            assertThat(result).isTrue();
            // Should not be in cache anymore - verify by calling exists and getMetadata
            when(delegate.exists(fileId)).thenReturn(false);
            when(delegate.getMetadata(fileId)).thenReturn(null);
            
            assertThat(cachedFileStorage.exists(fileId)).isFalse();
            assertThat(cachedFileStorage.getMetadata(fileId)).isNull();
        }

        @Test
        @DisplayName("Should not remove from cache after failed delete")
        void shouldNotRemoveFromCacheAfterFailedDelete() throws FileStorageException {
            // Given
            String fileId = "test-file.txt";
            FileMetadata metadata = FileMetadata.of("test.txt", "text/plain", 1024L);
            
            // First upload to cache
            UploadResult uploadResult = UploadResult.success(fileId, "test-url", metadata);
            when(delegate.upload(anyString(), any(File.class), any(FileMetadata.class))).thenReturn(uploadResult);
            cachedFileStorage.upload(fileId, new File("test.txt"), metadata);
            
            // Then delete
            when(delegate.delete(fileId)).thenReturn(false);

            // When
            boolean result = cachedFileStorage.delete(fileId);

            // Then
            assertThat(result).isFalse();
            // Should still be in cache
            assertThat(cachedFileStorage.exists(fileId)).isTrue();
            assertThat(cachedFileStorage.getMetadata(fileId)).isEqualTo(metadata);
        }
    }

    @Nested
    @DisplayName("Cache Operations Tests")
    class CacheOperationsTests {

        @Test
        @DisplayName("Should return cached exists result")
        void shouldReturnCachedExistsResult() throws FileStorageException {
            // Given
            String fileId = "test-file.txt";
            FileMetadata metadata = FileMetadata.of("test.txt", "text/plain", 1024L);
            
            // Upload to cache
            UploadResult uploadResult = UploadResult.success(fileId, "test-url", metadata);
            when(delegate.upload(anyString(), any(File.class), any(FileMetadata.class))).thenReturn(uploadResult);
            cachedFileStorage.upload(fileId, new File("test.txt"), metadata);

            // When
            boolean exists1 = cachedFileStorage.exists(fileId);
            boolean exists2 = cachedFileStorage.exists(fileId);

            // Then
            assertThat(exists1).isTrue();
            assertThat(exists2).isTrue();
            // Should not call delegate for exists since it's cached
            verify(delegate, never()).exists(fileId);
        }

        @Test
        @DisplayName("Should return cached metadata result")
        void shouldReturnCachedMetadataResult() throws FileStorageException {
            // Given
            String fileId = "test-file.txt";
            FileMetadata metadata = FileMetadata.of("test.txt", "text/plain", 1024L);
            
            // Upload to cache
            UploadResult uploadResult = UploadResult.success(fileId, "test-url", metadata);
            when(delegate.upload(anyString(), any(File.class), any(FileMetadata.class))).thenReturn(uploadResult);
            cachedFileStorage.upload(fileId, new File("test.txt"), metadata);

            // When
            FileMetadata metadata1 = cachedFileStorage.getMetadata(fileId);
            FileMetadata metadata2 = cachedFileStorage.getMetadata(fileId);

            // Then
            assertThat(metadata1).isEqualTo(metadata);
            assertThat(metadata2).isEqualTo(metadata);
            // Should not call delegate for getMetadata since it's cached
            verify(delegate, never()).getMetadata(fileId);
        }

        @Test
        @DisplayName("Should call delegate when not in cache")
        void shouldCallDelegateWhenNotInCache() throws FileStorageException {
            // Given
            String fileId = "test-file.txt";
            when(delegate.exists(fileId)).thenReturn(true);
            when(delegate.getMetadata(fileId)).thenReturn(FileMetadata.of("test.txt", "text/plain", 1024L));

            // When
            boolean exists = cachedFileStorage.exists(fileId);
            FileMetadata metadata = cachedFileStorage.getMetadata(fileId);

            // Then
            assertThat(exists).isTrue();
            assertThat(metadata).isNotNull();
            verify(delegate).exists(fileId);
            verify(delegate).getMetadata(fileId);
        }

        @Test
        @DisplayName("Should cache negative exists result")
        void shouldCacheNegativeExistsResult() throws FileStorageException {
            // Given
            String fileId = "test-file.txt";
            when(delegate.exists(fileId)).thenReturn(false);

            // When
            boolean exists1 = cachedFileStorage.exists(fileId);
            boolean exists2 = cachedFileStorage.exists(fileId);

            // Then
            assertThat(exists1).isFalse();
            assertThat(exists2).isFalse();
            // Should only call delegate once
            verify(delegate, times(1)).exists(fileId);
        }
    }

    @Nested
    @DisplayName("Cache Management Tests")
    class CacheManagementTests {

        @Test
        @DisplayName("Should clear cache")
        void shouldClearCache() throws FileStorageException {
            // Given
            String fileId = "test-file.txt";
            FileMetadata metadata = FileMetadata.of("test.txt", "text/plain", 1024L);
            
            // Upload to cache
            UploadResult uploadResult = UploadResult.success(fileId, "test-url", metadata);
            when(delegate.upload(anyString(), any(File.class), any(FileMetadata.class))).thenReturn(uploadResult);
            cachedFileStorage.upload(fileId, new File("test.txt"), metadata);

            // When
            cachedFileStorage.clearCache();

            // Then
            // Should call delegate after cache clear
            when(delegate.exists(fileId)).thenReturn(true);
            when(delegate.getMetadata(fileId)).thenReturn(metadata);
            
            boolean exists = cachedFileStorage.exists(fileId);
            FileMetadata retrievedMetadata = cachedFileStorage.getMetadata(fileId);

            assertThat(exists).isTrue();
            assertThat(retrievedMetadata).isEqualTo(metadata);
            verify(delegate).exists(fileId);
            verify(delegate).getMetadata(fileId);
        }

        @Test
        @DisplayName("Should remove specific file from cache")
        void shouldRemoveSpecificFileFromCache() throws FileStorageException {
            // Given
            String fileId = "test-file.txt";
            FileMetadata metadata = FileMetadata.of("test.txt", "text/plain", 1024L);
            
            // Upload to cache
            UploadResult uploadResult = UploadResult.success(fileId, "test-url", metadata);
            when(delegate.upload(anyString(), any(File.class), any(FileMetadata.class))).thenReturn(uploadResult);
            cachedFileStorage.upload(fileId, new File("test.txt"), metadata);

            // When
            cachedFileStorage.removeFromCache(fileId);

            // Then
            // Should call delegate after removal
            when(delegate.exists(fileId)).thenReturn(true);
            when(delegate.getMetadata(fileId)).thenReturn(metadata);
            
            boolean exists = cachedFileStorage.exists(fileId);
            FileMetadata retrievedMetadata = cachedFileStorage.getMetadata(fileId);

            assertThat(exists).isTrue();
            assertThat(retrievedMetadata).isEqualTo(metadata);
            verify(delegate).exists(fileId);
            verify(delegate).getMetadata(fileId);
        }

        @Test
        @DisplayName("Should get cache statistics")
        void shouldGetCacheStatistics() throws FileStorageException {
            // Given
            String fileId1 = "test-file1.txt";
            String fileId2 = "test-file2.txt";
            FileMetadata metadata1 = FileMetadata.of("test1.txt", "text/plain", 1024L);
            FileMetadata metadata2 = FileMetadata.of("test2.txt", "text/plain", 2048L);
            
            // Upload files to cache
            UploadResult uploadResult1 = UploadResult.success(fileId1, "test-url1", metadata1);
            UploadResult uploadResult2 = UploadResult.success(fileId2, "test-url2", metadata2);
            when(delegate.upload(anyString(), any(File.class), any(FileMetadata.class)))
                .thenReturn(uploadResult1)
                .thenReturn(uploadResult2);
            
            cachedFileStorage.upload(fileId1, new File("test1.txt"), metadata1);
            cachedFileStorage.upload(fileId2, new File("test2.txt"), metadata2);

            // When
            Map<String, Object> stats = cachedFileStorage.getCacheStats();

            // Then
            assertThat(stats).containsEntry("metadataCacheSize", 2);
            assertThat(stats).containsEntry("existsCacheSize", 2);
        }
    }

    @Nested
    @DisplayName("Delegation Tests")
    class DelegationTests {

        @Test
        @DisplayName("Should delegate list files operations")
        void shouldDelegateListFilesOperations() throws FileStorageException {
            // Given
            String prefix = "test/";
            int maxResults = 10;
            List<FileMetadata> expectedFiles = List.of(
                FileMetadata.of("file1.txt", "text/plain", 1024L)
            );

            when(delegate.listFiles(prefix, maxResults)).thenReturn(expectedFiles);

            // When
            List<FileMetadata> result = cachedFileStorage.listFiles(prefix, maxResults);

            // Then
            assertThat(result).isEqualTo(expectedFiles);
            verify(delegate).listFiles(prefix, maxResults);
        }

        @Test
        @DisplayName("Should delegate list files with continuation token")
        void shouldDelegateListFilesWithContinuationToken() throws FileStorageException {
            // Given
            String prefix = "test/";
            String continuationToken = "token123";
            int maxResults = 10;
            Map<String, Object> expectedResult = Map.of("files", List.of(), "nextToken", "token456");

            when(delegate.listFiles(prefix, continuationToken, maxResults)).thenReturn(expectedResult);

            // When
            Map<String, Object> result = cachedFileStorage.listFiles(prefix, continuationToken, maxResults);

            // Then
            assertThat(result).isEqualTo(expectedResult);
            verify(delegate).listFiles(prefix, continuationToken, maxResults);
        }

        @Test
        @DisplayName("Should delegate generate presigned URL")
        void shouldDelegateGeneratePresignedUrl() throws FileStorageException {
            // Given
            String fileId = "test-file.txt";
            int expirationMinutes = 60;
            String expectedUrl = "https://example.com/presigned-url";

            when(delegate.generatePresignedUrl(fileId, expirationMinutes)).thenReturn(expectedUrl);

            // When
            String result = cachedFileStorage.generatePresignedUrl(fileId, expirationMinutes);

            // Then
            assertThat(result).isEqualTo(expectedUrl);
            verify(delegate).generatePresignedUrl(fileId, expirationMinutes);
        }

        @Test
        @DisplayName("Should delegate async operations")
        void shouldDelegateAsyncOperations() throws FileStorageException {
            // Given
            String fileId = "test-file.txt";
            File file = new File("test.txt");
            FileMetadata metadata = FileMetadata.of("test.txt", "text/plain", 1024L);
            UploadResult uploadResult = UploadResult.success(fileId, "test-url", metadata);
            CompletableFuture<UploadResult> expectedFuture = CompletableFuture.completedFuture(uploadResult);

            when(delegate.uploadAsync(fileId, file, metadata)).thenReturn(expectedFuture);

            // When
            CompletableFuture<UploadResult> result = cachedFileStorage.uploadAsync(fileId, file, metadata);

            // Then
            assertThat(result).isEqualTo(expectedFuture);
            verify(delegate).uploadAsync(fileId, file, metadata);
        }

        @Test
        @DisplayName("Should delegate provider name and capabilities")
        void shouldDelegateProviderNameAndCapabilities() {
            // Given
            String expectedProviderName = "test-provider";
            Map<String, Boolean> expectedCapabilities = Map.of("upload", true, "download", true);

            when(delegate.getProviderName()).thenReturn(expectedProviderName);
            when(delegate.getCapabilities()).thenReturn(expectedCapabilities);

            // When
            String providerName = cachedFileStorage.getProviderName();
            Map<String, Boolean> capabilities = cachedFileStorage.getCapabilities();

            // Then
            assertThat(providerName).isEqualTo(expectedProviderName);
            assertThat(capabilities).isEqualTo(expectedCapabilities);
            verify(delegate).getProviderName();
            verify(delegate).getCapabilities();
        }
    }
}
