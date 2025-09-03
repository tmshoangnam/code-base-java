package com.mycompany.base.core;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StringUtilsTest {

    @Test
    void testIsBlank() {
        assertThat(StringUtils.isBlank(null)).isTrue();
        assertThat(StringUtils.isBlank("   ")).isTrue();
        assertThat(StringUtils.isBlank("abc")).isFalse();
    }

    @Test
    void testHasText() {
        assertThat(StringUtils.hasText("abc")).isTrue();
        assertThat(StringUtils.hasText("")).isFalse();
    }
}