package com.sporekart.ai.conversation.application;

import com.sporekart.ai.conversation.domain.ConversationStatus;
import com.sporekart.ai.conversation.infrastructure.persistence.ConversationSessionEntity;
import com.sporekart.ai.conversation.infrastructure.persistence.ConversationSessionRepository;
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
class ConversationSessionManagerTest {

    @Mock
    private ConversationSessionRepository repository;

    private ConversationSessionManager manager;

    @BeforeEach
    void setUp() {
        manager = new ConversationSessionManager(repository);
    }

    @Test
    void shouldCreateSession() {
        when(repository.save(any())).thenAnswer(invocation -> {
            var entity = invocation.<ConversationSessionEntity>getArgument(0);
            entity.setId(UUID.randomUUID());
            return entity;
        });

        var session = manager.createSession("user-1", "Test Session");

        assertNotNull(session.id());
        assertEquals("user-1", session.userId());
        assertEquals("Test Session", session.title());
        assertEquals(ConversationStatus.ACTIVE, session.status());
        verify(repository).save(any());
    }

    @Test
    void shouldGetSession() {
        var id = UUID.randomUUID();
        var entity = new ConversationSessionEntity();
        entity.setId(id);
        entity.setUserId("user-1");
        entity.setStatus(ConversationStatus.ACTIVE);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        when(repository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(entity));

        var result = manager.getSession(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().id());
    }

    @Test
    void shouldReturnEmptyForNonExistentSession() {
        var id = UUID.randomUUID();
        when(repository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.empty());

        var result = manager.getSession(id);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldGetUserSessions() {
        var entity = new ConversationSessionEntity();
        entity.setId(UUID.randomUUID());
        entity.setUserId("user-1");
        entity.setStatus(ConversationStatus.ACTIVE);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        when(repository.findByUserIdAndIsDeletedFalse("user-1")).thenReturn(List.of(entity));

        var sessions = manager.getUserSessions("user-1");

        assertEquals(1, sessions.size());
        assertEquals("user-1", sessions.get(0).userId());
    }

    @Test
    void shouldUpdateStatus() {
        var id = UUID.randomUUID();
        var entity = new ConversationSessionEntity();
        entity.setId(id);
        entity.setUserId("user-1");
        entity.setStatus(ConversationStatus.ACTIVE);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        when(repository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(entity));
        when(repository.save(any())).thenReturn(entity);

        var session = manager.updateStatus(id, ConversationStatus.CLOSED);

        assertEquals(ConversationStatus.CLOSED, session.status());
    }

    @Test
    void shouldDeleteSession() {
        var id = UUID.randomUUID();
        var entity = new ConversationSessionEntity();
        entity.setId(id);
        entity.setDeleted(false);
        when(repository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(entity));

        manager.deleteSession(id);

        assertTrue(entity.isDeleted());
        verify(repository).save(entity);
    }

    @Test
    void shouldSuspendSession() {
        var id = UUID.randomUUID();
        var entity = new ConversationSessionEntity();
        entity.setId(id);
        entity.setStatus(ConversationStatus.ACTIVE);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        when(repository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(entity));
        when(repository.save(any())).thenReturn(entity);

        var session = manager.suspendSession(id);

        assertEquals(ConversationStatus.ARCHIVED, session.status());
    }

    @Test
    void shouldResumeSession() {
        var id = UUID.randomUUID();
        var entity = new ConversationSessionEntity();
        entity.setId(id);
        entity.setStatus(ConversationStatus.ARCHIVED);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        when(repository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(entity));
        when(repository.save(any())).thenReturn(entity);

        var session = manager.resumeSession(id);

        assertEquals(ConversationStatus.ACTIVE, session.status());
    }

    @Test
    void shouldCloseSession() {
        var id = UUID.randomUUID();
        var entity = new ConversationSessionEntity();
        entity.setId(id);
        entity.setStatus(ConversationStatus.ACTIVE);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        when(repository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(entity));
        when(repository.save(any())).thenReturn(entity);

        var session = manager.closeSession(id);

        assertEquals(ConversationStatus.CLOSED, session.status());
    }

    @Test
    void shouldCheckIfSessionIsActive() {
        var id = UUID.randomUUID();
        var entity = new ConversationSessionEntity();
        entity.setId(id);
        entity.setStatus(ConversationStatus.ACTIVE);
        when(repository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(entity));

        assertTrue(manager.isSessionActive(id));
    }

    @Test
    void shouldReturnFalseForInactiveSession() {
        var id = UUID.randomUUID();
        var entity = new ConversationSessionEntity();
        entity.setId(id);
        entity.setStatus(ConversationStatus.CLOSED);
        when(repository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(entity));

        assertFalse(manager.isSessionActive(id));
    }

    @Test
    void shouldThrowExceptionWhenSessionNotFoundForUpdate() {
        var id = UUID.randomUUID();
        when(repository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.empty());

        assertThrows(ConversationException.class, () -> manager.updateStatus(id, ConversationStatus.CLOSED));
    }

    @Test
    void shouldThrowExceptionWhenSessionNotFoundForDelete() {
        var id = UUID.randomUUID();
        when(repository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.empty());

        assertThrows(ConversationException.class, () -> manager.deleteSession(id));
    }
}
