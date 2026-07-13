package com.sporekart.ai.compliance.api;

import com.sporekart.ai.compliance.domain.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ComplianceAssessmentService {
    ComplianceAssessment createAssessment(UUID frameworkId, String module, String action, Map<String, Object> context);
    ComplianceAssessment getAssessment(UUID id);
    List<ComplianceAssessment> getAssessmentsByFramework(UUID frameworkId);
    ComplianceAssessment completeAssessment(UUID id, ComplianceStatus result);
}
