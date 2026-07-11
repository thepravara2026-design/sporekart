package com.sporekart.ai.knowledge.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class KnowledgeKafkaEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeKafkaEventPublisher.class);
    private static final String TOPIC = "knowledge-events";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KnowledgeKafkaEventPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishDocumentCreated(UUID documentId, String title, UUID createdBy) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("eventType", "KnowledgeDocumentCreated");
        payload.put("documentId", documentId.toString());
        payload.put("title", title);
        payload.put("createdBy", createdBy != null ? createdBy.toString() : null);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("KnowledgeDocumentCreated", payload);
    }

    public void publishDocumentUpdated(UUID documentId, UUID updatedBy) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("eventType", "KnowledgeDocumentUpdated");
        payload.put("documentId", documentId.toString());
        payload.put("updatedBy", updatedBy != null ? updatedBy.toString() : null);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("KnowledgeDocumentUpdated", payload);
    }

    public void publishDocumentDeleted(UUID documentId) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("eventType", "KnowledgeDocumentDeleted");
        payload.put("documentId", documentId.toString());
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("KnowledgeDocumentDeleted", payload);
    }

    public void publishChunkCreated(UUID documentId, int chunkIndex) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("eventType", "KnowledgeChunkCreated");
        payload.put("documentId", documentId.toString());
        payload.put("chunkIndex", chunkIndex);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("KnowledgeChunkCreated", payload);
    }

    public void publishChunkUpdated(UUID documentId, int chunkIndex) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("eventType", "KnowledgeChunkUpdated");
        payload.put("documentId", documentId.toString());
        payload.put("chunkIndex", chunkIndex);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("KnowledgeChunkUpdated", payload);
    }

    public void publishIndexed(UUID documentId, int chunkCount) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("eventType", "KnowledgeIndexed");
        payload.put("documentId", documentId.toString());
        payload.put("chunkCount", chunkCount);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("KnowledgeIndexed", payload);
    }

    public void publishRetrieved(UUID documentId, UUID requestId) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("eventType", "KnowledgeRetrieved");
        payload.put("documentId", documentId.toString());
        payload.put("requestId", requestId != null ? requestId.toString() : null);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("KnowledgeRetrieved", payload);
    }

    public void publishSearchExecuted(String query, int resultCount) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("eventType", "KnowledgeSearchExecuted");
        payload.put("query", query);
        payload.put("resultCount", resultCount);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("KnowledgeSearchExecuted", payload);
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
