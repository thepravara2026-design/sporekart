package com.sporekart.ai.policy.domain;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public record PolicyRule(
    UUID id, UUID policyId, String name, String description,
    String expression, Map<String, Object> parameters,
    PolicyDecision decision, int order, boolean isActive,
    OffsetDateTime createdAt, OffsetDateTime updatedAt) {}
