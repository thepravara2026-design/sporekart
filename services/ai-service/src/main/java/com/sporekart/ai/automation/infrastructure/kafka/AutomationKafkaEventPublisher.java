package com.sporekart.ai.automation.infrastructure.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Profile("!test")
@Slf4j
@Component
public class AutomationKafkaEventPublisher {

    private static final String TOPIC = "automation-events";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public AutomationKafkaEventPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishWorkflowStarted(UUID executionId, String workflowName) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "WorkflowStarted");
        payload.put("executionId", executionId.toString());
        payload.put("workflowName", workflowName);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("WorkflowStarted", executionId.toString(), payload);
    }

    public void publishWorkflowCompleted(UUID executionId, String workflowName) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "WorkflowCompleted");
        payload.put("executionId", executionId.toString());
        payload.put("workflowName", workflowName);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("WorkflowCompleted", executionId.toString(), payload);
    }

    public void publishWorkflowFailed(UUID executionId, String workflowName, String error) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "WorkflowFailed");
        payload.put("executionId", executionId.toString());
        payload.put("workflowName", workflowName);
        payload.put("error", error);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("WorkflowFailed", executionId.toString(), payload);
    }

    public void publishJobScheduled(UUID taskId, String taskName) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "JobScheduled");
        payload.put("taskId", taskId.toString());
        payload.put("taskName", taskName);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("JobScheduled", taskId.toString(), payload);
    }

    public void publishJobExecuted(UUID jobId, String jobName, boolean success) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "JobExecuted");
        payload.put("jobId", jobId.toString());
        payload.put("jobName", jobName);
        payload.put("success", success);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("JobExecuted", jobId.toString(), payload);
    }

    public void publishRetryTriggered(UUID entityId, String entityType, int attempt) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "RetryTriggered");
        payload.put("entityId", entityId.toString());
        payload.put("entityType", entityType);
        payload.put("attempt", attempt);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("RetryTriggered", entityId.toString(), payload);
    }

    public void publishEscalationTriggered(UUID escalationId, UUID entityId, String entityType, String reason) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "EscalationTriggered");
        payload.put("escalationId", escalationId.toString());
        payload.put("entityId", entityId.toString());
        payload.put("entityType", entityType);
        payload.put("reason", reason);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("EscalationTriggered", escalationId.toString(), payload);
    }

    public void publishLifecycleTransitionCompleted(UUID entityId, String entityType, String fromState, String toState) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "LifecycleTransitionCompleted");
        payload.put("entityId", entityId.toString());
        payload.put("entityType", entityType);
        payload.put("fromState", fromState);
        payload.put("toState", toState);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("LifecycleTransitionCompleted", entityId.toString(), payload);
    }

    public void publishAutomationAuditRecorded(UUID auditId, String action, String entityType) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "AutomationAuditRecorded");
        payload.put("auditId", auditId.toString());
        payload.put("action", action);
        payload.put("entityType", entityType);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("AutomationAuditRecorded", auditId.toString(), payload);
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
