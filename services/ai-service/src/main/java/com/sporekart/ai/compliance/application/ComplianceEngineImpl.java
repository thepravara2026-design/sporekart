package com.sporekart.ai.compliance.application;

import com.sporekart.ai.compliance.domain.ComplianceAssessment;
import com.sporekart.ai.compliance.domain.ComplianceReport;
import com.sporekart.ai.compliance.domain.ComplianceRule;
import com.sporekart.ai.compliance.domain.ComplianceViolation;
import com.sporekart.ai.compliance.engine.ComplianceResult;
import com.sporekart.ai.compliance.infrastructure.persistence.ComplianceAssessmentRepository;
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
public class ComplianceEngineImpl implements ComplianceEngine {

    private final ComplianceRegistry complianceRegistry;
    private final ComplianceAssessmentService complianceAssessmentService;
    private final ComplianceValidator complianceValidator;
    private final ComplianceEvidenceService complianceEvidenceService;
    private final ComplianceReportingService complianceReportingService;
    private final ComplianceAuditService complianceAuditService;
    private final ComplianceMetricsService complianceMetricsService;
    private final ComplianceRuleRepository ruleRepository;
    private final ComplianceAssessmentRepository assessmentRepository;

    @Override
    public ComplianceResult validate(UUID frameworkId, String module, String action, Map<String, Object> context, String userId) {
        log.info("Starting validation for framework {} module {} action {}", frameworkId, module, action);
        long startTime = System.currentTimeMillis();

        List<ComplianceRule> rules = ruleRepository.findByFrameworkIdAndActiveTrue(frameworkId);
        log.info("Resolved {} active rules for framework {}", rules.size(), frameworkId);

        ComplianceAssessment assessment = complianceAssessmentService.createAssessment(frameworkId, module, action, context);

        complianceEvidenceService.collectEvidence(assessment.id(), "ENGINE", "validation", Map.of("rulesCount", rules.size()));

        List<ComplianceViolation> violations = complianceValidator.validate(assessment.id(), context, rules);

        boolean compliant = violations.isEmpty();
        com.sporekart.ai.compliance.domain.ComplianceStatus status = compliant
            ? com.sporekart.ai.compliance.domain.ComplianceStatus.PASSED
            : com.sporekart.ai.compliance.domain.ComplianceStatus.FAILED;

        complianceAssessmentService.completeAssessment(assessment.id(), status);

        complianceAuditService.recordAudit(
            "ENGINE_VALIDATE",
            "ASSESSMENT",
            assessment.id(),
            UUID.fromString(userId),
            Map.of("frameworkId", frameworkId.toString(), "module", module, "action", action, "compliant", compliant, "violations", violations.size()),
            "engine"
        );

        complianceMetricsService.recordValidation(compliant);
        violations.forEach(v -> complianceMetricsService.recordViolation());
        long latency = System.currentTimeMillis() - startTime;
        complianceMetricsService.recordAssessmentLatency(latency);

        log.info("Validation completed in {}ms: compliant={}", latency, compliant);

        return new ComplianceResult(
            compliant,
            status,
            violations,
            List.of(),
            assessment.id(),
            null,
            Map.of("totalRules", rules.size(), "violationsCount", violations.size(), "latencyMs", latency)
        );
    }

    @Override
    public ComplianceAssessment assess(UUID frameworkId, String module, String action, Map<String, Object> context) {
        return complianceAssessmentService.createAssessment(frameworkId, module, action, context);
    }

    @Override
    public ComplianceReport generateReport(UUID assessmentId, UUID generatedBy) {
        return complianceReportingService.generateReport(assessmentId, generatedBy);
    }

    @Override
    public Optional<ComplianceAssessment> getStatus(UUID assessmentId) {
        return assessmentRepository.findById(assessmentId);
    }
}
