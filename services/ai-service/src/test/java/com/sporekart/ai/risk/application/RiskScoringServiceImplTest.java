package com.sporekart.ai.risk.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import com.sporekart.ai.risk.domain.*;
import com.sporekart.ai.risk.infrastructure.persistence.RiskFactorEntity;
import com.sporekart.ai.risk.infrastructure.persistence.RiskFactorRepository;
import com.sporekart.ai.risk.infrastructure.persistence.RiskScoreEntity;
import com.sporekart.ai.risk.infrastructure.persistence.RiskScoreRepository;
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
class RiskScoringServiceImplTest {

    @Mock
    private RiskScoreRepository riskScoreRepository;
    @Mock
    private RiskFactorRepository riskFactorRepository;
    @Mock
    private ObjectMapper objectMapper;

    private RiskScoringServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new RiskScoringServiceImpl(riskScoreRepository, riskFactorRepository, objectMapper);
    }

    @Test
    void calculateScoreShouldReturnComputedRiskScore() {
        var assessmentId = UUID.randomUUID();
        var assessment = new RiskAssessment(assessmentId, "prompt", "generate", RiskAssessmentStatus.PENDING, Map.of(), null, Instant.now(), null);

        var factorEntity1 = new RiskFactorEntity();
        factorEntity1.setId(UUID.randomUUID());
        factorEntity1.setAssessmentId(assessmentId);
        factorEntity1.setName("factor1");
        factorEntity1.setCategory(RiskCategory.SECURITY.name());
        factorEntity1.setWeight(0.6);
        factorEntity1.setScore(80.0);
        factorEntity1.setEvidence("{}");

        var factorEntity2 = new RiskFactorEntity();
        factorEntity2.setId(UUID.randomUUID());
        factorEntity2.setAssessmentId(assessmentId);
        factorEntity2.setName("factor2");
        factorEntity2.setCategory(RiskCategory.COMPLIANCE.name());
        factorEntity2.setWeight(0.4);
        factorEntity2.setScore(40.0);
        factorEntity2.setEvidence("{}");

        when(riskFactorRepository.findByAssessmentId(assessmentId)).thenReturn(List.of(factorEntity1, factorEntity2));
        when(riskScoreRepository.save(any(RiskScoreEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(objectMapper.writeValueAsString(any())).thenReturn("{}");
        when(objectMapper.readValue(anyString(), any(Class.class))).thenReturn(Map.of());

        var result = service.calculateScore(assessment);

        assertNotNull(result);
        assertEquals(assessmentId, result.assessmentId());
        assertTrue(result.overallScore() > 0);
        assertEquals(2, result.factorCount());
        assertNotNull(result.riskLevel());

        verify(riskFactorRepository).findByAssessmentId(assessmentId);
        verify(riskScoreRepository).save(any(RiskScoreEntity.class));
    }

    @Test
    void calculateScoreShouldReturnFallbackWhenNoFactors() {
        var assessmentId = UUID.randomUUID();
        var assessment = new RiskAssessment(assessmentId, "prompt", "generate", RiskAssessmentStatus.PENDING, Map.of(), null, Instant.now(), null);

        when(riskFactorRepository.findByAssessmentId(assessmentId)).thenReturn(List.of());
        when(riskScoreRepository.save(any(RiskScoreEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var result = service.calculateScore(assessment);

        assertNotNull(result);
        assertEquals(0.0, result.overallScore());
        assertEquals(RiskLevel.LOW, result.riskLevel());
        assertEquals(0, result.factorCount());
    }

    @Test
    void determineRiskLevelShouldMapCorrectly() {
        assertEquals(RiskLevel.LOW, service.determineRiskLevel(0));
        assertEquals(RiskLevel.LOW, service.determineRiskLevel(20));
        assertEquals(RiskLevel.MEDIUM, service.determineRiskLevel(21));
        assertEquals(RiskLevel.MEDIUM, service.determineRiskLevel(40));
        assertEquals(RiskLevel.HIGH, service.determineRiskLevel(41));
        assertEquals(RiskLevel.HIGH, service.determineRiskLevel(70));
        assertEquals(RiskLevel.CRITICAL, service.determineRiskLevel(71));
        assertEquals(RiskLevel.CRITICAL, service.determineRiskLevel(100));
    }

    @Test
    void determineRiskLevelBoundaries() {
        assertEquals(RiskLevel.LOW, service.determineRiskLevel(0));
        assertEquals(RiskLevel.LOW, service.determineRiskLevel(20));
        assertEquals(RiskLevel.MEDIUM, service.determineRiskLevel(20.01));
        assertEquals(RiskLevel.MEDIUM, service.determineRiskLevel(40));
        assertEquals(RiskLevel.HIGH, service.determineRiskLevel(40.01));
        assertEquals(RiskLevel.HIGH, service.determineRiskLevel(70));
        assertEquals(RiskLevel.CRITICAL, service.determineRiskLevel(70.01));
        assertEquals(RiskLevel.CRITICAL, service.determineRiskLevel(100));
    }

    @Test
    void getScoreShouldReturnScoreWhenFound() {
        var assessmentId = UUID.randomUUID();
        var entity = new RiskScoreEntity();
        entity.setId(UUID.randomUUID());
        entity.setAssessmentId(assessmentId);
        entity.setOverallScore(65.0);
        entity.setRiskLevel(RiskLevel.HIGH.name());
        entity.setCategoryScores("{}");
        entity.setFactorCount(3);
        entity.setCalculatedAt(LocalDateTime.now());

        when(riskScoreRepository.findByAssessmentId(assessmentId)).thenReturn(List.of(entity));
        when(objectMapper.readValue(anyString(), any(Class.class))).thenReturn(Map.of());

        var result = service.getScore(assessmentId);

        assertEquals(assessmentId, result.assessmentId());
        assertEquals(65.0, result.overallScore());
        assertEquals(RiskLevel.HIGH, result.riskLevel());
    }

    @Test
    void getScoreShouldThrowWhenNotFound() {
        when(riskScoreRepository.findByAssessmentId(any())).thenReturn(List.of());
        assertThrows(RuntimeException.class, () -> service.getScore(UUID.randomUUID()));
    }
}
