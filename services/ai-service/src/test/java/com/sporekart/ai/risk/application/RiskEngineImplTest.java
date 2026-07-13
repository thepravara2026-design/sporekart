package com.sporekart.ai.risk.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import com.sporekart.ai.risk.api.*;
import com.sporekart.ai.risk.domain.*;
import com.sporekart.ai.risk.engine.RiskAssessmentRequest;
import com.sporekart.ai.risk.engine.RiskAssessmentResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Instant;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class RiskEngineImplTest {

    @Mock
    private RiskAssessmentService riskAssessmentService;
    @Mock
    private RiskScoringService riskScoringService;
    @Mock
    private RiskClassificationService riskClassificationService;
    @Mock
    private TrustEngine trustEngine;
    @Mock
    private ConfidenceCalculator confidenceCalculator;
    @Mock
    private RiskRecommendationService riskRecommendationService;
    @Mock
    private RiskAuditService riskAuditService;
    @Mock
    private RiskMetricsService riskMetricsService;

    private RiskEngineImpl riskEngine;

    @BeforeEach
    void setUp() {
        riskEngine = new RiskEngineImpl(
            riskAssessmentService, riskScoringService, riskClassificationService,
            trustEngine, confidenceCalculator, riskRecommendationService,
            riskAuditService, riskMetricsService
        );
    }

    @Test
    void assessShouldRunFullPipelineAndReturnResult() {
        var assessmentId = UUID.randomUUID();
        var request = new RiskAssessmentRequest(assessmentId, "prompt", "generate", Map.of("key", "value"));
        var assessment = new RiskAssessment(assessmentId, "prompt", "generate", RiskAssessmentStatus.PENDING, Map.of("key", "value"), null, Instant.now(), null);
        var riskScore = new RiskScore(UUID.randomUUID(), assessmentId, 25.0, RiskLevel.MEDIUM, Map.of(RiskCategory.SECURITY, 25.0), 2, Instant.now());
        var trustAssessment = new TrustAssessment(UUID.randomUUID(), assessmentId, 80.0, Map.of(TrustFactor.PROVIDER_RELIABILITY, 80.0), Map.of(TrustFactor.PROVIDER_RELIABILITY, "ok"), Instant.now());
        var confidenceScore = new ConfidenceScore(UUID.randomUUID(), assessmentId, 75.0, Map.of(ConfidenceFactor.KNOWLEDGE_MATCH, 75.0), "explanation", Instant.now());
        var recommendation = new RiskRecommendation(UUID.randomUUID(), assessmentId, RecommendationType.PROCEED, "title", "desc", Map.of(), 1, Instant.now());

        when(riskAssessmentService.createAssessment(anyString(), anyString(), anyMap())).thenReturn(assessment);
        when(riskScoringService.calculateScore(assessment)).thenReturn(riskScore);
        when(riskClassificationService.classify(assessment)).thenReturn(RiskCategory.SECURITY);
        when(trustEngine.calculateTrust(assessment)).thenReturn(trustAssessment);
        when(confidenceCalculator.calculateConfidence(assessment)).thenReturn(confidenceScore);
        when(riskRecommendationService.generateRecommendation(assessment, riskScore, trustAssessment, confidenceScore)).thenReturn(recommendation);
        doNothing().when(riskAssessmentService).completeAssessment(assessmentId);
        doNothing().when(riskAuditService).recordAudit(anyString(), anyString(), any(), isNull(), anyMap(), isNull());

        var result = riskEngine.assess(request);

        assertNotNull(result);
        assertEquals(assessmentId, result.id());
        assertTrue(result.proceed());
        assertEquals(RiskLevel.MEDIUM, result.riskLevel());
        assertEquals(25.0, result.overallScore());
        assertEquals(80.0, result.trustScore());
        assertEquals(75.0, result.confidenceScore());
        assertEquals(recommendation, result.recommendation());
        assertNotNull(result.details());
        assertEquals(RiskCategory.SECURITY.name(), result.details().get("primaryCategory"));

        verify(riskAssessmentService).createAssessment(request.module(), request.action(), request.context());
        verify(riskScoringService).calculateScore(assessment);
        verify(riskClassificationService).classify(assessment);
        verify(trustEngine).calculateTrust(assessment);
        verify(confidenceCalculator).calculateConfidence(assessment);
        verify(riskRecommendationService).generateRecommendation(assessment, riskScore, trustAssessment, confidenceScore);
        verify(riskAssessmentService).completeAssessment(assessmentId);
        verify(riskAuditService).recordAudit(eq("RISK_ASSESSMENT"), eq("ASSESSMENT"), eq(assessmentId), isNull(), anyMap(), isNull());
        verify(riskMetricsService).recordAssessment(RiskLevel.MEDIUM);
        verify(riskMetricsService).recordRecommendation(RecommendationType.PROCEED);
        verify(riskMetricsService).recordTrustScore(80.0);
        verify(riskMetricsService).recordConfidence(75.0);
        verify(riskMetricsService).recordLatency(anyLong());
    }

    @Test
    void assessShouldNotProceedOnHighRisk() {
        var assessmentId = UUID.randomUUID();
        var request = new RiskAssessmentRequest(assessmentId, "prompt", "generate", Map.of());
        var assessment = new RiskAssessment(assessmentId, "prompt", "generate", RiskAssessmentStatus.PENDING, Map.of(), null, Instant.now(), null);
        var riskScore = new RiskScore(UUID.randomUUID(), assessmentId, 85.0, RiskLevel.HIGH, Map.of(), 1, Instant.now());
        var trustAssessment = new TrustAssessment(UUID.randomUUID(), assessmentId, 50.0, Map.of(), Map.of(), Instant.now());
        var confidenceScore = new ConfidenceScore(UUID.randomUUID(), assessmentId, 40.0, Map.of(), "low", Instant.now());
        var recommendation = new RiskRecommendation(UUID.randomUUID(), assessmentId, RecommendationType.REQUIRE_APPROVAL, "title", "desc", Map.of(), 4, Instant.now());

        when(riskAssessmentService.createAssessment(anyString(), anyString(), anyMap())).thenReturn(assessment);
        when(riskScoringService.calculateScore(assessment)).thenReturn(riskScore);
        when(riskClassificationService.classify(assessment)).thenReturn(RiskCategory.TECHNICAL);
        when(trustEngine.calculateTrust(assessment)).thenReturn(trustAssessment);
        when(confidenceCalculator.calculateConfidence(assessment)).thenReturn(confidenceScore);
        when(riskRecommendationService.generateRecommendation(assessment, riskScore, trustAssessment, confidenceScore)).thenReturn(recommendation);
        doNothing().when(riskAssessmentService).completeAssessment(assessmentId);
        doNothing().when(riskAuditService).recordAudit(anyString(), anyString(), any(), isNull(), anyMap(), isNull());

        var result = riskEngine.assess(request);

        assertFalse(result.proceed());
        assertEquals(RiskLevel.HIGH, result.riskLevel());
    }
}
