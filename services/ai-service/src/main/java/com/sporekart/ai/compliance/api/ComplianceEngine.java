package com.sporekart.ai.compliance.api;

import com.sporekart.ai.compliance.domain.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ComplianceEngine {
    ComplianceResult validate(ComplianceRequest request);
    ComplianceAssessment assess(UUID frameworkId, Map<String, Object> context);
    ComplianceReport generateReport(UUID frameworkId);
    ComplianceStatus getStatus(UUID assessmentId);
}
