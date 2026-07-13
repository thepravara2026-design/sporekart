package com.sporekart.ai.risk.application;

import com.sporekart.ai.risk.api.ConfidenceCalculator;
import com.sporekart.ai.risk.domain.*;
import com.sporekart.ai.risk.engine.ConfidenceCalculatorEngine;
import com.sporekart.ai.risk.infrastructure.persistence.ConfidenceScoreEntity;
import com.sporekart.ai.risk.infrastructure.persistence.ConfidenceScoreRepository;
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
public class ConfidenceCalculatorImpl implements ConfidenceCalculator {

    private final ConfidenceScoreRepository confidenceScoreRepository;
    private final ConfidenceCalculatorEngine confidenceCalculatorEngine;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public ConfidenceScore calculateConfidence(RiskAssessment assessment) {
        var result = confidenceCalculatorEngine.calculate(assessment);

        ConfidenceScore confidenceScore = new ConfidenceScore(
            UUID.randomUUID(), assessment.id(), result.score(),
            result.factorScores(), result.explanation(), java.time.Instant.now()
        );

        saveConfidenceScore(confidenceScore);
        log.info("Confidence calculated for assessment {}: {}", assessment.id(), result.score());
        return confidenceScore;
    }

    @Override
    @Transactional
    public ConfidenceScore recalculateConfidence(UUID assessmentId) {
        var entities = confidenceScoreRepository.findByAssessmentId(assessmentId);
        if (entities.isEmpty()) {
            throw new RuntimeException("Confidence score not found for assessment: " + assessmentId);
        }
        var existing = entities.get(0);
        ConfidenceScore current = toDomain(existing);
        ConfidenceScore recalculated = new ConfidenceScore(
            current.id(), current.assessmentId(), current.overallConfidence(),
            current.factorScores(), current.explanation(), java.time.Instant.now()
        );

        saveConfidenceScore(recalculated);
        log.info("Confidence recalculated for assessment {}: {}", assessmentId, recalculated.overallConfidence());
        return recalculated;
    }

    @Override
    public double getConfidenceScore(UUID assessmentId) {
        return confidenceScoreRepository.findByAssessmentId(assessmentId).stream()
            .findFirst()
            .map(ConfidenceScoreEntity::getOverallConfidence)
            .orElse(0.0);
    }

    private void saveConfidenceScore(ConfidenceScore score) {
        var entity = new ConfidenceScoreEntity();
        entity.setId(score.id());
        entity.setAssessmentId(score.assessmentId());
        entity.setOverallConfidence(score.overallConfidence());
        entity.setFactorScores(toJson(score.factorScores()));
        entity.setExplanation(score.explanation());
        entity.setCalculatedAt(LocalDateTime.now());
        confidenceScoreRepository.save(entity);
    }

    private ConfidenceScore toDomain(ConfidenceScoreEntity entity) {
        return new ConfidenceScore(
            entity.getId(), entity.getAssessmentId(), entity.getOverallConfidence(),
            fromJson(entity.getFactorScores(), new TypeReference<Map<ConfidenceFactor, Double>>() {}),
            entity.getExplanation(),
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
