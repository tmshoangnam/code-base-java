package io.github.base.aws.api.s3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for {@link S3UploadResult}.
 *
 * @author java-base-team
 * @since 1.0.5-SNAPSHOT
 */
@DisplayName("S3UploadResult Tests")
class S3UploadResultTest {

    private static final String BUCKET_NAME = "test-bucket";
    private static final String KEY = "test-file.txt";
    private static final String ETAG = "\"d41d8cd98f00b204e9800998ecf8427e\"";
    private static final String VERSION_ID = "version-123";
    private static final String ERROR_MESSAGE = "Upload failed";
    private static final String ERROR_CODE = "AccessDenied";
    private static final S3Metadata METADATA = S3Metadata.of("test-bucket", "test-file.txt", "text/plain", 1024);

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create S3UploadResult with all parameters")
        void shouldCreateS3UploadResultWithAllParameters() {
            // Given
            Instant now = Instant.now();

            // When
            S3UploadResult result = new S3UploadResult(
                true, BUCKET_NAME, KEY, ETAG, VERSION_ID, "s3://test-bucket/test-file.txt",
                null, null, now, METADATA
            );

            // Then
            assertThat(result.success()).isTrue();
            assertThat(result.bucketName()).isEqualTo(BUCKET_NAME);
            assertThat(result.key()).isEqualTo(KEY);
            assertThat(result.etag()).isEqualTo(ETAG);
            assertThat(result.versionId()).isEqualTo(VERSION_ID);
            assertThat(result.location()).isEqualTo("s3://test-bucket/test-file.txt");
            assertThat(result.errorMessage()).isNull();
            assertThat(result.errorCode()).isNull();
            assertThat(result.uploadedAt()).isEqualTo(now);
            assertThat(result.metadata()).isEqualTo(METADATA);
        }

        @Test
        @DisplayName("Should create S3UploadResult with null values")
        void shouldCreateS3UploadResultWithNullValues() {
            // When
            S3UploadResult result = new S3UploadResult(
                false, null, null, null, null, null, null, null, null, null
            );

            // Then
            assertThat(result.success()).isFalse();
            assertThat(result.bucketName()).isNull();
            assertThat(result.key()).isNull();
            assertThat(result.etag()).isNull();
            assertThat(result.versionId()).isNull();
            assertThat(result.location()).isNull();
            assertThat(result.errorMessage()).isNull();
            assertThat(result.errorCode()).isNull();
            assertThat(result.uploadedAt()).isNull();
            assertThat(result.metadata()).isNull();
        }
    }

    @Nested
    @DisplayName("Factory Method Tests - Success")
    class SuccessFactoryMethodTests {

        @Test
        @DisplayName("success method with basic parameters should create successful result")
        void successMethodWithBasicParametersShouldCreateSuccessfulResult() {
            // When
            S3UploadResult result = S3UploadResult.success(BUCKET_NAME, KEY, ETAG);

            // Then
            assertThat(result.success()).isTrue();
            assertThat(result.bucketName()).isEqualTo(BUCKET_NAME);
            assertThat(result.key()).isEqualTo(KEY);
            assertThat(result.etag()).isEqualTo(ETAG);
            assertThat(result.versionId()).isNull();
            assertThat(result.location()).isEqualTo("s3://" + BUCKET_NAME + "/" + KEY);
            assertThat(result.errorMessage()).isNull();
            assertThat(result.errorCode()).isNull();
            assertThat(result.uploadedAt()).isNotNull();
            assertThat(result.metadata()).isNull();
        }

        @Test
        @DisplayName("success method with metadata should create successful result")
        void successMethodWithMetadataShouldCreateSuccessfulResult() {
            // When
            S3UploadResult result = S3UploadResult.success(BUCKET_NAME, KEY, ETAG, METADATA);

            // Then
            assertThat(result.success()).isTrue();
            assertThat(result.bucketName()).isEqualTo(BUCKET_NAME);
            assertThat(result.key()).isEqualTo(KEY);
            assertThat(result.etag()).isEqualTo(ETAG);
            assertThat(result.versionId()).isNull();
            assertThat(result.location()).isEqualTo("s3://" + BUCKET_NAME + "/" + KEY);
            assertThat(result.errorMessage()).isNull();
            assertThat(result.errorCode()).isNull();
            assertThat(result.uploadedAt()).isNotNull();
            assertThat(result.metadata()).isEqualTo(METADATA);
        }

        @Test
        @DisplayName("success method with version ID should create successful result")
        void successMethodWithVersionIdShouldCreateSuccessfulResult() {
            // When
            S3UploadResult result = S3UploadResult.success(BUCKET_NAME, KEY, ETAG, VERSION_ID, METADATA);

            // Then
            assertThat(result.success()).isTrue();
            assertThat(result.bucketName()).isEqualTo(BUCKET_NAME);
            assertThat(result.key()).isEqualTo(KEY);
            assertThat(result.etag()).isEqualTo(ETAG);
            assertThat(result.versionId()).isEqualTo(VERSION_ID);
            assertThat(result.location()).isEqualTo("s3://" + BUCKET_NAME + "/" + KEY);
            assertThat(result.errorMessage()).isNull();
            assertThat(result.errorCode()).isNull();
            assertThat(result.uploadedAt()).isNotNull();
            assertThat(result.metadata()).isEqualTo(METADATA);
        }

        @Test
        @DisplayName("success method should trim whitespace from bucket name and key")
        void successMethodShouldTrimWhitespaceFromBucketNameAndKey() {
            // Given
            String bucketNameWithWhitespace = "  " + BUCKET_NAME + "  ";
            String keyWithWhitespace = "  " + KEY + "  ";

            // When
            S3UploadResult result = S3UploadResult.success(bucketNameWithWhitespace, keyWithWhitespace, ETAG);

            // Then
            assertThat(result.bucketName()).isEqualTo(BUCKET_NAME);
            assertThat(result.key()).isEqualTo(KEY);
        }

        @Test
        @DisplayName("success method should throw IllegalArgumentException for null bucket name")
        void successMethodShouldThrowIllegalArgumentExceptionForNullBucketName() {
            // When & Then
            assertThatThrownBy(() -> S3UploadResult.success(null, KEY, ETAG))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Bucket name cannot be null or empty");
        }

        @Test
        @DisplayName("success method should throw IllegalArgumentException for empty bucket name")
        void successMethodShouldThrowIllegalArgumentExceptionForEmptyBucketName() {
            // When & Then
            assertThatThrownBy(() -> S3UploadResult.success("", KEY, ETAG))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Bucket name cannot be null or empty");
        }

        @Test
        @DisplayName("success method should throw IllegalArgumentException for blank bucket name")
        void successMethodShouldThrowIllegalArgumentExceptionForBlankBucketName() {
            // When & Then
            assertThatThrownBy(() -> S3UploadResult.success("   ", KEY, ETAG))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Bucket name cannot be null or empty");
        }

        @Test
        @DisplayName("success method should throw IllegalArgumentException for null key")
        void successMethodShouldThrowIllegalArgumentExceptionForNullKey() {
            // When & Then
            assertThatThrownBy(() -> S3UploadResult.success(BUCKET_NAME, null, ETAG))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Key cannot be null or empty");
        }

        @Test
        @DisplayName("success method should throw IllegalArgumentException for empty key")
        void successMethodShouldThrowIllegalArgumentExceptionForEmptyKey() {
            // When & Then
            assertThatThrownBy(() -> S3UploadResult.success(BUCKET_NAME, "", ETAG))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Key cannot be null or empty");
        }

        @Test
        @DisplayName("success method should throw IllegalArgumentException for blank key")
        void successMethodShouldThrowIllegalArgumentExceptionForBlankKey() {
            // When & Then
            assertThatThrownBy(() -> S3UploadResult.success(BUCKET_NAME, "   ", ETAG))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Key cannot be null or empty");
        }
    }

    @Nested
    @DisplayName("Factory Method Tests - Failure")
    class FailureFactoryMethodTests {

        @Test
        @DisplayName("failure method with error message should create failed result")
        void failureMethodWithErrorMessageShouldCreateFailedResult() {
            // When
            S3UploadResult result = S3UploadResult.failure(BUCKET_NAME, KEY, ERROR_MESSAGE);

            // Then
            assertThat(result.success()).isFalse();
            assertThat(result.bucketName()).isEqualTo(BUCKET_NAME);
            assertThat(result.key()).isEqualTo(KEY);
            assertThat(result.etag()).isNull();
            assertThat(result.versionId()).isNull();
            assertThat(result.location()).isNull();
            assertThat(result.errorMessage()).isEqualTo(ERROR_MESSAGE);
            assertThat(result.errorCode()).isNull();
            assertThat(result.uploadedAt()).isNotNull();
            assertThat(result.metadata()).isNull();
        }

        @Test
        @DisplayName("failure method with error code should create failed result")
        void failureMethodWithErrorCodeShouldCreateFailedResult() {
            // When
            S3UploadResult result = S3UploadResult.failure(BUCKET_NAME, KEY, ERROR_MESSAGE, ERROR_CODE);

            // Then
            assertThat(result.success()).isFalse();
            assertThat(result.bucketName()).isEqualTo(BUCKET_NAME);
            assertThat(result.key()).isEqualTo(KEY);
            assertThat(result.etag()).isNull();
            assertThat(result.versionId()).isNull();
            assertThat(result.location()).isNull();
            assertThat(result.errorMessage()).isEqualTo(ERROR_MESSAGE);
            assertThat(result.errorCode()).isEqualTo(ERROR_CODE);
            assertThat(result.uploadedAt()).isNotNull();
            assertThat(result.metadata()).isNull();
        }

        @Test
        @DisplayName("failure method should trim whitespace from parameters")
        void failureMethodShouldTrimWhitespaceFromParameters() {
            // Given
            String bucketNameWithWhitespace = "  " + BUCKET_NAME + "  ";
            String keyWithWhitespace = "  " + KEY + "  ";
            String errorMessageWithWhitespace = "  " + ERROR_MESSAGE + "  ";

            // When
            S3UploadResult result = S3UploadResult.failure(bucketNameWithWhitespace, keyWithWhitespace, errorMessageWithWhitespace);

            // Then
            assertThat(result.bucketName()).isEqualTo(BUCKET_NAME);
            assertThat(result.key()).isEqualTo(KEY);
            assertThat(result.errorMessage()).isEqualTo(ERROR_MESSAGE);
        }

        @Test
        @DisplayName("failure method should throw IllegalArgumentException for null bucket name")
        void failureMethodShouldThrowIllegalArgumentExceptionForNullBucketName() {
            // When & Then
            assertThatThrownBy(() -> S3UploadResult.failure(null, KEY, ERROR_MESSAGE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Bucket name cannot be null or empty");
        }

        @Test
        @DisplayName("failure method should throw IllegalArgumentException for null key")
        void failureMethodShouldThrowIllegalArgumentExceptionForNullKey() {
            // When & Then
            assertThatThrownBy(() -> S3UploadResult.failure(BUCKET_NAME, null, ERROR_MESSAGE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Key cannot be null or empty");
        }

        @Test
        @DisplayName("failure method should throw IllegalArgumentException for null error message")
        void failureMethodShouldThrowIllegalArgumentExceptionForNullErrorMessage() {
            // When & Then
            assertThatThrownBy(() -> S3UploadResult.failure(BUCKET_NAME, KEY, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Error message cannot be null or empty");
        }

        @Test
        @DisplayName("failure method should throw IllegalArgumentException for empty error message")
        void failureMethodShouldThrowIllegalArgumentExceptionForEmptyErrorMessage() {
            // When & Then
            assertThatThrownBy(() -> S3UploadResult.failure(BUCKET_NAME, KEY, ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Error message cannot be null or empty");
        }

        @Test
        @DisplayName("failure method should throw IllegalArgumentException for blank error message")
        void failureMethodShouldThrowIllegalArgumentExceptionForBlankErrorMessage() {
            // When & Then
            assertThatThrownBy(() -> S3UploadResult.failure(BUCKET_NAME, KEY, "   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Error message cannot be null or empty");
        }
    }

    @Nested
    @DisplayName("Status Check Tests")
    class StatusCheckTests {

        @Test
        @DisplayName("isSuccess should return true for successful result")
        void isSuccessShouldReturnTrueForSuccessfulResult() {
            // Given
            S3UploadResult result = S3UploadResult.success(BUCKET_NAME, KEY, ETAG);

            // When & Then
            assertThat(result.isSuccess()).isTrue();
        }

        @Test
        @DisplayName("isSuccess should return false for failed result")
        void isSuccessShouldReturnFalseForFailedResult() {
            // Given
            S3UploadResult result = S3UploadResult.failure(BUCKET_NAME, KEY, ERROR_MESSAGE);

            // When & Then
            assertThat(result.isSuccess()).isFalse();
        }

        @Test
        @DisplayName("isFailure should return false for successful result")
        void isFailureShouldReturnFalseForSuccessfulResult() {
            // Given
            S3UploadResult result = S3UploadResult.success(BUCKET_NAME, KEY, ETAG);

            // When & Then
            assertThat(result.isFailure()).isFalse();
        }

        @Test
        @DisplayName("isFailure should return true for failed result")
        void isFailureShouldReturnTrueForFailedResult() {
            // Given
            S3UploadResult result = S3UploadResult.failure(BUCKET_NAME, KEY, ERROR_MESSAGE);

            // When & Then
            assertThat(result.isFailure()).isTrue();
        }
    }

    @Nested
    @DisplayName("Property Access Tests")
    class PropertyAccessTests {

        @Test
        @DisplayName("getBucketName should return bucket name")
        void getBucketNameShouldReturnBucketName() {
            // Given
            S3UploadResult result = S3UploadResult.success(BUCKET_NAME, KEY, ETAG);

            // When
            String bucketName = result.getBucketName();

            // Then
            assertThat(bucketName).isEqualTo(BUCKET_NAME);
        }

        @Test
        @DisplayName("getKey should return key")
        void getKeyShouldReturnKey() {
            // Given
            S3UploadResult result = S3UploadResult.success(BUCKET_NAME, KEY, ETAG);

            // When
            String key = result.getKey();

            // Then
            assertThat(key).isEqualTo(KEY);
        }

        @Test
        @DisplayName("getEtag should return ETag for successful result")
        void getEtagShouldReturnEtagForSuccessfulResult() {
            // Given
            S3UploadResult result = S3UploadResult.success(BUCKET_NAME, KEY, ETAG);

            // When
            String etag = result.getEtag();

            // Then
            assertThat(etag).isEqualTo(ETAG);
        }

        @Test
        @DisplayName("getEtag should return null for failed result")
        void getEtagShouldReturnNullForFailedResult() {
            // Given
            S3UploadResult result = S3UploadResult.failure(BUCKET_NAME, KEY, ERROR_MESSAGE);

            // When
            String etag = result.getEtag();

            // Then
            assertThat(etag).isNull();
        }

        @Test
        @DisplayName("getVersionId should return version ID when present")
        void getVersionIdShouldReturnVersionIdWhenPresent() {
            // Given
            S3UploadResult result = S3UploadResult.success(BUCKET_NAME, KEY, ETAG, VERSION_ID, METADATA);

            // When
            String versionId = result.getVersionId();

            // Then
            assertThat(versionId).isEqualTo(VERSION_ID);
        }

        @Test
        @DisplayName("getVersionId should return null when not present")
        void getVersionIdShouldReturnNullWhenNotPresent() {
            // Given
            S3UploadResult result = S3UploadResult.success(BUCKET_NAME, KEY, ETAG);

            // When
            String versionId = result.getVersionId();

            // Then
            assertThat(versionId).isNull();
        }

        @Test
        @DisplayName("getLocation should return location for successful result")
        void getLocationShouldReturnLocationForSuccessfulResult() {
            // Given
            S3UploadResult result = S3UploadResult.success(BUCKET_NAME, KEY, ETAG);

            // When
            String location = result.getLocation();

            // Then
            assertThat(location).isEqualTo("s3://" + BUCKET_NAME + "/" + KEY);
        }

        @Test
        @DisplayName("getLocation should return null for failed result")
        void getLocationShouldReturnNullForFailedResult() {
            // Given
            S3UploadResult result = S3UploadResult.failure(BUCKET_NAME, KEY, ERROR_MESSAGE);

            // When
            String location = result.getLocation();

            // Then
            assertThat(location).isNull();
        }

        @Test
        @DisplayName("getErrorMessage should return null for successful result")
        void getErrorMessageShouldReturnNullForSuccessfulResult() {
            // Given
            S3UploadResult result = S3UploadResult.success(BUCKET_NAME, KEY, ETAG);

            // When
            String errorMessage = result.getErrorMessage();

            // Then
            assertThat(errorMessage).isNull();
        }

        @Test
        @DisplayName("getErrorMessage should return error message for failed result")
        void getErrorMessageShouldReturnErrorMessageForFailedResult() {
            // Given
            S3UploadResult result = S3UploadResult.failure(BUCKET_NAME, KEY, ERROR_MESSAGE);

            // When
            String errorMessage = result.getErrorMessage();

            // Then
            assertThat(errorMessage).isEqualTo(ERROR_MESSAGE);
        }

        @Test
        @DisplayName("getErrorCode should return null for successful result")
        void getErrorCodeShouldReturnNullForSuccessfulResult() {
            // Given
            S3UploadResult result = S3UploadResult.success(BUCKET_NAME, KEY, ETAG);

            // When
            String errorCode = result.getErrorCode();

            // Then
            assertThat(errorCode).isNull();
        }

        @Test
        @DisplayName("getErrorCode should return error code for failed result")
        void getErrorCodeShouldReturnErrorCodeForFailedResult() {
            // Given
            S3UploadResult result = S3UploadResult.failure(BUCKET_NAME, KEY, ERROR_MESSAGE, ERROR_CODE);

            // When
            String errorCode = result.getErrorCode();

            // Then
            assertThat(errorCode).isEqualTo(ERROR_CODE);
        }

        @Test
        @DisplayName("getUploadedAt should return upload timestamp")
        void getUploadedAtShouldReturnUploadTimestamp() {
            // Given
            S3UploadResult result = S3UploadResult.success(BUCKET_NAME, KEY, ETAG);

            // When
            Instant uploadedAt = result.getUploadedAt();

            // Then
            assertThat(uploadedAt).isNotNull();
            assertThat(uploadedAt).isBeforeOrEqualTo(Instant.now());
        }

        @Test
        @DisplayName("getMetadata should return metadata for successful result")
        void getMetadataShouldReturnMetadataForSuccessfulResult() {
            // Given
            S3UploadResult result = S3UploadResult.success(BUCKET_NAME, KEY, ETAG, METADATA);

            // When
            S3Metadata metadata = result.getMetadata();

            // Then
            assertThat(metadata).isEqualTo(METADATA);
        }

        @Test
        @DisplayName("getMetadata should return null for failed result")
        void getMetadataShouldReturnNullForFailedResult() {
            // Given
            S3UploadResult result = S3UploadResult.failure(BUCKET_NAME, KEY, ERROR_MESSAGE);

            // When
            S3Metadata metadata = result.getMetadata();

            // Then
            assertThat(metadata).isNull();
        }
    }

    @Nested
    @DisplayName("Versioning Tests")
    class VersioningTests {

        @Test
        @DisplayName("hasVersioning should return true when version ID is present")
        void hasVersioningShouldReturnTrueWhenVersionIdIsPresent() {
            // Given
            S3UploadResult result = S3UploadResult.success(BUCKET_NAME, KEY, ETAG, VERSION_ID, METADATA);

            // When & Then
            assertThat(result.hasVersioning()).isTrue();
        }

        @Test
        @DisplayName("hasVersioning should return false when version ID is null")
        void hasVersioningShouldReturnFalseWhenVersionIdIsNull() {
            // Given
            S3UploadResult result = S3UploadResult.success(BUCKET_NAME, KEY, ETAG);

            // When & Then
            assertThat(result.hasVersioning()).isFalse();
        }

        @Test
        @DisplayName("hasVersioning should return false when version ID is empty")
        void hasVersioningShouldReturnFalseWhenVersionIdIsEmpty() {
            // Given
            S3UploadResult result = S3UploadResult.success(BUCKET_NAME, KEY, ETAG, "", METADATA);

            // When & Then
            assertThat(result.hasVersioning()).isFalse();
        }

        @Test
        @DisplayName("hasVersioning should return false when version ID is blank")
        void hasVersioningShouldReturnFalseWhenVersionIdIsBlank() {
            // Given
            S3UploadResult result = S3UploadResult.success(BUCKET_NAME, KEY, ETAG, "   ", METADATA);

            // When & Then
            assertThat(result.hasVersioning()).isFalse();
        }
    }

    @Nested
    @DisplayName("Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal to identical S3UploadResult")
        void shouldBeEqualToIdenticalS3UploadResult() {
            // Given
            S3UploadResult result1 = S3UploadResult.success(BUCKET_NAME, KEY, ETAG, VERSION_ID, METADATA);
            S3UploadResult result2 = S3UploadResult.success(BUCKET_NAME, KEY, ETAG, VERSION_ID, METADATA);

            // When & Then
            assertThat(result1).isEqualTo(result2);
            assertThat(result1.hashCode()).isEqualTo(result2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal to different S3UploadResult")
        void shouldNotBeEqualToDifferentS3UploadResult() {
            // Given
            S3UploadResult result1 = S3UploadResult.success(BUCKET_NAME, KEY, ETAG, VERSION_ID, METADATA);
            S3UploadResult result2 = S3UploadResult.success("different-bucket", KEY, ETAG, VERSION_ID, METADATA);

            // When & Then
            assertThat(result1).isNotEqualTo(result2);
        }

        @Test
        @DisplayName("Should not be equal to null")
        void shouldNotBeEqualToNull() {
            // Given
            S3UploadResult result = S3UploadResult.success(BUCKET_NAME, KEY, ETAG);

            // When & Then
            assertThat(result).isNotEqualTo(null);
        }

        @Test
        @DisplayName("Should not be equal to different type")
        void shouldNotBeEqualToDifferentType() {
            // Given
            S3UploadResult result = S3UploadResult.success(BUCKET_NAME, KEY, ETAG);
            String differentType = "not-result";

            // When & Then
            assertThat(result).isNotEqualTo(differentType);
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("toString should contain all fields for successful result")
        void toStringShouldContainAllFieldsForSuccessfulResult() {
            // Given
            S3UploadResult result = S3UploadResult.success(BUCKET_NAME, KEY, ETAG, VERSION_ID, METADATA);

            // When
            String string = result.toString();

            // Then
            assertThat(string).contains("S3UploadResult{");
            assertThat(string).contains("success=true");
            assertThat(string).contains("bucketName='" + BUCKET_NAME + "'");
            assertThat(string).contains("key='" + KEY + "'");
            assertThat(string).contains("etag='" + ETAG + "'");
            assertThat(string).contains("versionId='" + VERSION_ID + "'");
            assertThat(string).contains("location='s3://" + BUCKET_NAME + "/" + KEY + "'");
            assertThat(string).contains("errorMessage='null'");
            assertThat(string).contains("errorCode='null'");
            assertThat(string).contains("uploadedAt=");
            assertThat(string).contains("metadata=" + METADATA);
        }

        @Test
        @DisplayName("toString should contain all fields for failed result")
        void toStringShouldContainAllFieldsForFailedResult() {
            // Given
            S3UploadResult result = S3UploadResult.failure(BUCKET_NAME, KEY, ERROR_MESSAGE, ERROR_CODE);

            // When
            String string = result.toString();

            // Then
            assertThat(string).contains("S3UploadResult{");
            assertThat(string).contains("success=false");
            assertThat(string).contains("bucketName='" + BUCKET_NAME + "'");
            assertThat(string).contains("key='" + KEY + "'");
            assertThat(string).contains("etag='null'");
            assertThat(string).contains("versionId='null'");
            assertThat(string).contains("location='null'");
            assertThat(string).contains("errorMessage='" + ERROR_MESSAGE + "'");
            assertThat(string).contains("errorCode='" + ERROR_CODE + "'");
            assertThat(string).contains("uploadedAt=");
            assertThat(string).contains("metadata=null");
        }

        @Test
        @DisplayName("toString should handle null values")
        void toStringShouldHandleNullValues() {
            // Given
            S3UploadResult result = new S3UploadResult(
                false, null, null, null, null, null, null, null, null, null
            );

            // When
            String string = result.toString();

            // Then
            assertThat(string).contains("success=false");
            assertThat(string).contains("bucketName='null'");
            assertThat(string).contains("key='null'");
            assertThat(string).contains("etag='null'");
            assertThat(string).contains("versionId='null'");
            assertThat(string).contains("location='null'");
            assertThat(string).contains("errorMessage='null'");
            assertThat(string).contains("errorCode='null'");
            assertThat(string).contains("uploadedAt=null");
            assertThat(string).contains("metadata=null");
        }
    }
}
