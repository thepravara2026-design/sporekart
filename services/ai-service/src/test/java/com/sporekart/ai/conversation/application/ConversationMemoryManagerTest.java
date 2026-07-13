package com.sporekart.ai.conversation.application;

import com.sporekart.ai.conversation.domain.MemoryType;
import com.sporekart.ai.conversation.infrastructure.persistence.ConversationMemoryEntity;
import com.sporekart.ai.conversation.infrastructure.persistence.ConversationMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConversationMemoryManagerTest {

    @Mock
    private ConversationMemoryRepository repository;

    private ConversationMemoryManager manager;

    @BeforeEach
    void setUp() {
        manager = new ConversationMemoryManager(repository);
    }

    @Test
    void shouldStoreMemory() {
        var sessionId = UUID.randomUUID();
        when(repository.save(any())).thenAnswer(invocation -> {
            var entity = invocation.<ConversationMemoryEntity>getArgument(0);
            entity.setId(UUID.randomUUID());
            return entity;
        });

        var memory = manager.storeMemory(sessionId, MemoryType.SHORT_TERM, "Test summary", "keyword1,keyword2", 0.9);

        assertNotNull(memory.id());
        assertEquals(sessionId, memory.sessionId());
        assertEquals(MemoryType.SHORT_TERM, memory.type());
        assertEquals("Test summary", memory.summary());
        assertEquals(0.9, memory.relevanceScore());
    }

    @Test
    void shouldGetMemory() {
        var id = UUID.randomUUID();
        var entity = new ConversationMemoryEntity();
        entity.setId(id);
        entity.setSessionId(UUID.randomUUID());
        entity.setMemoryType(MemoryType.LONG_TERM);
        entity.setSummary("Summary");
        entity.setRelevanceScore(0.8);
        entity.setCreatedAt(OffsetDateTime.now());
        when(repository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(entity));

        var result = manager.getMemory(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().id());
    }

    @Test
    void shouldGetSessionMemories() {
        var sessionId = UUID.randomUUID();
        var entity = new ConversationMemoryEntity();
        entity.setId(UUID.randomUUID());
        entity.setSessionId(sessionId);
        entity.setMemoryType(MemoryType.LONG_TERM);
        entity.setRelevanceScore(0.8);
        entity.setCreatedAt(OffsetDateTime.now());
        when(repository.findBySessionIdAndIsDeletedFalseOrderByCreatedAtDesc(sessionId))
                .thenReturn(List.of(entity));

        var memories = manager.getSessionMemories(sessionId);

        assertEquals(1, memories.size());
    }

    @Test
    void shouldGetSessionMemoriesByType() {
        var sessionId = UUID.randomUUID();
        when(repository.findBySessionIdAndMemoryTypeAndIsDeletedFalseOrderByCreatedAtDesc(
                sessionId, MemoryType.SHORT_TERM)).thenReturn(List.of());

        var memories = manager.getSessionMemoriesByType(sessionId, MemoryType.SHORT_TERM);

        assertTrue(memories.isEmpty());
    }

    @Test
    void shouldGetRelevantMemories() {
        var sessionId = UUID.randomUUID();
        var entity = new ConversationMemoryEntity();
        entity.setId(UUID.randomUUID());
        entity.setSessionId(sessionId);
        entity.setRelevanceScore(0.9);
        entity.setCreatedAt(OffsetDateTime.now());
        when(repository.findBySessionIdAndIsDeletedFalseOrderByCreatedAtDesc(sessionId))
                .thenReturn(List.of(entity));

        var memories = manager.getRelevantMemories(sessionId, "test", 5);

        assertFalse(memories.isEmpty());
    }

    @Test
    void shouldDeleteMemory() {
        var id = UUID.randomUUID();
        var entity = new ConversationMemoryEntity();
        entity.setId(id);
        entity.setDeleted(false);
        when(repository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(entity));

        manager.deleteMemory(id);

        assertTrue(entity.isDeleted());
        verify(repository).save(entity);
    }

    @Test
    void shouldDeleteSessionMemories() {
        var sessionId = UUID.randomUUID();
        var entity = new ConversationMemoryEntity();
        entity.setSessionId(sessionId);
        entity.setDeleted(false);
        when(repository.findBySessionIdAndIsDeletedFalseOrderByCreatedAtDesc(sessionId))
                .thenReturn(List.of(entity));

        manager.deleteSessionMemories(sessionId);

        assertTrue(entity.isDeleted());
        verify(repository).saveAll(anyList());
    }

    @Test
    void shouldUpdateRelevance() {
        var id = UUID.randomUUID();
        var entity = new ConversationMemoryEntity();
        entity.setId(id);
        entity.setRelevanceScore(0.5);
        when(repository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(entity));
        when(repository.save(any())).thenReturn(entity);

        var memory = manager.updateRelevance(id, 0.9);

        assertEquals(0.9, memory.relevanceScore());
    }
}
