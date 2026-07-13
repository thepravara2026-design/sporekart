package com.sporekart.ai.approval.api;

import com.sporekart.ai.approval.domain.*;
import java.util.List;
import java.util.UUID;

public interface ApprovalEngine {
    ApprovalRequest submit(ApprovalRequest request);
    ApprovalRequest approve(UUID requestId, String reviewerId, String comment);
    ApprovalRequest reject(UUID requestId, String reviewerId, String comment);
    ApprovalRequest delegate(UUID requestId, UUID fromReviewerId, UUID toReviewerId, String reason);
    ApprovalRequest escalate(UUID requestId, UUID reviewerId, EscalationReason reason, String details);
    ApprovalRequest cancel(UUID requestId, String userId, String reason);
    ApprovalRequest getStatus(UUID requestId);
    List<ApprovalRequest> getPendingApprovals(String userId);
    List<ApprovalHistory> getHistory(UUID requestId);
}
