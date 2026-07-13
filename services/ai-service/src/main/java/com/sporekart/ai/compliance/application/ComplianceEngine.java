package com.sporekart.ai.compliance.application;

import com.sporekart.ai.compliance.domain.ComplianceAssessment;
import com.sporekart.ai.compliance.domain.ComplianceReport;
import com.sporekart.ai.compliance.engine.ComplianceResult;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ComplianceEngine {
    ComplianceResult validate(UUID frameworkId, String module, String action, Map<String, Object> context, String userId);
    ComplianceAssessment assess(UUID frameworkId, String module, String action, Map<String, Object> context);
    ComplianceReport generateReport(UUID assessmentId, UUID generatedBy);
    Optional<ComplianceAssessment> getStatus(UUID assessmentId);
}
