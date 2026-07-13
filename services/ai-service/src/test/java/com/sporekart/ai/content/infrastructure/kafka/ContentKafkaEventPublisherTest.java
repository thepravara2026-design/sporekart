package com.sporekart.ai.content.infrastructure.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContentKafkaEventPublisherTest {

    @Mock private KafkaTemplate<String, String> kafkaTemplate;

    private ContentKafkaEventPublisher publisher;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        publisher = new ContentKafkaEventPublisher(kafkaTemplate, objectMapper);
    }

    @Test
    void shouldPublishContentGenerationStarted() {
        var contentId = UUID.randomUUID();
        publisher.publishContentGenerationStarted(contentId, "ARTICLE");
        verify(kafkaTemplate).send(eq("content-events"), eq(contentId.toString()), anyString());
    }

    @Test
    void shouldPublishContentGenerated() {
        var contentId = UUID.randomUUID();
        publisher.publishContentGenerated(contentId, "ARTICLE");
        verify(kafkaTemplate).send(eq("content-events"), eq(contentId.toString()), anyString());
    }

    @Test
    void shouldPublishContentGeneratedWithNullContentType() {
        var contentId = UUID.randomUUID();
        publisher.publishContentGenerated(contentId, null);
        verify(kafkaTemplate).send(eq("content-events"), eq(contentId.toString()), anyString());
    }

    @Test
    void shouldPublishContentGenerationFailed() {
        var contentId = UUID.randomUUID();
        publisher.publishContentGenerationFailed(contentId, "TEXT", "Error message");
        verify(kafkaTemplate).send(eq("content-events"), eq(contentId.toString()), anyString());
    }

    @Test
    void shouldPublishSummaryGenerated() {
        var contentId = UUID.randomUUID();
        var summaryId = UUID.randomUUID();
        publisher.publishSummaryGenerated(contentId, summaryId);
        verify(kafkaTemplate).send(eq("content-events"), eq(contentId.toString()), anyString());
    }

    @Test
    void shouldPublishTranslationCompleted() {
        var contentId = UUID.randomUUID();
        publisher.publishTranslationCompleted(contentId, "fr");
        verify(kafkaTemplate).send(eq("content-events"), eq(contentId.toString()), anyString());
    }

    @Test
    void shouldPublishClassificationCompleted() {
        var contentId = UUID.randomUUID();
        publisher.publishClassificationCompleted(contentId, "tech");
        verify(kafkaTemplate).send(eq("content-events"), eq(contentId.toString()), anyString());
    }

    @Test
    void shouldPublishRecommendationGenerated() {
        var contentId = UUID.randomUUID();
        var recommendationId = UUID.randomUUID();
        publisher.publishRecommendationGenerated(contentId, recommendationId);
        verify(kafkaTemplate).send(eq("content-events"), eq(contentId.toString()), anyString());
    }

    @Test
    void shouldPublishContentModerated() {
        var contentId = UUID.randomUUID();
        publisher.publishContentModerated(contentId, true, "approved");
        verify(kafkaTemplate).send(eq("content-events"), eq(contentId.toString()), anyString());
    }

    @Test
    void shouldPublishContentModeratedWithRejection() {
        var contentId = UUID.randomUUID();
        publisher.publishContentModerated(contentId, false, "contains profanity");
        verify(kafkaTemplate).send(eq("content-events"), eq(contentId.toString()), anyString());
    }

    @Test
    void shouldPublishContentReviewed() {
        var contentId = UUID.randomUUID();
        publisher.publishContentReviewed(contentId, "APPROVED");
        verify(kafkaTemplate).send(eq("content-events"), eq(contentId.toString()), anyString());
    }

    @Test
    void shouldHandleKafkaFailure() {
        var contentId = UUID.randomUUID();
        doThrow(new RuntimeException("Kafka down"))
                .when(kafkaTemplate).send(anyString(), anyString(), anyString());

        publisher.publishContentGenerationStarted(contentId, "TEXT");

        verify(kafkaTemplate).send(eq("content-events"), eq(contentId.toString()), anyString());
    }
}
