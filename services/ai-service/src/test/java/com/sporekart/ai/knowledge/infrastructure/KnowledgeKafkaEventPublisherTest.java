package com.sporekart.ai.knowledge.infrastructure;

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
class KnowledgeKafkaEventPublisherTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    private KnowledgeKafkaEventPublisher publisher;

    @BeforeEach
    void setUp() {
        publisher = new KnowledgeKafkaEventPublisher(kafkaTemplate, new ObjectMapper());
    }

    @Test
    void shouldPublishDocumentCreatedEvent() {
        publisher.publishDocumentCreated(UUID.randomUUID(), "test doc", UUID.randomUUID());
        verify(kafkaTemplate).send(eq("knowledge-events"), eq("KnowledgeDocumentCreated"), anyString());
    }

    @Test
    void shouldPublishDocumentUpdatedEvent() {
        publisher.publishDocumentUpdated(UUID.randomUUID(), UUID.randomUUID());
        verify(kafkaTemplate).send(eq("knowledge-events"), eq("KnowledgeDocumentUpdated"), anyString());
    }

    @Test
    void shouldPublishDocumentDeletedEvent() {
        publisher.publishDocumentDeleted(UUID.randomUUID());
        verify(kafkaTemplate).send(eq("knowledge-events"), eq("KnowledgeDocumentDeleted"), anyString());
    }

    @Test
    void shouldPublishChunkCreatedEvent() {
        publisher.publishChunkCreated(UUID.randomUUID(), 1);
        verify(kafkaTemplate).send(eq("knowledge-events"), eq("KnowledgeChunkCreated"), anyString());
    }

    @Test
    void shouldPublishChunkUpdatedEvent() {
        publisher.publishChunkUpdated(UUID.randomUUID(), 1);
        verify(kafkaTemplate).send(eq("knowledge-events"), eq("KnowledgeChunkUpdated"), anyString());
    }

    @Test
    void shouldPublishIndexedEvent() {
        publisher.publishIndexed(UUID.randomUUID(), 5);
        verify(kafkaTemplate).send(eq("knowledge-events"), eq("KnowledgeIndexed"), anyString());
    }

    @Test
    void shouldPublishRetrievedEvent() {
        publisher.publishRetrieved(UUID.randomUUID(), UUID.randomUUID());
        verify(kafkaTemplate).send(eq("knowledge-events"), eq("KnowledgeRetrieved"), anyString());
    }

    @Test
    void shouldPublishSearchExecutedEvent() {
        publisher.publishSearchExecuted("test query", 3);
        verify(kafkaTemplate).send(eq("knowledge-events"), eq("KnowledgeSearchExecuted"), anyString());
    }
}
