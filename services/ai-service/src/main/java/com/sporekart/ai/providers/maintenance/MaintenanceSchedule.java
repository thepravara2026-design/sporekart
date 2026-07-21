package com.sporekart.ai.providers.maintenance;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

public record MaintenanceSchedule(
    String providerId,
    List<DayOfWeek> allowedDays,
    LocalTime windowStart,
    LocalTime windowEnd,
    Duration maxDuration,
    boolean recurring
) {}
