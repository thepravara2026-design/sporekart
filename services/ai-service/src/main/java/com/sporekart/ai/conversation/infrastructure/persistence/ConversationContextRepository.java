package com.sporekart.ai.conversation.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ConversationContextRepository extends JpaRepository<ConversationContextEntity, UUID> {
    List<ConversationContextEntity> findBySessionIdAndIsDeletedFalse(UUID sessionId);
    void deleteBySessionIdAndIsDeletedFalse(UUID sessionId);
    List<ConversationContextEntity> findBySessionIdAndSourceAndIsDeletedFalse(UUID sessionId, String source);
}
