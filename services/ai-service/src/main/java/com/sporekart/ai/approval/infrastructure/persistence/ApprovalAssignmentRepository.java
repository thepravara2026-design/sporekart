package com.sporekart.ai.approval.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ApprovalAssignmentRepository extends JpaRepository<ApprovalAssignmentEntity, UUID> {
    List<ApprovalAssignmentEntity> findByRequestIdAndIsDeletedFalse(UUID requestId);
    List<ApprovalAssignmentEntity> findByReviewerUserIdAndIsDeletedFalse(String reviewerUserId);
    List<ApprovalAssignmentEntity> findByStatusAndIsDeletedFalse(String status);
}
