package com.sporekart.ai.assistant.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AssistantMetricsRepository extends JpaRepository<AssistantMetricsEntity, UUID> {

    List<AssistantMetricsEntity> findByAssistantIdAndIsDeletedFalse(UUID assistantId);

    List<AssistantMetricsEntity> findByRecordedAtBetweenAndIsDeletedFalse(OffsetDateTime start, OffsetDateTime end);
}
