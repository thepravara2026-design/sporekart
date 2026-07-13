package com.sporekart.ai.approval.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ApprovalHistoryRepository extends JpaRepository<ApprovalHistoryEntity, UUID> {
    List<ApprovalHistoryEntity> findByRequestIdAndIsDeletedFalse(UUID requestId);
    List<ApprovalHistoryEntity> findByReviewerIdAndIsDeletedFalse(UUID reviewerId);
}
