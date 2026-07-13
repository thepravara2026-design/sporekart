package com.sporekart.ai.decision.infrastructure.kafka;
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
public class DecisionKafkaEventPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private static final String TOPIC = "decision-events";

    public void publishEvaluated(String requestId, String action, String status, long timeMs) {
        publishEvent("DecisionEvaluated", Map.of("requestId", requestId, "action", action, "status", status, "timeMs", timeMs));
    }
    public void publishAllowed(String requestId) { publishEvent("DecisionAllowed", Map.of("requestId", requestId)); }
    public void publishDenied(String requestId) { publishEvent("DecisionDenied", Map.of("requestId", requestId)); }
    public void publishEscalated(String requestId) { publishEvent("DecisionEscalated", Map.of("requestId", requestId)); }
    public void publishExplanationGenerated(String decisionId) { publishEvent("DecisionExplanationGenerated", Map.of("decisionId", decisionId)); }
    public void publishAuditCreated(String auditId) { publishEvent("DecisionAuditCreated", Map.of("auditId", auditId)); }
    public void publishReplayStarted(String requestId) { publishEvent("DecisionReplayStarted", Map.of("requestId", requestId)); }
    public void publishReplayCompleted(String requestId) { publishEvent("DecisionReplayCompleted", Map.of("requestId", requestId)); }

    private void publishEvent(String eventType, Map<String, Object> details) {
        try {
            String payload = objectMapper.writeValueAsString(Map.of(
                "id", UUID.randomUUID().toString(), "type", eventType,
                "timestamp", OffsetDateTime.now().toString(), "source", "decision", "details", details));
            kafkaTemplate.send(TOPIC, eventType, payload);
        } catch (JsonProcessingException e) { log.error("Failed to serialize decision event: {}", eventType, e); }
    }
}
