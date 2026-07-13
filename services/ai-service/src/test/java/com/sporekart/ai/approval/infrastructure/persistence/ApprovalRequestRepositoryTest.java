package com.sporekart.ai.approval.infrastructure.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ApprovalRequestRepositoryTest {

    @Autowired
    private ApprovalRequestRepository repository;

    @Test
    void shouldSaveAndFindById() {
        var entity = new ApprovalRequestEntity();
        entity.setModule("content");
        entity.setAction("publish");
        entity.setUserId("user1");
        entity.setStatus("PENDING");
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        entity.setIsDeleted(false);
        var saved = repository.save(entity);

        var found = repository.findByIdAndIsDeletedFalse(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("content", found.get().getModule());
        assertEquals("publish", found.get().getAction());
        assertEquals("user1", found.get().getUserId());
        assertEquals("PENDING", found.get().getStatus());
    }

    @Test
    void shouldRespectSoftDelete() {
        var entity = new ApprovalRequestEntity();
        entity.setModule("content");
        entity.setAction("publish");
        entity.setUserId("user1");
        entity.setStatus("PENDING");
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        entity.setIsDeleted(false);
        var saved = repository.save(entity);

        saved.setIsDeleted(true);
        repository.save(saved);

        var found = repository.findByIdAndIsDeletedFalse(saved.getId());
        assertTrue(found.isEmpty());
    }
}
