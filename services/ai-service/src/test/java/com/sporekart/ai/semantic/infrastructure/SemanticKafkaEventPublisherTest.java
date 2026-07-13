package com.sporekart.ai.semantic.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SemanticKafkaEventPublisherTest {

    @Mock private KafkaTemplate<String, String> kafkaTemplate;

    private ObjectMapper objectMapper;
    private SemanticKafkaEventPublisher publisher;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        publisher = new SemanticKafkaEventPublisher(kafkaTemplate, objectMapper);
    }

    @Test
    void testPublishEmbeddingCreated() {
        publisher.publishEmbeddingCreated("emb-1", "OPENAI", UUID.randomUUID());
        verify(kafkaTemplate).send(eq("semantic-events"), eq("emb-1"), anyString());
    }

    @Test
    void testPublishEmbeddingUpdated() {
        publisher.publishEmbeddingUpdated("emb-1", UUID.randomUUID());
        verify(kafkaTemplate).send(eq("semantic-events"), eq("emb-1"), anyString());
    }

    @Test
    void testPublishEmbeddingDeleted() {
        publisher.publishEmbeddingDeleted("emb-1");
        verify(kafkaTemplate).send(eq("semantic-events"), eq("emb-1"), anyString());
    }

    @Test
    void testPublishSearchExecuted() {
        publisher.publishSearchExecuted("search-1", "query", 5, 100L);
        verify(kafkaTemplate).send(eq("semantic-events"), eq("search-1"), anyString());
    }

    @Test
    void testPublishSimilarityCalculated() {
        publisher.publishSimilarityCalculated("sim-1", "src-1", "tgt-1", 0.95);
        verify(kafkaTemplate).send(eq("semantic-events"), eq("sim-1"), anyString());
    }

    @Test
    void testPublishVectorIndexBuilt() {
        publisher.publishVectorIndexBuilt("idx-1");
        verify(kafkaTemplate).send(eq("semantic-events"), eq("idx-1"), anyString());
    }

    @Test
    void testPublishVectorIndexRebuilt() {
        publisher.publishVectorIndexRebuilt("idx-1");
        verify(kafkaTemplate).send(eq("semantic-events"), eq("idx-1"), anyString());
    }

    @Test
    void testPublishRankingCompleted() {
        publisher.publishRankingCompleted("result-1");
        verify(kafkaTemplate).send(eq("semantic-events"), eq("result-1"), anyString());
    }

    @Test
    void testPublishHybridSearchCompleted() {
        publisher.publishHybridSearchCompleted("result-1");
        verify(kafkaTemplate).send(eq("semantic-events"), eq("result-1"), anyString());
    }
}
