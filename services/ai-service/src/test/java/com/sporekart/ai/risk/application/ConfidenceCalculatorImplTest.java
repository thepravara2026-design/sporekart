package com.sporekart.ai.risk.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import com.sporekart.ai.risk.domain.*;
import com.sporekart.ai.risk.engine.ConfidenceCalculatorEngine;
import com.sporekart.ai.risk.infrastructure.persistence.ConfidenceScoreEntity;
import com.sporekart.ai.risk.infrastructure.persistence.ConfidenceScoreRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class ConfidenceCalculatorImplTest {

    @Mock
    private ConfidenceScoreRepository confidenceScoreRepository;
    @Mock
    private ConfidenceCalculatorEngine confidenceCalculatorEngine;
    @Mock
    private ObjectMapper objectMapper;

    private ConfidenceCalculatorImpl calculator;

    @BeforeEach
    void setUp() {
        calculator = new ConfidenceCalculatorImpl(confidenceScoreRepository, confidenceCalculatorEngine, objectMapper);
    }

    @Test
    void calculateConfidenceShouldReturnConfidenceScoreWithSixFactors() {
        var assessmentId = UUID.randomUUID();
        var assessment = new RiskAssessment(assessmentId, "prompt", "generate", RiskAssessmentStatus.PENDING, Map.of(), null, Instant.now(), null);

        var factorScores = new EnumMap<ConfidenceFactor, Double>(ConfidenceFactor.class);
        for (var factor : ConfidenceFactor.values()) {
            factorScores.put(factor, 80.0);
        }

        var confidenceResult = new ConfidenceCalculatorEngine.ConfidenceResult(80.0, factorScores, "explanation");
        when(confidenceCalculatorEngine.calculate(assessment)).thenReturn(confidenceResult);
        when(confidenceScoreRepository.save(any(ConfidenceScoreEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(objectMapper.writeValueAsString(any())).thenReturn("{}");

        var result = calculator.calculateConfidence(assessment);

        assertNotNull(result);
        assertEquals(assessmentId, result.assessmentId());
        assertEquals(80.0, result.overallConfidence());
        assertEquals(6, result.factorScores().size());
        assertEquals("explanation", result.explanation());

        verify(confidenceCalculatorEngine).calculate(assessment);
        verify(confidenceScoreRepository).save(any(ConfidenceScoreEntity.class));
    }

    @Test
    void calculateConfidenceShouldIncludeAllConfidenceFactors() {
        var assessmentId = UUID.randomUUID();
        var assessment = new RiskAssessment(assessmentId, "prompt", "generate", RiskAssessmentStatus.PENDING, Map.of(), null, Instant.now(), null);

        var factorScores = new EnumMap<ConfidenceFactor, Double>(ConfidenceFactor.class);
        factorScores.put(ConfidenceFactor.KNOWLEDGE_MATCH, 90.0);
        factorScores.put(ConfidenceFactor.SEMANTIC_SIMILARITY, 85.0);
        factorScores.put(ConfidenceFactor.PROMPT_QUALITY, 80.0);
        factorScores.put(ConfidenceFactor.CONVERSATION_CONTEXT, 75.0);
        factorScores.put(ConfidenceFactor.WORKFLOW_SUCCESS, 70.0);
        factorScores.put(ConfidenceFactor.PROVIDER_METADATA, 65.0);

        var confidenceResult = new ConfidenceCalculatorEngine.ConfidenceResult(77.5, factorScores, "detailed explanation");
        when(confidenceCalculatorEngine.calculate(assessment)).thenReturn(confidenceResult);
        when(confidenceScoreRepository.save(any(ConfidenceScoreEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(objectMapper.writeValueAsString(any())).thenReturn("{}");

        var result = calculator.calculateConfidence(assessment);

        assertEquals(90.0, result.factorScores().get(ConfidenceFactor.KNOWLEDGE_MATCH));
        assertEquals(85.0, result.factorScores().get(ConfidenceFactor.SEMANTIC_SIMILARITY));
        assertEquals(80.0, result.factorScores().get(ConfidenceFactor.PROMPT_QUALITY));
        assertEquals(75.0, result.factorScores().get(ConfidenceFactor.CONVERSATION_CONTEXT));
        assertEquals(70.0, result.factorScores().get(ConfidenceFactor.WORKFLOW_SUCCESS));
        assertEquals(65.0, result.factorScores().get(ConfidenceFactor.PROVIDER_METADATA));
    }

    @Test
    void recalculateConfidenceShouldReturnExistingScore() {
        var assessmentId = UUID.randomUUID();
        var entity = new ConfidenceScoreEntity();
        entity.setId(UUID.randomUUID());
        entity.setAssessmentId(assessmentId);
        entity.setOverallConfidence(70.0);
        entity.setFactorScores("{}");
        entity.setExplanation("recalculated");
        entity.setCalculatedAt(LocalDateTime.now());

        when(confidenceScoreRepository.findByAssessmentId(assessmentId)).thenReturn(List.of(entity));
        when(confidenceScoreRepository.save(any(ConfidenceScoreEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(objectMapper.readValue(anyString(), any(Class.class))).thenReturn(Map.of());
        when(objectMapper.writeValueAsString(any())).thenReturn("{}");

        var result = calculator.recalculateConfidence(assessmentId);

        assertEquals(70.0, result.overallConfidence());
    }

    @Test
    void recalculateConfidenceShouldThrowWhenNotFound() {
        when(confidenceScoreRepository.findByAssessmentId(any())).thenReturn(List.of());
        assertThrows(RuntimeException.class, () -> calculator.recalculateConfidence(UUID.randomUUID()));
    }

    @Test
    void getConfidenceScoreShouldReturnScore() {
        var assessmentId = UUID.randomUUID();
        var entity = new ConfidenceScoreEntity();
        entity.setOverallConfidence(88.5);

        when(confidenceScoreRepository.findByAssessmentId(assessmentId)).thenReturn(List.of(entity));

        assertEquals(88.5, calculator.getConfidenceScore(assessmentId));
    }

    @Test
    void getConfidenceScoreShouldReturnZeroWhenNotFound() {
        when(confidenceScoreRepository.findByAssessmentId(any())).thenReturn(List.of());
        assertEquals(0.0, calculator.getConfidenceScore(UUID.randomUUID()));
    }
}
