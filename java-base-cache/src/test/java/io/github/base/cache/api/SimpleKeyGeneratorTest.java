package io.github.base.cache.api;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for SimpleKeyGenerator.
 *
 * @since 1.0.0
 */
class SimpleKeyGeneratorTest {

    private final SimpleKeyGenerator generator = new SimpleKeyGenerator();

    @Test
    void shouldGenerateKeyWithSingleParameter() {
        String key = generator.generate("user");
        assertThat(key).isEqualTo("user");
    }

    @Test
    void shouldGenerateKeyWithMultipleParameters() {
        String key = generator.generate("user", 123, "orders");
        assertThat(key).isEqualTo("user:123:orders");
    }

    @Test
    void shouldHandleNullParameters() {
        String key = generator.generate("user", null, "orders");
        assertThat(key).isEqualTo("user:null:orders");
    }

    @Test
    void shouldHandleEmptyParameters() {
        String key = generator.generate();
        assertThat(key).isEqualTo("");
    }

    @Test
    void shouldHandleNullArray() {
        String key = generator.generate((Object[]) null);
        assertThat(key).isEqualTo("");
    }

    @Test
    void shouldUseCustomSeparator() {
        SimpleKeyGenerator customGenerator = new SimpleKeyGenerator("-");
        String key = customGenerator.generate("user", 123, "orders");
        assertThat(key).isEqualTo("user-123-orders");
    }

    @Test
    void shouldThrowExceptionForNullSeparator() {
        assertThatThrownBy(() -> new SimpleKeyGenerator(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Separator cannot be null");
    }
}
