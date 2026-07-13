package com.sporekart.ai.policy.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PolicyVersionRepository extends JpaRepository<PolicyVersionEntity, UUID> {

    List<PolicyVersionEntity> findByPolicyIdAndIsDeletedFalse(UUID policyId);

    List<PolicyVersionEntity> findByPolicyIdOrderByVersionNumberDesc(UUID policyId);
}
