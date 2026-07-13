package com.sporekart.ai.compliance.application;

import com.sporekart.ai.compliance.domain.*;
import com.sporekart.ai.compliance.infrastructure.persistence.ComplianceAuditRepository;
import com.sporekart.ai.compliance.infrastructure.persistence.ComplianceRuleRepository;
import com.sporekart.ai.compliance.infrastructure.persistence.ComplianceViolationRepository;
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
class ComplianceValidatorImplTest {

    @Mock private ComplianceRuleRepository ruleRepository;
    @Mock private ComplianceViolationRepository violationRepository;
    @Mock private ComplianceAuditService complianceAuditService;

    private ComplianceValidatorImpl validator;

    @BeforeEach
    void setUp() {
        validator = new ComplianceValidatorImpl(ruleRepository, violationRepository, complianceAuditService);
    }

    @Test
    void testValidateReturnsEmptyForCompliantRules() {
        UUID assessmentId = UUID.randomUUID();
        Map<String, Object> context = Map.of("authorized", true);
        List<ComplianceRule> rules = List.of(new ComplianceRule(
            UUID.randomUUID(), UUID.randomUUID(), "R1", "Rule", "desc", "cat",
            RiskLevel.LOW, "context.authorized", true, Instant.now(), Instant.now().plusSeconds(86400)
        ));

        List<ComplianceViolation> violations = validator.validate(assessmentId, context, rules);

        assertTrue(violations.isEmpty());
        verify(violationRepository, never()).saveAll(any());
        verify(complianceAuditService, never()).recordAudit(anyString(), anyString(), any(), any(), anyMap(), anyString());
    }

    @Test
    void testValidateReturnsViolationsForNonCompliantRules() {
        UUID assessmentId = UUID.randomUUID();
        Map<String, Object> context = Map.of("authorized", false);
        List<ComplianceRule> rules = List.of(new ComplianceRule(
            UUID.randomUUID(), UUID.randomUUID(), "R1", "Rule", "desc", "cat",
            RiskLevel.CRITICAL, "context.authorized", true, Instant.now(), Instant.now().plusSeconds(86400)
        ));

        List<ComplianceViolation> violations = validator.validate(assessmentId, context, rules);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals(ViolationSeverity.CRITICAL, violations.get(0).severity());
        verify(violationRepository).saveAll(anyList());
        verify(complianceAuditService).recordAudit(anyString(), anyString(), any(), any(), anyMap(), anyString());
    }

    @Test
    void testValidateSkipsInactiveRules() {
        UUID assessmentId = UUID.randomUUID();
        Map<String, Object> context = Map.of("authorized", false);
        List<ComplianceRule> rules = List.of(new ComplianceRule(
            UUID.randomUUID(), UUID.randomUUID(), "R1", "Rule", "desc", "cat",
            RiskLevel.HIGH, "context.authorized", false, Instant.now(), Instant.now().plusSeconds(86400)
        ));

        List<ComplianceViolation> violations = validator.validate(assessmentId, context, rules);

        assertTrue(violations.isEmpty());
    }

    @Test
    void testValidateRulesDelegatesToValidate() {
        UUID assessmentId = UUID.randomUUID();
        Map<String, Object> context = Map.of();
        List<ComplianceRule> rules = List.of();

        List<ComplianceViolation> violations = validator.validateRules(assessmentId, context, rules);

        assertTrue(violations.isEmpty());
    }

    @Test
    void testValidateAgainstFrameworkFetchesRulesAndValidates() {
        UUID assessmentId = UUID.randomUUID();
        UUID frameworkId = UUID.randomUUID();
        Map<String, Object> context = Map.of("authorized", true);

        List<ComplianceRule> rules = List.of(new ComplianceRule(
            UUID.randomUUID(), frameworkId, "R1", "Rule", "desc", "cat",
            RiskLevel.LOW, "context.authorized", true, Instant.now(), Instant.now().plusSeconds(86400)
        ));
        when(ruleRepository.findByFrameworkIdAndActiveTrue(frameworkId)).thenReturn(rules);

        List<ComplianceViolation> violations = validator.validateAgainstFramework(assessmentId, context, frameworkId);

        assertTrue(violations.isEmpty());
        verify(ruleRepository).findByFrameworkIdAndActiveTrue(frameworkId);
    }
}
