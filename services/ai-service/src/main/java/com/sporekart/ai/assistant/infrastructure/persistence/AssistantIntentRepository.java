package com.sporekart.ai.assistant.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AssistantIntentRepository extends JpaRepository<AssistantIntentEntity, UUID> {

    List<AssistantIntentEntity> findBySessionIdAndIsDeletedFalse(UUID sessionId);

    List<AssistantIntentEntity> findByStatusAndIsDeletedFalse(String status);

    List<AssistantIntentEntity> findByCreatedAtBetweenAndIsDeletedFalse(OffsetDateTime start, OffsetDateTime end);
}
