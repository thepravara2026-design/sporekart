package com.sporekart.ai.policy.domain;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public record PolicyViolation(
    UUID id, UUID evaluationId, UUID ruleId, String ruleName,
    String message, PolicySeverity severity, Map<String, Object> details,
    boolean overridable, OffsetDateTime timestamp) {}
