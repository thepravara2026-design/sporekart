package com.sporekart.ai.automation.domain;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record AutomationJob(
    UUID id,
    JobType type,
    String name,
    Map<String, Object> params,
    AutomationStatus status,
    int retryCount,
    int maxRetries,
    Instant scheduledAt,
    Instant startedAt,
    Instant completedAt,
    String errorMessage
) {}
