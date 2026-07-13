package com.sporekart.ai.admin.domain;

import java.time.Instant;
import java.util.UUID;

public record MaintenanceWindow(
    UUID id,
    String title,
    String description,
    MaintenanceStatus status,
    Instant scheduledStart,
    Instant scheduledEnd,
    Instant actualStart,
    Instant actualEnd,
    UUID scheduledBy
) {}
