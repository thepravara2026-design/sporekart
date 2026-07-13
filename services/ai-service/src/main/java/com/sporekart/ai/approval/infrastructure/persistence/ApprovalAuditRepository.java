package com.sporekart.ai.approval.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ApprovalAuditRepository extends JpaRepository<ApprovalAuditEntity, UUID> {
    List<ApprovalAuditEntity> findByRequestIdAndIsDeletedFalse(UUID requestId);
    List<ApprovalAuditEntity> findByReviewerIdAndIsDeletedFalse(UUID reviewerId);
    List<ApprovalAuditEntity> findByDecisionAndIsDeletedFalse(String decision);
    List<ApprovalAuditEntity> findByTimestampBetweenAndIsDeletedFalse(OffsetDateTime start, OffsetDateTime end);
}
