package com.sporekart.ai.assistant.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AssistantFeedbackRepository extends JpaRepository<AssistantFeedbackEntity, UUID> {

    List<AssistantFeedbackEntity> findBySessionIdAndIsDeletedFalse(UUID sessionId);

    List<AssistantFeedbackEntity> findByUserIdAndIsDeletedFalse(UUID userId);

    List<AssistantFeedbackEntity> findByCreatedAtBetweenAndIsDeletedFalse(OffsetDateTime start, OffsetDateTime end);
}
