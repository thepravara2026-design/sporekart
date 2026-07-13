package com.sporekart.ai.compliance.application;

import com.sporekart.ai.compliance.domain.ComplianceFramework;
import com.sporekart.ai.compliance.domain.ComplianceRule;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ComplianceRegistry {
    ComplianceFramework registerFramework(ComplianceFramework framework);
    ComplianceFramework updateFramework(ComplianceFramework framework);
    Optional<ComplianceFramework> getFramework(UUID frameworkId);
    List<ComplianceFramework> getAllFrameworks();
    void deleteFramework(UUID frameworkId);
    ComplianceRule registerRule(ComplianceRule rule);
    ComplianceRule updateRule(ComplianceRule rule);
    Optional<ComplianceRule> getRule(UUID ruleId);
    List<ComplianceRule> getRulesByFramework(UUID frameworkId);
    List<ComplianceRule> getActiveRules(UUID frameworkId);
    void deleteRule(UUID ruleId);
}
