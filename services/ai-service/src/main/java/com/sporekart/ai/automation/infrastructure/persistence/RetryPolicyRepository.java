package com.sporekart.ai.automation.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RetryPolicyRepository extends JpaRepository<RetryPolicyEntity, UUID> {
    Optional<RetryPolicyEntity> findByName(String name);
}
