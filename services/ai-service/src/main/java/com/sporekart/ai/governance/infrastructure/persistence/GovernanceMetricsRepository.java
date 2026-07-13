package com.sporekart.ai.governance.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GovernanceMetricsRepository extends JpaRepository<GovernanceMetricsEntity, UUID> {
}
