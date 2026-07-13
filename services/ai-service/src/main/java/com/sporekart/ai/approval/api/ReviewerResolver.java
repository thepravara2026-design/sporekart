package com.sporekart.ai.approval.api;

import com.sporekart.ai.approval.domain.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewerResolver {
    List<ApprovalReviewer> resolveReviewers(ApprovalRequest request);
    List<ApprovalReviewer> resolveByRole(String role);
    List<ApprovalReviewer> resolveByDepartment(String department);
    List<ApprovalReviewer> resolveByGroup(UUID groupId);
    Optional<ApprovalReviewer> resolveFallback(ApprovalRequest request);
    Optional<ApprovalReviewer> resolveEmergency(ApprovalRequest request);
    ApprovalReviewer selectReviewer(List<ApprovalReviewer> candidates, AssignmentStrategy strategy);
}
