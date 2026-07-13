package com.sporekart.ai.workflow.infrastructure.kafka;

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
class WorkflowKafkaEventPublisherTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    private WorkflowKafkaEventPublisher publisher;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        publisher = new WorkflowKafkaEventPublisher(kafkaTemplate, objectMapper);
    }

    @Test
    void shouldPublishWorkflowCreated() {
        var workflowId = UUID.randomUUID();
        var createdBy = UUID.randomUUID();

        publisher.publishWorkflowCreated(workflowId, "Test", createdBy);

        verify(kafkaTemplate).send(eq("workflow-events"), eq(workflowId.toString()), anyString());
    }

    @Test
    void shouldPublishWorkflowPublished() {
        var workflowId = UUID.randomUUID();

        publisher.publishWorkflowPublished(workflowId);

        verify(kafkaTemplate).send(eq("workflow-events"), eq(workflowId.toString()), anyString());
    }

    @Test
    void shouldPublishWorkflowDeactivated() {
        var workflowId = UUID.randomUUID();

        publisher.publishWorkflowDeactivated(workflowId);

        verify(kafkaTemplate).send(eq("workflow-events"), eq(workflowId.toString()), anyString());
    }

    @Test
    void shouldPublishWorkflowExecutionStarted() {
        var executionId = UUID.randomUUID();
        var workflowId = UUID.randomUUID();

        publisher.publishWorkflowExecutionStarted(executionId, workflowId);

        verify(kafkaTemplate).send(eq("workflow-events"), eq(executionId.toString()), anyString());
    }

    @Test
    void shouldPublishWorkflowExecutionCompleted() {
        var executionId = UUID.randomUUID();
        var workflowId = UUID.randomUUID();

        publisher.publishWorkflowExecutionCompleted(executionId, workflowId);

        verify(kafkaTemplate).send(eq("workflow-events"), eq(executionId.toString()), anyString());
    }

    @Test
    void shouldPublishWorkflowExecutionFailed() {
        var executionId = UUID.randomUUID();
        var workflowId = UUID.randomUUID();

        publisher.publishWorkflowExecutionFailed(executionId, workflowId, "Step failed");

        verify(kafkaTemplate).send(eq("workflow-events"), eq(executionId.toString()), anyString());
    }

    @Test
    void shouldPublishWorkflowExecutionPaused() {
        var executionId = UUID.randomUUID();
        var workflowId = UUID.randomUUID();

        publisher.publishWorkflowExecutionPaused(executionId, workflowId);

        verify(kafkaTemplate).send(eq("workflow-events"), eq(executionId.toString()), anyString());
    }

    @Test
    void shouldPublishWorkflowExecutionResumed() {
        var executionId = UUID.randomUUID();
        var workflowId = UUID.randomUUID();

        publisher.publishWorkflowExecutionResumed(executionId, workflowId);

        verify(kafkaTemplate).send(eq("workflow-events"), eq(executionId.toString()), anyString());
    }

    @Test
    void shouldPublishWorkflowExecutionCancelled() {
        var executionId = UUID.randomUUID();
        var workflowId = UUID.randomUUID();

        publisher.publishWorkflowExecutionCancelled(executionId, workflowId);

        verify(kafkaTemplate).send(eq("workflow-events"), eq(executionId.toString()), anyString());
    }

    @Test
    void shouldHandleKafkaFailure() {
        var workflowId = UUID.randomUUID();
        doThrow(new RuntimeException("Kafka down"))
                .when(kafkaTemplate).send(anyString(), anyString(), anyString());

        publisher.publishWorkflowCreated(workflowId, "Test", UUID.randomUUID());

        verify(kafkaTemplate).send(eq("workflow-events"), eq(workflowId.toString()), anyString());
    }
}
