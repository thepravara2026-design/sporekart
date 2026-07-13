package com.sporekart.ai.analytics.domain;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record ReportSchedule(
    UUID id,
    ReportType reportType,
    ScheduleFrequency frequency,
    Map<String, Object> configuration,
    boolean active,
    UUID createdBy,
    Instant createdAt,
    Instant updatedAt
) {}
