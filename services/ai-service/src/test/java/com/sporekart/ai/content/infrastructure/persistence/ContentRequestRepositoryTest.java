package com.sporekart.ai.content.infrastructure.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ContentRequestRepositoryTest {

    @Autowired
    private ContentRequestRepository repository;

    @Test
    void shouldSaveAndFindById() {
        var entity = new ContentRequestEntity();
        entity.setUserId(UUID.randomUUID());
        entity.setContentType("TEXT");
        entity.setCategory("BLOG");
        entity.setPrompt("Test prompt");
        entity.setStatus("COMPLETED");
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setDeleted(false);
        var saved = repository.save(entity);

        var found = repository.findByIdAndIsDeletedFalse(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("Test prompt", found.get().getPrompt());
        assertEquals("COMPLETED", found.get().getStatus());
        assertFalse(found.get().isDeleted());
    }

    @Test
    void shouldFindByUserIdAndIsDeletedFalse() {
        var userId = UUID.randomUUID();
        var entity = new ContentRequestEntity();
        entity.setUserId(userId);
        entity.setContentType("TEXT");
        entity.setPrompt("User prompt");
        entity.setStatus("PENDING");
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setDeleted(false);
        repository.save(entity);

        var results = repository.findByUserIdAndIsDeletedFalse(userId);

        assertFalse(results.isEmpty());
        assertEquals(userId, results.get(0).getUserId());
    }

    @Test
    void shouldRespectSoftDelete() {
        var entity = new ContentRequestEntity();
        entity.setUserId(UUID.randomUUID());
        entity.setContentType("TEXT");
        entity.setPrompt("Deleted prompt");
        entity.setStatus("COMPLETED");
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setDeleted(true);
        var saved = repository.save(entity);

        var found = repository.findByIdAndIsDeletedFalse(saved.getId());

        assertTrue(found.isEmpty());
    }

    @Test
    void shouldFindByStatusAndIsDeletedFalse() {
        var entity = new ContentRequestEntity();
        entity.setUserId(UUID.randomUUID());
        entity.setContentType("TEXT");
        entity.setPrompt("Status test");
        entity.setStatus("COMPLETED");
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setDeleted(false);
        repository.save(entity);

        var results = repository.findByStatusAndIsDeletedFalse("COMPLETED");

        assertFalse(results.isEmpty());
        assertEquals("COMPLETED", results.get(0).getStatus());
    }

    @Test
    void shouldFindByUserIdOrderByCreatedAtDesc() {
        var userId = UUID.randomUUID();
        var entity1 = new ContentRequestEntity();
        entity1.setUserId(userId);
        entity1.setContentType("TEXT");
        entity1.setPrompt("First");
        entity1.setStatus("COMPLETED");
        entity1.setCreatedAt(OffsetDateTime.now().minusHours(1));
        entity1.setDeleted(false);
        repository.save(entity1);

        var entity2 = new ContentRequestEntity();
        entity2.setUserId(userId);
        entity2.setContentType("TEXT");
        entity2.setPrompt("Second");
        entity2.setStatus("PENDING");
        entity2.setCreatedAt(OffsetDateTime.now());
        entity2.setDeleted(false);
        repository.save(entity2);

        var results = repository.findByUserIdOrderByCreatedAtDesc(userId);

        assertEquals(2, results.size());
        assertEquals("Second", results.get(0).getPrompt());
        assertEquals("First", results.get(1).getPrompt());
    }
}
