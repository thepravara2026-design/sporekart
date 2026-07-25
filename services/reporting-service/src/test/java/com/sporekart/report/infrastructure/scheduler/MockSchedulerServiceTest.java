package com.sporekart.report.infrastructure.scheduler;

import com.sporekart.report.domain.model.*;
import com.sporekart.report.domain.repository.ReportRepositoryPort;
import com.sporekart.report.infrastructure.persistence.InMemoryReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MockSchedulerServiceTest {
    private MockSchedulerService schedulerService;
    private ReportRepositoryPort repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryReportRepository();
        schedulerService = new MockSchedulerService(repository);
    }

    @Test
    void shouldCreateSchedule() {
        ReportSchedule s = schedulerService.createSchedule("Daily", "r1", "Report",
            ScheduleFrequency.DAILY, "0 0 8 * * ?", "PDF", "ceo@co.com");
        assertNotNull(s.id());
        assertTrue(s.active());
        assertNotNull(s.nextRunAt());
    }

    @Test
    void shouldGetSchedule() {
        ReportSchedule created = schedulerService.createSchedule("Test", "r1", "T",
            ScheduleFrequency.WEEKLY, "0 0 8 * * 1", "CSV", "a@b.com");
        ReportSchedule found = schedulerService.getSchedule(created.id());
        assertEquals(created.id(), found.id());
    }

    @Test
    void shouldGetAllSchedules() {
        schedulerService.createSchedule("S1", "r1", "R1", ScheduleFrequency.DAILY, "", "PDF", "a@b.com");
        schedulerService.createSchedule("S2", "r2", "R2", ScheduleFrequency.WEEKLY, "", "CSV", "c@d.com");
        assertEquals(2, schedulerService.getAllSchedules().size());
    }

    @Test
    void shouldGetActiveSchedules() {
        schedulerService.createSchedule("S1", "r1", "R1", ScheduleFrequency.DAILY, "", "PDF", "a@b.com");
        ReportSchedule s2 = schedulerService.createSchedule("S2", "r2", "R2", ScheduleFrequency.WEEKLY, "", "CSV", "c@d.com");
        schedulerService.pauseSchedule(s2.id());
        List<ReportSchedule> active = schedulerService.getActiveSchedules();
        assertEquals(1, active.size());
    }

    @Test
    void shouldPauseAndResumeSchedule() {
        ReportSchedule s = schedulerService.createSchedule("Test", "r1", "T",
            ScheduleFrequency.MONTHLY, "", "PDF", "a@b.com");
        ReportSchedule paused = schedulerService.pauseSchedule(s.id());
        assertFalse(paused.active());
        ReportSchedule resumed = schedulerService.resumeSchedule(s.id());
        assertTrue(resumed.active());
    }

    @Test
    void shouldDeleteSchedule() {
        ReportSchedule s = schedulerService.createSchedule("Del", "r1", "T",
            ScheduleFrequency.DAILY, "", "PDF", "a@b.com");
        schedulerService.deleteSchedule(s.id());
        assertEquals(0, schedulerService.getAllSchedules().size());
    }

    @Test
    void shouldExecuteNow() {
        ReportSchedule s = schedulerService.createSchedule("Exec", "r1", "T",
            ScheduleFrequency.DAILY, "", "PDF", "a@b.com");
        ReportSchedule executed = schedulerService.executeNow(s.id());
        assertNotNull(executed.lastRunAt());
        assertTrue(executed.nextRunAt().isAfter(executed.lastRunAt()));
    }

    @Test
    void shouldCalculateNextRunForWeekly() {
        ReportSchedule s = schedulerService.createSchedule("Weekly", "r1", "T",
            ScheduleFrequency.WEEKLY, "0 0 8 * * 1", "PDF", "a@b.com");
        assertNotNull(s.nextRunAt());
    }

    @Test
    void shouldCalculateNextRunForMonthly() {
        ReportSchedule s = schedulerService.createSchedule("Monthly", "r1", "T",
            ScheduleFrequency.MONTHLY, "0 0 8 1 * ?", "PDF", "a@b.com");
        assertNotNull(s.nextRunAt());
    }

    @Test
    void shouldThrowForMissingSchedule() {
        assertThrows(IllegalArgumentException.class, () ->
            schedulerService.getSchedule("missing"));
    }
}
