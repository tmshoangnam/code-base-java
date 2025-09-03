package com.mycompany.base.core.api.logging;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CorrelationIdGeneratorTest {

    @Test
    void testGenerateDefault() {
        String correlationId = CorrelationIdGenerator.generate();

        assertThat(correlationId).isNotNull();
        assertThat(correlationId).startsWith("req-");
        assertThat(correlationId).matches("req-\\d{8}-\\d{6}-[a-z0-9]{12}");
    }

    @Test
    void testGenerateWithPrefix() {
        String correlationId = CorrelationIdGenerator.generate("payment");

        assertThat(correlationId).isNotNull();
        assertThat(correlationId).startsWith("payment-");
        assertThat(correlationId).matches("payment-\\d{8}-\\d{6}-[a-z0-9]{12}");
    }

    @Test
    void testGenerateWithPrefixAndLength() {
        String correlationId = CorrelationIdGenerator.generate("api", 8);

        assertThat(correlationId).isNotNull();
        assertThat(correlationId).startsWith("api-");
        assertThat(correlationId).matches("api-\\d{8}-\\d{6}-[a-z0-9]{8}");
    }

    @Test
    void testGenerateSimple() {
        String correlationId = CorrelationIdGenerator.generateSimple("test");

        assertThat(correlationId).isNotNull();
        assertThat(correlationId).startsWith("test-");
        assertThat(correlationId).matches("test-[a-z0-9]{12}");
    }

    @Test
    void testGenerateSimpleWithLength() {
        String correlationId = CorrelationIdGenerator.generateSimple("test", 6);

        assertThat(correlationId).isNotNull();
        assertThat(correlationId).startsWith("test-");
        assertThat(correlationId).matches("test-[a-z0-9]{6}");
    }

    @Test
    void testGenerateUniqueIds() {
        String id1 = CorrelationIdGenerator.generate();
        String id2 = CorrelationIdGenerator.generate();

        assertThat(id1).isNotEqualTo(id2);
    }

    @Test
    void testGenerateWithNullPrefix() {
        assertThatThrownBy(() -> CorrelationIdGenerator.generate(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("prefix cannot be null");
    }

    @Test
    void testGenerateWithEmptyPrefix() {
        assertThatThrownBy(() -> CorrelationIdGenerator.generate(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("prefix cannot be empty");
    }

    @Test
    void testGenerateWithBlankPrefix() {
        assertThatThrownBy(() -> CorrelationIdGenerator.generate("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("prefix cannot be empty");
    }

    @Test
    void testGenerateWithInvalidLength() {
        assertThatThrownBy(() -> CorrelationIdGenerator.generate("test", 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("randomLength must be positive");
    }

    @Test
    void testGenerateWithExcessiveLength() {
        assertThatThrownBy(() -> CorrelationIdGenerator.generate("test", 51))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("randomLength cannot exceed 50");
    }

    @Test
    void testGenerateSimpleWithNullPrefix() {
        assertThatThrownBy(() -> CorrelationIdGenerator.generateSimple(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("prefix cannot be null");
    }

    @Test
    void testGenerateSimpleWithEmptyPrefix() {
        assertThatThrownBy(() -> CorrelationIdGenerator.generateSimple(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("prefix cannot be empty");
    }
}
