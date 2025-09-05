package io.github.base.core.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StringUtilsTest {

    @Test
    void testIsBlank() {
        assertThat(StringUtils.isBlank(null)).isTrue();
        assertThat(StringUtils.isBlank("")).isTrue();
        assertThat(StringUtils.isBlank("   ")).isTrue();
        assertThat(StringUtils.isBlank("\t\n")).isTrue();
        assertThat(StringUtils.isBlank("abc")).isFalse();
        assertThat(StringUtils.isBlank(" abc ")).isFalse();
    }

    @Test
    void testHasText() {
        assertThat(StringUtils.hasText("abc")).isTrue();
        assertThat(StringUtils.hasText(" abc ")).isTrue();
        assertThat(StringUtils.hasText(null)).isFalse();
        assertThat(StringUtils.hasText("")).isFalse();
        assertThat(StringUtils.hasText("   ")).isFalse();
    }

    @Test
    void testDefaultIfBlank() {
        assertThat(StringUtils.defaultIfBlank(null, "default")).isEqualTo("default");
        assertThat(StringUtils.defaultIfBlank("", "default")).isEqualTo("default");
        assertThat(StringUtils.defaultIfBlank("   ", "default")).isEqualTo("default");
        assertThat(StringUtils.defaultIfBlank("value", "default")).isEqualTo("value");
        assertThat(StringUtils.defaultIfBlank(" value ", "default")).isEqualTo(" value ");
    }

    @Test
    void testIsEmpty() {
        assertThat(StringUtils.isEmpty(null)).isTrue();
        assertThat(StringUtils.isEmpty("")).isTrue();
        assertThat(StringUtils.isEmpty("   ")).isFalse();
        assertThat(StringUtils.isEmpty("abc")).isFalse();
    }

    @Test
    void testIsNotEmpty() {
        assertThat(StringUtils.isNotEmpty("abc")).isTrue();
        assertThat(StringUtils.isNotEmpty("   ")).isTrue();
        assertThat(StringUtils.isNotEmpty(null)).isFalse();
        assertThat(StringUtils.isNotEmpty("")).isFalse();
    }

    @Test
    void testTrimToNull() {
        assertThat(StringUtils.trimToNull(null)).isNull();
        assertThat(StringUtils.trimToNull("")).isNull();
        assertThat(StringUtils.trimToNull("   ")).isNull();
        assertThat(StringUtils.trimToNull("abc")).isEqualTo("abc");
        assertThat(StringUtils.trimToNull(" abc ")).isEqualTo("abc");
    }

    @Test
    void testTrimToEmpty() {
        assertThat(StringUtils.trimToEmpty(null)).isEqualTo("");
        assertThat(StringUtils.trimToEmpty("")).isEqualTo("");
        assertThat(StringUtils.trimToEmpty("   ")).isEqualTo("");
        assertThat(StringUtils.trimToEmpty("abc")).isEqualTo("abc");
        assertThat(StringUtils.trimToEmpty(" abc ")).isEqualTo("abc");
    }
}