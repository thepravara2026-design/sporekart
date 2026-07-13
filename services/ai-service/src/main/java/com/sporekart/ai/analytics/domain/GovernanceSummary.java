package com.sporekart.ai.analytics.domain;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record GovernanceSummary(
    UUID id,
    String title,
    Map<String, Object> metrics,
    Map<String, Object> kpis,
    Map<String, Object> trends,
    Instant generatedAt
) {}
