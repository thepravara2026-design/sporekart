package com.sporekart.ai.conversation.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConversationSessionRepository extends JpaRepository<ConversationSessionEntity, UUID> {
    List<ConversationSessionEntity> findByUserIdAndIsDeletedFalse(String userId);
    Optional<ConversationSessionEntity> findByIdAndIsDeletedFalse(UUID id);
    List<ConversationSessionEntity> findByStatusAndIsDeletedFalse(String status);
    long countByUserIdAndIsDeletedFalse(String userId);
}
