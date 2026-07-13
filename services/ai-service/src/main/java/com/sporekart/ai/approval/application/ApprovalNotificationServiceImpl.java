package com.sporekart.ai.approval.application;

import com.sporekart.ai.approval.api.ApprovalNotificationService;
import com.sporekart.ai.approval.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ApprovalNotificationServiceImpl implements ApprovalNotificationService {

    @Override
    public void notifyAssigned(ApprovalAssignment assignment) {
        log.info("Notification: Assignment created for request {} assigned to reviewer {}",
            assignment.requestId(), assignment.reviewerUserId());
    }

    @Override
    public void notifyApproved(ApprovalRequest request) {
        log.info("Notification: Request {} approved", request.id());
    }

    @Override
    public void notifyRejected(ApprovalRequest request) {
        log.info("Notification: Request {} rejected", request.id());
    }

    @Override
    public void notifyEscalated(ApprovalEscalation escalation) {
        log.info("Notification: Request {} escalated from reviewer {}",
            escalation.requestId(), escalation.fromReviewerId());
    }

    @Override
    public void notifyDelegated(ApprovalDelegation delegation) {
        log.info("Notification: Request {} delegated from {} to {}",
            delegation.requestId(), delegation.fromReviewerId(), delegation.toReviewerId());
    }

    @Override
    public void notifyReminder(ApprovalAssignment assignment) {
        log.info("Notification: Reminder for assignment {} on request {}",
            assignment.id(), assignment.requestId());
    }

    @Override
    public void notifyExpired(ApprovalRequest request) {
        log.info("Notification: Request {} expired", request.id());
    }
}
