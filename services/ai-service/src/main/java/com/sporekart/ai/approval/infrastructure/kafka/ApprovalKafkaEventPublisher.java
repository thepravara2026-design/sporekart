package com.sporekart.ai.approval.infrastructure.kafka;

import com.sporekart.ai.approval.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApprovalKafkaEventPublisher {

    private static final String TOPIC = "approval-events";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishApprovalRequested(ApprovalRequest request) {
        kafkaTemplate.send(TOPIC, "APPROVAL_REQUESTED", request);
        log.info("Published APPROVAL_REQUESTED for request: {}", request.id());
    }

    public void publishApprovalAssigned(ApprovalAssignment assignment) {
        kafkaTemplate.send(TOPIC, "APPROVAL_ASSIGNED", assignment);
        log.info("Published APPROVAL_ASSIGNED for assignment: {}", assignment.id());
    }

    public void publishApprovalReminderSent(ApprovalAssignment assignment) {
        kafkaTemplate.send(TOPIC, "APPROVAL_REMINDER_SENT", assignment);
        log.info("Published APPROVAL_REMINDER_SENT for assignment: {}", assignment.id());
    }

    public void publishApprovalApproved(ApprovalRequest request) {
        kafkaTemplate.send(TOPIC, "APPROVAL_APPROVED", request);
        log.info("Published APPROVAL_APPROVED for request: {}", request.id());
    }

    public void publishApprovalRejected(ApprovalRequest request) {
        kafkaTemplate.send(TOPIC, "APPROVAL_REJECTED", request);
        log.info("Published APPROVAL_REJECTED for request: {}", request.id());
    }

    public void publishApprovalEscalated(ApprovalEscalation escalation) {
        kafkaTemplate.send(TOPIC, "APPROVAL_ESCALATED", escalation);
        log.info("Published APPROVAL_ESCALATED for escalation: {}", escalation.id());
    }

    public void publishApprovalDelegated(ApprovalDelegation delegation) {
        kafkaTemplate.send(TOPIC, "APPROVAL_DELEGATED", delegation);
        log.info("Published APPROVAL_DELEGATED for delegation: {}", delegation.id());
    }

    public void publishApprovalExpired(ApprovalRequest request) {
        kafkaTemplate.send(TOPIC, "APPROVAL_EXPIRED", request);
        log.info("Published APPROVAL_EXPIRED for request: {}", request.id());
    }

    public void publishApprovalCancelled(ApprovalRequest request) {
        kafkaTemplate.send(TOPIC, "APPROVAL_CANCELLED", request);
        log.info("Published APPROVAL_CANCELLED for request: {}", request.id());
    }

    public void publishApprovalCompleted(ApprovalRequest request) {
        kafkaTemplate.send(TOPIC, "APPROVAL_COMPLETED", request);
        log.info("Published APPROVAL_COMPLETED for request: {}", request.id());
    }
}
