package com.sporekart.ai.analytics.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GovernanceDashboardRepository extends JpaRepository<GovernanceDashboardEntity, UUID> {
    Optional<GovernanceDashboardEntity> findByName(String name);
}
