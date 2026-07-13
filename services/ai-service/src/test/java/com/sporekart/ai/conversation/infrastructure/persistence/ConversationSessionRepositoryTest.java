package com.sporekart.ai.conversation.infrastructure.persistence;

import com.sporekart.ai.conversation.domain.ConversationStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ConversationSessionRepositoryTest {

    @Autowired
    private ConversationSessionRepository repository;

    @Test
    void shouldSaveAndFindSession() {
        var entity = new ConversationSessionEntity();
        entity.setUserId("user-1");
        entity.setTitle("Test");
        entity.setStatus(ConversationStatus.ACTIVE);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        entity.setDeleted(false);
        var saved = repository.save(entity);

        var found = repository.findByIdAndIsDeletedFalse(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("user-1", found.get().getUserId());
    }

    @Test
    void shouldFindByUserId() {
        var entity = new ConversationSessionEntity();
        entity.setUserId("user-1");
        entity.setStatus(ConversationStatus.ACTIVE);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        entity.setDeleted(false);
        repository.save(entity);

        var sessions = repository.findByUserIdAndIsDeletedFalse("user-1");

        assertFalse(sessions.isEmpty());
    }

    @Test
    void shouldRespectSoftDelete() {
        var entity = new ConversationSessionEntity();
        entity.setUserId("user-1");
        entity.setStatus(ConversationStatus.ACTIVE);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        entity.setDeleted(true);
        var saved = repository.save(entity);

        var found = repository.findByIdAndIsDeletedFalse(saved.getId());

        assertTrue(found.isEmpty());
    }

    @Test
    void shouldCountByUserId() {
        var entity = new ConversationSessionEntity();
        entity.setUserId("user-1");
        entity.setStatus(ConversationStatus.ACTIVE);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        entity.setDeleted(false);
        repository.save(entity);

        var count = repository.countByUserIdAndIsDeletedFalse("user-1");

        assertEquals(1, count);
    }
}
