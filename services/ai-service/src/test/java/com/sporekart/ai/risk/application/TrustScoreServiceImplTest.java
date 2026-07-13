package com.sporekart.ai.risk.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import com.sporekart.ai.risk.api.TrustEngine;
import com.sporekart.ai.risk.domain.*;
import com.sporekart.ai.risk.infrastructure.persistence.TrustScoreEntity;
import com.sporekart.ai.risk.infrastructure.persistence.TrustScoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class TrustScoreServiceImplTest {

    @Mock
    private TrustEngine trustEngine;
    @Mock
    private TrustScoreRepository trustScoreRepository;

    private TrustScoreServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new TrustScoreServiceImpl(trustEngine, trustScoreRepository);
    }

    @Test
    void calculateTrustScoreShouldReturnTrustAssessment() {
        var assessmentId = UUID.randomUUID();
        var entity = new TrustScoreEntity();
        entity.setId(UUID.randomUUID());
        entity.setAssessmentId(assessmentId);
        entity.setOverallTrustScore(85.0);
        entity.setFactorScores("{}");
        entity.setFactorReasons("{}");
        entity.setCalculatedAt(LocalDateTime.now());

        when(trustScoreRepository.findByAssessmentId(assessmentId)).thenReturn(List.of(entity));

        var result = service.calculateTrustScore(assessmentId);

        assertNotNull(result);
        assertEquals(assessmentId, result.assessmentId());
        assertEquals(85.0, result.overallTrustScore());
    }

    @Test
    void calculateTrustScoreShouldThrowWhenNotFound() {
        when(trustScoreRepository.findByAssessmentId(any())).thenReturn(List.of());
        assertThrows(RuntimeException.class, () -> service.calculateTrustScore(UUID.randomUUID()));
    }

    @Test
    void getFactorDetailsShouldReturnFactorScores() {
        var assessmentId = UUID.randomUUID();

        var factorScores = new EnumMap<TrustFactor, Double>(TrustFactor.class);
        factorScores.put(TrustFactor.PROVIDER_RELIABILITY, 90.0);
        factorScores.put(TrustFactor.KNOWLEDGE_QUALITY, 80.0);

        var trustAssessment = new TrustAssessment(UUID.randomUUID(), assessmentId, 85.0, factorScores, Map.of(), Instant.now());

        when(trustScoreRepository.findByAssessmentId(assessmentId)).thenReturn(List.of(new TrustScoreEntity()));
        when(trustEngine.calculateTrust(any(RiskAssessment.class))).thenReturn(trustAssessment);

        var details = service.getFactorDetails(assessmentId);

        assertEquals(2, details.size());
        assertEquals(90.0, details.get(TrustFactor.PROVIDER_RELIABILITY));
        assertEquals(80.0, details.get(TrustFactor.KNOWLEDGE_QUALITY));
    }

    @Test
    void getFactorDetailsShouldReturnEmptyMapWhenNoTrustScore() {
        when(trustScoreRepository.findByAssessmentId(any())).thenReturn(List.of());
        var details = service.getFactorDetails(UUID.randomUUID());
        assertTrue(details.isEmpty());
    }

    @Test
    void getFactorScoreShouldReturnValueForValidFactor() {
        var assessmentId = UUID.randomUUID();

        var factorScores = new EnumMap<TrustFactor, Double>(TrustFactor.class);
        factorScores.put(TrustFactor.PROVIDER_RELIABILITY, 95.0);

        var trustAssessment = new TrustAssessment(UUID.randomUUID(), assessmentId, 85.0, factorScores, Map.of(), Instant.now());

        when(trustScoreRepository.findByAssessmentId(assessmentId)).thenReturn(List.of(new TrustScoreEntity()));
        when(trustEngine.calculateTrust(any(RiskAssessment.class))).thenReturn(trustAssessment);

        var score = service.getFactorScore(assessmentId, "PROVIDER_RELIABILITY");

        assertEquals(95.0, score);
    }

    @Test
    void getFactorScoreShouldReturnZeroForInvalidFactor() {
        when(trustScoreRepository.findByAssessmentId(any())).thenReturn(List.of(new TrustScoreEntity()));
        var score = service.getFactorScore(UUID.randomUUID(), "INVALID_FACTOR");
        assertEquals(0.0, score);
    }

    @Test
    void getFactorScoreShouldReturnZeroWhenNoData() {
        when(trustScoreRepository.findByAssessmentId(any())).thenReturn(List.of());
        assertEquals(0.0, service.getFactorScore(UUID.randomUUID(), "PROVIDER_RELIABILITY"));
    }
}
