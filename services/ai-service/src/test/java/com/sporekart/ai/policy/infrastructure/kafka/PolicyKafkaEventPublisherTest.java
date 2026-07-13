package com.sporekart.ai.policy.infrastructure.kafka;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.policy.infrastructure.kafka.PolicyKafkaEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PolicyKafkaEventPublisherTest {
    @Mock private KafkaTemplate<String, String> kafkaTemplate;
    private PolicyKafkaEventPublisher publisher;

    @BeforeEach void setUp() { publisher = new PolicyKafkaEventPublisher(kafkaTemplate, new ObjectMapper()); }

    @Test void testPublishCreated() { publisher.publishCreated("p1", "test"); verify(kafkaTemplate).send(eq("policy-events"), eq("PolicyCreated"), anyString()); }
    @Test void testPublishUpdated() { publisher.publishUpdated("p1", "test"); verify(kafkaTemplate).send(eq("policy-events"), eq("PolicyUpdated"), anyString()); }
    @Test void testPublishDeleted() { publisher.publishDeleted("p1"); verify(kafkaTemplate).send(eq("policy-events"), eq("PolicyDeleted"), anyString()); }
    @Test void testPublishActivated() { publisher.publishActivated("p1"); verify(kafkaTemplate).send(eq("policy-events"), eq("PolicyActivated"), anyString()); }
    @Test void testPublishEvaluated() { publisher.publishEvaluated("r1", "ALLOW", 42L); verify(kafkaTemplate).send(eq("policy-events"), eq("PolicyEvaluated"), anyString()); }
    @Test void testPublishViolationDetected() { publisher.publishViolationDetected("r1", "rule1", "WARNING"); verify(kafkaTemplate).send(eq("policy-events"), eq("PolicyViolationDetected"), anyString()); }
    @Test void testPublishEvaluationFailed() { publisher.publishEvaluationFailed("r1", "error"); verify(kafkaTemplate).send(eq("policy-events"), eq("PolicyEvaluationFailed"), anyString()); }
}
