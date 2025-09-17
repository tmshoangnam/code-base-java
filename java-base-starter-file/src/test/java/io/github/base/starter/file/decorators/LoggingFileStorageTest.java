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
import org.slf4j.MDC;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link LoggingFileStorage}.
 * 
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("LoggingFileStorage Tests")
class LoggingFileStorageTest {

    @Mock
    private FileStorage delegate;

    private LoggingFileStorage loggingFileStorage;

    @BeforeEach
    void setUp() {
        loggingFileStorage = new LoggingFileStorage(delegate);
    }

    @Nested
    @DisplayName("Upload Operations Tests")
    class UploadOperationsTests {

        @Test
        @DisplayName("Should log successful file upload")
        void shouldLogSuccessfulFileUpload() throws FileStorageException {
            // Given
            String fileId = "test-file.txt";
            File file = new File("test.txt");
            FileMetadata metadata = FileMetadata.of("test.txt", "text/plain", 1024L);
            UploadResult uploadResult = UploadResult.success(fileId, "test-url", metadata);

            when(delegate.upload(fileId, file, metadata)).thenReturn(uploadResult);

            // When
            UploadResult result = loggingFileStorage.upload(fileId, file, metadata);

            // Then
            assertThat(result).isEqualTo(uploadResult);
            verify(delegate).upload(fileId, file, metadata);
        }

        @Test
        @DisplayName("Should log successful byte array upload")
        void shouldLogSuccessfulByteArrayUpload() throws FileStorageException {
            // Given
            String fileId = "test-file.txt";
            byte[] data = "test content".getBytes();
            FileMetadata metadata = FileMetadata.of("test.txt", "text/plain", data.length);
            UploadResult uploadResult = UploadResult.success(fileId, "test-url", metadata);

            when(delegate.upload(fileId, data, metadata)).thenReturn(uploadResult);

            // When
            UploadResult result = loggingFileStorage.upload(fileId, data, metadata);

            // Then
            assertThat(result).isEqualTo(uploadResult);
            verify(delegate).upload(fileId, data, metadata);
        }

        @Test
        @DisplayName("Should log successful input stream upload")
        void shouldLogSuccessfulInputStreamUpload() throws FileStorageException {
            // Given
            String fileId = "test-file.txt";
            InputStream inputStream = new java.io.ByteArrayInputStream("test content".getBytes());
            FileMetadata metadata = FileMetadata.of("test.txt", "text/plain", 12L);
            UploadResult uploadResult = UploadResult.success(fileId, "test-url", metadata);

            when(delegate.upload(fileId, inputStream, metadata)).thenReturn(uploadResult);

            // When
            UploadResult result = loggingFileStorage.upload(fileId, inputStream, metadata);

            // Then
            assertThat(result).isEqualTo(uploadResult);
            verify(delegate).upload(fileId, inputStream, metadata);
        }

        @Test
        @DisplayName("Should log failed upload and rethrow exception")
        void shouldLogFailedUploadAndRethrowException() throws FileStorageException {
            // Given
            String fileId = "test-file.txt";
            File file = new File("test.txt");
            FileMetadata metadata = FileMetadata.of("test.txt", "text/plain", 1024L);
            FileStorageException exception = new FileStorageException("Upload failed");

            when(delegate.upload(fileId, file, metadata)).thenThrow(exception);

            // When & Then
            assertThatThrownBy(() -> loggingFileStorage.upload(fileId, file, metadata))
                .isInstanceOf(FileStorageException.class)
                .hasMessage("Upload failed");

            verify(delegate).upload(fileId, file, metadata);
        }

        @Test
        @DisplayName("Should log unexpected exception and wrap it")
        void shouldLogUnexpectedExceptionAndWrapIt() throws FileStorageException {
            // Given
            String fileId = "test-file.txt";
            File file = new File("test.txt");
            FileMetadata metadata = FileMetadata.of("test.txt", "text/plain", 1024L);
            RuntimeException exception = new RuntimeException("Unexpected error");

            when(delegate.upload(fileId, file, metadata)).thenThrow(exception);

            // When & Then
            assertThatThrownBy(() -> loggingFileStorage.upload(fileId, file, metadata))
                .isInstanceOf(FileStorageException.class)
                .hasMessage("Operation failed: upload")
                .hasCause(exception);

            verify(delegate).upload(fileId, file, metadata);
        }
    }

    @Nested
    @DisplayName("Download Operations Tests")
    class DownloadOperationsTests {

        @Test
        @DisplayName("Should log successful file download")
        void shouldLogSuccessfulFileDownload() throws FileStorageException {
            // Given
            String fileId = "test-file.txt";
            File expectedFile = new File("test.txt");
            DownloadResult<File> expectedResult = DownloadResult.success(expectedFile);

            when(delegate.downloadAsFile(fileId)).thenReturn(expectedResult);

            // When
            DownloadResult<File> result = loggingFileStorage.downloadAsFile(fileId);

            // Then
            assertThat(result).isEqualTo(expectedResult);
            verify(delegate).downloadAsFile(fileId);
        }

        @Test
        @DisplayName("Should log successful generic download")
        void shouldLogSuccessfulGenericDownload() throws FileStorageException {
            // Given
            String fileId = "test-file.txt";
            byte[] expectedData = "test content".getBytes();
            DownloadResult<byte[]> expectedResult = DownloadResult.success(expectedData);

            when(delegate.download(fileId, byte[].class)).thenReturn(expectedResult);

            // When
            DownloadResult<byte[]> result = loggingFileStorage.download(fileId, byte[].class);

            // Then
            assertThat(result).isEqualTo(expectedResult);
            verify(delegate).download(fileId, byte[].class);
        }

        @Test
        @DisplayName("Should log failed download and rethrow exception")
        void shouldLogFailedDownloadAndRethrowException() throws FileStorageException {
            // Given
            String fileId = "test-file.txt";
            FileStorageException exception = new FileStorageException("Download failed");

            when(delegate.downloadAsFile(fileId)).thenThrow(exception);

            // When & Then
            assertThatThrownBy(() -> loggingFileStorage.downloadAsFile(fileId))
                .isInstanceOf(FileStorageException.class)
                .hasMessage("Download failed");

            verify(delegate).downloadAsFile(fileId);
        }
    }

    @Nested
    @DisplayName("Other Operations Tests")
    class OtherOperationsTests {

        @Test
        @DisplayName("Should log successful delete operation")
        void shouldLogSuccessfulDeleteOperation() throws FileStorageException {
            // Given
            String fileId = "test-file.txt";
            when(delegate.delete(fileId)).thenReturn(true);

            // When
            boolean result = loggingFileStorage.delete(fileId);

            // Then
            assertThat(result).isTrue();
            verify(delegate).delete(fileId);
        }

        @Test
        @DisplayName("Should log successful exists check")
        void shouldLogSuccessfulExistsCheck() throws FileStorageException {
            // Given
            String fileId = "test-file.txt";
            when(delegate.exists(fileId)).thenReturn(true);

            // When
            boolean result = loggingFileStorage.exists(fileId);

            // Then
            assertThat(result).isTrue();
            verify(delegate).exists(fileId);
        }

        @Test
        @DisplayName("Should log successful metadata retrieval")
        void shouldLogSuccessfulMetadataRetrieval() throws FileStorageException {
            // Given
            String fileId = "test-file.txt";
            FileMetadata expectedMetadata = FileMetadata.of("test.txt", "text/plain", 1024L);
            when(delegate.getMetadata(fileId)).thenReturn(expectedMetadata);

            // When
            FileMetadata result = loggingFileStorage.getMetadata(fileId);

            // Then
            assertThat(result).isEqualTo(expectedMetadata);
            verify(delegate).getMetadata(fileId);
        }

        @Test
        @DisplayName("Should log successful list files operation")
        void shouldLogSuccessfulListFilesOperation() throws FileStorageException {
            // Given
            String prefix = "test/";
            int maxResults = 10;
            List<FileMetadata> expectedFiles = List.of(
                FileMetadata.of("file1.txt", "text/plain", 1024L)
            );

            when(delegate.listFiles(prefix, maxResults)).thenReturn(expectedFiles);

            // When
            List<FileMetadata> result = loggingFileStorage.listFiles(prefix, maxResults);

            // Then
            assertThat(result).isEqualTo(expectedFiles);
            verify(delegate).listFiles(prefix, maxResults);
        }

        @Test
        @DisplayName("Should log successful list files with continuation token")
        void shouldLogSuccessfulListFilesWithContinuationToken() throws FileStorageException {
            // Given
            String prefix = "test/";
            String continuationToken = "token123";
            int maxResults = 10;
            Map<String, Object> expectedResult = Map.of("files", List.of(), "nextToken", "token456");

            when(delegate.listFiles(prefix, continuationToken, maxResults)).thenReturn(expectedResult);

            // When
            Map<String, Object> result = loggingFileStorage.listFiles(prefix, continuationToken, maxResults);

            // Then
            assertThat(result).isEqualTo(expectedResult);
            verify(delegate).listFiles(prefix, continuationToken, maxResults);
        }

        @Test
        @DisplayName("Should log successful presigned URL generation")
        void shouldLogSuccessfulPresignedUrlGeneration() throws FileStorageException {
            // Given
            String fileId = "test-file.txt";
            int expirationMinutes = 60;
            String expectedUrl = "https://example.com/presigned-url";

            when(delegate.generatePresignedUrl(fileId, expirationMinutes)).thenReturn(expectedUrl);

            // When
            String result = loggingFileStorage.generatePresignedUrl(fileId, expirationMinutes);

            // Then
            assertThat(result).isEqualTo(expectedUrl);
            verify(delegate).generatePresignedUrl(fileId, expirationMinutes);
        }
    }

    @Nested
    @DisplayName("Async Operations Tests")
    class AsyncOperationsTests {

        @Test
        @DisplayName("Should log successful async upload")
        void shouldLogSuccessfulAsyncUpload() throws FileStorageException {
            // Given
            String fileId = "test-file.txt";
            File file = new File("test.txt");
            FileMetadata metadata = FileMetadata.of("test.txt", "text/plain", 1024L);
            UploadResult uploadResult = UploadResult.success(fileId, "test-url", metadata);
            CompletableFuture<UploadResult> expectedFuture = CompletableFuture.completedFuture(uploadResult);

            when(delegate.uploadAsync(fileId, file, metadata)).thenReturn(expectedFuture);

            // When
            CompletableFuture<UploadResult> result = loggingFileStorage.uploadAsync(fileId, file, metadata);

            // Then
            assertThat(result).isEqualTo(expectedFuture);
            verify(delegate).uploadAsync(fileId, file, metadata);
        }

        @Test
        @DisplayName("Should log successful async byte array upload")
        void shouldLogSuccessfulAsyncByteArrayUpload() throws FileStorageException {
            // Given
            String fileId = "test-file.txt";
            byte[] data = "test content".getBytes();
            FileMetadata metadata = FileMetadata.of("test.txt", "text/plain", data.length);
            UploadResult uploadResult = UploadResult.success(fileId, "test-url", metadata);
            CompletableFuture<UploadResult> expectedFuture = CompletableFuture.completedFuture(uploadResult);

            when(delegate.uploadAsync(fileId, data, metadata)).thenReturn(expectedFuture);

            // When
            CompletableFuture<UploadResult> result = loggingFileStorage.uploadAsync(fileId, data, metadata);

            // Then
            assertThat(result).isEqualTo(expectedFuture);
            verify(delegate).uploadAsync(fileId, data, metadata);
        }

        @Test
        @DisplayName("Should log successful async download")
        void shouldLogSuccessfulAsyncDownload() throws FileStorageException {
            // Given
            String fileId = "test-file.txt";
            byte[] expectedData = "test content".getBytes();
            DownloadResult<byte[]> downloadResult = DownloadResult.success(expectedData);
            CompletableFuture<DownloadResult<byte[]>> expectedFuture = CompletableFuture.completedFuture(downloadResult);

            when(delegate.downloadAsync(fileId)).thenReturn(expectedFuture);

            // When
            CompletableFuture<DownloadResult<byte[]>> result = loggingFileStorage.downloadAsync(fileId);

            // Then
            assertThat(result).isEqualTo(expectedFuture);
            verify(delegate).downloadAsync(fileId);
        }

        @Test
        @DisplayName("Should log failed async operation and rethrow exception")
        void shouldLogFailedAsyncOperationAndRethrowException() throws FileStorageException {
            // Given
            String fileId = "test-file.txt";
            File file = new File("test.txt");
            FileMetadata metadata = FileMetadata.of("test.txt", "text/plain", 1024L);
            FileStorageException exception = new FileStorageException("Async upload failed");

            when(delegate.uploadAsync(fileId, file, metadata)).thenThrow(exception);

            // When & Then
            assertThatThrownBy(() -> loggingFileStorage.uploadAsync(fileId, file, metadata))
                .isInstanceOf(FileStorageException.class)
                .hasMessage("Async operation failed: uploadAsync")
                .hasCause(exception);

            verify(delegate).uploadAsync(fileId, file, metadata);
        }
    }

    @Nested
    @DisplayName("MDC Context Tests")
    class MdcContextTests {

        @Test
        @DisplayName("Should set and clear MDC context for sync operations")
        void shouldSetAndClearMdcContextForSyncOperations() throws FileStorageException {
            // Given
            String fileId = "test-file.txt";
            File file = new File("test.txt");
            FileMetadata metadata = FileMetadata.of("test.txt", "text/plain", 1024L);
            UploadResult uploadResult = UploadResult.success(fileId, "test-url", metadata);

            when(delegate.upload(fileId, file, metadata)).thenReturn(uploadResult);

            // When
            loggingFileStorage.upload(fileId, file, metadata);

            // Then
            // MDC should be cleared after operation
            assertThat(MDC.get("operation")).isNull();
            assertThat(MDC.get("fileId")).isNull();
        }

        @Test
        @DisplayName("Should set and clear MDC context for async operations")
        void shouldSetAndClearMdcContextForAsyncOperations() throws FileStorageException {
            // Given
            String fileId = "test-file.txt";
            File file = new File("test.txt");
            FileMetadata metadata = FileMetadata.of("test.txt", "text/plain", 1024L);
            UploadResult uploadResult = UploadResult.success(fileId, "test-url", metadata);
            CompletableFuture<UploadResult> expectedFuture = CompletableFuture.completedFuture(uploadResult);

            when(delegate.uploadAsync(fileId, file, metadata)).thenReturn(expectedFuture);

            // When
            loggingFileStorage.uploadAsync(fileId, file, metadata);

            // Then
            // MDC should be cleared after operation
            assertThat(MDC.get("operation")).isNull();
            assertThat(MDC.get("fileId")).isNull();
        }

        @Test
        @DisplayName("Should clear MDC context even when exception occurs")
        void shouldClearMdcContextEvenWhenExceptionOccurs() throws FileStorageException {
            // Given
            String fileId = "test-file.txt";
            File file = new File("test.txt");
            FileMetadata metadata = FileMetadata.of("test.txt", "text/plain", 1024L);
            RuntimeException exception = new RuntimeException("Unexpected error");

            when(delegate.upload(fileId, file, metadata)).thenThrow(exception);

            // When & Then
            assertThatThrownBy(() -> loggingFileStorage.upload(fileId, file, metadata))
                .isInstanceOf(FileStorageException.class);

            // MDC should be cleared even after exception
            assertThat(MDC.get("operation")).isNull();
            assertThat(MDC.get("fileId")).isNull();
        }
    }

    @Nested
    @DisplayName("Delegation Tests")
    class DelegationTests {

        @Test
        @DisplayName("Should delegate provider name and capabilities")
        void shouldDelegateProviderNameAndCapabilities() {
            // Given
            String expectedProviderName = "test-provider";
            Map<String, Boolean> expectedCapabilities = Map.of("upload", true, "download", true);

            when(delegate.getProviderName()).thenReturn(expectedProviderName);
            when(delegate.getCapabilities()).thenReturn(expectedCapabilities);

            // When
            String providerName = loggingFileStorage.getProviderName();
            Map<String, Boolean> capabilities = loggingFileStorage.getCapabilities();

            // Then
            assertThat(providerName).isEqualTo(expectedProviderName);
            assertThat(capabilities).isEqualTo(expectedCapabilities);
            verify(delegate).getProviderName();
            verify(delegate).getCapabilities();
        }
    }
}
