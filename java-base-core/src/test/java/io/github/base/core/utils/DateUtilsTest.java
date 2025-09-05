package io.github.base.core.utils;

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
        String result = DateTimeUtils.formatCurrentTime();

        assertThat(result).isNotNull();
        assertThat(result).matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");
    }

    @Test
    void testFormatCurrentTimeWithPattern() {
        String result = DateTimeUtils.formatCurrentTime("yyyy/MM/dd");

        assertThat(result).isNotNull();
        assertThat(result).matches("\\d{4}/\\d{2}/\\d{2}");
    }

    @Test
    void testFormatCurrentDate() {
        String result = DateTimeUtils.formatCurrentDate();

        assertThat(result).isNotNull();
        assertThat(result).matches("\\d{4}-\\d{2}-\\d{2}");
    }

    @Test
    void testFormatCurrentDateWithPattern() {
        String result = DateTimeUtils.formatCurrentDate("dd/MM/yyyy");

        assertThat(result).isNotNull();
        assertThat(result).matches("\\d{2}/\\d{2}/\\d{4}");
    }

    @Test
    void testFormatDateTime() {
        LocalDateTime dateTime = LocalDateTime.of(2023, 12, 1, 14, 30, 45);
        String result = DateTimeUtils.formatDateTime(dateTime, "yyyy-MM-dd HH:mm:ss");

        assertThat(result).isEqualTo("2023-12-01 14:30:45");
    }

    @Test
    void testFormatDateTimeWithNull() {
        String result = DateTimeUtils.formatDateTime(null, "yyyy-MM-dd HH:mm:ss");

        assertThat(result).isNull();
    }

    @Test
    void testFormatDate() {
        LocalDate date = LocalDate.of(2023, 12, 1);
        String result = DateTimeUtils.formatDate(date, "yyyy-MM-dd");

        assertThat(result).isEqualTo("2023-12-01");
    }

    @Test
    void testFormatDateWithNull() {
        String result = DateTimeUtils.formatDate(null, "yyyy-MM-dd");

        assertThat(result).isNull();
    }

    @Test
    void testParseDate() {
        LocalDate result = DateTimeUtils.parseDate("2023-12-01", "yyyy-MM-dd");

        assertThat(result).isEqualTo(LocalDate.of(2023, 12, 1));
    }

    @Test
    void testParseDateWithNull() {
        LocalDate result = DateTimeUtils.parseDate(null, "yyyy-MM-dd");

        assertThat(result).isNull();
    }

    @Test
    void testParseDateWithInvalidFormat() {
        assertThatThrownBy(() -> DateTimeUtils.parseDate("2023-12-01", "dd/MM/yyyy"))
                .isInstanceOf(DateTimeParseException.class);
    }

    @Test
    void testParseDateTime() {
        LocalDateTime result = DateTimeUtils.parseDateTime("2023-12-01 14:30:45", "yyyy-MM-dd HH:mm:ss");

        assertThat(result).isEqualTo(LocalDateTime.of(2023, 12, 1, 14, 30, 45));
    }

    @Test
    void testParseDateTimeWithNull() {
        LocalDateTime result = DateTimeUtils.parseDateTime(null, "yyyy-MM-dd HH:mm:ss");

        assertThat(result).isNull();
    }

    @Test
    void testToUtc() {
        LocalDateTime dateTime = LocalDateTime.of(2023, 12, 1, 14, 30, 45);
        ZonedDateTime result = DateTimeUtils.toUtc(dateTime);

        assertThat(result).isNotNull();
        assertThat(result.getZone()).isEqualTo(ZoneId.of("UTC"));
    }

    @Test
    void testToUtcWithNull() {
        ZonedDateTime result = DateTimeUtils.toUtc(null);

        assertThat(result).isNull();
    }

    @Test
    void testToTimezone() {
        LocalDateTime dateTime = LocalDateTime.of(2023, 12, 1, 14, 30, 45);
        ZonedDateTime result = DateTimeUtils.toTimezone(dateTime, ZoneId.of("America/New_York"));

        assertThat(result).isNotNull();
        assertThat(result.getZone()).isEqualTo(ZoneId.of("America/New_York"));
    }

    @Test
    void testToTimezoneWithNull() {
        ZonedDateTime result = DateTimeUtils.toTimezone(null, ZoneId.of("UTC"));

        assertThat(result).isNull();
    }

    @Test
    void testFromInstant() {
        Instant instant = Instant.parse("2023-12-01T14:30:45Z");
        LocalDateTime result = DateTimeUtils.fromInstant(instant);

        assertThat(result).isNotNull();
    }

    @Test
    void testFromInstantWithNull() {
        LocalDateTime result = DateTimeUtils.fromInstant(null);

        assertThat(result).isNull();
    }

    @Test
    void testToInstant() {
        LocalDateTime dateTime = LocalDateTime.of(2023, 12, 1, 14, 30, 45);
        Instant result = DateTimeUtils.toInstant(dateTime);

        assertThat(result).isNotNull();
    }

    @Test
    void testToInstantWithNull() {
        Instant result = DateTimeUtils.toInstant(null);

        assertThat(result).isNull();
    }

    @Test
    void testFormatAsIso() {
        Instant instant = Instant.parse("2023-12-01T14:30:45Z");
        String result = DateTimeUtils.formatAsIso(instant);

        assertThat(result).isEqualTo("2023-12-01T14:30:45Z");
    }

    @Test
    void testFormatAsIsoWithNull() {
        String result = DateTimeUtils.formatAsIso(null);

        assertThat(result).isNull();
    }

    @Test
    void testParseIso() {
        Instant result = DateTimeUtils.parseIso("2023-12-01T14:30:45Z");

        assertThat(result).isEqualTo(Instant.parse("2023-12-01T14:30:45Z"));
    }

    @Test
    void testParseIsoWithNull() {
        Instant result = DateTimeUtils.parseIso(null);

        assertThat(result).isNull();
    }

    @Test
    void testParseIsoWithInvalidFormat() {
        assertThatThrownBy(() -> DateTimeUtils.parseIso("invalid-iso-format"))
                .isInstanceOf(DateTimeParseException.class);
    }

    @Test
    void testValidatePatternWithNull() {
        assertThatThrownBy(() -> DateTimeUtils.formatCurrentTime(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("pattern cannot be null");
    }

    @Test
    void testValidatePatternWithEmpty() {
        assertThatThrownBy(() -> DateTimeUtils.formatCurrentTime(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("pattern cannot be empty");
    }
}
