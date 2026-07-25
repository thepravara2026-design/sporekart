package com.sporekart.report.infrastructure.scheduler;

import com.sporekart.report.domain.model.ReportSchedule;
import com.sporekart.report.domain.model.ScheduleFrequency;
import com.sporekart.report.domain.repository.ReportRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
public class MockSchedulerService {
    private final ReportRepositoryPort repository;

    public MockSchedulerService(ReportRepositoryPort repository) {
        this.repository = repository;
    }

    public ReportSchedule createSchedule(
        String name, String reportId, String reportTitle,
        ScheduleFrequency frequency, String cronExpression,
        String exportFormat, String recipientEmail
    ) {
        ReportSchedule schedule = ReportSchedule.create(
            name, reportId, reportTitle, frequency, cronExpression,
            com.sporekart.report.domain.model.ExportFormat.valueOf(exportFormat.toUpperCase()),
            recipientEmail
        );
        Instant nextRun = calculateNextRun(frequency);
        schedule = schedule.withNextRunAt(nextRun);
        return repository.saveSchedule(schedule);
    }

    public ReportSchedule getSchedule(String scheduleId) {
        return repository.findScheduleById(scheduleId)
            .orElseThrow(() -> new IllegalArgumentException("Schedule not found: " + scheduleId));
    }

    public List<ReportSchedule> getAllSchedules() {
        return repository.findAllSchedules();
    }

    public List<ReportSchedule> getActiveSchedules() {
        return repository.findActiveSchedules();
    }

    public ReportSchedule pauseSchedule(String scheduleId) {
        ReportSchedule schedule = getSchedule(scheduleId);
        schedule = schedule.withActive(false);
        return repository.saveSchedule(schedule);
    }

    public ReportSchedule resumeSchedule(String scheduleId) {
        ReportSchedule schedule = getSchedule(scheduleId);
        schedule = schedule.withActive(true).withNextRunAt(calculateNextRun(schedule.frequency()));
        return repository.saveSchedule(schedule);
    }

    public void deleteSchedule(String scheduleId) {
        repository.deleteSchedule(scheduleId);
    }

    public ReportSchedule executeNow(String scheduleId) {
        ReportSchedule schedule = getSchedule(scheduleId);
        schedule = schedule.withLastRunAt(Instant.now())
            .withNextRunAt(calculateNextRun(schedule.frequency()));
        return repository.saveSchedule(schedule);
    }

    private Instant calculateNextRun(ScheduleFrequency frequency) {
        LocalDate today = LocalDate.now(ZoneId.systemDefault());
        LocalDate next = switch (frequency) {
            case DAILY -> today.plusDays(1);
            case WEEKLY -> today.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
            case MONTHLY -> today.withDayOfMonth(1).plusMonths(1);
            case QUARTERLY -> today.withDayOfMonth(1).plusMonths(3);
            case YEARLY -> today.withDayOfMonth(1).plusYears(1);
            case CUSTOM -> today.plusDays(1);
        };
        return next.atStartOfDay(ZoneId.systemDefault()).toInstant();
    }
}
