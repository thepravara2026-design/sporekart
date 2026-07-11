package com.sporekart.ai.prompt.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PromptKafkaEventPublisherTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    private PromptKafkaEventPublisher publisher;

    @BeforeEach
    void setUp() {
        publisher = new PromptKafkaEventPublisher(kafkaTemplate, new ObjectMapper());
    }

    @Test
    void shouldPublishCreatedEvent() {
        publisher.publishCreated(UUID.randomUUID(), "test", UUID.randomUUID());
        verify(kafkaTemplate).send(anyString(), anyString(), anyString());
    }

    @Test
    void shouldPublishUpdatedEvent() {
        publisher.publishUpdated(UUID.randomUUID(), "test", UUID.randomUUID());
        verify(kafkaTemplate).send(anyString(), anyString(), anyString());
    }

    @Test
    void shouldPublishPublishedEvent() {
        publisher.publishPublished(UUID.randomUUID(), 1, UUID.randomUUID());
        verify(kafkaTemplate).send(anyString(), anyString(), anyString());
    }

    @Test
    void shouldPublishDeprecatedEvent() {
        publisher.publishDeprecated(UUID.randomUUID(), UUID.randomUUID());
        verify(kafkaTemplate).send(anyString(), anyString(), anyString());
    }

    @Test
    void shouldPublishRolledBackEvent() {
        publisher.publishRolledBack(UUID.randomUUID(), 2, 3, UUID.randomUUID());
        verify(kafkaTemplate).send(anyString(), anyString(), anyString());
    }

    @Test
    void shouldPublishExecutionStartedEvent() {
        publisher.publishExecutionStarted(UUID.randomUUID(), "corr-123");
        verify(kafkaTemplate).send(anyString(), anyString(), anyString());
    }

    @Test
    void shouldPublishExecutionCompletedEvent() {
        publisher.publishExecutionCompleted(UUID.randomUUID(), "corr-123", 150L, true);
        verify(kafkaTemplate).send(anyString(), anyString(), anyString());
    }
}
