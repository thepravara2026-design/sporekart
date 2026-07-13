package com.sporekart.ai.workflow.infrastructure.kafka;

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
public class WorkflowKafkaEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(WorkflowKafkaEventPublisher.class);
    private static final String TOPIC = "workflow-events";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public WorkflowKafkaEventPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishWorkflowCreated(UUID workflowId, String name, UUID createdBy) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "WorkflowCreated");
        payload.put("workflowId", workflowId.toString());
        payload.put("name", name);
        payload.put("createdBy", createdBy.toString());
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("WorkflowCreated", workflowId.toString(), payload);
    }

    public void publishWorkflowPublished(UUID workflowId) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "WorkflowPublished");
        payload.put("workflowId", workflowId.toString());
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("WorkflowPublished", workflowId.toString(), payload);
    }

    public void publishWorkflowDeactivated(UUID workflowId) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "WorkflowDeactivated");
        payload.put("workflowId", workflowId.toString());
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("WorkflowDeactivated", workflowId.toString(), payload);
    }

    public void publishWorkflowExecutionStarted(UUID executionId, UUID workflowId) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "WorkflowExecutionStarted");
        payload.put("executionId", executionId.toString());
        payload.put("workflowId", workflowId.toString());
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("WorkflowExecutionStarted", executionId.toString(), payload);
    }

    public void publishWorkflowExecutionCompleted(UUID executionId, UUID workflowId) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "WorkflowExecutionCompleted");
        payload.put("executionId", executionId.toString());
        payload.put("workflowId", workflowId.toString());
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("WorkflowExecutionCompleted", executionId.toString(), payload);
    }

    public void publishWorkflowExecutionFailed(UUID executionId, UUID workflowId, String error) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "WorkflowExecutionFailed");
        payload.put("executionId", executionId.toString());
        payload.put("workflowId", workflowId.toString());
        payload.put("error", error);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("WorkflowExecutionFailed", executionId.toString(), payload);
    }

    public void publishWorkflowExecutionPaused(UUID executionId, UUID workflowId) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "WorkflowExecutionPaused");
        payload.put("executionId", executionId.toString());
        payload.put("workflowId", workflowId.toString());
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("WorkflowExecutionPaused", executionId.toString(), payload);
    }

    public void publishWorkflowExecutionResumed(UUID executionId, UUID workflowId) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "WorkflowExecutionResumed");
        payload.put("executionId", executionId.toString());
        payload.put("workflowId", workflowId.toString());
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("WorkflowExecutionResumed", executionId.toString(), payload);
    }

    public void publishWorkflowExecutionCancelled(UUID executionId, UUID workflowId) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "WorkflowExecutionCancelled");
        payload.put("executionId", executionId.toString());
        payload.put("workflowId", workflowId.toString());
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("WorkflowExecutionCancelled", executionId.toString(), payload);
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
