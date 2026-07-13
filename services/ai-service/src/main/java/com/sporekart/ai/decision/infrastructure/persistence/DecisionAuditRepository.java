package com.sporekart.ai.decision.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface DecisionAuditRepository extends JpaRepository<DecisionAuditEntity, UUID> {

    List<DecisionAuditEntity> findByRequestIdAndIsDeletedFalse(UUID requestId);

    List<DecisionAuditEntity> findByUserIdAndIsDeletedFalse(String userId);

    List<DecisionAuditEntity> findByActionAndIsDeletedFalse(String action);

    List<DecisionAuditEntity> findByStatusAndIsDeletedFalse(String status);

    List<DecisionAuditEntity> findByTimestampBetweenAndIsDeletedFalse(OffsetDateTime start, OffsetDateTime end);
}
