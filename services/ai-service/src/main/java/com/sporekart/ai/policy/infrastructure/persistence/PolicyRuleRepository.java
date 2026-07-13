package com.sporekart.ai.policy.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PolicyRuleRepository extends JpaRepository<PolicyRuleEntity, UUID> {

    List<PolicyRuleEntity> findByPolicyIdAndIsDeletedFalse(UUID policyId);

    List<PolicyRuleEntity> findByIsDeletedFalse();
}
