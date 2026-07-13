package com.sporekart.ai.assistant.infrastructure.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Profile("!test")
@Component
public class AssistantKafkaEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(AssistantKafkaEventPublisher.class);
    private static final String TOPIC = "assistant-events";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public AssistantKafkaEventPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishAssistantInvoked(UUID sessionId, UUID userId) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "AssistantInvoked");
        payload.put("sessionId", sessionId.toString());
        payload.put("userId", userId.toString());
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("AssistantInvoked", sessionId.toString(), payload);
    }

    public void publishIntentResolved(UUID sessionId, String intent, double confidence) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "IntentResolved");
        payload.put("sessionId", sessionId.toString());
        payload.put("intent", intent);
        payload.put("confidence", confidence);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("IntentResolved", sessionId.toString(), payload);
    }

    public void publishTaskPlanned(UUID sessionId, int taskCount) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "TaskPlanned");
        payload.put("sessionId", sessionId.toString());
        payload.put("taskCount", taskCount);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("TaskPlanned", sessionId.toString(), payload);
    }

    public void publishTaskStarted(UUID taskId, String taskName) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "TaskStarted");
        payload.put("taskId", taskId.toString());
        payload.put("taskName", taskName);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("TaskStarted", taskId.toString(), payload);
    }

    public void publishTaskCompleted(UUID taskId, boolean success) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "TaskCompleted");
        payload.put("taskId", taskId.toString());
        payload.put("success", success);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("TaskCompleted", taskId.toString(), payload);
    }

    public void publishTaskFailed(UUID taskId, String error) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "TaskFailed");
        payload.put("taskId", taskId.toString());
        payload.put("error", error);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("TaskFailed", taskId.toString(), payload);
    }

    public void publishResponseGenerated(UUID sessionId) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "AssistantResponseGenerated");
        payload.put("sessionId", sessionId.toString());
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("AssistantResponseGenerated", sessionId.toString(), payload);
    }

    public void publishFeedbackReceived(UUID sessionId, int rating) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "AssistantFeedbackReceived");
        payload.put("sessionId", sessionId.toString());
        payload.put("rating", rating);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("AssistantFeedbackReceived", sessionId.toString(), payload);
    }

    public void publishRecommendationGenerated(UUID sessionId) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "RecommendationGenerated");
        payload.put("sessionId", sessionId.toString());
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("RecommendationGenerated", sessionId.toString(), payload);
    }

    public void publishWorkflowTriggered(UUID sessionId, String workflow) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "WorkflowTriggered");
        payload.put("sessionId", sessionId.toString());
        payload.put("workflow", workflow);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("WorkflowTriggered", sessionId.toString(), payload);
    }

    private void publish(String eventType, String key, Map<String, Object> payload) {
        try {
            var message = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send(TOPIC, key, message);
            log.debug("Published {} event to {}", eventType, TOPIC);
        } catch (Exception e) {
            log.warn("Failed to publish {} event: {}", eventType, e.getMessage());
        }
    }
}
