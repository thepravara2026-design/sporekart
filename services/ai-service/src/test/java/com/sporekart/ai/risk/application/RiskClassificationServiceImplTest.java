package com.sporekart.ai.risk.application;

import static org.junit.jupiter.api.Assertions.*;
import com.sporekart.ai.risk.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.*;

class RiskClassificationServiceImplTest {

    private RiskClassificationServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new RiskClassificationServiceImpl();
    }

    @Test
    void classifyShouldReturnTechnicalWhenContextIsEmpty() {
        var assessment = new RiskAssessment(UUID.randomUUID(), "prompt", "generate", RiskAssessmentStatus.PENDING, Map.of(), null, Instant.now(), null);
        assertEquals(RiskCategory.TECHNICAL, service.classify(assessment));
    }

    @Test
    void classifyShouldReturnTechnicalWhenContextIsNull() {
        var assessment = new RiskAssessment(UUID.randomUUID(), "prompt", "generate", RiskAssessmentStatus.PENDING, null, null, Instant.now(), null);
        assertEquals(RiskCategory.TECHNICAL, service.classify(assessment));
    }

    @Test
    void classifyShouldReturnSecurityWhenContextHasSecurityKey() {
        var assessment = assessmentWithContext("security", "high");
        assertEquals(RiskCategory.SECURITY, service.classify(assessment));
    }

    @Test
    void classifyShouldReturnSecurityWhenContextHasPrivacyKey() {
        var assessment = assessmentWithContext("privacy", "strict");
        assertEquals(RiskCategory.SECURITY, service.classify(assessment));
    }

    @Test
    void classifyShouldReturnComplianceWhenContextHasComplianceKey() {
        var assessment = assessmentWithContext("compliance", "gdpr");
        assertEquals(RiskCategory.COMPLIANCE, service.classify(assessment));
    }

    @Test
    void classifyShouldReturnComplianceWhenContextHasRegulationKey() {
        var assessment = assessmentWithContext("regulation", "hipaa");
        assertEquals(RiskCategory.COMPLIANCE, service.classify(assessment));
    }

    @Test
    void classifyShouldReturnFinancialWhenContextHasFinancialKey() {
        var assessment = assessmentWithContext("financial", "10000");
        assertEquals(RiskCategory.FINANCIAL, service.classify(assessment));
    }

    @Test
    void classifyShouldReturnFinancialWhenContextHasRevenueKey() {
        var assessment = assessmentWithContext("revenue", "high");
        assertEquals(RiskCategory.FINANCIAL, service.classify(assessment));
    }

    @Test
    void classifyShouldReturnReputationalWhenContextHasReputationKey() {
        var assessment = assessmentWithContext("reputation", "risk");
        assertEquals(RiskCategory.REPUTATIONAL, service.classify(assessment));
    }

    @Test
    void classifyShouldReturnReputationalWhenContextHasBrandKey() {
        var assessment = assessmentWithContext("brand", "value");
        assertEquals(RiskCategory.REPUTATIONAL, service.classify(assessment));
    }

    @Test
    void classifyShouldReturnEthicalWhenContextHasEthicalKey() {
        var assessment = assessmentWithContext("ethical", "bias");
        assertEquals(RiskCategory.ETHICAL, service.classify(assessment));
    }

    @Test
    void classifyShouldReturnEthicalWhenContextHasFairnessKey() {
        var assessment = assessmentWithContext("fairness", "check");
        assertEquals(RiskCategory.ETHICAL, service.classify(assessment));
    }

    @Test
    void classifyShouldReturnOperationalWhenContextHasOperationalKey() {
        var assessment = assessmentWithContext("operational", "efficiency");
        assertEquals(RiskCategory.OPERATIONAL, service.classify(assessment));
    }

    @Test
    void classifyShouldReturnOperationalWhenContextHasProcessKey() {
        var assessment = assessmentWithContext("process", "optimization");
        assertEquals(RiskCategory.OPERATIONAL, service.classify(assessment));
    }

    @Test
    void classifyFactorsShouldReturnMultipleCategories() {
        var context = new HashMap<String, Object>();
        context.put("security", "high");
        context.put("financial", "impact");
        context.put("ethical", "bias");
        var assessment = new RiskAssessment(UUID.randomUUID(), "prompt", "generate", RiskAssessmentStatus.PENDING, context, null, Instant.now(), null);

        var categories = service.classifyFactors(assessment);

        assertTrue(categories.contains(RiskCategory.SECURITY));
        assertTrue(categories.contains(RiskCategory.FINANCIAL));
        assertTrue(categories.contains(RiskCategory.ETHICAL));
    }

    @Test
    void classifyFactorsShouldReturnTechnicalWhenNoContext() {
        var assessment = new RiskAssessment(UUID.randomUUID(), "prompt", "generate", RiskAssessmentStatus.PENDING, Map.of(), null, Instant.now(), null);

        var categories = service.classifyFactors(assessment);

        assertEquals(List.of(RiskCategory.TECHNICAL), categories);
    }

    @Test
    void categorizeScoresShouldMergeDuplicates() {
        var scores = Map.of(
            RiskCategory.SECURITY, 80.0,
            RiskCategory.SECURITY, 90.0,
            RiskCategory.COMPLIANCE, 70.0
        );
        var result = service.categorizeScores(scores);
        assertEquals(2, result.size());
    }

    private RiskAssessment assessmentWithContext(String key, String value) {
        return new RiskAssessment(UUID.randomUUID(), "prompt", "generate", RiskAssessmentStatus.PENDING, Map.of(key, value), null, Instant.now(), null);
    }
}
