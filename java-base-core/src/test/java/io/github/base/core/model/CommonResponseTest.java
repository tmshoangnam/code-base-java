package io.github.base.core.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommonResponseTest {

    @Test
    void testSuccessResponse() {
        String data = "test data";
        CommonResponse<String> response = CommonResponse.success(data);

        assertThat(response.getStatus()).isEqualTo("success");
        assertThat(response.getMessage()).isEqualTo("Operation completed successfully");
        assertThat(response.getCode()).isEqualTo("OK");
        assertThat(response.getData()).isEqualTo(data);
        assertThat(response.getTimestamp()).isNotNull();
        assertThat(response.getMetadata()).isEmpty();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.isError()).isFalse();
        assertThat(response.isWarning()).isFalse();
    }

    @Test
    void testSuccessResponseWithCustomMessage() {
        String data = "test data";
        String message = "Custom success message";
        CommonResponse<String> response = CommonResponse.success(data, message);

        assertThat(response.getStatus()).isEqualTo("success");
        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.getCode()).isEqualTo("OK");
        assertThat(response.getData()).isEqualTo(data);
        assertThat(response.isSuccess()).isTrue();
    }

    @Test
    void testErrorResponse() {
        String code = "USER_NOT_FOUND";
        String message = "User does not exist";
        CommonResponse<Void> response = CommonResponse.error(code, message);

        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.getCode()).isEqualTo(code);
        assertThat(response.getData()).isNull();
        assertThat(response.getTimestamp()).isNotNull();
        assertThat(response.getMetadata()).isEmpty();
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.isError()).isTrue();
        assertThat(response.isWarning()).isFalse();
    }

    @Test
    void testErrorResponseWithMetadata() {
        String code = "VALIDATION_ERROR";
        String message = "Validation failed";
        Map<String, Object> metadata = Map.of("field", "email", "reason", "Invalid format");
        CommonResponse<Void> response = CommonResponse.error(code, message, metadata);

        assertThat(response.getStatus()).isEqualTo("error");
        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.getCode()).isEqualTo(code);
        assertThat(response.getData()).isNull();
        assertThat(response.getMetadata()).isEqualTo(metadata);
        assertThat(response.isError()).isTrue();
    }

    @Test
    void testWarningResponse() {
        String data = "warning data";
        String message = "Warning message";
        CommonResponse<String> response = CommonResponse.warning(data, message);

        assertThat(response.getStatus()).isEqualTo("warning");
        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.getCode()).isEqualTo("WARNING");
        assertThat(response.getData()).isEqualTo(data);
        assertThat(response.getTimestamp()).isNotNull();
        assertThat(response.getMetadata()).isEmpty();
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.isError()).isFalse();
        assertThat(response.isWarning()).isTrue();
    }

    @Test
    void testBuilder() {
        String data = "test data";
        String message = "Custom message";
        String code = "CUSTOM_CODE";
        Instant timestamp = Instant.now();
        Map<String, Object> metadata = Map.of("key", "value");

        CommonResponse<String> response = CommonResponse.<String>builder()
                .status("success")
                .message(message)
                .code(code)
                .data(data)
                .timestamp(timestamp)
                .metadata(metadata)
                .build();

        assertThat(response.getStatus()).isEqualTo("success");
        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.getCode()).isEqualTo(code);
        assertThat(response.getData()).isEqualTo(data);
        assertThat(response.getTimestamp()).isEqualTo(timestamp);
        assertThat(response.getMetadata()).isEqualTo(metadata);
    }

    @Test
    void testBuilderWithNullMetadata() {
        CommonResponse<String> response = CommonResponse.<String>builder()
                .status("success")
                .message("message")
                .code("CODE")
                .data("data")
                .metadata(null)
                .build();

        assertThat(response.getMetadata()).isEmpty();
    }

    @Test
    void testBuilderWithMissingStatus() {
        assertThatThrownBy(() -> CommonResponse.<String>builder()
                .message("message")
                .code("CODE")
                .data("data")
                .build())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("status is required");
    }

    @Test
    void testBuilderWithEmptyStatus() {
        assertThatThrownBy(() -> CommonResponse.<String>builder()
                .status("")
                .message("message")
                .code("CODE")
                .data("data")
                .build())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("status is required");
    }

    @Test
    void testBuilderWithMissingMessage() {
        assertThatThrownBy(() -> CommonResponse.<String>builder()
                .status("success")
                .code("CODE")
                .data("data")
                .build())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("message is required");
    }

    @Test
    void testBuilderWithEmptyMessage() {
        assertThatThrownBy(() -> CommonResponse.<String>builder()
                .status("success")
                .message("")
                .code("CODE")
                .data("data")
                .build())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("message is required");
    }

    @Test
    void testBuilderWithMissingCode() {
        assertThatThrownBy(() -> CommonResponse.<String>builder()
                .status("success")
                .message("message")
                .data("data")
                .build())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("code is required");
    }

    @Test
    void testBuilderWithEmptyCode() {
        assertThatThrownBy(() -> CommonResponse.<String>builder()
                .status("success")
                .message("message")
                .code("")
                .data("data")
                .build())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("code is required");
    }
}
