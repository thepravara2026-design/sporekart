package com.sporekart.ai.compliance.engine;

import com.sporekart.ai.compliance.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class RuleEvaluationEngineTest {

    private RuleEvaluationEngine engine;

    @BeforeEach
    void setUp() {
        engine = new RuleEvaluationEngine();
    }

    @Test
    void testEvaluateWithMatchingRules() {
        UUID frameworkId = UUID.randomUUID();
        UUID assessmentId = UUID.randomUUID();
        Map<String, Object> context = Map.of("authorized", true);

        ComplianceAssessment assessment = new ComplianceAssessment(
            assessmentId, frameworkId, "module", "action",
            AssessmentStatus.PLANNED, null, context, null, Instant.now(), null
        );

        List<ComplianceRule> rules = List.of(
            new ComplianceRule(UUID.randomUUID(), frameworkId, "R1", "Rule1", "desc", "cat",
                RiskLevel.LOW, "context.authorized", true, Instant.now(), Instant.now().plusSeconds(86400))
        );

        ComplianceResult result = engine.evaluate(assessment, rules);

        assertTrue(result.compliant());
        assertEquals(ComplianceStatus.PASSED, result.status());
        assertTrue(result.violations().isEmpty());
        assertEquals(1, result.findings().size());
        assertEquals(ComplianceStatus.PASSED, result.findings().get(0).status());
    }

    @Test
    void testEvaluateWithNonMatchingRules() {
        UUID frameworkId = UUID.randomUUID();
        UUID assessmentId = UUID.randomUUID();
        Map<String, Object> context = Map.of("authorized", false);

        ComplianceAssessment assessment = new ComplianceAssessment(
            assessmentId, frameworkId, "module", "action",
            AssessmentStatus.PLANNED, null, context, null, Instant.now(), null
        );

        List<ComplianceRule> rules = List.of(
            new ComplianceRule(UUID.randomUUID(), frameworkId, "R1", "Rule1", "desc", "cat",
                RiskLevel.HIGH, "context.authorized", true, Instant.now(), Instant.now().plusSeconds(86400))
        );

        ComplianceResult result = engine.evaluate(assessment, rules);

        assertFalse(result.compliant());
        assertEquals(ComplianceStatus.FAILED, result.status());
        assertFalse(result.violations().isEmpty());
        assertEquals(1, result.violations().size());
        assertEquals(ViolationSeverity.MAJOR, result.violations().get(0).severity());
    }

    @Test
    void testEvaluateSkipsInactiveRules() {
        UUID frameworkId = UUID.randomUUID();
        UUID assessmentId = UUID.randomUUID();
        Map<String, Object> context = Map.of("authorized", false);

        ComplianceAssessment assessment = new ComplianceAssessment(
            assessmentId, frameworkId, "module", "action",
            AssessmentStatus.PLANNED, null, context, null, Instant.now(), null
        );

        List<ComplianceRule> rules = List.of(
            new ComplianceRule(UUID.randomUUID(), frameworkId, "R1", "Rule1", "desc", "cat",
                RiskLevel.HIGH, "context.authorized", false, Instant.now(), Instant.now().plusSeconds(86400))
        );

        ComplianceResult result = engine.evaluate(assessment, rules);

        assertTrue(result.compliant());
        assertTrue(result.violations().isEmpty());
        assertEquals(0, result.findings().size());
    }

    @Test
    void testEvaluateWithMixedRules() {
        UUID frameworkId = UUID.randomUUID();
        UUID assessmentId = UUID.randomUUID();
        Map<String, Object> context = Map.of("authorized", true, "role", "admin");

        ComplianceAssessment assessment = new ComplianceAssessment(
            assessmentId, frameworkId, "module", "action",
            AssessmentStatus.PLANNED, null, context, null, Instant.now(), null
        );

        List<ComplianceRule> rules = List.of(
            new ComplianceRule(UUID.randomUUID(), frameworkId, "R1", "Passing", "desc", "cat",
                RiskLevel.LOW, "context.authorized", true, Instant.now(), Instant.now().plusSeconds(86400)),
            new ComplianceRule(UUID.randomUUID(), frameworkId, "R2", "Failing", "desc", "cat",
                RiskLevel.CRITICAL, "context.nonexistent", true, Instant.now(), Instant.now().plusSeconds(86400))
        );

        ComplianceResult result = engine.evaluate(assessment, rules);

        assertFalse(result.compliant());
        assertEquals(1, result.violations().size());
        assertEquals(2, result.findings().size());
        assertEquals(ComplianceStatus.PASSED, result.findings().get(0).status());
        assertEquals(ComplianceStatus.FAILED, result.findings().get(1).status());
    }

    @Test
    void testEvaluateWithNullExpression() {
        UUID frameworkId = UUID.randomUUID();
        UUID assessmentId = UUID.randomUUID();
        Map<String, Object> context = Map.of();

        ComplianceAssessment assessment = new ComplianceAssessment(
            assessmentId, frameworkId, "module", "action",
            AssessmentStatus.PLANNED, null, context, null, Instant.now(), null
        );

        List<ComplianceRule> rules = List.of(
            new ComplianceRule(UUID.randomUUID(), frameworkId, "R1", "Rule", "desc", "cat",
                RiskLevel.LOW, null, true, Instant.now(), Instant.now().plusSeconds(86400))
        );

        ComplianceResult result = engine.evaluate(assessment, rules);

        assertTrue(result.compliant());
        assertTrue(result.violations().isEmpty());
    }

    @Test
    void testEvaluateWithNoRules() {
        UUID frameworkId = UUID.randomUUID();
        UUID assessmentId = UUID.randomUUID();
        Map<String, Object> context = Map.of();

        ComplianceAssessment assessment = new ComplianceAssessment(
            assessmentId, frameworkId, "module", "action",
            AssessmentStatus.PLANNED, null, context, null, Instant.now(), null
        );

        ComplianceResult result = engine.evaluate(assessment, List.of());

        assertTrue(result.compliant());
        assertTrue(result.violations().isEmpty());
        assertTrue(result.findings().isEmpty());
    }
}
