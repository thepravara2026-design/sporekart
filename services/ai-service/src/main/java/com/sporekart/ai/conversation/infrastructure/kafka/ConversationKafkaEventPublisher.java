package com.sporekart.ai.conversation.infrastructure.kafka;

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
public class ConversationKafkaEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(ConversationKafkaEventPublisher.class);
    private static final String TOPIC = "conversation-events";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public ConversationKafkaEventPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishSessionCreated(UUID sessionId, String userId) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "SessionCreated");
        payload.put("sessionId", sessionId.toString());
        payload.put("userId", userId);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("SessionCreated", sessionId.toString(), payload);
    }

    public void publishSessionClosed(UUID sessionId, String userId) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "SessionClosed");
        payload.put("sessionId", sessionId.toString());
        payload.put("userId", userId);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("SessionClosed", sessionId.toString(), payload);
    }

    public void publishMessageSent(UUID messageId, UUID sessionId, String role) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "MessageSent");
        payload.put("messageId", messageId.toString());
        payload.put("sessionId", sessionId.toString());
        payload.put("role", role);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("MessageSent", messageId.toString(), payload);
    }

    public void publishMemoryStored(UUID memoryId, UUID sessionId, String memoryType) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "MemoryStored");
        payload.put("memoryId", memoryId.toString());
        payload.put("sessionId", sessionId.toString());
        payload.put("memoryType", memoryType);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("MemoryStored", memoryId.toString(), payload);
    }

    public void publishContextRefreshed(UUID sessionId) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "ContextRefreshed");
        payload.put("sessionId", sessionId.toString());
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("ContextRefreshed", sessionId.toString(), payload);
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
