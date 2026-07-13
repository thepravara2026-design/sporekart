package com.sporekart.ai.policy.infrastructure.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class PolicyKafkaEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String TOPIC = "policy-events";

    public void publishCreated(String policyId, String name) {
        publishEvent("PolicyCreated", Map.of("policyId", policyId, "name", name));
    }

    public void publishUpdated(String policyId, String name) {
        publishEvent("PolicyUpdated", Map.of("policyId", policyId, "name", name));
    }

    public void publishDeleted(String policyId) {
        publishEvent("PolicyDeleted", Map.of("policyId", policyId));
    }

    public void publishActivated(String policyId) {
        publishEvent("PolicyActivated", Map.of("policyId", policyId));
    }

    public void publishDeactivated(String policyId) {
        publishEvent("PolicyDeactivated", Map.of("policyId", policyId));
    }

    public void publishEvaluated(String requestId, String decision, long timeMs) {
        publishEvent("PolicyEvaluated", Map.of(
            "requestId", requestId, "decision", decision, "evaluationTimeMs", timeMs
        ));
    }

    public void publishViolationDetected(String requestId, String ruleName, String severity) {
        publishEvent("PolicyViolationDetected", Map.of(
            "requestId", requestId, "ruleName", ruleName, "severity", severity
        ));
    }

    public void publishEvaluationFailed(String requestId, String error) {
        publishEvent("PolicyEvaluationFailed", Map.of(
            "requestId", requestId, "error", error
        ));
    }

    private void publishEvent(String eventType, Map<String, Object> details) {
        try {
            Map<String, Object> event = Map.of(
                "id", UUID.randomUUID().toString(),
                "type", eventType,
                "timestamp", OffsetDateTime.now().toString(),
                "source", "policy",
                "details", details
            );
            String payload = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(TOPIC, eventType, payload);
            log.debug("Published policy event: {} to topic {}", eventType, TOPIC);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize policy event: {}", eventType, e);
        }
    }
}
