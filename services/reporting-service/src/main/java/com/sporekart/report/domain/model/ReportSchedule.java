package com.sporekart.report.domain.model;

import java.time.Instant;
import java.util.UUID;

public record ReportSchedule(
    String id,
    String name,
    String reportId,
    String reportTitle,
    ScheduleFrequency frequency,
    String cronExpression,
    ExportFormat exportFormat,
    String recipientEmail,
    boolean active,
    Instant nextRunAt,
    Instant lastRunAt,
    Instant createdAt
) {
    public static ReportSchedule create(
        String name, String reportId, String reportTitle,
        ScheduleFrequency frequency, String cronExpression,
        ExportFormat exportFormat, String recipientEmail
    ) {
        return new ReportSchedule(
            UUID.randomUUID().toString(),
            name, reportId, reportTitle, frequency, cronExpression,
            exportFormat, recipientEmail, true, Instant.now(), null, Instant.now()
        );
    }

    public ReportSchedule withActive(boolean active) {
        return new ReportSchedule(id, name, reportId, reportTitle, frequency,
            cronExpression, exportFormat, recipientEmail, active, nextRunAt, lastRunAt, createdAt);
    }

    public ReportSchedule withNextRunAt(Instant nextRunAt) {
        return new ReportSchedule(id, name, reportId, reportTitle, frequency,
            cronExpression, exportFormat, recipientEmail, active, nextRunAt, lastRunAt, createdAt);
    }

    public ReportSchedule withLastRunAt(Instant lastRunAt) {
        return new ReportSchedule(id, name, reportId, reportTitle, frequency,
            cronExpression, exportFormat, recipientEmail, active, nextRunAt, lastRunAt, createdAt);
    }
}
