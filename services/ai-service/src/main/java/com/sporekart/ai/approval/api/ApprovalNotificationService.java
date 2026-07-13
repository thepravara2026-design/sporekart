package com.sporekart.ai.approval.api;

import com.sporekart.ai.approval.domain.*;

public interface ApprovalNotificationService {
    void notifyAssigned(ApprovalAssignment assignment);
    void notifyApproved(ApprovalRequest request);
    void notifyRejected(ApprovalRequest request);
    void notifyEscalated(ApprovalEscalation escalation);
    void notifyDelegated(ApprovalDelegation delegation);
    void notifyReminder(ApprovalAssignment assignment);
    void notifyExpired(ApprovalRequest request);
}
