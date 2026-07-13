package com.sporekart.ai.governance.infrastructure.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class GovernanceKafkaEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String TOPIC = "governance-events";

    public void publishInitialized(Map<String, Object> details) {
        publishEvent("GovernanceInitialized", details);
    }

    public void publishReloaded(Map<String, Object> details) {
        publishEvent("GovernanceReloaded", details);
    }

    public void publishValidated(String requestId, String decision, long processingTimeMs) {
        publishEvent("GovernanceValidated", Map.of(
            "requestId", requestId, "decision", decision,
            "processingTimeMs", processingTimeMs
        ));
    }

    public void publishHealthChanged(String status, Map<String, Object> details) {
        publishEvent("GovernanceHealthChanged", Map.of(
            "status", status, "details", details
        ));
    }

    public void publishConfigurationChanged(String key, String value) {
        publishEvent("GovernanceConfigurationChanged", Map.of(
            "key", key, "value", value
        ));
    }

    public void publishAuditCreated(String auditId, String requestId, String decision) {
        publishEvent("GovernanceAuditCreated", Map.of(
            "auditId", auditId, "requestId", requestId, "decision", decision
        ));
    }

    private void publishEvent(String eventType, Map<String, Object> details) {
        try {
            Map<String, Object> event = Map.of(
                "id", UUID.randomUUID().toString(),
                "type", eventType,
                "timestamp", OffsetDateTime.now().toString(),
                "source", "governance",
                "details", details
            );
            String payload = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(TOPIC, eventType, payload);
            log.debug("Published governance event: {} to topic {}", eventType, TOPIC);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize governance event: {}", eventType, e);
        }
    }
}
