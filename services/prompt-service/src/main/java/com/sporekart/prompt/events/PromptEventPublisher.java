package com.sporekart.prompt.events;

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
public class PromptEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(PromptEventPublisher.class);
    private static final String TOPIC = "prompt-events";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public PromptEventPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishPromptCreated(UUID promptId, String name, UUID createdBy) {
        publish("prompt.created", promptId.toString(), Map.of(
                "eventType", "PromptCreated",
                "promptId", promptId.toString(),
                "name", name,
                "createdBy", createdBy.toString(),
                "timestamp", OffsetDateTime.now().toString()));
    }

    public void promptUpdated(UUID promptId, UUID updatedBy) {
        publish("prompt.updated", promptId.toString(), Map.of(
                "eventType", "PromptUpdated",
                "promptId", promptId.toString(),
                "updatedBy", updatedBy.toString(),
                "timestamp", OffsetDateTime.now().toString()));
    }

    public void promptPublished(UUID promptId, UUID versionId, UUID performedBy) {
        publish("prompt.published", promptId.toString(), Map.of(
                "eventType", "PromptPublished",
                "promptId", promptId.toString(),
                "versionId", versionId.toString(),
                "performedBy", performedBy.toString(),
                "timestamp", OffsetDateTime.now().toString()));
    }

    public void promptRollback(UUID promptId, Integer targetVersion, UUID performedBy) {
        publish("prompt.rollback", promptId.toString(), Map.of(
                "eventType", "PromptRollback",
                "promptId", promptId.toString(),
                "targetVersion", targetVersion.toString(),
                "performedBy", performedBy.toString(),
                "timestamp", OffsetDateTime.now().toString()));
    }

    public void promptArchived(UUID promptId, UUID performedBy) {
        publish("prompt.archived", promptId.toString(), Map.of(
                "eventType", "PromptArchived",
                "promptId", promptId.toString(),
                "performedBy", performedBy.toString(),
                "timestamp", OffsetDateTime.now().toString()));
    }

    public void promptExecuted(UUID promptId, UUID versionId, UUID userId) {
        publish("prompt.executed", promptId.toString(), Map.of(
                "eventType", "PromptExecuted",
                "promptId", promptId.toString(),
                "versionId", versionId.toString(),
                "userId", userId != null ? userId.toString() : null,
                "timestamp", OffsetDateTime.now().toString()));
    }

    public void promptApproved(UUID promptId, UUID versionId, UUID approver) {
        publish("prompt.approved", promptId.toString(), Map.of(
                "eventType", "PromptApproved",
                "promptId", promptId.toString(),
                "versionId", versionId.toString(),
                "approver", approver.toString(),
                "timestamp", OffsetDateTime.now().toString()));
    }

    private void publish(String eventType, String key, Map<String, String> payload) {
        try {
            var message = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send(TOPIC, key, message);
            log.debug("Published {} event to {}", eventType, TOPIC);
        } catch (Exception e) {
            log.warn("Failed to publish {} event: {}", eventType, e.getMessage());
        }
    }
}
