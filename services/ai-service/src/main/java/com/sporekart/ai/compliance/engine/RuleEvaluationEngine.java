package com.sporekart.ai.compliance.engine;

import com.sporekart.ai.compliance.domain.ComplianceAssessment;
import com.sporekart.ai.compliance.domain.ComplianceFinding;
import com.sporekart.ai.compliance.domain.ComplianceRule;
import com.sporekart.ai.compliance.domain.ComplianceStatus;
import com.sporekart.ai.compliance.domain.ComplianceViolation;
import com.sporekart.ai.compliance.domain.ViolationSeverity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RuleEvaluationEngine {

    public ComplianceResult evaluate(ComplianceAssessment assessment, List<ComplianceRule> rules) {
        log.info("Evaluating {} rules for assessment {}", rules.size(), assessment.id());
        List<ComplianceViolation> violations = new ArrayList<>();
        List<ComplianceFinding> findings = new ArrayList<>();

        for (ComplianceRule rule : rules) {
            if (!rule.active()) {
                continue;
            }
            boolean passed = evaluateRule(rule, assessment.context());
            if (!passed) {
                ComplianceViolation violation = new ComplianceViolation(
                    UUID.randomUUID(),
                    rule.id(),
                    assessment.id(),
                    assessment.module(),
                    ViolationSeverity.MAJOR,
                    "Rule violated: " + rule.name() + " - " + rule.description(),
                    Map.of("ruleId", rule.id(), "ruleName", rule.name(), "expression", rule.expression()),
                    false,
                    Instant.now(),
                    null
                );
                violations.add(violation);
                findings.add(new ComplianceFinding(
                    UUID.randomUUID(),
                    assessment.id(),
                    "FIND-" + rule.ruleId(),
                    "Compliance finding: " + rule.name(),
                    rule.description(),
                    ViolationSeverity.MAJOR,
                    ComplianceStatus.FAILED,
                    List.of(violation),
                    "Review and remediate rule: " + rule.name(),
                    Instant.now(),
                    null
                ));
            } else {
                findings.add(new ComplianceFinding(
                    UUID.randomUUID(),
                    assessment.id(),
                    "FIND-" + rule.ruleId(),
                    "Compliance finding: " + rule.name(),
                    rule.description(),
                    ViolationSeverity.INFO,
                    ComplianceStatus.PASSED,
                    List.of(),
                    "No action required",
                    Instant.now(),
                    null
                ));
            }
        }

        boolean compliant = violations.isEmpty();
        ComplianceStatus status = compliant ? ComplianceStatus.PASSED : ComplianceStatus.FAILED;

        log.info("Evaluation complete: compliant={}, violations={}", compliant, violations.size());

        return new ComplianceResult(
            compliant,
            status,
            violations,
            findings,
            assessment.id(),
            null,
            Map.of(
                "totalRules", rules.size(),
                "violationsCount", violations.size(),
                "findingsCount", findings.size()
            )
        );
    }

    private boolean evaluateRule(ComplianceRule rule, Map<String, Object> context) {
        if (rule.expression() == null || rule.expression().isBlank()) {
            return true;
        }
        String expr = rule.expression().trim();
        if (expr.startsWith("context.")) {
            String key = expr.substring(8);
            Object value = context.get(key);
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
