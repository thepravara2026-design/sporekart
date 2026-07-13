package com.sporekart.ai.risk.application;

import com.sporekart.ai.risk.api.TrustEngine;
import com.sporekart.ai.risk.domain.*;
import com.sporekart.ai.risk.engine.TrustScoreCalculator;
import com.sporekart.ai.risk.infrastructure.persistence.TrustScoreEntity;
import com.sporekart.ai.risk.infrastructure.persistence.TrustScoreRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrustEngineImpl implements TrustEngine {

    private final TrustScoreRepository trustScoreRepository;
    private final TrustScoreCalculator trustScoreCalculator;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public TrustAssessment calculateTrust(RiskAssessment assessment) {
        var result = trustScoreCalculator.calculate(assessment);

        TrustAssessment trustAssessment = new TrustAssessment(
            UUID.randomUUID(), assessment.id(), result.score(),
            result.factorScores(), result.reasons(), java.time.Instant.now()
        );

        saveTrustScore(trustAssessment);
        log.info("Trust calculated for assessment {}: {}", assessment.id(), result.score());
        return trustAssessment;
    }

    @Override
    @Transactional
    public TrustAssessment recalculateTrust(UUID assessmentId) {
        var existing = trustScoreRepository.findByAssessmentId(assessmentId).stream()
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Trust score not found for assessment: " + assessmentId));

        TrustAssessment current = toDomain(existing);
        TrustAssessment recalculated = new TrustAssessment(
            current.id(), current.assessmentId(), current.overallTrustScore(),
            current.factorScores(), current.factorReasons(), java.time.Instant.now()
        );

        saveTrustScore(recalculated);
        log.info("Trust recalculated for assessment {}: {}", assessmentId, recalculated.overallTrustScore());
        return recalculated;
    }

    @Override
    public double getTrustScore(UUID assessmentId) {
        return trustScoreRepository.findByAssessmentId(assessmentId).stream()
            .findFirst()
            .map(TrustScoreEntity::getOverallTrustScore)
            .orElse(0.0);
    }

    private void saveTrustScore(TrustAssessment assessment) {
        var entity = new TrustScoreEntity();
        entity.setId(assessment.id());
        entity.setAssessmentId(assessment.assessmentId());
        entity.setOverallTrustScore(assessment.overallTrustScore());
        entity.setFactorScores(toJson(assessment.factorScores()));
        entity.setFactorReasons(toJson(assessment.factorReasons()));
        entity.setCalculatedAt(LocalDateTime.now());
        trustScoreRepository.save(entity);
    }

    private TrustAssessment toDomain(TrustScoreEntity entity) {
        return new TrustAssessment(
            entity.getId(), entity.getAssessmentId(), entity.getOverallTrustScore(),
            fromJson(entity.getFactorScores(), new TypeReference<Map<TrustFactor, Double>>() {}),
            fromJson(entity.getFactorReasons(), new TypeReference<Map<TrustFactor, String>>() {}),
            entity.getCalculatedAt() != null ? entity.getCalculatedAt().toInstant(java.time.ZoneOffset.UTC) : null
        );
    }

    private String toJson(Object value) {
        try {
            return value == null ? null : objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            throw new RuntimeException("JSON conversion error", e);
        }
    }

    private <T> T fromJson(String json, TypeReference<T> type) {
        try {
            return json == null ? null : objectMapper.readValue(json, type);
        } catch (Exception e) {
            return null;
        }
    }
}
