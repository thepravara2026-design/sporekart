package com.sporekart.ai.compliance.api;

import com.sporekart.ai.compliance.domain.*;
import java.util.List;
import java.util.UUID;

public interface ComplianceRegistry {
    ComplianceFramework registerFramework(ComplianceFramework framework);
    List<ComplianceFramework> findAllFrameworks();
    ComplianceFramework findFrameworkById(UUID id);
    ComplianceFramework findFrameworkByType(ComplianceFrameworkType type);
    ComplianceRule registerRule(ComplianceRule rule);
    List<ComplianceRule> findRulesByFramework(UUID frameworkId);
    ComplianceRule findRuleById(UUID id);
    List<ComplianceRule> findActiveRules();
}
