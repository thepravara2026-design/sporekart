package com.sporekart.ai.governance.infrastructure.governance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sporekart.ai.governance.infrastructure.kafka.GovernanceKafkaEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GovernanceKafkaEventPublisherTest {

    @Mock private KafkaTemplate<String, String> kafkaTemplate;
    private GovernanceKafkaEventPublisher publisher;

    @BeforeEach
    void setUp() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        publisher = new GovernanceKafkaEventPublisher(kafkaTemplate, mapper);
    }

    @Test
    void testPublishInitialized() {
        publisher.publishInitialized(Map.of("key", "value"));
        verify(kafkaTemplate).send(eq("governance-events"), eq("GovernanceInitialized"), anyString());
    }

    @Test
    void testPublishReloaded() {
        publisher.publishReloaded(Map.of("reason", "test"));
        verify(kafkaTemplate).send(eq("governance-events"), eq("GovernanceReloaded"), anyString());
    }

    @Test
    void testPublishValidated() {
        publisher.publishValidated("req-1", "ALLOW", 42L);
        verify(kafkaTemplate).send(eq("governance-events"), eq("GovernanceValidated"), anyString());
    }

    @Test
    void testPublishHealthChanged() {
        publisher.publishHealthChanged("UP", Map.of("detail", "ok"));
        verify(kafkaTemplate).send(eq("governance-events"), eq("GovernanceHealthChanged"), anyString());
    }

    @Test
    void testPublishConfigurationChanged() {
        publisher.publishConfigurationChanged("key1", "value1");
        verify(kafkaTemplate).send(eq("governance-events"), eq("GovernanceConfigurationChanged"), anyString());
    }

    @Test
    void testPublishAuditCreated() {
        publisher.publishAuditCreated("audit-1", "req-1", "ALLOW");
        verify(kafkaTemplate).send(eq("governance-events"), eq("GovernanceAuditCreated"), anyString());
    }
}
