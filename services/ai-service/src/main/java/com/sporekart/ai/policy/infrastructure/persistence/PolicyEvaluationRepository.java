package com.sporekart.ai.policy.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PolicyEvaluationRepository extends JpaRepository<PolicyEvaluationEntity, UUID> {

    List<PolicyEvaluationEntity> findByRequestIdAndIsDeletedFalse(UUID requestId);

    List<PolicyEvaluationEntity> findByPolicyIdAndIsDeletedFalse(UUID policyId);

    List<PolicyEvaluationEntity> findByDecisionAndIsDeletedFalse(String decision);
}
