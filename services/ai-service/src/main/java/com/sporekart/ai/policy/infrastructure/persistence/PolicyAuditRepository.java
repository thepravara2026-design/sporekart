package com.sporekart.ai.policy.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PolicyAuditRepository extends JpaRepository<PolicyAuditEntity, UUID> {

    List<PolicyAuditEntity> findByPolicyIdAndIsDeletedFalse(UUID policyId);

    List<PolicyAuditEntity> findByRequestIdAndIsDeletedFalse(UUID requestId);

    List<PolicyAuditEntity> findByUserIdAndIsDeletedFalse(String userId);

    List<PolicyAuditEntity> findByDecisionAndIsDeletedFalse(String decision);

    List<PolicyAuditEntity> findByTimestampBetweenAndIsDeletedFalse(OffsetDateTime start, OffsetDateTime end);
}
