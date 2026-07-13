package com.sporekart.ai.admin.infrastructure.kafka;

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
public class AdminKafkaEventPublisher {

    private static final String TOPIC = "admin-events";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public AdminKafkaEventPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishConfigurationUpdated(UUID configId, String key, String module, String environment) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "ConfigurationUpdated");
        payload.put("configId", configId.toString());
        payload.put("key", key);
        payload.put("module", module);
        payload.put("environment", environment);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("ConfigurationUpdated", configId.toString(), payload);
    }

    public void publishConfigurationRolledBack(UUID configId, int targetVersion, int newVersion) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "ConfigurationRolledBack");
        payload.put("configId", configId.toString());
        payload.put("targetVersion", targetVersion);
        payload.put("newVersion", newVersion);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("ConfigurationRolledBack", configId.toString(), payload);
    }

    public void publishFeatureFlagChanged(UUID flagId, String key, boolean enabled) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "FeatureFlagChanged");
        payload.put("flagId", flagId.toString());
        payload.put("key", key);
        payload.put("enabled", enabled);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("FeatureFlagChanged", flagId.toString(), payload);
    }

    public void publishEnvironmentProfileUpdated(UUID envId, String name) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "EnvironmentProfileUpdated");
        payload.put("envId", envId.toString());
        payload.put("name", name);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("EnvironmentProfileUpdated", envId.toString(), payload);
    }

    public void publishAdministrationOperationCompleted(UUID operationId, String operationType) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "AdministrationOperationCompleted");
        payload.put("operationId", operationId.toString());
        payload.put("operationType", operationType);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("AdministrationOperationCompleted", operationId.toString(), payload);
    }

    public void publishAdministrationAuditCreated(UUID auditId, String action, String entityType) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "AdministrationAuditCreated");
        payload.put("auditId", auditId.toString());
        payload.put("action", action);
        payload.put("entityType", entityType);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("AdministrationAuditCreated", auditId.toString(), payload);
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
