package com.sporekart.ai.approval.api;

import com.sporekart.ai.approval.domain.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApprovalAssignmentService {
    ApprovalAssignment assign(ApprovalRequest request);
    ApprovalAssignment assignToReviewer(UUID requestId, UUID reviewerId);
    List<ApprovalAssignment> getAssignments(UUID requestId);
    Optional<ApprovalAssignment> getActiveAssignment(UUID requestId);
    void completeAssignment(UUID assignmentId, ApprovalDecision decision);
    List<ApprovalAssignment> getPendingAssignments(String reviewerUserId);
}
