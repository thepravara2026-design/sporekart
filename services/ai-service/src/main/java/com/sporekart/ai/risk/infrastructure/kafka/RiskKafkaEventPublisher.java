package com.sporekart.ai.risk.infrastructure.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class RiskKafkaEventPublisher {

    private static final String TOPIC = "risk-events";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishRiskAssessmentStarted(String assessmentId, String module, String action) {
        kafkaTemplate.send(TOPIC, "RISK_ASSESSMENT_STARTED",
            Map.of("assessmentId", assessmentId, "module", module, "action", action));
        log.info("Published RISK_ASSESSMENT_STARTED for assessment: {}", assessmentId);
    }

    public void publishRiskAssessmentCompleted(String assessmentId, String riskLevel, long latencyMs) {
        kafkaTemplate.send(TOPIC, "RISK_ASSESSMENT_COMPLETED",
            Map.of("assessmentId", assessmentId, "riskLevel", riskLevel, "latencyMs", latencyMs));
        log.info("Published RISK_ASSESSMENT_COMPLETED for assessment: {}", assessmentId);
    }

    public void publishRiskLevelChanged(String assessmentId, String previousLevel, String newLevel) {
        kafkaTemplate.send(TOPIC, "RISK_LEVEL_CHANGED",
            Map.of("assessmentId", assessmentId, "previousLevel", previousLevel, "newLevel", newLevel));
        log.info("Published RISK_LEVEL_CHANGED for assessment: {} ({} -> {})", assessmentId, previousLevel, newLevel);
    }

    public void publishTrustScoreCalculated(String assessmentId, double trustScore) {
        kafkaTemplate.send(TOPIC, "TRUST_SCORE_CALCULATED",
            Map.of("assessmentId", assessmentId, "trustScore", trustScore));
        log.info("Published TRUST_SCORE_CALCULATED for assessment: {} score={}", assessmentId, trustScore);
    }

    public void publishConfidenceCalculated(String assessmentId, double confidenceScore) {
        kafkaTemplate.send(TOPIC, "CONFIDENCE_CALCULATED",
            Map.of("assessmentId", assessmentId, "confidenceScore", confidenceScore));
        log.info("Published CONFIDENCE_CALCULATED for assessment: {} score={}", assessmentId, confidenceScore);
    }

    public void publishRecommendationGenerated(String assessmentId, String recommendationType, String riskLevel) {
        kafkaTemplate.send(TOPIC, "RECOMMENDATION_GENERATED",
            Map.of("assessmentId", assessmentId, "recommendationType", recommendationType, "riskLevel", riskLevel));
        log.info("Published RECOMMENDATION_GENERATED for assessment: {} type={}", assessmentId, recommendationType);
    }

    public void publishRiskAuditRecorded(String action, String entityType, String entityId) {
        kafkaTemplate.send(TOPIC, "RISK_AUDIT_RECORDED",
            Map.of("action", action, "entityType", entityType, "entityId", entityId));
        log.info("Published RISK_AUDIT_RECORDED: {} on {} {}", action, entityType, entityId);
    }
}
