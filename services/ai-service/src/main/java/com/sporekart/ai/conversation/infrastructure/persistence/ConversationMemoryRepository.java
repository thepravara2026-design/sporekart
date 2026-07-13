package com.sporekart.ai.conversation.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConversationMemoryRepository extends JpaRepository<ConversationMemoryEntity, UUID> {
    List<ConversationMemoryEntity> findBySessionIdAndIsDeletedFalseOrderByCreatedAtDesc(UUID sessionId);
    List<ConversationMemoryEntity> findBySessionIdAndMemoryTypeAndIsDeletedFalseOrderByCreatedAtDesc(UUID sessionId, com.sporekart.ai.conversation.domain.MemoryType memoryType);
    Optional<ConversationMemoryEntity> findByIdAndIsDeletedFalse(UUID id);
    void deleteBySessionIdAndIsDeletedFalse(UUID sessionId);
}
