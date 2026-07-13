package com.sporekart.ai.compliance.domain;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record ComplianceAssessment(
    UUID id,
    UUID frameworkId,
    String module,
    String action,
    AssessmentStatus status,
    ComplianceStatus result,
    Map<String, Object> context,
    UUID reviewerId,
    Instant assessedAt,
    Instant completedAt
) {}
