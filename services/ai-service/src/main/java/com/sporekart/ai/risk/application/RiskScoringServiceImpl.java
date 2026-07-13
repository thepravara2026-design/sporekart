package com.sporekart.ai.risk.application;

import com.sporekart.ai.risk.api.RiskScoringService;
import com.sporekart.ai.risk.domain.*;
import com.sporekart.ai.risk.infrastructure.persistence.RiskFactorEntity;
import com.sporekart.ai.risk.infrastructure.persistence.RiskFactorRepository;
import com.sporekart.ai.risk.infrastructure.persistence.RiskScoreEntity;
import com.sporekart.ai.risk.infrastructure.persistence.RiskScoreRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RiskScoringServiceImpl implements RiskScoringService {

    private final RiskScoreRepository riskScoreRepository;
    private final RiskFactorRepository riskFactorRepository;
    private final ObjectMapper objectMapper;

    @Override
    public RiskScore calculateScore(RiskAssessment assessment) {
        List<RiskFactorEntity> factorEntities = riskFactorRepository.findByAssessmentId(assessment.id());

        if (factorEntities.isEmpty()) {
            RiskScore fallback = new RiskScore(
                UUID.randomUUID(), assessment.id(), 0.0, RiskLevel.LOW,
                new HashMap<>(), 0, java.time.Instant.now()
            );
            saveScore(fallback);
            return fallback;
        }

        List<RiskFactor> factors = factorEntities.stream()
            .map(this::toDomain)
            .collect(Collectors.toList());

        double totalWeight = factors.stream().mapToDouble(RiskFactor::weight).sum();
        double weightedSum = factors.stream()
            .mapToDouble(f -> f.weight() * f.score())
            .sum();

        double overallScore = totalWeight > 0 ? Math.min(100, Math.max(0, weightedSum / totalWeight)) : 0.0;

        Map<RiskCategory, Double> categoryScores = new HashMap<>();
        for (RiskFactor factor : factors) {
            categoryScores.merge(factor.category(), factor.score(), Double::sum);
        }
        for (Map.Entry<RiskCategory, Double> entry : categoryScores.entrySet()) {
            long count = factors.stream().filter(f -> f.category() == entry.getKey()).count();
            entry.setValue(entry.getValue() / count);
        }

        RiskLevel riskLevel = determineRiskLevel(overallScore);

        RiskScore riskScore = new RiskScore(
            UUID.randomUUID(), assessment.id(), overallScore, riskLevel,
            categoryScores, factors.size(), java.time.Instant.now()
        );

        saveScore(riskScore);
        log.info("Risk score calculated for assessment {}: {} ({})", assessment.id(), overallScore, riskLevel);
        return riskScore;
    }

    @Override
    public RiskScore getScore(UUID assessmentId) {
        return riskScoreRepository.findByAssessmentId(assessmentId).stream()
            .findFirst()
            .map(this::toDomain)
            .orElseThrow(() -> new RuntimeException("Risk score not found for assessment: " + assessmentId));
    }

    @Override
    public RiskLevel determineRiskLevel(double score) {
        if (score <= 20) return RiskLevel.LOW;
        if (score <= 40) return RiskLevel.MEDIUM;
        if (score <= 70) return RiskLevel.HIGH;
        return RiskLevel.CRITICAL;
    }

    private void saveScore(RiskScore score) {
        var entity = new RiskScoreEntity();
        entity.setId(score.id());
        entity.setAssessmentId(score.assessmentId());
        entity.setOverallScore(score.overallScore());
        entity.setRiskLevel(score.riskLevel().name());
        entity.setCategoryScores(toJson(score.categoryScores()));
        entity.setFactorCount(score.factorCount());
        entity.setCalculatedAt(LocalDateTime.now());
        riskScoreRepository.save(entity);
    }

    private RiskScore toDomain(RiskScoreEntity entity) {
        return new RiskScore(
            entity.getId(), entity.getAssessmentId(), entity.getOverallScore(),
            RiskLevel.valueOf(entity.getRiskLevel()),
            fromJson(entity.getCategoryScores(), new TypeReference<Map<RiskCategory, Double>>() {}),
            entity.getFactorCount(),
            entity.getCalculatedAt() != null ? entity.getCalculatedAt().toInstant(java.time.ZoneOffset.UTC) : null
        );
    }

    private RiskFactor toDomain(RiskFactorEntity entity) {
        return new RiskFactor(
            entity.getId(), entity.getAssessmentId(), entity.getName(),
            RiskCategory.valueOf(entity.getCategory()),
            entity.getWeight(), entity.getScore(),
            fromJson(entity.getEvidence(), new TypeReference<Map<String, Object>>() {})
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
