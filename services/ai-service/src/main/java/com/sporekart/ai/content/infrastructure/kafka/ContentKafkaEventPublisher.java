package com.sporekart.ai.content.infrastructure.kafka;

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
public class ContentKafkaEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(ContentKafkaEventPublisher.class);
    private static final String TOPIC = "content-events";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public ContentKafkaEventPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishContentGenerationStarted(UUID contentId, String contentType) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "ContentGenerationStarted");
        payload.put("contentId", contentId.toString());
        payload.put("contentType", contentType);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("ContentGenerationStarted", contentId.toString(), payload);
    }

    public void publishContentGenerated(UUID contentId, String contentType) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "ContentGenerated");
        payload.put("contentId", contentId.toString());
        payload.put("contentType", contentType);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("ContentGenerated", contentId.toString(), payload);
    }

    public void publishContentGenerationFailed(UUID contentId, String contentType, String error) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "ContentGenerationFailed");
        payload.put("contentId", contentId.toString());
        payload.put("contentType", contentType);
        payload.put("error", error);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("ContentGenerationFailed", contentId.toString(), payload);
    }

    public void publishSummaryGenerated(UUID contentId, UUID summaryId) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "SummaryGenerated");
        payload.put("contentId", contentId.toString());
        payload.put("summaryId", summaryId.toString());
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("SummaryGenerated", contentId.toString(), payload);
    }

    public void publishTranslationCompleted(UUID contentId, String targetLanguage) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "TranslationCompleted");
        payload.put("contentId", contentId.toString());
        payload.put("targetLanguage", targetLanguage);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("TranslationCompleted", contentId.toString(), payload);
    }

    public void publishClassificationCompleted(UUID contentId, String category) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "ClassificationCompleted");
        payload.put("contentId", contentId.toString());
        payload.put("category", category);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("ClassificationCompleted", contentId.toString(), payload);
    }

    public void publishRecommendationGenerated(UUID contentId, UUID recommendationId) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "RecommendationGenerated");
        payload.put("contentId", contentId.toString());
        payload.put("recommendationId", recommendationId.toString());
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("RecommendationGenerated", contentId.toString(), payload);
    }

    public void publishContentModerated(UUID contentId, boolean approved, String reason) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "ContentModerated");
        payload.put("contentId", contentId.toString());
        payload.put("approved", approved);
        payload.put("reason", reason);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("ContentModerated", contentId.toString(), payload);
    }

    public void publishContentReviewed(UUID contentId, String reviewStatus) {
        var payload = new HashMap<String, Object>();
        payload.put("eventType", "ContentReviewed");
        payload.put("contentId", contentId.toString());
        payload.put("reviewStatus", reviewStatus);
        payload.put("timestamp", OffsetDateTime.now().toString());
        publish("ContentReviewed", contentId.toString(), payload);
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
