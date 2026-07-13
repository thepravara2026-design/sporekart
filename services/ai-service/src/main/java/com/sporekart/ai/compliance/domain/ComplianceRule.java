package com.sporekart.ai.compliance.domain;

import java.time.Instant;
import java.util.UUID;

public record ComplianceRule(
    UUID id,
    UUID frameworkId,
    String ruleId,
    String name,
    String description,
    String category,
    RiskLevel riskLevel,
    String expression,
    boolean active,
    Instant effectiveFrom,
    Instant effectiveTo
) {}
