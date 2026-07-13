package com.sporekart.ai.analytics.domain;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record DashboardFilter(
    UUID id,
    String name,
    String field,
    String operator,
    String value,
    Map<String, Object> options
) {}
