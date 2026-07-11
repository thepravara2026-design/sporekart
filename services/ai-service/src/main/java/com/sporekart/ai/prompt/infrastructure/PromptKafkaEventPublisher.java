package com.sporekart.ai.prompt.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Component
public class PromptKafkaEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(PromptKafkaEventPublisher.class);
    private static final String TOPIC = "ai-prompt-events";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public PromptKafkaEventPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishCreated(UUID templateId, String name, UUID createdBy) {
        publish("PromptCreated", Map.of(
                "templateId", templateId.toString(),
                "name", name,
                "createdBy", createdBy != null ? createdBy.toString() : null,
                "timestamp", OffsetDateTime.now().toString()));
    }

    public void publishUpdated(UUID templateId, String name, UUID updatedBy) {
        publish("PromptUpdated", Map.of(
                "templateId", templateId.toString(),
                "name", name,
                "updatedBy", updatedBy != null ? updatedBy.toString() : null,
                "timestamp", OffsetDateTime.now().toString()));
    }

    public void publishPublished(UUID templateId, int versionNumber, UUID publishedBy) {
        publish("PromptPublished", Map.of(
                "templateId", templateId.toString(),
                "versionNumber", versionNumber,
                "publishedBy", publishedBy != null ? publishedBy.toString() : null,
                "timestamp", OffsetDateTime.now().toString()));
    }

    public void publishDeprecated(UUID templateId, UUID deprecatedBy) {
        publish("PromptDeprecated", Map.of(
                "templateId", templateId.toString(),
                "deprecatedBy", deprecatedBy != null ? deprecatedBy.toString() : null,
                "timestamp", OffsetDateTime.now().toString()));
    }

    public void publishRolledBack(UUID templateId, int fromVersion, int toVersion, UUID rolledBackBy) {
        publish("PromptRolledBack", Map.of(
                "templateId", templateId.toString(),
                "fromVersion", fromVersion,
                "toVersion", toVersion,
                "rolledBackBy", rolledBackBy != null ? rolledBackBy.toString() : null,
                "timestamp", OffsetDateTime.now().toString()));
    }

    public void publishExecutionStarted(UUID templateId, String correlationId) {
        publish("PromptExecutionStarted", Map.of(
                "templateId", templateId.toString(),
                "correlationId", correlationId,
                "timestamp", OffsetDateTime.now().toString()));
    }

    public void publishExecutionCompleted(UUID templateId, String correlationId, long durationMs, boolean success) {
        publish("PromptExecutionCompleted", Map.of(
                "templateId", templateId.toString(),
                "correlationId", correlationId,
                "durationMs", durationMs,
                "success", success,
                "timestamp", OffsetDateTime.now().toString()));
    }

    private void publish(String eventType, Map<String, Object> payload) {
        try {
            String message = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send(TOPIC, eventType, message);
            log.debug("Published {} event to {}", eventType, TOPIC);
        } catch (Exception e) {
            log.warn("Failed to publish {} event: {}", eventType, e.getMessage());
        }
    }
}
