package com.mycompany.base.core.api.util;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DateUtilsTest {

    @Test
    void testFormatCurrentTime() {
        String result = DateUtils.formatCurrentTime();

        assertThat(result).isNotNull();
        assertThat(result).matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");
    }

    @Test
    void testFormatCurrentTimeWithPattern() {
        String result = DateUtils.formatCurrentTime("yyyy/MM/dd");

        assertThat(result).isNotNull();
        assertThat(result).matches("\\d{4}/\\d{2}/\\d{2}");
    }

    @Test
    void testFormatCurrentDate() {
        String result = DateUtils.formatCurrentDate();

        assertThat(result).isNotNull();
        assertThat(result).matches("\\d{4}-\\d{2}-\\d{2}");
    }

    @Test
    void testFormatCurrentDateWithPattern() {
        String result = DateUtils.formatCurrentDate("dd/MM/yyyy");

        assertThat(result).isNotNull();
        assertThat(result).matches("\\d{2}/\\d{2}/\\d{4}");
    }

    @Test
    void testFormatDateTime() {
        LocalDateTime dateTime = LocalDateTime.of(2023, 12, 1, 14, 30, 45);
        String result = DateUtils.formatDateTime(dateTime, "yyyy-MM-dd HH:mm:ss");

        assertThat(result).isEqualTo("2023-12-01 14:30:45");
    }

    @Test
    void testFormatDateTimeWithNull() {
        String result = DateUtils.formatDateTime(null, "yyyy-MM-dd HH:mm:ss");

        assertThat(result).isNull();
    }

    @Test
    void testFormatDate() {
        LocalDate date = LocalDate.of(2023, 12, 1);
        String result = DateUtils.formatDate(date, "yyyy-MM-dd");

        assertThat(result).isEqualTo("2023-12-01");
    }

    @Test
    void testFormatDateWithNull() {
        String result = DateUtils.formatDate(null, "yyyy-MM-dd");

        assertThat(result).isNull();
    }

    @Test
    void testParseDate() {
        LocalDate result = DateUtils.parseDate("2023-12-01", "yyyy-MM-dd");

        assertThat(result).isEqualTo(LocalDate.of(2023, 12, 1));
    }

    @Test
    void testParseDateWithNull() {
        LocalDate result = DateUtils.parseDate(null, "yyyy-MM-dd");

        assertThat(result).isNull();
    }

    @Test
    void testParseDateWithInvalidFormat() {
        assertThatThrownBy(() -> DateUtils.parseDate("2023-12-01", "dd/MM/yyyy"))
                .isInstanceOf(DateTimeParseException.class);
    }

    @Test
    void testParseDateTime() {
        LocalDateTime result = DateUtils.parseDateTime("2023-12-01 14:30:45", "yyyy-MM-dd HH:mm:ss");

        assertThat(result).isEqualTo(LocalDateTime.of(2023, 12, 1, 14, 30, 45));
    }

    @Test
    void testParseDateTimeWithNull() {
        LocalDateTime result = DateUtils.parseDateTime(null, "yyyy-MM-dd HH:mm:ss");

        assertThat(result).isNull();
    }

    @Test
    void testToUtc() {
        LocalDateTime dateTime = LocalDateTime.of(2023, 12, 1, 14, 30, 45);
        ZonedDateTime result = DateUtils.toUtc(dateTime);

        assertThat(result).isNotNull();
        assertThat(result.getZone()).isEqualTo(ZoneId.of("UTC"));
    }

    @Test
    void testToUtcWithNull() {
        ZonedDateTime result = DateUtils.toUtc(null);

        assertThat(result).isNull();
    }

    @Test
    void testToTimezone() {
        LocalDateTime dateTime = LocalDateTime.of(2023, 12, 1, 14, 30, 45);
        ZonedDateTime result = DateUtils.toTimezone(dateTime, ZoneId.of("America/New_York"));

        assertThat(result).isNotNull();
        assertThat(result.getZone()).isEqualTo(ZoneId.of("America/New_York"));
    }

    @Test
    void testToTimezoneWithNull() {
        ZonedDateTime result = DateUtils.toTimezone(null, ZoneId.of("UTC"));

        assertThat(result).isNull();
    }

    @Test
    void testFromInstant() {
        Instant instant = Instant.parse("2023-12-01T14:30:45Z");
        LocalDateTime result = DateUtils.fromInstant(instant);

        assertThat(result).isNotNull();
    }

    @Test
    void testFromInstantWithNull() {
        LocalDateTime result = DateUtils.fromInstant(null);

        assertThat(result).isNull();
    }

    @Test
    void testToInstant() {
        LocalDateTime dateTime = LocalDateTime.of(2023, 12, 1, 14, 30, 45);
        Instant result = DateUtils.toInstant(dateTime);

        assertThat(result).isNotNull();
    }

    @Test
    void testToInstantWithNull() {
        Instant result = DateUtils.toInstant(null);

        assertThat(result).isNull();
    }

    @Test
    void testFormatAsIso() {
        Instant instant = Instant.parse("2023-12-01T14:30:45Z");
        String result = DateUtils.formatAsIso(instant);

        assertThat(result).isEqualTo("2023-12-01T14:30:45Z");
    }

    @Test
    void testFormatAsIsoWithNull() {
        String result = DateUtils.formatAsIso(null);

        assertThat(result).isNull();
    }

    @Test
    void testParseIso() {
        Instant result = DateUtils.parseIso("2023-12-01T14:30:45Z");

        assertThat(result).isEqualTo(Instant.parse("2023-12-01T14:30:45Z"));
    }

    @Test
    void testParseIsoWithNull() {
        Instant result = DateUtils.parseIso(null);

        assertThat(result).isNull();
    }

    @Test
    void testParseIsoWithInvalidFormat() {
        assertThatThrownBy(() -> DateUtils.parseIso("invalid-iso-format"))
                .isInstanceOf(DateTimeParseException.class);
    }

    @Test
    void testValidatePatternWithNull() {
        assertThatThrownBy(() -> DateUtils.formatCurrentTime(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("pattern cannot be null");
    }

    @Test
    void testValidatePatternWithEmpty() {
        assertThatThrownBy(() -> DateUtils.formatCurrentTime(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("pattern cannot be empty");
    }
}
