package com.sporekart.ai.compliance.domain;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ComplianceFinding(
    UUID id,
    UUID assessmentId,
    String findingId,
    String title,
    String description,
    ViolationSeverity severity,
    ComplianceStatus status,
    List<ComplianceViolation> violations,
    String recommendation,
    Instant detectedAt,
    Instant resolvedAt
) {}
