package com.sporekart.ai.analytics.domain;

import java.util.Map;
import java.util.UUID;

public record GovernanceStatistic(
    UUID id,
    String name,
    String module,
    double value,
    Map<String, Object> dimensions
) {}
