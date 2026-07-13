package com.sporekart.ai.automation.infrastructure.kafka;

import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.automation.domain.*;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

@ExtendWith(MockitoExtension.class)
class AutomationKafkaEventPublisherTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    private AutomationKafkaEventPublisher publisher;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        publisher = new AutomationKafkaEventPublisher(kafkaTemplate, objectMapper);
    }

    @Test
    void publishWorkflowStartedShouldSendToAutomationEventsTopic() {
        var executionId = UUID.randomUUID();
        publisher.publishWorkflowStarted(executionId, "test-workflow");

        verify(kafkaTemplate).send(eq("automation-events"), anyString(), anyString());
    }

    @Test
    void publishWorkflowCompletedShouldSendToAutomationEventsTopic() {
        publisher.publishWorkflowCompleted(UUID.randomUUID(), "test-workflow");
        verify(kafkaTemplate).send(eq("automation-events"), anyString(), anyString());
    }

    @Test
    void publishWorkflowFailedShouldSendToAutomationEventsTopic() {
        publisher.publishWorkflowFailed(UUID.randomUUID(), "test-workflow", "error");
        verify(kafkaTemplate).send(eq("automation-events"), anyString(), anyString());
    }

    @Test
    void publishJobScheduledShouldSendToAutomationEventsTopic() {
        publisher.publishJobScheduled(UUID.randomUUID(), "test-task");
        verify(kafkaTemplate).send(eq("automation-events"), anyString(), anyString());
    }

    @Test
    void publishJobExecutedShouldSendToAutomationEventsTopic() {
        publisher.publishJobExecuted(UUID.randomUUID(), "test-job", true);
        verify(kafkaTemplate).send(eq("automation-events"), anyString(), anyString());
    }

    @Test
    void publishRetryTriggeredShouldSendToAutomationEventsTopic() {
        publisher.publishRetryTriggered(UUID.randomUUID(), "Job", 1);
        verify(kafkaTemplate).send(eq("automation-events"), anyString(), anyString());
    }

    @Test
    void publishEscalationTriggeredShouldSendToAutomationEventsTopic() {
        publisher.publishEscalationTriggered(UUID.randomUUID(), UUID.randomUUID(), "policy", "critical");
        verify(kafkaTemplate).send(eq("automation-events"), anyString(), anyString());
    }

    @Test
    void publishLifecycleTransitionCompletedShouldSendToAutomationEventsTopic() {
        publisher.publishLifecycleTransitionCompleted(UUID.randomUUID(), "policy", "CREATED", "ACTIVE");
        verify(kafkaTemplate).send(eq("automation-events"), anyString(), anyString());
    }

    @Test
    void publishAutomationAuditRecordedShouldSendToAutomationEventsTopic() {
        publisher.publishAutomationAuditRecorded(UUID.randomUUID(), "JOB_CREATED", "AutomationJob");
        verify(kafkaTemplate).send(eq("automation-events"), anyString(), anyString());
    }

    @Test
    void publishShouldHandleException() {
        doThrow(new RuntimeException("kafka down")).when(kafkaTemplate).send(anyString(), anyString(), anyString());
        publisher.publishWorkflowStarted(UUID.randomUUID(), "test");
        verify(kafkaTemplate).send(anyString(), anyString(), anyString());
    }

    @Test
    void allPublishMethodsShouldSendWithCorrectTopic() {
        var executionId = UUID.randomUUID();
        var entityId = UUID.randomUUID();
        var escalationId = UUID.randomUUID();
        var taskId = UUID.randomUUID();
        var jobId = UUID.randomUUID();
        var auditId = UUID.randomUUID();

        publisher.publishWorkflowStarted(executionId, "wf");
        publisher.publishWorkflowCompleted(executionId, "wf");
        publisher.publishWorkflowFailed(executionId, "wf", "err");
        publisher.publishJobScheduled(taskId, "task");
        publisher.publishJobExecuted(jobId, "job", true);
        publisher.publishRetryTriggered(entityId, "Job", 1);
        publisher.publishEscalationTriggered(escalationId, entityId, "policy", "reason");
        publisher.publishLifecycleTransitionCompleted(entityId, "policy", "A", "B");
        publisher.publishAutomationAuditRecorded(auditId, "CREATE", "Entity");

        verify(kafkaTemplate, times(9)).send(eq("automation-events"), anyString(), anyString());
    }
}
