package com.sporekart.ai.assistant.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AssistantTaskRepository extends JpaRepository<AssistantTaskEntity, UUID> {

    List<AssistantTaskEntity> findBySessionIdAndIsDeletedFalse(UUID sessionId);

    List<AssistantTaskEntity> findByStatusAndIsDeletedFalse(String status);

    List<AssistantTaskEntity> findByCreatedAtBetweenAndIsDeletedFalse(OffsetDateTime start, OffsetDateTime end);
}
