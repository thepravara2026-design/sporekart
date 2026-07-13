package com.sporekart.ai.approval.infrastructure.kafka;

import com.sporekart.ai.approval.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApprovalKafkaEventPublisherTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    private ApprovalKafkaEventPublisher publisher;

    @BeforeEach
    void setUp() {
        publisher = new ApprovalKafkaEventPublisher(kafkaTemplate);
    }

    @Test
    void shouldPublishApprovalRequested() {
        var request = new ApprovalRequest(UUID.randomUUID(), "content", "publish",
            Map.of(), Map.of(), "user1", List.of("admin"),
            "reason", "normal", null, Map.of(), null, ApprovalStatus.PENDING, OffsetDateTime.now());

        publisher.publishApprovalRequested(request);

        verify(kafkaTemplate).send(eq("approval-events"), eq("APPROVAL_REQUESTED"), eq(request));
    }

    @Test
    void shouldPublishApprovalAssigned() {
        var assignment = new ApprovalAssignment(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
            "reviewer1", AssignmentStrategy.ROLE_BASED, 1, ApprovalStatus.ASSIGNED,
            OffsetDateTime.now(), null, null);

        publisher.publishApprovalAssigned(assignment);

        verify(kafkaTemplate).send(eq("approval-events"), eq("APPROVAL_ASSIGNED"), eq(assignment));
    }

    @Test
    void shouldPublishApprovalApproved() {
        var request = new ApprovalRequest(UUID.randomUUID(), "content", "publish",
            Map.of(), Map.of(), "user1", List.of("admin"),
            "reason", "normal", null, Map.of(), null, ApprovalStatus.APPROVED, OffsetDateTime.now());

        publisher.publishApprovalApproved(request);

        verify(kafkaTemplate).send(eq("approval-events"), eq("APPROVAL_APPROVED"), eq(request));
    }

    @Test
    void shouldPublishApprovalRejected() {
        var request = new ApprovalRequest(UUID.randomUUID(), "content", "publish",
            Map.of(), Map.of(), "user1", List.of("admin"),
            "reason", "normal", null, Map.of(), null, ApprovalStatus.REJECTED, OffsetDateTime.now());

        publisher.publishApprovalRejected(request);

        verify(kafkaTemplate).send(eq("approval-events"), eq("APPROVAL_REJECTED"), eq(request));
    }

    @Test
    void shouldPublishApprovalEscalated() {
        var escalation = new ApprovalEscalation(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
            null, EscalationReason.TIMEOUT, "details", 1,
            OffsetDateTime.now(), null);

        publisher.publishApprovalEscalated(escalation);

        verify(kafkaTemplate).send(eq("approval-events"), eq("APPROVAL_ESCALATED"), eq(escalation));
    }

    @Test
    void shouldPublishApprovalDelegated() {
        var delegation = new ApprovalDelegation(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
            UUID.randomUUID(), "reason", true, OffsetDateTime.now(), null);

        publisher.publishApprovalDelegated(delegation);

        verify(kafkaTemplate).send(eq("approval-events"), eq("APPROVAL_DELEGATED"), eq(delegation));
    }

    @Test
    void shouldPublishApprovalCancelled() {
        var request = new ApprovalRequest(UUID.randomUUID(), "content", "publish",
            Map.of(), Map.of(), "user1", List.of("admin"),
            "reason", "normal", null, Map.of(), null, ApprovalStatus.CANCELLED, OffsetDateTime.now());

        publisher.publishApprovalCancelled(request);

        verify(kafkaTemplate).send(eq("approval-events"), eq("APPROVAL_CANCELLED"), eq(request));
    }

    @Test
    void shouldPublishApprovalCompleted() {
        var request = new ApprovalRequest(UUID.randomUUID(), "content", "publish",
            Map.of(), Map.of(), "user1", List.of("admin"),
            "reason", "normal", null, Map.of(), null, ApprovalStatus.COMPLETED, OffsetDateTime.now());

        publisher.publishApprovalCompleted(request);

        verify(kafkaTemplate).send(eq("approval-events"), eq("APPROVAL_COMPLETED"), eq(request));
    }
}
