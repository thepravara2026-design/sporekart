package com.sporekart.ai.assistant.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AssistantSessionRepository extends JpaRepository<AssistantSessionEntity, UUID> {

    List<AssistantSessionEntity> findByAssistantIdAndIsDeletedFalse(UUID assistantId);

    List<AssistantSessionEntity> findByUserIdAndIsDeletedFalse(UUID userId);

    List<AssistantSessionEntity> findByStatusAndIsDeletedFalse(String status);

    List<AssistantSessionEntity> findByCreatedAtBetweenAndIsDeletedFalse(OffsetDateTime start, OffsetDateTime end);
}
