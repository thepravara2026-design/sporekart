package com.sporekart.ai.conversation.infrastructure.persistence;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConversationMessageRepository extends JpaRepository<ConversationMessageEntity, UUID> {
    List<ConversationMessageEntity> findBySessionIdAndIsDeletedFalseOrderByCreatedAtAsc(UUID sessionId);
    List<ConversationMessageEntity> findBySessionIdAndIsDeletedFalseOrderByCreatedAtAsc(UUID sessionId, Pageable pageable);
    Optional<ConversationMessageEntity> findByIdAndIsDeletedFalse(UUID id);
    long countBySessionIdAndIsDeletedFalse(UUID sessionId);
    void deleteBySessionIdAndIsDeletedFalse(UUID sessionId);
}
