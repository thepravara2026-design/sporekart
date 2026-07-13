package com.sporekart.ai.conversation.application;

import com.sporekart.ai.conversation.api.MemoryManager;
import com.sporekart.ai.conversation.domain.MemoryEntry;
import com.sporekart.ai.conversation.domain.MemoryType;
import com.sporekart.ai.conversation.infrastructure.persistence.ConversationMemoryEntity;
import com.sporekart.ai.conversation.infrastructure.persistence.ConversationMemoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConversationMemoryManager implements MemoryManager {

    private static final Logger log = LoggerFactory.getLogger(ConversationMemoryManager.class);

    private final ConversationMemoryRepository repository;

    public ConversationMemoryManager(ConversationMemoryRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public MemoryEntry storeMemory(UUID sessionId, MemoryType type, String summary, String keywords, double relevanceScore) {
        var entity = new ConversationMemoryEntity();
        entity.setSessionId(sessionId);
        entity.setMemoryType(type);
        entity.setSummary(summary);
        entity.setKeywords(keywords);
        entity.setRelevanceScore(relevanceScore);
        entity.setCreatedAt(OffsetDateTime.now());
        if (type == MemoryType.SHORT_TERM) {
            entity.setExpiresAt(OffsetDateTime.now().plusHours(24));
        }
        entity.setDeleted(false);
        var saved = repository.save(entity);
        log.info("Stored {} memory for session {}", type, sessionId);
        return toDomain(saved);
    }

    @Override
    public Optional<MemoryEntry> getMemory(UUID memoryId) {
        return repository.findByIdAndIsDeletedFalse(memoryId).map(this::toDomain);
    }

    @Override
    public List<MemoryEntry> getSessionMemories(UUID sessionId) {
        return repository.findBySessionIdAndIsDeletedFalseOrderByCreatedAtDesc(sessionId)
                .stream().map(this::toDomain).toList();
    }

    @Override
    public List<MemoryEntry> getSessionMemoriesByType(UUID sessionId, MemoryType type) {
        return repository.findBySessionIdAndMemoryTypeAndIsDeletedFalseOrderByCreatedAtDesc(
                sessionId, type).stream().map(this::toDomain).toList();
    }

    @Override
    public List<MemoryEntry> getRelevantMemories(UUID sessionId, String query, int limit) {
        return repository.findBySessionIdAndIsDeletedFalseOrderByCreatedAtDesc(sessionId)
                .stream().map(this::toDomain)
                .sorted(Comparator.comparingDouble(MemoryEntry::relevanceScore).reversed())
                .limit(limit)
                .toList();
    }

    @Override
    @Transactional
    public void deleteMemory(UUID memoryId) {
        var entity = repository.findByIdAndIsDeletedFalse(memoryId)
                .orElseThrow(() -> new ConversationException("Memory not found: " + memoryId));
        entity.setDeleted(true);
        repository.save(entity);
        log.info("Deleted memory {}", memoryId);
    }

    @Override
    @Transactional
    public void deleteSessionMemories(UUID sessionId) {
        var memories = repository.findBySessionIdAndIsDeletedFalseOrderByCreatedAtDesc(sessionId);
        memories.forEach(m -> m.setDeleted(true));
        repository.saveAll(memories);
        log.info("Deleted all memories for session {}", sessionId);
    }

    @Override
    @Transactional
    public MemoryEntry updateRelevance(UUID memoryId, double relevanceScore) {
        var entity = repository.findByIdAndIsDeletedFalse(memoryId)
                .orElseThrow(() -> new ConversationException("Memory not found: " + memoryId));
        entity.setRelevanceScore(relevanceScore);
        var saved = repository.save(entity);
        log.info("Updated memory {} relevance to {}", memoryId, relevanceScore);
        return toDomain(saved);
    }

    private MemoryEntry toDomain(ConversationMemoryEntity entity) {
        return new MemoryEntry(
                entity.getId(),
                entity.getSessionId(),
                entity.getMemoryType(),
                entity.getSummary(),
                entity.getKeywords(),
                entity.getRelevanceScore(),
                entity.getCreatedAt(),
                entity.getExpiresAt()
        );
    }
}
