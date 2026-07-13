package com.sporekart.ai.assistant.infrastructure.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssistantKafkaEventPublisherTest {

    @Mock private KafkaTemplate<String, String> kafkaTemplate;

    private AssistantKafkaEventPublisher publisher;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        publisher = new AssistantKafkaEventPublisher(kafkaTemplate, objectMapper);
    }

    @Test
    void shouldPublishAssistantInvoked() {
        var sessionId = UUID.randomUUID();
        var userId = UUID.randomUUID();
        publisher.publishAssistantInvoked(sessionId, userId);
        verify(kafkaTemplate).send(eq("assistant-events"), eq(sessionId.toString()), anyString());
    }

    @Test
    void shouldPublishIntentResolved() {
        var sessionId = UUID.randomUUID();
        publisher.publishIntentResolved(sessionId, "product_search", 0.95);
        verify(kafkaTemplate).send(eq("assistant-events"), eq(sessionId.toString()), anyString());
    }

    @Test
    void shouldPublishTaskPlanned() {
        var sessionId = UUID.randomUUID();
        publisher.publishTaskPlanned(sessionId, 3);
        verify(kafkaTemplate).send(eq("assistant-events"), eq(sessionId.toString()), anyString());
    }

    @Test
    void shouldPublishTaskStarted() {
        var taskId = UUID.randomUUID();
        publisher.publishTaskStarted(taskId, "QueryProducts");
        verify(kafkaTemplate).send(eq("assistant-events"), eq(taskId.toString()), anyString());
    }

    @Test
    void shouldPublishTaskCompleted() {
        var taskId = UUID.randomUUID();
        publisher.publishTaskCompleted(taskId, true);
        verify(kafkaTemplate).send(eq("assistant-events"), eq(taskId.toString()), anyString());
    }

    @Test
    void shouldPublishTaskCompletedWithFailure() {
        var taskId = UUID.randomUUID();
        publisher.publishTaskCompleted(taskId, false);
        verify(kafkaTemplate).send(eq("assistant-events"), eq(taskId.toString()), anyString());
    }

    @Test
    void shouldPublishTaskFailed() {
        var taskId = UUID.randomUUID();
        publisher.publishTaskFailed(taskId, "Timeout");
        verify(kafkaTemplate).send(eq("assistant-events"), eq(taskId.toString()), anyString());
    }

    @Test
    void shouldPublishResponseGenerated() {
        var sessionId = UUID.randomUUID();
        publisher.publishResponseGenerated(sessionId);
        verify(kafkaTemplate).send(eq("assistant-events"), eq(sessionId.toString()), anyString());
    }

    @Test
    void shouldPublishFeedbackReceived() {
        var sessionId = UUID.randomUUID();
        publisher.publishFeedbackReceived(sessionId, 5);
        verify(kafkaTemplate).send(eq("assistant-events"), eq(sessionId.toString()), anyString());
    }

    @Test
    void shouldPublishRecommendationGenerated() {
        var sessionId = UUID.randomUUID();
        publisher.publishRecommendationGenerated(sessionId);
        verify(kafkaTemplate).send(eq("assistant-events"), eq(sessionId.toString()), anyString());
    }

    @Test
    void shouldPublishWorkflowTriggered() {
        var sessionId = UUID.randomUUID();
        publisher.publishWorkflowTriggered(sessionId, "order_processing");
        verify(kafkaTemplate).send(eq("assistant-events"), eq(sessionId.toString()), anyString());
    }

    @Test
    void shouldHandleKafkaFailureGracefully() {
        var sessionId = UUID.randomUUID();
        doThrow(new RuntimeException("Kafka down"))
                .when(kafkaTemplate).send(anyString(), anyString(), anyString());

        publisher.publishAssistantInvoked(sessionId, UUID.randomUUID());

        verify(kafkaTemplate).send(eq("assistant-events"), eq(sessionId.toString()), anyString());
    }
}
