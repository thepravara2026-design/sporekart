package com.sporekart.ai.conversation.application;

import com.sporekart.ai.conversation.domain.MessageRole;
import com.sporekart.ai.conversation.domain.MessageStatus;
import com.sporekart.ai.conversation.infrastructure.persistence.ConversationContextEntity;
import com.sporekart.ai.conversation.infrastructure.persistence.ConversationContextRepository;
import com.sporekart.ai.conversation.infrastructure.persistence.ConversationMessageEntity;
import com.sporekart.ai.conversation.infrastructure.persistence.ConversationMessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConversationContextBuilderTest {

    @Mock
    private ConversationContextRepository contextRepository;

    @Mock
    private ConversationMessageRepository messageRepository;

    private ConversationContextBuilder builder;

    @BeforeEach
    void setUp() {
        builder = new ConversationContextBuilder(contextRepository, messageRepository);
    }

    @Test
    void shouldBuildContextWithStoredEntriesAndMessages() {
        var sessionId = UUID.randomUUID();
        var ctxEntity = new ConversationContextEntity();
        ctxEntity.setSessionId(sessionId);
        ctxEntity.setSource("knowledge_base");
        ctxEntity.setContent("Some knowledge");
        ctxEntity.setWeight(1.0);
        when(contextRepository.findBySessionIdAndIsDeletedFalse(sessionId)).thenReturn(List.of(ctxEntity));

        var msgEntity = new ConversationMessageEntity();
        msgEntity.setSessionId(sessionId);
        msgEntity.setRole(MessageRole.USER);
        msgEntity.setContent("Hello");
        msgEntity.setStatus(MessageStatus.SENT);
        msgEntity.setCreatedAt(OffsetDateTime.now());
        when(messageRepository.findBySessionIdAndIsDeletedFalseOrderByCreatedAtAsc(sessionId))
                .thenReturn(List.of(msgEntity));

        var context = builder.buildContext(sessionId);

        assertFalse(context.isEmpty());
        assertTrue(context.stream().anyMatch(e -> e.source().equals("knowledge_base")));
        assertTrue(context.stream().anyMatch(e -> e.source().equals("conversation_history")));
    }

    @Test
    void shouldBuildContextWithUserQuery() {
        var sessionId = UUID.randomUUID();
        when(contextRepository.findBySessionIdAndIsDeletedFalse(sessionId)).thenReturn(List.of());
        when(messageRepository.findBySessionIdAndIsDeletedFalseOrderByCreatedAtAsc(sessionId))
                .thenReturn(List.of());

        var context = builder.buildContext(sessionId, "test query");

        assertTrue(context.stream().anyMatch(e -> e.source().equals("user_query")));
    }

    @Test
    void shouldBuildContextString() {
        var sessionId = UUID.randomUUID();
        when(contextRepository.findBySessionIdAndIsDeletedFalse(sessionId)).thenReturn(List.of());
        when(messageRepository.findBySessionIdAndIsDeletedFalseOrderByCreatedAtAsc(sessionId))
                .thenReturn(List.of());

        var result = builder.buildContextString(sessionId);

        assertEquals("", result);
    }

    @Test
    void shouldBuildContextStringWithQuery() {
        var sessionId = UUID.randomUUID();
        when(contextRepository.findBySessionIdAndIsDeletedFalse(sessionId)).thenReturn(List.of());
        when(messageRepository.findBySessionIdAndIsDeletedFalseOrderByCreatedAtAsc(sessionId))
                .thenReturn(List.of());

        var result = builder.buildContextString(sessionId, "test");

        assertTrue(result.contains("test"));
    }

    @Test
    void shouldGetContextSources() {
        var sessionId = UUID.randomUUID();
        var ctxEntity = new ConversationContextEntity();
        ctxEntity.setSource("knowledge");
        ctxEntity.setWeight(1.0);
        ctxEntity.setSessionId(sessionId);
        when(contextRepository.findBySessionIdAndIsDeletedFalse(sessionId)).thenReturn(List.of(ctxEntity));

        var sources = builder.getContextSources(sessionId);

        assertTrue(sources.containsKey("knowledge"));
    }

    @Test
    void shouldRefreshContext() {
        var sessionId = UUID.randomUUID();

        builder.refreshContext(sessionId);

        verify(contextRepository).deleteBySessionIdAndIsDeletedFalse(sessionId);
    }
}
