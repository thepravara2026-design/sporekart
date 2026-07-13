package com.sporekart.ai.governance.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface GovernanceAuditRepository extends JpaRepository<GovernanceAuditEntity, UUID> {

    List<GovernanceAuditEntity> findByRequestIdAndIsDeletedFalse(UUID requestId);

    List<GovernanceAuditEntity> findByUserIdAndIsDeletedFalse(String userId);

    List<GovernanceAuditEntity> findByDecisionAndIsDeletedFalse(String decision);

    List<GovernanceAuditEntity> findByModuleAndIsDeletedFalse(String module);

    List<GovernanceAuditEntity> findByTimestampBetweenAndIsDeletedFalse(OffsetDateTime start, OffsetDateTime end);
}
