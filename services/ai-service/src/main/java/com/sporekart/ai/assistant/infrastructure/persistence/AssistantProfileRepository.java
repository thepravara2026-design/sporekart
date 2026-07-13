package com.sporekart.ai.assistant.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AssistantProfileRepository extends JpaRepository<AssistantProfileEntity, UUID> {

    List<AssistantProfileEntity> findByAssistantIdAndIsDeletedFalse(UUID assistantId);

    List<AssistantProfileEntity> findByCreatedAtBetweenAndIsDeletedFalse(OffsetDateTime start, OffsetDateTime end);
}
