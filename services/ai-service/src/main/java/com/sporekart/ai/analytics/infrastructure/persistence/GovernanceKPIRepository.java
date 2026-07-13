package com.sporekart.ai.analytics.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface GovernanceKPIRepository extends JpaRepository<GovernanceKPIEntity, UUID> {
    List<GovernanceKPIEntity> findByModule(String module);
    List<GovernanceKPIEntity> findByStatus(String status);
    List<GovernanceKPIEntity> findByName(String name);
}
