package com.sporekart.ai.risk.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import com.sporekart.ai.risk.domain.*;
import com.sporekart.ai.risk.infrastructure.persistence.RiskRecommendationEntity;
import com.sporekart.ai.risk.infrastructure.persistence.RiskRecommendationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Instant;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class RiskRecommendationServiceImplTest {

    @Mock
    private RiskRecommendationRepository riskRecommendationRepository;
    @Mock
    private ObjectMapper objectMapper;

    private RiskRecommendationServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new RiskRecommendationServiceImpl(riskRecommendationRepository, objectMapper);
    }

    @Test
    void generateRecommendationLowRiskHighTrustShouldReturnProceed() {
        var assessment = assessment();
        var score = new RiskScore(UUID.randomUUID(), assessment.id(), 10.0, RiskLevel.LOW, Map.of(), 1, Instant.now());
        var trust = new TrustAssessment(UUID.randomUUID(), assessment.id(), 80.0, Map.of(), Map.of(), Instant.now());
        var confidence = new ConfidenceScore(UUID.randomUUID(), assessment.id(), 85.0, Map.of(), "", Instant.now());

        when(riskRecommendationRepository.save(any(RiskRecommendationEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(objectMapper.writeValueAsString(any())).thenReturn("{}");

        var result = service.generateRecommendation(assessment, score, trust, confidence);

        assertEquals(RecommendationType.PROCEED, result.type());
    }

    @Test
    void generateRecommendationLowRiskLowTrustShouldReturnRetry() {
        var assessment = assessment();
        var score = new RiskScore(UUID.randomUUID(), assessment.id(), 15.0, RiskLevel.LOW, Map.of(), 1, Instant.now());
        var trust = new TrustAssessment(UUID.randomUUID(), assessment.id(), 30.0, Map.of(), Map.of(), Instant.now());
        var confidence = new ConfidenceScore(UUID.randomUUID(), assessment.id(), 40.0, Map.of(), "", Instant.now());

        when(riskRecommendationRepository.save(any(RiskRecommendationEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(objectMapper.writeValueAsString(any())).thenReturn("{}");

        var result = service.generateRecommendation(assessment, score, trust, confidence);

        assertEquals(RecommendationType.RETRY, result.type());
    }

    @Test
    void generateRecommendationMediumRiskHighTrustShouldReturnProceed() {
        var assessment = assessment();
        var score = new RiskScore(UUID.randomUUID(), assessment.id(), 30.0, RiskLevel.MEDIUM, Map.of(), 1, Instant.now());
        var trust = new TrustAssessment(UUID.randomUUID(), assessment.id(), 80.0, Map.of(), Map.of(), Instant.now());
        var confidence = new ConfidenceScore(UUID.randomUUID(), assessment.id(), 80.0, Map.of(), "", Instant.now());

        when(riskRecommendationRepository.save(any(RiskRecommendationEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(objectMapper.writeValueAsString(any())).thenReturn("{}");

        var result = service.generateRecommendation(assessment, score, trust, confidence);

        assertEquals(RecommendationType.PROCEED, result.type());
    }

    @Test
    void generateRecommendationMediumRiskModerateTrustShouldReturnRetry() {
        var assessment = assessment();
        var score = new RiskScore(UUID.randomUUID(), assessment.id(), 35.0, RiskLevel.MEDIUM, Map.of(), 1, Instant.now());
        var trust = new TrustAssessment(UUID.randomUUID(), assessment.id(), 60.0, Map.of(), Map.of(), Instant.now());
        var confidence = new ConfidenceScore(UUID.randomUUID(), assessment.id(), 50.0, Map.of(), "", Instant.now());

        when(riskRecommendationRepository.save(any(RiskRecommendationEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(objectMapper.writeValueAsString(any())).thenReturn("{}");

        var result = service.generateRecommendation(assessment, score, trust, confidence);

        assertEquals(RecommendationType.RETRY, result.type());
    }

    @Test
    void generateRecommendationMediumRiskLowTrustShouldReturnUseAlternateProvider() {
        var assessment = assessment();
        var score = new RiskScore(UUID.randomUUID(), assessment.id(), 40.0, RiskLevel.MEDIUM, Map.of(), 1, Instant.now());
        var trust = new TrustAssessment(UUID.randomUUID(), assessment.id(), 30.0, Map.of(), Map.of(), Instant.now());
        var confidence = new ConfidenceScore(UUID.randomUUID(), assessment.id(), 30.0, Map.of(), "", Instant.now());

        when(riskRecommendationRepository.save(any(RiskRecommendationEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(objectMapper.writeValueAsString(any())).thenReturn("{}");

        var result = service.generateRecommendation(assessment, score, trust, confidence);

        assertEquals(RecommendationType.USE_ALTERNATE_PROVIDER, result.type());
    }

    @Test
    void generateRecommendationHighRiskHighTrustShouldReturnRequireApproval() {
        var assessment = assessment();
        var score = new RiskScore(UUID.randomUUID(), assessment.id(), 60.0, RiskLevel.HIGH, Map.of(), 1, Instant.now());
        var trust = new TrustAssessment(UUID.randomUUID(), assessment.id(), 85.0, Map.of(), Map.of(), Instant.now());
        var confidence = new ConfidenceScore(UUID.randomUUID(), assessment.id(), 90.0, Map.of(), "", Instant.now());

        when(riskRecommendationRepository.save(any(RiskRecommendationEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(objectMapper.writeValueAsString(any())).thenReturn("{}");

        var result = service.generateRecommendation(assessment, score, trust, confidence);

        assertEquals(RecommendationType.REQUIRE_APPROVAL, result.type());
    }

    @Test
    void generateRecommendationHighRiskModerateTrustShouldReturnReduceContext() {
        var assessment = assessment();
        var score = new RiskScore(UUID.randomUUID(), assessment.id(), 65.0, RiskLevel.HIGH, Map.of(), 1, Instant.now());
        var trust = new TrustAssessment(UUID.randomUUID(), assessment.id(), 60.0, Map.of(), Map.of(), Instant.now());
        var confidence = new ConfidenceScore(UUID.randomUUID(), assessment.id(), 50.0, Map.of(), "", Instant.now());

        when(riskRecommendationRepository.save(any(RiskRecommendationEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(objectMapper.writeValueAsString(any())).thenReturn("{}");

        var result = service.generateRecommendation(assessment, score, trust, confidence);

        assertEquals(RecommendationType.REDUCE_CONTEXT, result.type());
    }

    @Test
    void generateRecommendationHighRiskLowTrustShouldReturnRequestHumanReview() {
        var assessment = assessment();
        var score = new RiskScore(UUID.randomUUID(), assessment.id(), 70.0, RiskLevel.HIGH, Map.of(), 1, Instant.now());
        var trust = new TrustAssessment(UUID.randomUUID(), assessment.id(), 30.0, Map.of(), Map.of(), Instant.now());
        var confidence = new ConfidenceScore(UUID.randomUUID(), assessment.id(), 30.0, Map.of(), "", Instant.now());

        when(riskRecommendationRepository.save(any(RiskRecommendationEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(objectMapper.writeValueAsString(any())).thenReturn("{}");

        var result = service.generateRecommendation(assessment, score, trust, confidence);

        assertEquals(RecommendationType.REQUEST_HUMAN_REVIEW, result.type());
    }

    @Test
    void generateRecommendationCriticalRiskShouldReturnBlockExecution() {
        var assessment = assessment();
        var score = new RiskScore(UUID.randomUUID(), assessment.id(), 90.0, RiskLevel.CRITICAL, Map.of(), 1, Instant.now());
        var trust = new TrustAssessment(UUID.randomUUID(), assessment.id(), 90.0, Map.of(), Map.of(), Instant.now());
        var confidence = new ConfidenceScore(UUID.randomUUID(), assessment.id(), 90.0, Map.of(), "", Instant.now());

        when(riskRecommendationRepository.save(any(RiskRecommendationEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(objectMapper.writeValueAsString(any())).thenReturn("{}");

        var result = service.generateRecommendation(assessment, score, trust, confidence);

        assertEquals(RecommendationType.BLOCK_EXECUTION, result.type());
        assertEquals(5, result.priority());
    }

    @Test
    void getRecommendationsShouldReturnList() {
        var assessmentId = UUID.randomUUID();
        var entity = new RiskRecommendationEntity();
        entity.setId(UUID.randomUUID());
        entity.setAssessmentId(assessmentId);
        entity.setType(RecommendationType.PROCEED.name());
        entity.setTitle("title");
        entity.setDescription("desc");
        entity.setDetails("{}");
        entity.setPriority(1);

        when(riskRecommendationRepository.findByAssessmentId(assessmentId)).thenReturn(List.of(entity));
        when(objectMapper.readValue(anyString(), any(Class.class))).thenReturn(Map.of());

        var results = service.getRecommendations(assessmentId);

        assertEquals(1, results.size());
        assertEquals(RecommendationType.PROCEED, results.get(0).type());
    }

    @Test
    void getRecommendedActionShouldReturnAction() {
        var assessmentId = UUID.randomUUID();
        var entity = new RiskRecommendationEntity();
        entity.setType(RecommendationType.BLOCK_EXECUTION.name());

        when(riskRecommendationRepository.findByAssessmentId(assessmentId)).thenReturn(List.of(entity));

        assertEquals(RecommendationType.BLOCK_EXECUTION, service.getRecommendedAction(assessmentId));
    }

    @Test
    void getRecommendedActionShouldReturnProceedWhenNoRecommendation() {
        when(riskRecommendationRepository.findByAssessmentId(any())).thenReturn(List.of());
        assertEquals(RecommendationType.PROCEED, service.getRecommendedAction(UUID.randomUUID()));
    }

    private RiskAssessment assessment() {
        return new RiskAssessment(UUID.randomUUID(), "prompt", "generate", RiskAssessmentStatus.PENDING, Map.of(), null, Instant.now(), null);
    }
}
