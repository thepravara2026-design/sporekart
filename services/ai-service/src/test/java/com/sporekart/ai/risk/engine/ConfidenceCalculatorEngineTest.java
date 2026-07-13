package com.sporekart.ai.risk.engine;

import static org.junit.jupiter.api.Assertions.*;
import com.sporekart.ai.risk.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

class ConfidenceCalculatorEngineTest {

    private ConfidenceCalculatorEngine engine;

    @BeforeEach
    void setUp() {
        engine = new ConfidenceCalculatorEngine();
    }

    @Test
    void calculateShouldReturnScoresForAllSixConfidenceFactors() {
        var assessment = new RiskAssessment(UUID.randomUUID(), "prompt", "generate", RiskAssessmentStatus.PENDING, Map.of(), null, Instant.now(), null);

        var result = engine.calculate(assessment);

        assertNotNull(result);
        assertEquals(6, result.factorScores().size());

        assertTrue(result.factorScores().containsKey(ConfidenceFactor.KNOWLEDGE_MATCH));
        assertTrue(result.factorScores().containsKey(ConfidenceFactor.SEMANTIC_SIMILARITY));
        assertTrue(result.factorScores().containsKey(ConfidenceFactor.PROMPT_QUALITY));
        assertTrue(result.factorScores().containsKey(ConfidenceFactor.CONVERSATION_CONTEXT));
        assertTrue(result.factorScores().containsKey(ConfidenceFactor.WORKFLOW_SUCCESS));
        assertTrue(result.factorScores().containsKey(ConfidenceFactor.PROVIDER_METADATA));
    }

    @Test
    void calculateShouldReturnWeightedAverageScore() {
        var context = Map.of(
            "knowledgeMatch", 100.0,
            "semanticSimilarity", 100.0,
            "promptQuality", 100.0,
            "workflowSuccess", 100.0,
            "providerMetadata", 100.0
        );

        var assessment = new RiskAssessment(UUID.randomUUID(), "prompt", "generate", RiskAssessmentStatus.PENDING, context, null, Instant.now(), null);

        var result = engine.calculate(assessment);

        assertEquals(100.0, result.score(), 0.01);
    }

    @Test
    void calculateShouldComputeWeightedAverageCorrectly() {
        var context = Map.of(
            "knowledgeMatch", 80.0,
            "semanticSimilarity", 80.0,
            "promptQuality", 80.0,
            "workflowSuccess", 80.0,
            "providerMetadata", 80.0
        );

        var assessment = new RiskAssessment(UUID.randomUUID(), "prompt", "generate", RiskAssessmentStatus.PENDING, context, null, Instant.now(), null);

        var result = engine.calculate(assessment);

        assertEquals(80.0, result.score(), 0.01);
    }

    @Test
    void calculateShouldUseDefaultValuesWhenContextMissing() {
        var assessment = new RiskAssessment(UUID.randomUUID(), "prompt", "generate", RiskAssessmentStatus.PENDING, Map.of(), null, Instant.now(), null);

        var result = engine.calculate(assessment);

        assertTrue(result.score() > 0);
        assertTrue(result.score() <= 100);

        assertEquals(75.0, result.factorScores().get(ConfidenceFactor.KNOWLEDGE_MATCH));
        assertEquals(75.0, result.factorScores().get(ConfidenceFactor.SEMANTIC_SIMILARITY));
        assertEquals(80.0, result.factorScores().get(ConfidenceFactor.PROMPT_QUALITY));
        assertEquals(70.0, result.factorScores().get(ConfidenceFactor.CONVERSATION_CONTEXT));
        assertEquals(80.0, result.factorScores().get(ConfidenceFactor.WORKFLOW_SUCCESS));
        assertEquals(70.0, result.factorScores().get(ConfidenceFactor.PROVIDER_METADATA));
    }

    @Test
    void conversationContextShouldScaleWithDepth() {
        var shallowContext = new RiskAssessment(UUID.randomUUID(), "prompt", "generate", RiskAssessmentStatus.PENDING, Map.of("conversationDepth", 2), null, Instant.now(), null);
        assertEquals(60.0, engine.calculate(shallowContext).factorScores().get(ConfidenceFactor.CONVERSATION_CONTEXT));

        var midContext = new RiskAssessment(UUID.randomUUID(), "prompt", "generate", RiskAssessmentStatus.PENDING, Map.of("conversationDepth", 5), null, Instant.now(), null);
        assertEquals(75.0, engine.calculate(midContext).factorScores().get(ConfidenceFactor.CONVERSATION_CONTEXT));

        var deepContext = new RiskAssessment(UUID.randomUUID(), "prompt", "generate", RiskAssessmentStatus.PENDING, Map.of("conversationDepth", 10), null, Instant.now(), null);
        assertEquals(90.0, engine.calculate(deepContext).factorScores().get(ConfidenceFactor.CONVERSATION_CONTEXT));
    }

    @Test
    void calculateShouldIncludeExplanation() {
        var assessment = new RiskAssessment(UUID.randomUUID(), "prompt", "generate", RiskAssessmentStatus.PENDING, Map.of(), null, Instant.now(), null);

        var result = engine.calculate(assessment);

        assertNotNull(result.explanation());
        assertFalse(result.explanation().isEmpty());
        assertTrue(result.explanation().contains("KnowledgeMatch"));
        assertTrue(result.explanation().contains("SemanticSimilarity"));
        assertTrue(result.explanation().contains("PromptQuality"));
        assertTrue(result.explanation().contains("ConversationContext"));
        assertTrue(result.explanation().contains("WorkflowSuccess"));
        assertTrue(result.explanation().contains("ProviderMetadata"));
    }

    @Test
    void calculateShouldNotExceedHundred() {
        var context = Map.of(
            "knowledgeMatch", 200.0,
            "semanticSimilarity", 200.0,
            "promptQuality", 200.0,
            "conversationDepth", 20,
            "workflowSuccess", 200.0,
            "providerMetadata", 200.0
        );

        var assessment = new RiskAssessment(UUID.randomUUID(), "prompt", "generate", RiskAssessmentStatus.PENDING, context, null, Instant.now(), null);

        var result = engine.calculate(assessment);

        assertEquals(100.0, result.score(), 0.01);
    }

    @Test
    void calculateShouldNotGoBelowZero() {
        var context = Map.of("knowledgeMatch", -100.0, "semanticSimilarity", -100.0);

        var assessment = new RiskAssessment(UUID.randomUUID(), "prompt", "generate", RiskAssessmentStatus.PENDING, context, null, Instant.now(), null);

        var result = engine.calculate(assessment);

        assertEquals(0.0, result.score(), 0.01);
    }

    @Test
    void promptQualityShouldUseContextValueWhenPresent() {
        var context = Map.of("promptQuality", 95.0);
        var assessment = new RiskAssessment(UUID.randomUUID(), "prompt", "generate", RiskAssessmentStatus.PENDING, context, null, Instant.now(), null);

        var result = engine.calculate(assessment);

        assertEquals(95.0, result.factorScores().get(ConfidenceFactor.PROMPT_QUALITY));
    }
}
