package com.sporekart.ai.automation.domain;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record ScheduledTask(
    UUID id,
    String name,
    JobType jobType,
    ScheduleFrequency frequency,
    String cronExpression,
    Map<String, Object> params,
    boolean active,
    Instant lastRunAt,
    Instant nextRunAt,
    Instant createdAt,
    Instant updatedAt
) {}
