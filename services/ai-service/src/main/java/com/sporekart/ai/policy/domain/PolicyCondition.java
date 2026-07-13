package com.sporekart.ai.policy.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public record PolicyCondition(
    UUID id, UUID ruleId, String field, ConditionOperator operator,
    Object value, boolean negate, int order,
    OffsetDateTime createdAt) {}
