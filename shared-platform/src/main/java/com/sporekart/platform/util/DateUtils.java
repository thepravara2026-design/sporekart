package com.sporekart.platform.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateUtils {
    public static final ZoneId UTC = ZoneId.of("UTC");
    public static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_INSTANT;
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public static Instant now() {
        return Instant.now();
    }

    public static LocalDate today() {
        return LocalDate.now(UTC);
    }

    public static LocalDateTime nowLocal() {
        return LocalDateTime.now(UTC);
    }

    public static Instant toInstant(LocalDate date) {
        return date.atStartOfDay(UTC).toInstant();
    }

    public static Instant toInstant(LocalDateTime dateTime) {
        return dateTime.atZone(UTC).toInstant();
    }

    public static String format(Instant instant) {
        return instant != null ? ISO_FORMATTER.format(instant) : null;
    }

    public static String formatDate(LocalDate date) {
        return date != null ? DATE_FORMATTER.format(date) : null;
    }

    public static long daysBetween(Instant from, Instant to) {
        return ChronoUnit.DAYS.between(from, to);
    }

    public static Instant addDays(Instant instant, long days) {
        return instant.plus(days, ChronoUnit.DAYS);
    }

    public static Instant addHours(Instant instant, long hours) {
        return instant.plus(hours, ChronoUnit.HOURS);
    }

    public static boolean isAfter(Instant a, Instant b) {
        return a != null && b != null && a.isAfter(b);
    }

    public static boolean isBefore(Instant a, Instant b) {
        return a != null && b != null && a.isBefore(b);
    }
}
