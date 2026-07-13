package com.sporekart.ai.approval.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ApprovalDelegationRepository extends JpaRepository<ApprovalDelegationEntity, UUID> {
    List<ApprovalDelegationEntity> findByRequestIdAndIsDeletedFalse(UUID requestId);
    List<ApprovalDelegationEntity> findByFromReviewerIdAndIsDeletedFalse(UUID fromReviewerId);
    List<ApprovalDelegationEntity> findByIsActiveTrue();
}
