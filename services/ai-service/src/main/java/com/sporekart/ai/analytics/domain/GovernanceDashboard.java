package com.sporekart.ai.analytics.domain;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record GovernanceDashboard(
    UUID id,
    String name,
    String description,
    List<DashboardWidget> widgets,
    Map<String, Object> configuration,
    Instant createdAt,
    Instant updatedAt
) {}
