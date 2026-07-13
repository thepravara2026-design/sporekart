package com.sporekart.ai.usagetracking.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DailyUsageSummaryRepository extends JpaRepository<DailyUsageSummaryEntity, Long> {

    List<DailyUsageSummaryEntity> findByUsageDate(String usageDate);

    List<DailyUsageSummaryEntity> findByProviderId(String providerId);

    List<DailyUsageSummaryEntity> findByModelId(String modelId);
}
