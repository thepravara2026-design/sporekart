package com.sporekart.ai.compliance.application;

import com.sporekart.ai.compliance.domain.ComplianceAssessment;
import com.sporekart.ai.compliance.domain.ComplianceStatus;

import java.util.Map;
import java.util.UUID;

public interface ComplianceAssessmentService {
    ComplianceAssessment createAssessment(UUID frameworkId, String module, String action, Map<String, Object> context);
    ComplianceAssessment completeAssessment(UUID assessmentId, ComplianceStatus result);
}
