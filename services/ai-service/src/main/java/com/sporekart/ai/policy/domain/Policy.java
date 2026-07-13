package com.sporekart.ai.policy.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record Policy(
    UUID id, String name, String description, PolicyType type, PolicyStatus status,
    PolicySeverity severity, PolicyScope scope, int priority, String module,
    List<PolicyRule> rules, List<PolicyCondition> conditions,
    Map<String, Object> metadata, boolean isActive, boolean isSystem,
    UUID createdBy, OffsetDateTime createdAt, OffsetDateTime updatedAt) {}
