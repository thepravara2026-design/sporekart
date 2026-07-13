package com.sporekart.ai.risk.infrastructure.kafka;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

@ExtendWith(MockitoExtension.class)
class RiskKafkaEventPublisherTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    private RiskKafkaEventPublisher publisher;

    @BeforeEach
    void setUp() {
        publisher = new RiskKafkaEventPublisher(kafkaTemplate);
    }

    @Test
    void publishRiskAssessmentCompletedShouldSendToRiskEventsTopic() {
        var assessmentId = "assessment-123";
        var riskLevel = "HIGH";
        var latencyMs = 150L;

        publisher.publishRiskAssessmentCompleted(assessmentId, riskLevel, latencyMs);

        verify(kafkaTemplate).send(eq("risk-events"), eq("RISK_ASSESSMENT_COMPLETED"), anyMap());
    }

    @Test
    void publishRiskAssessmentStartedShouldSendToRiskEventsTopic() {
        publisher.publishRiskAssessmentStarted("a1", "prompt", "generate");

        verify(kafkaTemplate).send(eq("risk-events"), eq("RISK_ASSESSMENT_STARTED"), anyMap());
    }

    @Test
    void publishRiskLevelChangedShouldSendToRiskEventsTopic() {
        publisher.publishRiskLevelChanged("a1", "LOW", "HIGH");

        verify(kafkaTemplate).send(eq("risk-events"), eq("RISK_LEVEL_CHANGED"), anyMap());
    }

    @Test
    void publishTrustScoreCalculatedShouldSendToRiskEventsTopic() {
        publisher.publishTrustScoreCalculated("a1", 85.0);

        verify(kafkaTemplate).send(eq("risk-events"), eq("TRUST_SCORE_CALCULATED"), anyMap());
    }

    @Test
    void publishConfidenceCalculatedShouldSendToRiskEventsTopic() {
        publisher.publishConfidenceCalculated("a1", 75.0);

        verify(kafkaTemplate).send(eq("risk-events"), eq("CONFIDENCE_CALCULATED"), anyMap());
    }

    @Test
    void publishRecommendationGeneratedShouldSendToRiskEventsTopic() {
        publisher.publishRecommendationGenerated("a1", "PROCEED", "LOW");

        verify(kafkaTemplate).send(eq("risk-events"), eq("RECOMMENDATION_GENERATED"), anyMap());
    }

    @Test
    void publishRiskAuditRecordedShouldSendToRiskEventsTopic() {
        publisher.publishRiskAuditRecorded("CREATE", "ASSESSMENT", "e1");

        verify(kafkaTemplate).send(eq("risk-events"), eq("RISK_AUDIT_RECORDED"), anyMap());
    }
}
