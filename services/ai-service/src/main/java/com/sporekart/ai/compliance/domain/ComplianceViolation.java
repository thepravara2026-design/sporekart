package com.sporekart.ai.compliance.domain;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record ComplianceViolation(
    UUID id,
    UUID ruleId,
    UUID assessmentId,
    String module,
    ViolationSeverity severity,
    String description,
    Map<String, Object> details,
    boolean remediated,
    Instant detectedAt,
    Instant remediatedAt
) {}
