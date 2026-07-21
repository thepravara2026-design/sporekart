package com.sporekart.ai.providers.maintenance;

import java.time.Instant;

public record MaintenanceWindow(
    String windowId,
    String providerId,
    String reason,
    MaintenanceState state,
    Instant startedAt,
    Instant scheduledEnd,
    Instant actualEnd,
    String initiatedBy
) {}
