package com.sporekart.ai.conversation.infrastructure.persistence;

import com.sporekart.ai.conversation.domain.MessageRole;
import com.sporekart.ai.conversation.domain.MessageStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ConversationMessageRepositoryTest {

    @Autowired
    private ConversationMessageRepository repository;

    @Test
    void shouldSaveAndFindMessage() {
        var entity = new ConversationMessageEntity();
        entity.setSessionId(UUID.randomUUID());
        entity.setRole(MessageRole.USER);
        entity.setContent("Hello");
        entity.setStatus(MessageStatus.SENT);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setDeleted(false);
        var saved = repository.save(entity);

        var found = repository.findByIdAndIsDeletedFalse(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("Hello", found.get().getContent());
    }

    @Test
    void shouldFindBySessionId() {
        var sessionId = UUID.randomUUID();
        var entity = new ConversationMessageEntity();
        entity.setSessionId(sessionId);
        entity.setRole(MessageRole.USER);
        entity.setContent("Hello");
        entity.setStatus(MessageStatus.SENT);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setDeleted(false);
        repository.save(entity);

        var messages = repository.findBySessionIdAndIsDeletedFalseOrderByCreatedAtAsc(sessionId);

        assertFalse(messages.isEmpty());
    }

    @Test
    void shouldRespectSoftDelete() {
        var entity = new ConversationMessageEntity();
        entity.setSessionId(UUID.randomUUID());
        entity.setRole(MessageRole.USER);
        entity.setContent("Hello");
        entity.setStatus(MessageStatus.SENT);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setDeleted(true);
        var saved = repository.save(entity);

        var found = repository.findByIdAndIsDeletedFalse(saved.getId());

        assertTrue(found.isEmpty());
    }

    @Test
    void shouldCountBySessionId() {
        var sessionId = UUID.randomUUID();
        var entity = new ConversationMessageEntity();
        entity.setSessionId(sessionId);
        entity.setRole(MessageRole.USER);
        entity.setContent("Hello");
        entity.setStatus(MessageStatus.SENT);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setDeleted(false);
        repository.save(entity);

        var count = repository.countBySessionIdAndIsDeletedFalse(sessionId);

        assertEquals(1, count);
    }
}
