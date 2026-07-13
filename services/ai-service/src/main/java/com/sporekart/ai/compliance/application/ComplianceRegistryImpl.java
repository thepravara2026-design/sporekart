package com.sporekart.ai.compliance.application;

import com.sporekart.ai.compliance.domain.ComplianceFramework;
import com.sporekart.ai.compliance.domain.ComplianceRule;
import com.sporekart.ai.compliance.infrastructure.persistence.ComplianceFrameworkRepository;
import com.sporekart.ai.compliance.infrastructure.persistence.ComplianceRuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComplianceRegistryImpl implements ComplianceRegistry {

    private final ComplianceFrameworkRepository frameworkRepository;
    private final ComplianceRuleRepository ruleRepository;
    private final ComplianceAuditService complianceAuditService;

    @Override
    public ComplianceFramework registerFramework(ComplianceFramework framework) {
        log.info("Registering framework {}", framework.name());
        ComplianceFramework saved = frameworkRepository.save(framework);
        complianceAuditService.recordAudit(
            "FRAMEWORK_REGISTER", "FRAMEWORK", saved.id(), null,
            Map.of("name", saved.name(), "version", saved.version(), "type", saved.type().name()),
            "registry"
        );
        return saved;
    }

    @Override
    public ComplianceFramework updateFramework(ComplianceFramework framework) {
        log.info("Updating framework {}", framework.id());
        ComplianceFramework saved = frameworkRepository.save(framework);
        complianceAuditService.recordAudit(
            "FRAMEWORK_UPDATE", "FRAMEWORK", saved.id(), null,
            Map.of("name", saved.name(), "version", saved.version()),
            "registry"
        );
        return saved;
    }

    @Override
    public Optional<ComplianceFramework> getFramework(UUID frameworkId) {
        return frameworkRepository.findById(frameworkId);
    }

    @Override
    public List<ComplianceFramework> getAllFrameworks() {
        return frameworkRepository.findAll();
    }

    @Override
    public void deleteFramework(UUID frameworkId) {
        log.info("Deleting framework {}", frameworkId);
        frameworkRepository.deleteById(frameworkId);
        complianceAuditService.recordAudit(
            "FRAMEWORK_DELETE", "FRAMEWORK", frameworkId, null,
            Map.of("frameworkId", frameworkId.toString()),
            "registry"
        );
    }

    @Override
    public ComplianceRule registerRule(ComplianceRule rule) {
        log.info("Registering rule {} for framework {}", rule.ruleId(), rule.frameworkId());
        ComplianceRule saved = ruleRepository.save(rule);
        complianceAuditService.recordAudit(
            "RULE_REGISTER", "RULE", saved.id(), null,
            Map.of("ruleId", saved.ruleId(), "name", saved.name(), "frameworkId", saved.frameworkId().toString()),
            "registry"
        );
        return saved;
    }

    @Override
    public ComplianceRule updateRule(ComplianceRule rule) {
        log.info("Updating rule {}", rule.id());
        ComplianceRule saved = ruleRepository.save(rule);
        complianceAuditService.recordAudit(
            "RULE_UPDATE", "RULE", saved.id(), null,
            Map.of("ruleId", saved.ruleId(), "name", saved.name()),
            "registry"
        );
        return saved;
    }

    @Override
    public Optional<ComplianceRule> getRule(UUID ruleId) {
        return ruleRepository.findById(ruleId);
    }

    @Override
    public List<ComplianceRule> getRulesByFramework(UUID frameworkId) {
        return ruleRepository.findByFrameworkId(frameworkId);
    }

    @Override
    public List<ComplianceRule> getActiveRules(UUID frameworkId) {
        return ruleRepository.findByFrameworkIdAndActiveTrue(frameworkId);
    }

    @Override
    public void deleteRule(UUID ruleId) {
        log.info("Deleting rule {}", ruleId);
        ruleRepository.deleteById(ruleId);
        complianceAuditService.recordAudit(
            "RULE_DELETE", "RULE", ruleId, null,
            Map.of("ruleId", ruleId.toString()),
            "registry"
        );
    }
}
