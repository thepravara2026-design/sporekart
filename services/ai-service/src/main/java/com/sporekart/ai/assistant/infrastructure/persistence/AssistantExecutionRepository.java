package com.sporekart.ai.assistant.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AssistantExecutionRepository extends JpaRepository<AssistantExecutionEntity, UUID> {

    List<AssistantExecutionEntity> findByAssistantIdAndIsDeletedFalse(UUID assistantId);

    List<AssistantExecutionEntity> findBySessionIdAndIsDeletedFalse(UUID sessionId);

    List<AssistantExecutionEntity> findByCreatedAtBetweenAndIsDeletedFalse(OffsetDateTime start, OffsetDateTime end);
}
