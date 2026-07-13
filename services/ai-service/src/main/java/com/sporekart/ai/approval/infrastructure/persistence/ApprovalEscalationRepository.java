package com.sporekart.ai.approval.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ApprovalEscalationRepository extends JpaRepository<ApprovalEscalationEntity, UUID> {
    List<ApprovalEscalationEntity> findByRequestIdAndIsDeletedFalse(UUID requestId);
    List<ApprovalEscalationEntity> findByFromReviewerIdAndIsDeletedFalse(UUID fromReviewerId);
}
