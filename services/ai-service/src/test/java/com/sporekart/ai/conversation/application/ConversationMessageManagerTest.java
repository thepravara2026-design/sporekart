package com.sporekart.ai.conversation.application;

import com.sporekart.ai.conversation.domain.MessageRole;
import com.sporekart.ai.conversation.domain.MessageStatus;
import com.sporekart.ai.conversation.domain.ConversationStatus;
import com.sporekart.ai.conversation.infrastructure.persistence.ConversationMessageEntity;
import com.sporekart.ai.conversation.infrastructure.persistence.ConversationMessageRepository;
import com.sporekart.ai.conversation.infrastructure.persistence.ConversationSessionEntity;
import com.sporekart.ai.conversation.infrastructure.persistence.ConversationSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConversationMessageManagerTest {

    @Mock
    private ConversationMessageRepository messageRepository;

    @Mock
    private ConversationSessionRepository sessionRepository;

    private ConversationMessageManager manager;

    @BeforeEach
    void setUp() {
        manager = new ConversationMessageManager(messageRepository, sessionRepository);
    }

    @Test
    void shouldSendMessage() {
        var sessionId = UUID.randomUUID();
        var sessionEntity = new ConversationSessionEntity();
        sessionEntity.setId(sessionId);
        sessionEntity.setStatus(ConversationStatus.ACTIVE);
        when(sessionRepository.findByIdAndIsDeletedFalse(sessionId)).thenReturn(Optional.of(sessionEntity));
        when(messageRepository.save(any())).thenAnswer(invocation -> {
            var entity = invocation.<ConversationMessageEntity>getArgument(0);
            entity.setId(UUID.randomUUID());
            return entity;
        });

        var message = manager.sendMessage(sessionId, MessageRole.USER, "Hello");

        assertNotNull(message.id());
        assertEquals(sessionId, message.sessionId());
        assertEquals(MessageRole.USER, message.role());
        assertEquals("Hello", message.content());
        assertEquals(MessageStatus.SENT, message.status());
    }

    @Test
    void shouldThrowWhenSessionNotFound() {
        var sessionId = UUID.randomUUID();
        when(sessionRepository.findByIdAndIsDeletedFalse(sessionId)).thenReturn(Optional.empty());

        assertThrows(ConversationException.class,
                () -> manager.sendMessage(sessionId, MessageRole.USER, "Hello"));
    }

    @Test
    void shouldGetMessage() {
        var id = UUID.randomUUID();
        var entity = new ConversationMessageEntity();
        entity.setId(id);
        entity.setSessionId(UUID.randomUUID());
        entity.setRole(MessageRole.USER);
        entity.setContent("Hello");
        entity.setStatus(MessageStatus.SENT);
        entity.setCreatedAt(OffsetDateTime.now());
        when(messageRepository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(entity));

        var result = manager.getMessage(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().id());
    }

    @Test
    void shouldGetSessionMessages() {
        var sessionId = UUID.randomUUID();
        var entity = new ConversationMessageEntity();
        entity.setId(UUID.randomUUID());
        entity.setSessionId(sessionId);
        entity.setRole(MessageRole.USER);
        entity.setContent("Hello");
        entity.setStatus(MessageStatus.SENT);
        entity.setCreatedAt(OffsetDateTime.now());
        when(messageRepository.findBySessionIdAndIsDeletedFalseOrderByCreatedAtAsc(sessionId))
                .thenReturn(List.of(entity));

        var messages = manager.getSessionMessages(sessionId);

        assertEquals(1, messages.size());
    }

    @Test
    void shouldGetSessionMessagesWithPagination() {
        var sessionId = UUID.randomUUID();
        when(messageRepository.findBySessionIdAndIsDeletedFalseOrderByCreatedAtAsc(eq(sessionId), any(PageRequest.class)))
                .thenReturn(List.of());

        var messages = manager.getSessionMessages(sessionId, 10, 0);

        assertTrue(messages.isEmpty());
    }

    @Test
    void shouldUpdateMessageStatus() {
        var id = UUID.randomUUID();
        var entity = new ConversationMessageEntity();
        entity.setId(id);
        entity.setStatus(MessageStatus.SENT);
        when(messageRepository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(entity));
        when(messageRepository.save(any())).thenReturn(entity);

        var message = manager.updateStatus(id, "DELIVERED");

        assertEquals(MessageStatus.DELIVERED, message.status());
    }

    @Test
    void shouldDeleteMessage() {
        var id = UUID.randomUUID();
        var entity = new ConversationMessageEntity();
        entity.setId(id);
        entity.setDeleted(false);
        when(messageRepository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(entity));

        manager.deleteMessage(id);

        assertTrue(entity.isDeleted());
        verify(messageRepository).save(entity);
    }

    @Test
    void shouldDeleteSessionMessages() {
        var sessionId = UUID.randomUUID();
        var entity = new ConversationMessageEntity();
        entity.setSessionId(sessionId);
        entity.setDeleted(false);
        when(messageRepository.findBySessionIdAndIsDeletedFalseOrderByCreatedAtAsc(sessionId))
                .thenReturn(List.of(entity));

        manager.deleteSessionMessages(sessionId);

        assertTrue(entity.isDeleted());
        verify(messageRepository).saveAll(anyList());
    }

    @Test
    void shouldGetMessageCount() {
        var sessionId = UUID.randomUUID();
        when(messageRepository.countBySessionIdAndIsDeletedFalse(sessionId)).thenReturn(5L);

        var count = manager.getMessageCount(sessionId);

        assertEquals(5, count);
    }
}
