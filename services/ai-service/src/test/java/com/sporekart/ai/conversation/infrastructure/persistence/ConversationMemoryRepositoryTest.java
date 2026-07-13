package com.sporekart.ai.conversation.infrastructure.persistence;

import com.sporekart.ai.conversation.domain.MemoryType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ConversationMemoryRepositoryTest {

    @Autowired
    private ConversationMemoryRepository repository;

    @Test
    void shouldSaveAndFindMemory() {
        var entity = new ConversationMemoryEntity();
        entity.setSessionId(UUID.randomUUID());
        entity.setMemoryType(MemoryType.SHORT_TERM);
        entity.setSummary("Test memory");
        entity.setRelevanceScore(0.8);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setDeleted(false);
        var saved = repository.save(entity);

        var found = repository.findByIdAndIsDeletedFalse(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("Test memory", found.get().getSummary());
    }

    @Test
    void shouldFindBySessionId() {
        var sessionId = UUID.randomUUID();
        var entity = new ConversationMemoryEntity();
        entity.setSessionId(sessionId);
        entity.setMemoryType(MemoryType.LONG_TERM);
        entity.setSummary("Memory");
        entity.setRelevanceScore(0.9);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setDeleted(false);
        repository.save(entity);

        var memories = repository.findBySessionIdAndIsDeletedFalseOrderByCreatedAtDesc(sessionId);

        assertFalse(memories.isEmpty());
    }

    @Test
    void shouldFindBySessionIdAndType() {
        var sessionId = UUID.randomUUID();
        var entity = new ConversationMemoryEntity();
        entity.setSessionId(sessionId);
        entity.setMemoryType(MemoryType.SHORT_TERM);
        entity.setSummary("Short-term memory");
        entity.setRelevanceScore(0.5);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setDeleted(false);
        repository.save(entity);

        var memories = repository.findBySessionIdAndMemoryTypeAndIsDeletedFalseOrderByCreatedAtDesc(
                sessionId, MemoryType.SHORT_TERM);

        assertFalse(memories.isEmpty());
        assertEquals("Short-term memory", memories.get(0).getSummary());
    }

    @Test
    void shouldRespectSoftDelete() {
        var entity = new ConversationMemoryEntity();
        entity.setSessionId(UUID.randomUUID());
        entity.setMemoryType(MemoryType.LONG_TERM);
        entity.setSummary("Deleted memory");
        entity.setRelevanceScore(0.0);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setDeleted(true);
        var saved = repository.save(entity);

        var found = repository.findByIdAndIsDeletedFalse(saved.getId());

        assertTrue(found.isEmpty());
    }
}
