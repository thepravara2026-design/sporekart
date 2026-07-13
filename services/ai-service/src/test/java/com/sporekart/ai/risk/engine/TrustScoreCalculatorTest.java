package com.sporekart.ai.risk.engine;

import static org.junit.jupiter.api.Assertions.*;
import com.sporekart.ai.risk.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

class TrustScoreCalculatorTest {

    private TrustScoreCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new TrustScoreCalculator();
    }

    @Test
    void calculateShouldReturnScoresForAllNineTrustFactors() {
        var assessment = new RiskAssessment(UUID.randomUUID(), "prompt", "generate", RiskAssessmentStatus.PENDING, Map.of(), null, Instant.now(), null);

        var result = calculator.calculate(assessment);

        assertNotNull(result);
        assertEquals(9, result.factorScores().size());
        assertEquals(9, result.reasons().size());

        assertTrue(result.factorScores().containsKey(TrustFactor.PROVIDER_RELIABILITY));
        assertTrue(result.factorScores().containsKey(TrustFactor.KNOWLEDGE_QUALITY));
        assertTrue(result.factorScores().containsKey(TrustFactor.SEMANTIC_CONFIDENCE));
        assertTrue(result.factorScores().containsKey(TrustFactor.PROMPT_VALIDATION));
        assertTrue(result.factorScores().containsKey(TrustFactor.HISTORICAL_ACCURACY));
        assertTrue(result.factorScores().containsKey(TrustFactor.POLICY_COMPLIANCE));
        assertTrue(result.factorScores().containsKey(TrustFactor.WORKFLOW_SUCCESS));
        assertTrue(result.factorScores().containsKey(TrustFactor.CONTEXT_COMPLETENESS));
        assertTrue(result.factorScores().containsKey(TrustFactor.OUTPUT_VALIDATION));
    }

    @Test
    void calculateShouldReturnWeightedAverageScore() {
        var context = Map.of(
            "providerReliability", 100.0,
            "knowledgeQuality", 100.0,
            "semanticConfidence", 100.0,
            "promptValidation", 100.0,
            "historicalAccuracy", 100.0,
            "policyCompliance", 100.0,
            "workflowSuccess", 100.0,
            "outputValidation", 100.0
        );

        var assessment = new RiskAssessment(UUID.randomUUID(), "prompt", "generate", RiskAssessmentStatus.PENDING, context, null, Instant.now(), null);

        var result = calculator.calculate(assessment);

        assertEquals(100.0, result.score(), 0.01);
    }

    @Test
    void calculateShouldComputeWeightedAverageCorrectly() {
        var context = Map.of(
            "providerReliability", 80.0,
            "knowledgeQuality", 80.0,
            "semanticConfidence", 80.0,
            "promptValidation", 80.0,
            "historicalAccuracy", 80.0,
            "policyCompliance", 80.0,
            "workflowSuccess", 80.0,
            "contextCompleteness", 80.0,
            "outputValidation", 80.0
        );

        var assessment = new RiskAssessment(UUID.randomUUID(), "prompt", "generate", RiskAssessmentStatus.PENDING, context, null, Instant.now(), null);

        var result = calculator.calculate(assessment);

        assertEquals(80.0, result.score(), 0.01);
    }

    @Test
    void calculateShouldUseDefaultValuesWhenContextMissing() {
        var assessment = new RiskAssessment(UUID.randomUUID(), "prompt", "generate", RiskAssessmentStatus.PENDING, Map.of(), null, Instant.now(), null);

        var result = calculator.calculate(assessment);

        assertTrue(result.score() > 0);
        assertTrue(result.score() <= 100);

        assertEquals(85.0, result.factorScores().get(TrustFactor.PROVIDER_RELIABILITY));
        assertEquals(80.0, result.factorScores().get(TrustFactor.KNOWLEDGE_QUALITY));
        assertEquals(75.0, result.factorScores().get(TrustFactor.SEMANTIC_CONFIDENCE));
        assertEquals(90.0, result.factorScores().get(TrustFactor.PROMPT_VALIDATION));
        assertEquals(70.0, result.factorScores().get(TrustFactor.HISTORICAL_ACCURACY));
        assertEquals(85.0, result.factorScores().get(TrustFactor.POLICY_COMPLIANCE));
        assertEquals(80.0, result.factorScores().get(TrustFactor.WORKFLOW_SUCCESS));
        assertEquals(80.0, result.factorScores().get(TrustFactor.OUTPUT_VALIDATION));
    }

    @Test
    void contextCompletenessShouldScaleWithContextSize() {
        var emptyContext = new RiskAssessment(UUID.randomUUID(), "prompt", "generate", RiskAssessmentStatus.PENDING, Map.of(), null, Instant.now(), null);
        assertEquals(50.0, calculator.calculate(emptyContext).factorScores().get(TrustFactor.CONTEXT_COMPLETENESS));

        var smallContext = new RiskAssessment(UUID.randomUUID(), "prompt", "generate", RiskAssessmentStatus.PENDING, Map.of("a", 1, "b", 2, "c", 3), null, Instant.now(), null);
        assertEquals(65.0, calculator.calculate(smallContext).factorScores().get(TrustFactor.CONTEXT_COMPLETENESS));

        var mediumContext = new RiskAssessment(UUID.randomUUID(), "prompt", "generate", RiskAssessmentStatus.PENDING, Map.of("a", 1, "b", 2, "c", 3, "d", 4, "e", 5), null, Instant.now(), null);
        assertEquals(75.0, calculator.calculate(mediumContext).factorScores().get(TrustFactor.CONTEXT_COMPLETENESS));
    }

    @Test
    void calculateShouldHaveReasonsForAllFactors() {
        var assessment = new RiskAssessment(UUID.randomUUID(), "prompt", "generate", RiskAssessmentStatus.PENDING, Map.of(), null, Instant.now(), null);

        var result = calculator.calculate(assessment);

        for (var factor : TrustFactor.values()) {
            assertTrue(result.reasons().containsKey(factor));
            assertNotNull(result.reasons().get(factor));
            assertFalse(result.reasons().get(factor).isEmpty());
        }
    }

    @Test
    void calculateShouldNotExceedHundred() {
        var context = Map.of(
            "providerReliability", 200.0,
            "knowledgeQuality", 200.0,
            "semanticConfidence", 200.0,
            "promptValidation", 200.0,
            "historicalAccuracy", 200.0,
            "policyCompliance", 200.0,
            "workflowSuccess", 200.0,
            "outputValidation", 200.0
        );

        var assessment = new RiskAssessment(UUID.randomUUID(), "prompt", "generate", RiskAssessmentStatus.PENDING, context, null, Instant.now(), null);

        var result = calculator.calculate(assessment);

        assertEquals(100.0, result.score(), 0.01);
    }

    @Test
    void calculateShouldNotGoBelowZero() {
        var context = Map.of(
            "providerReliability", -100.0,
            "knowledgeQuality", -100.0
        );

        var assessment = new RiskAssessment(UUID.randomUUID(), "prompt", "generate", RiskAssessmentStatus.PENDING, context, null, Instant.now(), null);

        var result = calculator.calculate(assessment);

        assertEquals(0.0, result.score(), 0.01);
    }
}
