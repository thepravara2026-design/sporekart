package com.sporekart.ai.analytics.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface GovernanceReportRepository extends JpaRepository<GovernanceReportEntity, UUID> {
    List<GovernanceReportEntity> findByType(String type);
    List<GovernanceReportEntity> findByGeneratedAtBetween(LocalDateTime from, LocalDateTime to);
}
