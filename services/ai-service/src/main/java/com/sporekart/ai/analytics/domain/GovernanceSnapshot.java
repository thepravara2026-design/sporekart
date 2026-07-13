package com.sporekart.ai.analytics.domain;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record GovernanceSnapshot(
    UUID id,
    String name,
    Map<String, Object> data,
    Instant capturedAt
) {}
