package com.sporekart.ai.analytics.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReportScheduleRepository extends JpaRepository<ReportScheduleEntity, UUID> {
    List<ReportScheduleEntity> findByFrequency(String frequency);
    List<ReportScheduleEntity> findByActiveTrue();
}
