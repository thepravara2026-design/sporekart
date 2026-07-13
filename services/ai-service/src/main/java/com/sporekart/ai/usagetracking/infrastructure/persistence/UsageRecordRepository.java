package com.sporekart.ai.usagetracking.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface UsageRecordRepository extends JpaRepository<UsageRecordEntity, String> {

    List<UsageRecordEntity> findByProviderId(String providerId);

    List<UsageRecordEntity> findByModelId(String modelId);

    List<UsageRecordEntity> findByTimestampBetween(Instant start, Instant end);

    long countBySuccess(boolean success);
}
