package com.sporekart.ai.conversation.infrastructure.kafka;

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
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ConversationKafkaEventPublisherTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    private ConversationKafkaEventPublisher publisher;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        publisher = new ConversationKafkaEventPublisher(kafkaTemplate, objectMapper);
    }

    @Test
    void shouldPublishSessionCreated() {
        var sessionId = UUID.randomUUID();

        publisher.publishSessionCreated(sessionId, "user-1");

        verify(kafkaTemplate).send(eq("conversation-events"), eq(sessionId.toString()), anyString());
    }

    @Test
    void shouldPublishSessionClosed() {
        var sessionId = UUID.randomUUID();

        publisher.publishSessionClosed(sessionId, "user-1");

        verify(kafkaTemplate).send(eq("conversation-events"), eq(sessionId.toString()), anyString());
    }

    @Test
    void shouldPublishMessageSent() {
        var messageId = UUID.randomUUID();
        var sessionId = UUID.randomUUID();

        publisher.publishMessageSent(messageId, sessionId, "USER");

        verify(kafkaTemplate).send(eq("conversation-events"), eq(messageId.toString()), anyString());
    }

    @Test
    void shouldPublishMemoryStored() {
        var memoryId = UUID.randomUUID();
        var sessionId = UUID.randomUUID();

        publisher.publishMemoryStored(memoryId, sessionId, "SHORT_TERM");

        verify(kafkaTemplate).send(eq("conversation-events"), eq(memoryId.toString()), anyString());
    }

    @Test
    void shouldPublishContextRefreshed() {
        var sessionId = UUID.randomUUID();

        publisher.publishContextRefreshed(sessionId);

        verify(kafkaTemplate).send(eq("conversation-events"), eq(sessionId.toString()), anyString());
    }

    @Test
    void shouldHandleKafkaFailure() {
        var sessionId = UUID.randomUUID();
        org.mockito.Mockito.doThrow(new RuntimeException("Kafka down"))
                .when(kafkaTemplate).send(anyString(), anyString(), anyString());

        publisher.publishSessionCreated(sessionId, "user-1");

        verify(kafkaTemplate).send(eq("conversation-events"), eq(sessionId.toString()), anyString());
    }
}
