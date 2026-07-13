package com.sporekart.ai.admin.infrastructure.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminKafkaEventPublisherTest {

    @Mock private KafkaTemplate<String, String> kafkaTemplate;
    @Captor private ArgumentCaptor<String> topicCaptor;
    @Captor private ArgumentCaptor<String> keyCaptor;
    @Captor private ArgumentCaptor<String> messageCaptor;

    private AdminKafkaEventPublisher publisher;

    @BeforeEach
    void setUp() {
        publisher = new AdminKafkaEventPublisher(kafkaTemplate, new ObjectMapper());
    }

    @Test
    void testPublishConfigurationUpdated() {
        var configId = UUID.randomUUID();
        publisher.publishConfigurationUpdated(configId, "test-key", "governance", "production");

        verify(kafkaTemplate).send(topicCaptor.capture(), keyCaptor.capture(), messageCaptor.capture());
        assertEquals("admin-events", topicCaptor.getValue());
        assertEquals(configId.toString(), keyCaptor.getValue());
        assertTrue(messageCaptor.getValue().contains("ConfigurationUpdated"));
        assertTrue(messageCaptor.getValue().contains(configId.toString()));
        assertTrue(messageCaptor.getValue().contains("test-key"));
    }

    @Test
    void testPublishConfigurationRolledBack() {
        var configId = UUID.randomUUID();
        publisher.publishConfigurationRolledBack(configId, 1, 3);

        verify(kafkaTemplate).send(eq("admin-events"), eq(configId.toString()), anyString());
    }

    @Test
    void testPublishFeatureFlagChanged() {
        var flagId = UUID.randomUUID();
        publisher.publishFeatureFlagChanged(flagId, "ff-key", true);

        verify(kafkaTemplate).send(eq("admin-events"), eq(flagId.toString()), anyString());
    }

    @Test
    void testPublishEnvironmentProfileUpdated() {
        var envId = UUID.randomUUID();
        publisher.publishEnvironmentProfileUpdated(envId, "production");

        verify(kafkaTemplate).send(eq("admin-events"), eq(envId.toString()), anyString());
    }

    @Test
    void testPublishAdministrationOperationCompleted() {
        var opId = UUID.randomUUID();
        publisher.publishAdministrationOperationCompleted(opId, "CONFIG_UPDATE");

        verify(kafkaTemplate).send(eq("admin-events"), eq(opId.toString()), anyString());
    }

    @Test
    void testPublishAdministrationAuditCreated() {
        var auditId = UUID.randomUUID();
        publisher.publishAdministrationAuditCreated(auditId, "CONFIG_UPDATE", "AdminConfiguration");

        verify(kafkaTemplate).send(eq("admin-events"), eq(auditId.toString()), anyString());
    }

    @Test
    void testPublishConfigurationUpdated_MessageContent() {
        var configId = UUID.randomUUID();
        publisher.publishConfigurationUpdated(configId, "key1", "mod1", "env1");

        verify(kafkaTemplate).send(anyString(), anyString(), messageCaptor.capture());
        var msg = messageCaptor.getValue();
        assertTrue(msg.contains("ConfigurationUpdated"));
        assertTrue(msg.contains("key1"));
        assertTrue(msg.contains("mod1"));
        assertTrue(msg.contains("env1"));
        assertTrue(msg.contains(configId.toString()));
    }

    @Test
    void testPublishFeatureFlagChanged_MessageContent() {
        var flagId = UUID.randomUUID();
        publisher.publishFeatureFlagChanged(flagId, "flag-key", false);

        verify(kafkaTemplate).send(anyString(), anyString(), messageCaptor.capture());
        var msg = messageCaptor.getValue();
        assertTrue(msg.contains("FeatureFlagChanged"));
        assertTrue(msg.contains("flag-key"));
        assertTrue(msg.contains("false"));
        assertTrue(msg.contains(flagId.toString()));
    }
}
