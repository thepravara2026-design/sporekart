package com.sporekart.ai.compliance.domain;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record ComplianceException(
    UUID id,
    UUID ruleId,
    UUID assessmentId,
    String reason,
    String justification,
    String requestedBy,
    ExceptionStatus status,
    String approvedBy,
    Instant requestedAt,
    Instant decisionAt,
    Instant expiresAt
) {}
