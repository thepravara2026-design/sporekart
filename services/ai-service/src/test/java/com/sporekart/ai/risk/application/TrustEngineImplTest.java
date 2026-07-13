package com.sporekart.ai.risk.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import com.sporekart.ai.risk.domain.*;
import com.sporekart.ai.risk.engine.TrustScoreCalculator;
import com.sporekart.ai.risk.infrastructure.persistence.TrustScoreEntity;
import com.sporekart.ai.risk.infrastructure.persistence.TrustScoreRepository;
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
class TrustEngineImplTest {

    @Mock
    private TrustScoreRepository trustScoreRepository;
    @Mock
    private TrustScoreCalculator trustScoreCalculator;
    @Mock
    private ObjectMapper objectMapper;

    private TrustEngineImpl trustEngine;

    @BeforeEach
    void setUp() {
        trustEngine = new TrustEngineImpl(trustScoreRepository, trustScoreCalculator, objectMapper);
    }

    @Test
    void calculateTrustShouldReturnTrustAssessmentWithNineFactors() {
        var assessmentId = UUID.randomUUID();
        var assessment = new RiskAssessment(assessmentId, "prompt", "generate", RiskAssessmentStatus.PENDING, Map.of(), null, Instant.now(), null);

        var factorScores = new EnumMap<TrustFactor, Double>(TrustFactor.class);
        var reasons = new EnumMap<TrustFactor, String>(TrustFactor.class);
        for (var factor : TrustFactor.values()) {
            factorScores.put(factor, 75.0);
            reasons.put(factor, "reason for " + factor.name());
        }

        var trustResult = new TrustScoreCalculator.TrustResult(75.0, factorScores, reasons);
        when(trustScoreCalculator.calculate(assessment)).thenReturn(trustResult);
        when(trustScoreRepository.save(any(TrustScoreEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(objectMapper.writeValueAsString(any())).thenReturn("{}");

        var result = trustEngine.calculateTrust(assessment);

        assertNotNull(result);
        assertEquals(assessmentId, result.assessmentId());
        assertEquals(75.0, result.overallTrustScore());
        assertEquals(9, result.factorScores().size());
        assertEquals(9, result.factorReasons().size());

        verify(trustScoreCalculator).calculate(assessment);
        verify(trustScoreRepository).save(any(TrustScoreEntity.class));
    }

    @Test
    void calculateTrustShouldIncludeAllTrustFactors() {
        var assessmentId = UUID.randomUUID();
        var assessment = new RiskAssessment(assessmentId, "prompt", "generate", RiskAssessmentStatus.PENDING, Map.of(), null, Instant.now(), null);

        var factorScores = new EnumMap<TrustFactor, Double>(TrustFactor.class);
        var reasons = new EnumMap<TrustFactor, String>(TrustFactor.class);
        for (var factor : TrustFactor.values()) {
            factorScores.put(factor, 85.0);
            reasons.put(factor, "score for " + factor.name());
        }

        var trustResult = new TrustScoreCalculator.TrustResult(85.0, factorScores, reasons);
        when(trustScoreCalculator.calculate(assessment)).thenReturn(trustResult);
        when(trustScoreRepository.save(any(TrustScoreEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(objectMapper.writeValueAsString(any())).thenReturn("{}");

        var result = trustEngine.calculateTrust(assessment);

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
    void recalculateTrustShouldReturnExistingTrustAssessment() {
        var assessmentId = UUID.randomUUID();
        var entity = new TrustScoreEntity();
        entity.setId(UUID.randomUUID());
        entity.setAssessmentId(assessmentId);
        entity.setOverallTrustScore(80.0);
        entity.setFactorScores("{}");
        entity.setFactorReasons("{}");
        entity.setCalculatedAt(LocalDateTime.now());

        when(trustScoreRepository.findByAssessmentId(assessmentId)).thenReturn(List.of(entity));
        when(trustScoreRepository.save(any(TrustScoreEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(objectMapper.readValue(anyString(), any(Class.class))).thenReturn(Map.of());
        when(objectMapper.writeValueAsString(any())).thenReturn("{}");

        var result = trustEngine.recalculateTrust(assessmentId);

        assertNotNull(result);
        assertEquals(80.0, result.overallTrustScore());
        assertEquals(assessmentId, result.assessmentId());
    }

    @Test
    void getTrustScoreShouldReturnScore() {
        var assessmentId = UUID.randomUUID();
        var entity = new TrustScoreEntity();
        entity.setOverallTrustScore(90.0);

        when(trustScoreRepository.findByAssessmentId(assessmentId)).thenReturn(List.of(entity));

        var score = trustEngine.getTrustScore(assessmentId);
        assertEquals(90.0, score);
    }

    @Test
    void getTrustScoreShouldReturnZeroWhenNotFound() {
        when(trustScoreRepository.findByAssessmentId(any())).thenReturn(List.of());
        assertEquals(0.0, trustEngine.getTrustScore(UUID.randomUUID()));
    }
}
