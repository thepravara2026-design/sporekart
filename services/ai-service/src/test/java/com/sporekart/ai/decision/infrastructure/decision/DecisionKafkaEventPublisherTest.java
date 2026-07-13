package com.sporekart.ai.decision.infrastructure.decision;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.decision.infrastructure.kafka.DecisionKafkaEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

@ExtendWith(MockitoExtension.class)
class DecisionKafkaEventPublisherTest {

    @Mock private KafkaTemplate<String, String> kafkaTemplate;

    private ObjectMapper objectMapper;
    private DecisionKafkaEventPublisher publisher;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        publisher = new DecisionKafkaEventPublisher(kafkaTemplate, objectMapper);
    }

    @Test
    void publishEvaluatedSendsEvent() {
        publisher.publishEvaluated("req1", "ALLOW", "ALLOWED", 100L);
        verify(kafkaTemplate).send(eq("decision-events"), eq("DecisionEvaluated"), anyString());
    }

    @Test
    void publishAllowedSendsEvent() {
        publisher.publishAllowed("req1");
        verify(kafkaTemplate).send(eq("decision-events"), eq("DecisionAllowed"), anyString());
    }

    @Test
    void publishDeniedSendsEvent() {
        publisher.publishDenied("req1");
        verify(kafkaTemplate).send(eq("decision-events"), eq("DecisionDenied"), anyString());
    }

    @Test
    void publishEscalatedSendsEvent() {
        publisher.publishEscalated("req1");
        verify(kafkaTemplate).send(eq("decision-events"), eq("DecisionEscalated"), anyString());
    }

    @Test
    void publishExplanationGeneratedSendsEvent() {
        publisher.publishExplanationGenerated("dec1");
        verify(kafkaTemplate).send(eq("decision-events"), eq("DecisionExplanationGenerated"), anyString());
    }

    @Test
    void publishAuditCreatedSendsEvent() {
        publisher.publishAuditCreated("aud1");
        verify(kafkaTemplate).send(eq("decision-events"), eq("DecisionAuditCreated"), anyString());
    }

    @Test
    void publishReplayStartedSendsEvent() {
        publisher.publishReplayStarted("req1");
        verify(kafkaTemplate).send(eq("decision-events"), eq("DecisionReplayStarted"), anyString());
    }

    @Test
    void publishReplayCompletedSendsEvent() {
        publisher.publishReplayCompleted("req1");
        verify(kafkaTemplate).send(eq("decision-events"), eq("DecisionReplayCompleted"), anyString());
    }
}
