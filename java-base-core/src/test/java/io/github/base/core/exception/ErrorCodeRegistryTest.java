package io.github.base.core.exception;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ErrorCodeRegistryTest {

    @AfterEach
    void tearDown() {
        ErrorCodeRegistry.clear();
    }

    @Test
    void testRegisterErrorCode() {
        ErrorCodeRegistry.register("TEST_ERROR", "Test error description");
        
        assertThat(ErrorCodeRegistry.exists("TEST_ERROR")).isTrue();
        assertThat(ErrorCodeRegistry.getDescription("TEST_ERROR")).isEqualTo("Test error description");
    }

    @Test
    void testRegisterMultipleErrorCodes() {
        Map<String, String> errorCodes = Map.of(
            "ERROR_1", "First error",
            "ERROR_2", "Second error",
            "ERROR_3", "Third error"
        );
        
        ErrorCodeRegistry.registerAll(errorCodes);
        
        assertThat(ErrorCodeRegistry.exists("ERROR_1")).isTrue();
        assertThat(ErrorCodeRegistry.exists("ERROR_2")).isTrue();
        assertThat(ErrorCodeRegistry.exists("ERROR_3")).isTrue();
        assertThat(ErrorCodeRegistry.getDescription("ERROR_1")).isEqualTo("First error");
        assertThat(ErrorCodeRegistry.getDescription("ERROR_2")).isEqualTo("Second error");
        assertThat(ErrorCodeRegistry.getDescription("ERROR_3")).isEqualTo("Third error");
    }

    @Test
    void testGetDescriptionForNonExistentErrorCode() {
        String description = ErrorCodeRegistry.getDescription("NON_EXISTENT");
        assertThat(description).isNull();
    }

    @Test
    void testGetDescriptionForNullErrorCode() {
        String description = ErrorCodeRegistry.getDescription(null);
        assertThat(description).isNull();
    }

    @Test
    void testExistsForNonExistentErrorCode() {
        boolean exists = ErrorCodeRegistry.exists("NON_EXISTENT");
        assertThat(exists).isFalse();
    }

    @Test
    void testExistsForNullErrorCode() {
        boolean exists = ErrorCodeRegistry.exists(null);
        assertThat(exists).isFalse();
    }

    @Test
    void testGetAllErrorCodes() {
        ErrorCodeRegistry.register("ERROR_1", "First error");
        ErrorCodeRegistry.register("ERROR_2", "Second error");
        
        Map<String, String> allCodes = ErrorCodeRegistry.getAllErrorCodes();
        
        assertThat(allCodes).hasSize(2);
        assertThat(allCodes).containsEntry("ERROR_1", "First error");
        assertThat(allCodes).containsEntry("ERROR_2", "Second error");
    }

    @Test
    void testClear() {
        ErrorCodeRegistry.register("ERROR_1", "First error");
        assertThat(ErrorCodeRegistry.size()).isEqualTo(1);
        
        ErrorCodeRegistry.clear();
        assertThat(ErrorCodeRegistry.size()).isEqualTo(0);
        assertThat(ErrorCodeRegistry.exists("ERROR_1")).isFalse();
    }

    @Test
    void testSize() {
        assertThat(ErrorCodeRegistry.size()).isEqualTo(0);
        
        ErrorCodeRegistry.register("ERROR_1", "First error");
        assertThat(ErrorCodeRegistry.size()).isEqualTo(1);
        
        ErrorCodeRegistry.register("ERROR_2", "Second error");
        assertThat(ErrorCodeRegistry.size()).isEqualTo(2);
    }

    @Test
    void testRegisterWithNullErrorCode() {
        assertThatThrownBy(() -> ErrorCodeRegistry.register(null, "Description"))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("errorCode cannot be null");
    }

    @Test
    void testRegisterWithEmptyErrorCode() {
        assertThatThrownBy(() -> ErrorCodeRegistry.register("", "Description"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("errorCode cannot be empty");
    }

    @Test
    void testRegisterWithBlankErrorCode() {
        assertThatThrownBy(() -> ErrorCodeRegistry.register("   ", "Description"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("errorCode cannot be empty");
    }

    @Test
    void testRegisterWithNullDescription() {
        assertThatThrownBy(() -> ErrorCodeRegistry.register("ERROR", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("description cannot be null");
    }

    @Test
    void testRegisterWithEmptyDescription() {
        assertThatThrownBy(() -> ErrorCodeRegistry.register("ERROR", ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("description cannot be empty");
    }

    @Test
    void testRegisterAllWithNull() {
        assertThatThrownBy(() -> ErrorCodeRegistry.registerAll(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("errorCodes cannot be null");
    }
}
