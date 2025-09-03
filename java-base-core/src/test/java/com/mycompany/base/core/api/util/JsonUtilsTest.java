package com.mycompany.base.core.api.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JsonUtilsTest {

    private static class TestObject {
        private String name;
        private int age;
        private LocalDateTime timestamp;

        public TestObject() {}

        public TestObject(String name, int age, LocalDateTime timestamp) {
            this.name = name;
            this.age = age;
            this.timestamp = timestamp;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    }

    @Test
    void testToJson() throws JsonProcessingException {
        TestObject obj = new TestObject("John", 30, LocalDateTime.of(2023, 12, 1, 14, 30, 45));
        String json = JsonUtils.toJson(obj);

        assertThat(json).isNotNull();
        assertThat(json).contains("John");
        assertThat(json).contains("30");
    }

    @Test
    void testToJsonWithNull() throws JsonProcessingException {
        String json = JsonUtils.toJson(null);

        assertThat(json).isNull();
    }

    @Test
    void testToJsonSafe() {
        TestObject obj = new TestObject("John", 30, LocalDateTime.of(2023, 12, 1, 14, 30, 45));
        String json = JsonUtils.toJsonSafe(obj);

        assertThat(json).isNotNull();
        assertThat(json).contains("John");
        assertThat(json).contains("30");
    }

    @Test
    void testToJsonSafeWithNull() {
        String json = JsonUtils.toJsonSafe(null);

        assertThat(json).isNull();
    }

    @Test
    void testToPrettyJson() throws JsonProcessingException {
        TestObject obj = new TestObject("John", 30, LocalDateTime.of(2023, 12, 1, 14, 30, 45));
        String json = JsonUtils.toPrettyJson(obj);

        assertThat(json).isNotNull();
        assertThat(json).contains("John");
        assertThat(json).contains("30");
        assertThat(json).contains("\n"); // Pretty print should contain newlines
    }

    @Test
    void testToPrettyJsonWithNull() throws JsonProcessingException {
        String json = JsonUtils.toPrettyJson(null);

        assertThat(json).isNull();
    }

    @Test
    void testToPrettyJsonSafe() {
        TestObject obj = new TestObject("John", 30, LocalDateTime.of(2023, 12, 1, 14, 30, 45));
        String json = JsonUtils.toPrettyJsonSafe(obj);

        assertThat(json).isNotNull();
        assertThat(json).contains("John");
        assertThat(json).contains("30");
    }

    @Test
    void testToPrettyJsonSafeWithNull() {
        String json = JsonUtils.toPrettyJsonSafe(null);

        assertThat(json).isNull();
    }

    @Test
    void testFromJson() throws JsonProcessingException {
        String json = "{\"name\":\"John\",\"age\":30,\"timestamp\":\"2023-12-01T14:30:45\"}";
        TestObject obj = JsonUtils.fromJson(json, TestObject.class);

        assertThat(obj).isNotNull();
        assertThat(obj.getName()).isEqualTo("John");
        assertThat(obj.getAge()).isEqualTo(30);
    }

    @Test
    void testFromJsonWithNull() throws JsonProcessingException {
        TestObject obj = JsonUtils.fromJson(null, TestObject.class);

        assertThat(obj).isNull();
    }

    @Test
    void testFromJsonWithInvalidJson() {
        assertThatThrownBy(() -> JsonUtils.fromJson("invalid json", TestObject.class))
                .isInstanceOf(JsonProcessingException.class);
    }

    @Test
    void testFromJsonWithNullClass() {
        assertThatThrownBy(() -> JsonUtils.fromJson("{}", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("clazz cannot be null");
    }

    @Test
    void testFromJsonSafe() {
        String json = "{\"name\":\"John\",\"age\":30,\"timestamp\":\"2023-12-01T14:30:45\"}";
        TestObject obj = JsonUtils.fromJsonSafe(json, TestObject.class);

        assertThat(obj).isNotNull();
        assertThat(obj.getName()).isEqualTo("John");
        assertThat(obj.getAge()).isEqualTo(30);
    }

    @Test
    void testFromJsonSafeWithNull() {
        TestObject obj = JsonUtils.fromJsonSafe(null, TestObject.class);

        assertThat(obj).isNull();
    }

    @Test
    void testFromJsonSafeWithInvalidJson() {
        TestObject obj = JsonUtils.fromJsonSafe("invalid json", TestObject.class);

        assertThat(obj).isNull();
    }

    @Test
    void testIsValidJson() {
        assertThat(JsonUtils.isValidJson("{\"name\":\"John\"}")).isTrue();
        assertThat(JsonUtils.isValidJson("[]")).isTrue();
        assertThat(JsonUtils.isValidJson("\"string\"")).isTrue();
        assertThat(JsonUtils.isValidJson("123")).isTrue();
        assertThat(JsonUtils.isValidJson("true")).isTrue();
        assertThat(JsonUtils.isValidJson("null")).isTrue();
    }

    @Test
    void testIsValidJsonWithInvalidJson() {
        assertThat(JsonUtils.isValidJson("invalid json")).isFalse();
        assertThat(JsonUtils.isValidJson("{name:\"John\"}")).isFalse();
        assertThat(JsonUtils.isValidJson("")).isFalse();
        assertThat(JsonUtils.isValidJson("   ")).isFalse();
    }

    @Test
    void testIsValidJsonWithNull() {
        assertThat(JsonUtils.isValidJson(null)).isFalse();
    }

    @Test
    void testGetObjectMapper() {
        assertThat(JsonUtils.getObjectMapper()).isNotNull();
    }

    @Test
    void testRoundTripSerialization() throws JsonProcessingException {
        TestObject original = new TestObject("John", 30, LocalDateTime.of(2023, 12, 1, 14, 30, 45));

        String json = JsonUtils.toJson(original);
        TestObject deserialized = JsonUtils.fromJson(json, TestObject.class);

        assertThat(deserialized.getName()).isEqualTo(original.getName());
        assertThat(deserialized.getAge()).isEqualTo(original.getAge());
        assertThat(deserialized.getTimestamp()).isEqualTo(original.getTimestamp());
    }

    @Test
    void testMapSerialization() throws JsonProcessingException {
        Map<String, Object> map = Map.of(
                "name", "John",
                "age", 30,
                "active", true
        );

        String json = JsonUtils.toJson(map);
        assertThat(json).contains("John");
        assertThat(json).contains("30");
        assertThat(json).contains("true");
    }
}
