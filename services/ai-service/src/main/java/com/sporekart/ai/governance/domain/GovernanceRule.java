package com.sporekart.ai.governance.domain;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public record GovernanceRule(
    UUID id,
    UUID policyId,
    String name,
    String description,
    String ruleExpression,
    Map<String, Object> parameters,
    GovernanceDecision defaultDecision,
    int order,
    boolean isActive,
    OffsetDateTime createdAt) {
}
