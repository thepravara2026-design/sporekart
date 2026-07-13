package com.sporekart.ai.semantic.infrastructure;

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
public class SemanticKafkaEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(SemanticKafkaEventPublisher.class);
    private static final String TOPIC = "semantic-events";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public SemanticKafkaEventPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishEmbeddingCreated(String embeddingId, String provider, UUID createdBy) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("eventType", "EmbeddingCreated");
        payload.put("embeddingId", embeddingId);
        payload.put("provider", provider);
        payload.put("createdBy", createdBy != null ? createdBy.toString() : null);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("EmbeddingCreated", embeddingId, payload);
    }

    public void publishEmbeddingUpdated(String embeddingId, UUID updatedBy) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("eventType", "EmbeddingUpdated");
        payload.put("embeddingId", embeddingId);
        payload.put("updatedBy", updatedBy != null ? updatedBy.toString() : null);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("EmbeddingUpdated", embeddingId, payload);
    }

    public void publishEmbeddingDeleted(String embeddingId) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("eventType", "EmbeddingDeleted");
        payload.put("embeddingId", embeddingId);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("EmbeddingDeleted", embeddingId, payload);
    }

    public void publishSearchExecuted(String searchId, String query, int resultCount, long latencyMs) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("eventType", "SemanticSearchExecuted");
        payload.put("searchId", searchId);
        payload.put("query", query);
        payload.put("resultCount", resultCount);
        payload.put("latencyMs", latencyMs);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("SemanticSearchExecuted", searchId, payload);
    }

    public void publishSimilarityCalculated(String similarityId, String sourceId, String targetId, double score) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("eventType", "SimilarityCalculated");
        payload.put("similarityId", similarityId);
        payload.put("sourceId", sourceId);
        payload.put("targetId", targetId);
        payload.put("score", score);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("SimilarityCalculated", similarityId, payload);
    }

    public void publishVectorIndexBuilt(String indexName) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("eventType", "VectorIndexBuilt");
        payload.put("indexName", indexName);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("VectorIndexBuilt", indexName, payload);
    }

    public void publishVectorIndexRebuilt(String indexName) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("eventType", "VectorIndexRebuilt");
        payload.put("indexName", indexName);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("VectorIndexRebuilt", indexName, payload);
    }

    public void publishRankingCompleted(String resultId) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("eventType", "RankingCompleted");
        payload.put("resultId", resultId);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("RankingCompleted", resultId, payload);
    }

    public void publishHybridSearchCompleted(String resultId) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("eventType", "HybridSearchCompleted");
        payload.put("resultId", resultId);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("HybridSearchCompleted", resultId, payload);
    }

    private void publish(String eventType, String key, Map<String, Object> payload) {
        try {
            String message = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send(TOPIC, key, message);
            log.debug("Published {} event to {}", eventType, TOPIC);
        } catch (Exception e) {
            log.warn("Failed to publish {} event: {}", eventType, e.getMessage());
        }
    }
}
