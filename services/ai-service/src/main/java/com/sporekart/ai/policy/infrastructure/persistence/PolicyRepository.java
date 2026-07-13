package com.sporekart.ai.policy.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PolicyRepository extends JpaRepository<PolicyEntity, UUID> {

    Optional<PolicyEntity> findByIdAndIsDeletedFalse(UUID id);

    List<PolicyEntity> findByIsDeletedFalse();

    List<PolicyEntity> findByTypeAndIsDeletedFalse(String type);

    List<PolicyEntity> findByScopeAndIsDeletedFalse(String scope);

    List<PolicyEntity> findByModuleAndIsDeletedFalse(String module);

    List<PolicyEntity> findByStatusAndIsDeletedFalse(String status);
}
