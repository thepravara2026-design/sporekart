package com.sporekart.ai.compliance.engine;

import com.sporekart.ai.compliance.domain.ComplianceFinding;
import com.sporekart.ai.compliance.domain.ComplianceStatus;
import com.sporekart.ai.compliance.domain.ComplianceViolation;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record ComplianceResult(
    boolean compliant,
    ComplianceStatus status,
    List<ComplianceViolation> violations,
    List<ComplianceFinding> findings,
    UUID assessmentId,
    UUID reportId,
    Map<String, Object> details
) {}
