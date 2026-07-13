package com.sporekart.ai.compliance.engine;

import com.sporekart.ai.compliance.application.ComplianceAssessmentService;
import com.sporekart.ai.compliance.application.ComplianceAuditService;
import com.sporekart.ai.compliance.application.ComplianceEvidenceService;
import com.sporekart.ai.compliance.application.ComplianceMetricsService;
import com.sporekart.ai.compliance.application.ComplianceReportingService;
import com.sporekart.ai.compliance.application.ComplianceValidator;
import com.sporekart.ai.compliance.domain.ComplianceAssessment;
import com.sporekart.ai.compliance.domain.ComplianceReport;
import com.sporekart.ai.compliance.domain.ComplianceRule;
import com.sporekart.ai.compliance.domain.ComplianceViolation;
import com.sporekart.ai.compliance.infrastructure.kafka.ComplianceKafkaEventPublisher;
import com.sporekart.ai.compliance.infrastructure.monitoring.ComplianceMonitoringService;
import com.sporekart.ai.compliance.infrastructure.persistence.ComplianceRuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CompliancePipeline {

    private final ComplianceRuleRepository ruleRepository;
    private final ComplianceAssessmentService assessmentService;
    private final ComplianceEvidenceService evidenceService;
    private final RuleEvaluationEngine evaluationEngine;
    private final ComplianceValidator complianceValidator;
    private final ComplianceReportingService reportingService;
    private final ComplianceAuditService auditService;
    private final ComplianceMetricsService metricsService;
    private final ComplianceMonitoringService monitoringService;
    private final ComplianceKafkaEventPublisher eventPublisher;

    public ComplianceResult execute(UUID frameworkId, ComplianceRequest request) {
        long startTime = System.currentTimeMillis();
        log.info("Starting compliance pipeline for framework {} module {} action {}", frameworkId, request.module(), request.action());

        eventPublisher.publishValidationStarted(frameworkId, request);

        List<ComplianceRule> rules = ruleRepository.findByFrameworkIdAndActiveTrue(frameworkId);
        log.info("Resolved {} active rules for framework {}", rules.size(), frameworkId);

        ComplianceAssessment assessment = assessmentService.createAssessment(
            frameworkId, request.module(), request.action(), request.context()
        );

        evidenceService.collectEvidence(assessment.id(), "SYSTEM", "pipeline", Map.of("rulesCount", rules.size()));

        List<ComplianceViolation> violations = complianceValidator.validate(
            assessment.id(), assessment.context(), rules
        );

        ComplianceResult result = evaluationEngine.evaluate(assessment, rules);
        result = new ComplianceResult(
            result.compliant(),
            result.status(),
            violations,
            result.findings(),
            result.assessmentId(),
            result.reportId(),
            result.details()
        );

        ComplianceReport report = reportingService.generateReport(assessment.id(), UUID.fromString(request.userId()));

        result = new ComplianceResult(
            result.compliant(),
            result.status(),
            result.violations(),
            result.findings(),
            result.assessmentId(),
            report.id(),
            result.details()
        );

        assessmentService.completeAssessment(assessment.id(), result.status());

        auditService.recordAudit(
            "PIPELINE_EXECUTE",
            "ASSESSMENT",
            assessment.id(),
            UUID.fromString(request.userId()),
            Map.of(
                "frameworkId", frameworkId.toString(),
                "module", request.module(),
                "action", request.action(),
                "compliant", result.compliant(),
                "violations", violations.size()
            ),
            "pipeline"
        );

        metricsService.recordValidation(result.compliant());
        violations.forEach(v -> metricsService.recordViolation());
        long latency = System.currentTimeMillis() - startTime;
        metricsService.recordAssessmentLatency(latency);
        monitoringService.recordValidation(result.compliant());
        monitoringService.recordAssessmentLatency(latency);

        eventPublisher.publishValidated(frameworkId, assessment.id(), result);

        log.info("Compliance pipeline completed in {}ms: compliant={}", latency, result.compliant());
        return result;
    }
}
