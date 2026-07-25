package com.sporekart.report.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReportScheduleTest {
    @Test
    void shouldCreateScheduleWithStaticFactory() {
        ReportSchedule s = ReportSchedule.create("Daily Sales", "report-1", "Sales Report",
            ScheduleFrequency.DAILY, "0 0 8 * * ?", ExportFormat.PDF, "ceo@sporekart.com");
        assertNotNull(s.id());
        assertEquals("Daily Sales", s.name());
        assertEquals(ScheduleFrequency.DAILY, s.frequency());
        assertTrue(s.active());
        assertNull(s.lastRunAt());
        assertNotNull(s.createdAt());
    }

    @Test
    void shouldToggleActive() {
        ReportSchedule s = ReportSchedule.create("Test", "r1", "T", ScheduleFrequency.DAILY,
            "0 0 8 * * ?", ExportFormat.PDF, "a@b.com");
        ReportSchedule paused = s.withActive(false);
        assertFalse(paused.active());
    }

    @Test
    void shouldUpdateNextRunAt() {
        ReportSchedule s = ReportSchedule.create("Test", "r1", "T", ScheduleFrequency.WEEKLY,
            "0 0 8 * * 1", ExportFormat.CSV, "a@b.com");
        assertNotNull(s.nextRunAt());
    }

    @Test
    void shouldUpdateLastRunAt() {
        ReportSchedule s = ReportSchedule.create("Test", "r1", "T", ScheduleFrequency.MONTHLY,
            "0 0 8 1 * ?", ExportFormat.JSON, "a@b.com");
        java.time.Instant now = java.time.Instant.now();
        ReportSchedule run = s.withLastRunAt(now);
        assertEquals(now, run.lastRunAt());
    }
}
