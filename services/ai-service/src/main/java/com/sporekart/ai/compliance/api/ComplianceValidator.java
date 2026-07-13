package com.sporekart.ai.compliance.api;

import com.sporekart.ai.compliance.domain.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ComplianceValidator {
    ComplianceResult validate(ComplianceAssessment assessment);
    List<ComplianceViolation> validateRules(ComplianceAssessment assessment, List<ComplianceRule> rules);
    ComplianceResult validateAgainstFramework(UUID frameworkId, Map<String, Object> context);
}
