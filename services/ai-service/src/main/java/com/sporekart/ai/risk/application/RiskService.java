package com.sporekart.ai.risk.application;

import com.sporekart.ai.risk.api.RiskEngine;
import com.sporekart.ai.risk.api.RiskMetricsService;
import com.sporekart.ai.risk.domain.ConfidenceFactor;
import com.sporekart.ai.risk.domain.ConfidenceScore;
import com.sporekart.ai.risk.domain.RiskAssessment;
import com.sporekart.ai.risk.domain.RiskAssessmentStatus;
import com.sporekart.ai.risk.domain.RiskHistory;
import com.sporekart.ai.risk.domain.RiskLevel;
import com.sporekart.ai.risk.domain.RiskThreshold;
import com.sporekart.ai.risk.domain.TrustAssessment;
import com.sporekart.ai.risk.domain.TrustFactor;
import com.sporekart.ai.risk.engine.RiskAssessmentRequest;
import com.sporekart.ai.risk.engine.RiskAssessmentResult;
import com.sporekart.ai.risk.infrastructure.kafka.RiskKafkaEventPublisher;
import com.sporekart.ai.risk.infrastructure.monitoring.RiskMonitoringService;
import com.sporekart.ai.risk.infrastructure.persistence.ConfidenceScoreEntity;
import com.sporekart.ai.risk.infrastructure.persistence.ConfidenceScoreRepository;
import com.sporekart.ai.risk.infrastructure.persistence.RiskHistoryEntity;
import com.sporekart.ai.risk.infrastructure.persistence.RiskHistoryRepository;
import com.sporekart.ai.risk.infrastructure.persistence.TrustScoreEntity;
import com.sporekart.ai.risk.infrastructure.persistence.TrustScoreRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RiskService {

    private final RiskEngine riskEngine;
    private final RiskMetricsService riskMetricsService;
    private final RiskConfigurationServiceImpl riskConfigurationService;
    private final RiskHistoryRepository riskHistoryRepository;
    private final TrustScoreRepository trustScoreRepository;
    private final ConfidenceScoreRepository confidenceScoreRepository;
    private final RiskKafkaEventPublisher riskKafkaEventPublisher;
    private final RiskMonitoringService riskMonitoringService;
    private final ObjectMapper objectMapper;

    @Transactional
    public RiskAssessmentResult assess(String module, String action, Map<String, Object> context) {
        riskMonitoringService.recordRequest();
        long start = System.currentTimeMillis();

        var request = new RiskAssessmentRequest(
            UUID.randomUUID(), module, action,
            context != null ? context : new HashMap<>()
        );

        var result = riskEngine.assess(request);

        long elapsed = System.currentTimeMillis() - start;
        riskMonitoringService.recordAssessment(elapsed);
        riskMonitoringService.recordRiskLevel(result.riskLevel().name());

        riskKafkaEventPublisher.publishAssessmentCompleted(
            result.id().toString(),
            result.riskLevel().name(),
            elapsed
        );

        return result;
    }

    public List<RiskHistory> getHistory() {
        return riskHistoryRepository.findAll().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    public TrustAssessment getTrustByAssessmentId(UUID assessmentId) {
        var scores = trustScoreRepository.findByAssessmentId(assessmentId);
        return scores.isEmpty() ? null : toDomain(scores.getFirst());
    }

    public List<TrustAssessment> getTrustHistory() {
        return trustScoreRepository.findAll().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    public ConfidenceScore getConfidenceByAssessmentId(UUID assessmentId) {
        var scores = confidenceScoreRepository.findByAssessmentId(assessmentId);
        return scores.isEmpty() ? null : toDomain(scores.getFirst());
    }

    public List<ConfidenceScore> getConfidenceHistory() {
        return confidenceScoreRepository.findAll().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Transactional
    public RiskAssessmentResult recalculate(UUID assessmentId) {
        riskMonitoringService.recordRequest();
        long start = System.currentTimeMillis();

        var result = riskEngine.reassess(assessmentId, Map.of());

        long elapsed = System.currentTimeMillis() - start;
        riskMonitoringService.recordRecalculation(elapsed);

        riskKafkaEventPublisher.publishRiskAssessmentCompleted(
            result.id().toString(),
            result.riskLevel().name(),
            elapsed
        );

        return result;
    }

    public Map<String, Object> getStatistics() {
        return riskMetricsService.getStatistics();
    }

    public List<RiskThreshold> getThresholds() {
        return riskConfigurationService.getThresholds();
    }

    public Map<String, Object> checkHealth() {
        return riskMonitoringService.checkHealth();
    }

    private RiskHistory toDomain(RiskHistoryEntity entity) {
        return new RiskHistory(
            entity.getId(),
            entity.getAssessmentId(),
            entity.getEventType(),
            entity.getDescription(),
            entity.getTimestamp() != null ? entity.getTimestamp().toInstant(ZoneOffset.UTC) : null
        );
    }

    private TrustAssessment toDomain(TrustScoreEntity entity) {
        return new TrustAssessment(
            entity.getId(),
            entity.getAssessmentId(),
            entity.getOverallTrustScore(),
            fromJson(entity.getFactorScores(), new TypeReference<Map<TrustFactor, Double>>() {}),
            fromJson(entity.getFactorReasons(), new TypeReference<Map<TrustFactor, String>>() {}),
            entity.getCalculatedAt() != null ? entity.getCalculatedAt().toInstant(ZoneOffset.UTC) : null
        );
    }

    private ConfidenceScore toDomain(ConfidenceScoreEntity entity) {
        return new ConfidenceScore(
            entity.getId(),
            entity.getAssessmentId(),
            entity.getOverallConfidence(),
            fromJson(entity.getFactorScores(), new TypeReference<Map<ConfidenceFactor, Double>>() {}),
            entity.getExplanation(),
            entity.getCalculatedAt() != null ? entity.getCalculatedAt().toInstant(ZoneOffset.UTC) : null
        );
    }

    private <T> T fromJson(String json, TypeReference<T> type) {
        try {
            return json == null ? null : objectMapper.readValue(json, type);
        } catch (Exception e) {
            return null;
        }
    }
}
