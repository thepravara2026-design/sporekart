package com.sporekart.platform.util;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class DateUtilsTest {

    @Test
    void testNow() {
        assertNotNull(DateUtils.now());
    }

    @Test
    void testToday() {
        assertNotNull(DateUtils.today());
    }

    @Test
    void testFormat() {
        Instant now = Instant.parse("2026-07-24T10:00:00Z");
        assertEquals("2026-07-24T10:00:00Z", DateUtils.format(now));
    }

    @Test
    void testDaysBetween() {
        Instant from = Instant.parse("2026-07-01T00:00:00Z");
        Instant to = Instant.parse("2026-07-24T00:00:00Z");
        assertEquals(23, DateUtils.daysBetween(from, to));
    }

    @Test
    void testAddDays() {
        Instant now = Instant.parse("2026-07-24T10:00:00Z");
        Instant later = DateUtils.addDays(now, 5);
        assertEquals("2026-07-29T10:00:00Z", later.toString());
    }

    @Test
    void testToInstantFromLocalDate() {
        LocalDate date = LocalDate.of(2026, 7, 24);
        Instant instant = DateUtils.toInstant(date);
        assertNotNull(instant);
    }

    @Test
    void testIsAfter() {
        Instant earlier = Instant.parse("2026-07-01T00:00:00Z");
        Instant later = Instant.parse("2026-07-24T00:00:00Z");
        assertTrue(DateUtils.isAfter(later, earlier));
        assertFalse(DateUtils.isAfter(earlier, later));
    }
}
