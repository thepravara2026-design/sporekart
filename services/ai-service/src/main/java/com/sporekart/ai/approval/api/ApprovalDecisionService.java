package com.sporekart.ai.approval.api;

import com.sporekart.ai.approval.domain.*;
import java.util.UUID;

public interface ApprovalDecisionService {
    ApprovalRequest approve(UUID requestId, String reviewerId, String comment);
    ApprovalRequest reject(UUID requestId, String reviewerId, String comment);
    ApprovalRequest requestChanges(UUID requestId, String reviewerId, String comment);
    boolean isApproved(UUID requestId);
    boolean isRejected(UUID requestId);
    ApprovalDecision getCurrentDecision(UUID requestId);
}
