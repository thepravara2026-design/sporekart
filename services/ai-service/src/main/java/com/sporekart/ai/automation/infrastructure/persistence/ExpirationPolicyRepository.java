package com.sporekart.ai.automation.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface ExpirationPolicyRepository extends JpaRepository<ExpirationPolicyEntity, UUID> {
    List<ExpirationPolicyEntity> findByEntityType(String entityType);
    List<ExpirationPolicyEntity> findByEnabledTrue();
}
