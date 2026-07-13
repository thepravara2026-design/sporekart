package com.sporekart.ai.automation.domain;

import java.time.Instant;
import java.util.UUID;

public record RetryPolicy(
    UUID id,
    String name,
    int maxRetries,
    long initialDelayMs,
    long maxDelayMs,
    double backoffMultiplier,
    boolean retryOnFailure,
    Instant createdAt,
    Instant updatedAt
) {}
