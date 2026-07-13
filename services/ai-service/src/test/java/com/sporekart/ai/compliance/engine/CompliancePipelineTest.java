package com.sporekart.ai.compliance.engine;

import com.sporekart.ai.compliance.application.*;
import com.sporekart.ai.compliance.domain.*;
import com.sporekart.ai.compliance.infrastructure.kafka.ComplianceKafkaEventPublisher;
import com.sporekart.ai.compliance.infrastructure.monitoring.ComplianceMonitoringService;
import com.sporekart.ai.compliance.infrastructure.persistence.ComplianceRuleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompliancePipelineTest {

    @Mock private ComplianceRuleRepository ruleRepository;
    @Mock private ComplianceAssessmentService assessmentService;
    @Mock private ComplianceEvidenceService evidenceService;
    @Mock private RuleEvaluationEngine evaluationEngine;
    @Mock private ComplianceValidator complianceValidator;
    @Mock private ComplianceReportingService reportingService;
    @Mock private ComplianceAuditService auditService;
    @Mock private ComplianceMetricsService metricsService;
    @Mock private ComplianceMonitoringService monitoringService;
    @Mock private ComplianceKafkaEventPublisher eventPublisher;

    private CompliancePipeline pipeline;

    @BeforeEach
    void setUp() {
        pipeline = new CompliancePipeline(ruleRepository, assessmentService, evidenceService,
            evaluationEngine, complianceValidator, reportingService, auditService,
            metricsService, monitoringService, eventPublisher);
    }

    @Test
    void testExecuteFullPipeline() {
        UUID frameworkId = UUID.randomUUID();
        ComplianceRequest request = new ComplianceRequest(
            "module", "action", Map.of("authorized", true), "user1", List.of("admin")
        );

        List<ComplianceRule> rules = List.of(new ComplianceRule(
            UUID.randomUUID(), frameworkId, "R1", "Rule", "desc", "cat",
            RiskLevel.LOW, "context.authorized", true, Instant.now(), Instant.now().plusSeconds(86400)
        ));
        when(ruleRepository.findByFrameworkIdAndActiveTrue(frameworkId)).thenReturn(rules);

        ComplianceAssessment assessment = new ComplianceAssessment(
            UUID.randomUUID(), frameworkId, "module", "action",
            AssessmentStatus.PLANNED, null, request.context(), null, Instant.now(), null
        );
        when(assessmentService.createAssessment(frameworkId, "module", "action", request.context()))
            .thenReturn(assessment);

        when(complianceValidator.validate(assessment.id(), request.context(), rules)).thenReturn(List.of());

        ComplianceResult evalResult = new ComplianceResult(
            true, ComplianceStatus.PASSED, List.of(), List.of(),
            assessment.id(), null, Map.of("totalRules", 1)
        );
        when(evaluationEngine.evaluate(assessment, rules)).thenReturn(evalResult);

        ComplianceReport report = new ComplianceReport(
            UUID.randomUUID(), frameworkId, "Report", ComplianceStatus.PASSED,
            List.of(), List.of(), Map.of(), Instant.now(), UUID.fromString(request.userId())
        );
        when(reportingService.generateReport(assessment.id(), UUID.fromString(request.userId())))
            .thenReturn(report);

        ComplianceResult result = pipeline.execute(frameworkId, request);

        assertNotNull(result);
        assertTrue(result.compliant());
        assertEquals(ComplianceStatus.PASSED, result.status());
        assertEquals(report.id(), result.reportId());

        verify(eventPublisher).publishValidationStarted(frameworkId, request);
        verify(evidenceService).collectEvidence(any(), anyString(), anyString(), anyMap());
        verify(assessmentService).completeAssessment(assessment.id(), result.status());
        verify(auditService).recordAudit(anyString(), anyString(), any(), any(), anyMap(), anyString());
        verify(metricsService).recordValidation(true);
        verify(monitoringService).recordValidation(true);
        verify(monitoringService).recordAssessmentLatency(anyLong());
        verify(eventPublisher).publishValidated(frameworkId, assessment.id(), result);
    }

    @Test
    void testExecuteWithViolations() {
        UUID frameworkId = UUID.randomUUID();
        ComplianceRequest request = new ComplianceRequest(
            "module", "action", Map.of("authorized", false), "user2", List.of("user")
        );

        List<ComplianceRule> rules = List.of(new ComplianceRule(
            UUID.randomUUID(), frameworkId, "R1", "Rule", "desc", "cat",
            RiskLevel.HIGH, "context.authorized", true, Instant.now(), Instant.now().plusSeconds(86400)
        ));
        when(ruleRepository.findByFrameworkIdAndActiveTrue(frameworkId)).thenReturn(rules);

        ComplianceAssessment assessment = new ComplianceAssessment(
            UUID.randomUUID(), frameworkId, "module", "action",
            AssessmentStatus.PLANNED, null, request.context(), null, Instant.now(), null
        );
        when(assessmentService.createAssessment(frameworkId, "module", "action", request.context()))
            .thenReturn(assessment);

        List<ComplianceViolation> violations = List.of(new ComplianceViolation(
            UUID.randomUUID(), rules.get(0).id(), assessment.id(), "module",
            ViolationSeverity.MAJOR, "Violation", Map.of(), false, Instant.now(), null
        ));
        when(complianceValidator.validate(assessment.id(), request.context(), rules)).thenReturn(violations);

        ComplianceResult evalResult = new ComplianceResult(
            false, ComplianceStatus.FAILED, violations, List.of(),
            assessment.id(), null, Map.of("totalRules", 1, "violationsCount", 1)
        );
        when(evaluationEngine.evaluate(assessment, rules)).thenReturn(evalResult);

        ComplianceReport report = new ComplianceReport(
            UUID.randomUUID(), frameworkId, "Report", ComplianceStatus.FAILED,
            List.of(), violations, Map.of(), Instant.now(), UUID.fromString(request.userId())
        );
        when(reportingService.generateReport(assessment.id(), UUID.fromString(request.userId())))
            .thenReturn(report);

        ComplianceResult result = pipeline.execute(frameworkId, request);

        assertFalse(result.compliant());
        assertEquals(ComplianceStatus.FAILED, result.status());
        assertFalse(result.violations().isEmpty());

        verify(metricsService).recordValidation(false);
        verify(metricsService).recordViolation();
        verify(monitoringService).recordValidation(false);
    }

    @Test
    void testExecuteWithNoRules() {
        UUID frameworkId = UUID.randomUUID();
        ComplianceRequest request = new ComplianceRequest(
            "module", "action", Map.of(), "user3", List.of()
        );

        when(ruleRepository.findByFrameworkIdAndActiveTrue(frameworkId)).thenReturn(List.of());

        ComplianceAssessment assessment = new ComplianceAssessment(
            UUID.randomUUID(), frameworkId, "module", "action",
            AssessmentStatus.PLANNED, null, request.context(), null, Instant.now(), null
        );
        when(assessmentService.createAssessment(frameworkId, "module", "action", request.context()))
            .thenReturn(assessment);

        when(complianceValidator.validate(assessment.id(), request.context(), List.of())).thenReturn(List.of());

        ComplianceResult evalResult = new ComplianceResult(
            true, ComplianceStatus.PASSED, List.of(), List.of(),
            assessment.id(), null, Map.of("totalRules", 0)
        );
        when(evaluationEngine.evaluate(assessment, List.of())).thenReturn(evalResult);

        ComplianceReport report = new ComplianceReport(
            UUID.randomUUID(), frameworkId, "Report", ComplianceStatus.PASSED,
            List.of(), List.of(), Map.of(), Instant.now(), UUID.fromString(request.userId())
        );
        when(reportingService.generateReport(assessment.id(), UUID.fromString(request.userId())))
            .thenReturn(report);

        ComplianceResult result = pipeline.execute(frameworkId, request);

        assertTrue(result.compliant());
        verify(eventPublisher).publishValidationStarted(frameworkId, request);
        verify(eventPublisher).publishValidated(frameworkId, assessment.id(), result);
    }
}
