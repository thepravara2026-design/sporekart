package com.sporekart.ai.compliance.domain;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record ComplianceReport(
    UUID id,
    UUID frameworkId,
    String title,
    ComplianceStatus overallStatus,
    List<ComplianceFinding> findings,
    List<ComplianceViolation> violations,
    Map<String, Object> summary,
    Instant generatedAt,
    UUID generatedBy
) {}
