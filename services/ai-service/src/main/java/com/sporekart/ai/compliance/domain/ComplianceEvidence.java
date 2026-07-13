package com.sporekart.ai.compliance.domain;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record ComplianceEvidence(
    UUID id,
    UUID assessmentId,
    String evidenceType,
    String source,
    Map<String, Object> data,
    boolean verified,
    UUID verifiedBy,
    Instant collectedAt,
    Instant verifiedAt
) {}
