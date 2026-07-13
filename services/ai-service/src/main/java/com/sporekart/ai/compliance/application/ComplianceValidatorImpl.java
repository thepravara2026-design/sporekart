package com.sporekart.ai.compliance.application;

import com.sporekart.ai.compliance.domain.ComplianceRule;
import com.sporekart.ai.compliance.domain.ComplianceViolation;
import com.sporekart.ai.compliance.domain.ViolationSeverity;
import com.sporekart.ai.compliance.infrastructure.persistence.ComplianceAuditRepository;
import com.sporekart.ai.compliance.infrastructure.persistence.ComplianceRuleRepository;
import com.sporekart.ai.compliance.infrastructure.persistence.ComplianceViolationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComplianceValidatorImpl implements ComplianceValidator {

    private final ComplianceRuleRepository ruleRepository;
    private final ComplianceViolationRepository violationRepository;
    private final ComplianceAuditService complianceAuditService;

    @Override
    public List<ComplianceViolation> validate(UUID assessmentId, Map<String, Object> context, List<ComplianceRule> rules) {
        log.info("Validating {} rules for assessment {}", rules.size(), assessmentId);
        List<ComplianceViolation> violations = new ArrayList<>();

        for (ComplianceRule rule : rules) {
            if (!rule.active()) {
                continue;
            }
            boolean passed = evaluateRule(rule, context);
            if (!passed) {
                ComplianceViolation violation = new ComplianceViolation(
                    UUID.randomUUID(),
                    rule.id(),
                    assessmentId,
                    context != null ? (String) context.getOrDefault("module", "unknown") : "unknown",
                    rule.riskLevel() != null
                        ? switch (rule.riskLevel()) {
                            case CRITICAL -> ViolationSeverity.CRITICAL;
                            case HIGH -> ViolationSeverity.MAJOR;
                            case MEDIUM -> ViolationSeverity.WARNING;
                            case LOW -> ViolationSeverity.INFO;
                          }
                        : ViolationSeverity.INFO,
                    "Rule violation: " + rule.name() + " - " + rule.description(),
                    Map.of("ruleId", rule.id().toString(), "ruleName", rule.name(), "expression", rule.expression()),
                    false,
                    Instant.now(),
                    null
                );
                violations.add(violation);
            }
        }

        if (!violations.isEmpty()) {
            violationRepository.saveAll(violations);
            complianceAuditService.recordAudit(
                "VALIDATION_VIOLATIONS", "ASSESSMENT", assessmentId, null,
                Map.of("violationsCount", violations.size(), "rulesEvaluated", rules.size()),
                "validator"
            );
        }

        return violations;
    }

    @Override
    public List<ComplianceViolation> validateRules(UUID assessmentId, Map<String, Object> context, List<ComplianceRule> rules) {
        return validate(assessmentId, context, rules);
    }

    @Override
    public List<ComplianceViolation> validateAgainstFramework(UUID assessmentId, Map<String, Object> context, UUID frameworkId) {
        List<ComplianceRule> rules = ruleRepository.findByFrameworkIdAndActiveTrue(frameworkId);
        return validate(assessmentId, context, rules);
    }

    private boolean evaluateRule(ComplianceRule rule, Map<String, Object> context) {
        if (rule.expression() == null || rule.expression().isBlank()) {
            return true;
        }
        String expr = rule.expression().trim();
        if (expr.startsWith("context.")) {
            String key = expr.substring(8);
            Object value = context != null ? context.get(key) : null;
            if (value == null) {
                return false;
            }
            if (value instanceof Boolean) {
                return (Boolean) value;
            }
            if (value instanceof String) {
                return !((String) value).isBlank();
            }
            return true;
        }
        return true;
    }
}
