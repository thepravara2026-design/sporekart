package com.sporekart.ai.compliance.application;

import com.sporekart.ai.compliance.domain.*;
import com.sporekart.ai.compliance.engine.ComplianceResult;
import com.sporekart.ai.compliance.infrastructure.persistence.ComplianceAssessmentRepository;
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
class ComplianceEngineImplTest {

    @Mock private ComplianceRegistry complianceRegistry;
    @Mock private ComplianceAssessmentService complianceAssessmentService;
    @Mock private ComplianceValidator complianceValidator;
    @Mock private ComplianceEvidenceService complianceEvidenceService;
    @Mock private ComplianceReportingService complianceReportingService;
    @Mock private ComplianceAuditService complianceAuditService;
    @Mock private ComplianceMetricsService complianceMetricsService;
    @Mock private ComplianceRuleRepository ruleRepository;
    @Mock private ComplianceAssessmentRepository assessmentRepository;

    private ComplianceEngineImpl engine;

    @BeforeEach
    void setUp() {
        engine = new ComplianceEngineImpl(complianceRegistry, complianceAssessmentService,
            complianceValidator, complianceEvidenceService, complianceReportingService,
            complianceAuditService, complianceMetricsService, ruleRepository, assessmentRepository);
    }

    @Test
    void testValidateReturnsResult() {
        UUID frameworkId = UUID.randomUUID();
        String module = "content";
        String action = "generate";
        Map<String, Object> context = Map.of("authorized", true);
        String userId = "user1";

        List<ComplianceRule> rules = List.of(new ComplianceRule(
            UUID.randomUUID(), frameworkId, "R1", "Rule", "desc", "cat",
            RiskLevel.LOW, "context.authorized", true, Instant.now(), Instant.now().plusSeconds(86400)
        ));
        when(ruleRepository.findByFrameworkIdAndActiveTrue(frameworkId)).thenReturn(rules);

        ComplianceAssessment assessment = new ComplianceAssessment(
            UUID.randomUUID(), frameworkId, module, action,
            AssessmentStatus.PLANNED, null, context, null, Instant.now(), null
        );
        when(complianceAssessmentService.createAssessment(frameworkId, module, action, context))
            .thenReturn(assessment);

        when(complianceValidator.validate(assessment.id(), context, rules)).thenReturn(List.of());

        ComplianceResult result = engine.validate(frameworkId, module, action, context, userId);

        assertNotNull(result);
        assertTrue(result.compliant());
        assertEquals(ComplianceStatus.PASSED, result.status());
        assertTrue(result.violations().isEmpty());

        verify(complianceAuditService).recordAudit(anyString(), anyString(), any(), any(), anyMap(), anyString());
        verify(complianceMetricsService).recordValidation(true);
    }

    @Test
    void testValidateReturnsNonCompliantResult() {
        UUID frameworkId = UUID.randomUUID();
        String module = "content";
        String action = "delete";
        Map<String, Object> context = Map.of("authorized", false);
        String userId = "user1";

        UUID ruleId = UUID.randomUUID();
        List<ComplianceRule> rules = List.of(new ComplianceRule(
            ruleId, frameworkId, "R1", "Rule", "desc", "cat",
            RiskLevel.HIGH, "context.authorized", true, Instant.now(), Instant.now().plusSeconds(86400)
        ));
        when(ruleRepository.findByFrameworkIdAndActiveTrue(frameworkId)).thenReturn(rules);

        ComplianceAssessment assessment = new ComplianceAssessment(
            UUID.randomUUID(), frameworkId, module, action,
            AssessmentStatus.PLANNED, null, context, null, Instant.now(), null
        );
        when(complianceAssessmentService.createAssessment(frameworkId, module, action, context))
            .thenReturn(assessment);

        List<ComplianceViolation> violations = List.of(new ComplianceViolation(
            UUID.randomUUID(), ruleId, assessment.id(), module,
            ViolationSeverity.MAJOR, "Violation", Map.of(), false, Instant.now(), null
        ));
        when(complianceValidator.validate(assessment.id(), context, rules)).thenReturn(violations);

        ComplianceResult result = engine.validate(frameworkId, module, action, context, userId);

        assertNotNull(result);
        assertFalse(result.compliant());
        assertEquals(ComplianceStatus.FAILED, result.status());
        assertFalse(result.violations().isEmpty());

        verify(complianceMetricsService).recordValidation(false);
        verify(complianceMetricsService).recordViolation();
    }

    @Test
    void testAssessCreatesAssessment() {
        UUID frameworkId = UUID.randomUUID();
        String module = "content";
        String action = "generate";
        Map<String, Object> context = Map.of();

        ComplianceAssessment assessment = new ComplianceAssessment(
            UUID.randomUUID(), frameworkId, module, action,
            AssessmentStatus.PLANNED, null, context, null, Instant.now(), null
        );
        when(complianceAssessmentService.createAssessment(frameworkId, module, action, context))
            .thenReturn(assessment);

        ComplianceAssessment result = engine.assess(frameworkId, module, action, context);

        assertNotNull(result);
        assertEquals(frameworkId, result.frameworkId());
        assertEquals(AssessmentStatus.PLANNED, result.status());
    }

    @Test
    void testGenerateReportReturnsReport() {
        UUID assessmentId = UUID.randomUUID();
        UUID generatedBy = UUID.randomUUID();

        ComplianceReport report = new ComplianceReport(
            UUID.randomUUID(), UUID.randomUUID(), "Report", ComplianceStatus.PASSED,
            List.of(), List.of(), Map.of(), Instant.now(), generatedBy
        );
        when(complianceReportingService.generateReport(assessmentId, generatedBy))
            .thenReturn(report);

        ComplianceReport result = engine.generateReport(assessmentId, generatedBy);

        assertNotNull(result);
        assertEquals("Report", result.title());
    }

    @Test
    void testGetStatusReturnsAssessment() {
        UUID assessmentId = UUID.randomUUID();
        ComplianceAssessment assessment = new ComplianceAssessment(
            assessmentId, UUID.randomUUID(), "mod", "act",
            AssessmentStatus.COMPLETED, ComplianceStatus.PASSED, Map.of(),
            null, Instant.now(), Instant.now()
        );
        when(assessmentRepository.findById(assessmentId))
            .thenReturn(Optional.of(assessment));

        Optional<ComplianceAssessment> result = engine.getStatus(assessmentId);

        assertTrue(result.isPresent());
        assertEquals(assessmentId, result.get().id());
    }

    @Test
    void testGetStatusReturnsEmptyWhenNotFound() {
        UUID assessmentId = UUID.randomUUID();
        when(assessmentRepository.findById(assessmentId))
            .thenReturn(Optional.empty());

        Optional<ComplianceAssessment> result = engine.getStatus(assessmentId);

        assertTrue(result.isEmpty());
    }
}
