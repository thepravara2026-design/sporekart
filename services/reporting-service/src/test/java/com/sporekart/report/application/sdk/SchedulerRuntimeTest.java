package com.sporekart.report.application.sdk;

import com.sporekart.report.domain.model.*;
import com.sporekart.report.domain.repository.ReportRepositoryPort;
import com.sporekart.report.infrastructure.persistence.InMemoryReportRepository;
import com.sporekart.report.infrastructure.scheduler.MockSchedulerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class SchedulerRuntimeTest {
    private SchedulerRuntime runtime;
    private ReportRepositoryPort repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryReportRepository();
        MockSchedulerService schedulerService = new MockSchedulerService(repository);
        runtime = new SchedulerRuntime(schedulerService);
    }

    @Test
    void shouldCreateSchedule() {
        ReportSchedule s = runtime.createSchedule("Daily Report", "r1", "Report",
            ScheduleFrequency.DAILY, "0 0 8 * * ?", "PDF", "ceo@co.com");
        assertNotNull(s.id());
        assertTrue(s.active());
    }

    @Test
    void shouldGetSchedule() {
        ReportSchedule created = runtime.createSchedule("Test", "r1", "T",
            ScheduleFrequency.WEEKLY, "0 0 8 * * 1", "CSV", "a@b.com");
        ReportSchedule found = runtime.getSchedule(created.id());
        assertEquals(created.id(), found.id());
    }

    @Test
    void shouldGetAllSchedules() {
        runtime.createSchedule("S1", "r1", "R1", ScheduleFrequency.DAILY, "", "PDF", "a@b.com");
        runtime.createSchedule("S2", "r2", "R2", ScheduleFrequency.WEEKLY, "", "CSV", "c@d.com");
        assertTrue(runtime.getAllSchedules().size() >= 2);
    }

    @Test
    void shouldPauseAndResumeSchedule() {
        ReportSchedule s = runtime.createSchedule("Test", "r1", "T",
            ScheduleFrequency.MONTHLY, "", "PDF", "a@b.com");
        ReportSchedule paused = runtime.pauseSchedule(s.id());
        assertFalse(paused.active());
        ReportSchedule resumed = runtime.resumeSchedule(s.id());
        assertTrue(resumed.active());
    }

    @Test
    void shouldExecuteNow() {
        ReportSchedule s = runtime.createSchedule("Test", "r1", "T",
            ScheduleFrequency.DAILY, "", "PDF", "a@b.com");
        ReportSchedule executed = runtime.executeNow(s.id());
        assertNotNull(executed.lastRunAt());
    }

    @Test
    void shouldDeleteSchedule() {
        ReportSchedule s = runtime.createSchedule("Del", "r1", "T",
            ScheduleFrequency.DAILY, "", "PDF", "a@b.com");
        runtime.deleteSchedule(s.id());
        assertTrue(runtime.getAllSchedules().isEmpty());
    }

    @Test
    void shouldGetActiveSchedules() {
        runtime.createSchedule("S1", "r1", "R1", ScheduleFrequency.DAILY, "", "PDF", "a@b.com");
        runtime.createSchedule("S2", "r2", "R2", ScheduleFrequency.WEEKLY, "", "CSV", "c@d.com");
        assertFalse(runtime.getActiveSchedules().isEmpty());
    }
}
