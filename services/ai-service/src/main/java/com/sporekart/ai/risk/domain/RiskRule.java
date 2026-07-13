package com.sporekart.ai.risk.domain;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record RiskRule(
    UUID id,
    String ruleId,
    String name,
    String description,
    RiskCategory category,
    double baseWeight,
    boolean active,
    Instant createdAt,
    Instant updatedAt
) {}
