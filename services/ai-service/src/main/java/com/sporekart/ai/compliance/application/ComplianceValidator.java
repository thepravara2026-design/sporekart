package com.sporekart.ai.compliance.application;

import com.sporekart.ai.compliance.domain.ComplianceRule;
import com.sporekart.ai.compliance.domain.ComplianceViolation;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ComplianceValidator {
    List<ComplianceViolation> validate(UUID assessmentId, Map<String, Object> context, List<ComplianceRule> rules);
    List<ComplianceViolation> validateRules(UUID assessmentId, Map<String, Object> context, List<ComplianceRule> rules);
    List<ComplianceViolation> validateAgainstFramework(UUID assessmentId, Map<String, Object> context, UUID frameworkId);
}
