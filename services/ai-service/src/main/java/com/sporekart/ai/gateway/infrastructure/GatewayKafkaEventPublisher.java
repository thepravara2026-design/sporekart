package com.sporekart.ai.gateway.infrastructure;

import com.sporekart.ai.gateway.domain.GatewayExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Profile("!test")
public class GatewayKafkaEventPublisher {
    private static final Logger log = LoggerFactory.getLogger(GatewayKafkaEventPublisher.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public GatewayKafkaEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishRequestReceived(GatewayExecutionContext context) {
        Map<String, Object> event = buildEvent("AIRequestReceived", context);
        kafkaTemplate.send("ai-gateway-events", context.executionId(), event);
        log.debug("Published AIRequestReceived for execution {}", context.executionId());
    }

    public void publishRequestValidated(GatewayExecutionContext context) {
        Map<String, Object> event = buildEvent("AIRequestValidated", context);
        kafkaTemplate.send("ai-gateway-events", context.executionId(), event);
    }

    public void publishRequestRejected(GatewayExecutionContext context, String reason) {
        Map<String, Object> event = buildEvent("AIRequestRejected", context);
        event.put("reason", reason);
        kafkaTemplate.send("ai-gateway-events", context.executionId(), event);
    }

    public void publishExecutionStarted(GatewayExecutionContext context) {
        Map<String, Object> event = buildEvent("AIExecutionStarted", context);
        kafkaTemplate.send("ai-gateway-events", context.executionId(), event);
    }

    public void publishExecutionCompleted(GatewayExecutionContext context, boolean success, long durationMs) {
        Map<String, Object> event = buildEvent(success ? "AIExecutionCompleted" : "AIExecutionFailed", context);
        event.put("durationMs", durationMs);
        event.put("success", success);
        kafkaTemplate.send("ai-gateway-events", context.executionId(), event);
    }

    public void publishGatewayHealthChanged(boolean healthy, String details) {
        Map<String, Object> event = new LinkedHashMap<>();
        event.put("event", "GatewayHealthChanged");
        event.put("healthy", healthy);
        event.put("details", details);
        event.put("timestamp", java.time.OffsetDateTime.now().toString());
        kafkaTemplate.send("ai-gateway-events", "health", event);
    }

    private Map<String, Object> buildEvent(String eventType, GatewayExecutionContext context) {
        Map<String, Object> event = new LinkedHashMap<>();
        event.put("event", eventType);
        event.put("executionId", context.executionId());
        event.put("correlationId", context.correlationId().id());
        event.put("module", context.module());
        event.put("provider", context.provider());
        event.put("userId", context.userId());
        event.put("timestamp", java.time.OffsetDateTime.now().toString());
        return event;
    }
}
