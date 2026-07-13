package com.sporekart.ai.semantic.infrastructure.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class SemanticVectorIndexRepositoryTest {

    @Autowired
    private SemanticVectorIndexRepository repository;

    private SemanticVectorIndexEntity createIndex(String name, String status) {
        SemanticVectorIndexEntity entity = new SemanticVectorIndexEntity();
        entity.setName(name);
        entity.setDescription("Test index");
        entity.setStatus(status);
        entity.setVectorCount(0);
        entity.setDimensions(768);
        entity.setCreatedAt(OffsetDateTime.now());
        return repository.save(entity);
    }

    @Test
    void testSaveAndFindById() {
        SemanticVectorIndexEntity saved = createIndex("test-idx", "ACTIVE");
        Optional<SemanticVectorIndexEntity> found = repository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("test-idx", found.get().getName());
    }

    @Test
    void testFindByName() {
        createIndex("unique-idx", "ACTIVE");
        Optional<SemanticVectorIndexEntity> found = repository.findByNameAndIsDeletedFalse("unique-idx");
        assertTrue(found.isPresent());
    }

    @Test
    void testFindByIsDeletedFalse() {
        createIndex("active", "ACTIVE");
        SemanticVectorIndexEntity deleted = createIndex("deleted", "ACTIVE");
        deleted.setDeleted(true);
        deleted.setDeletedAt(OffsetDateTime.now());
        repository.save(deleted);

        List<SemanticVectorIndexEntity> active = repository.findByIsDeletedFalse();
        assertFalse(active.isEmpty());
        assertTrue(active.stream().noneMatch(SemanticVectorIndexEntity::isDeleted));
    }

    @Test
    void testSoftDelete() {
        SemanticVectorIndexEntity entity = createIndex("delete-me", "ACTIVE");
        entity.setDeleted(true);
        entity.setDeletedAt(OffsetDateTime.now());
        repository.save(entity);

        assertFalse(repository.findByIdAndIsDeletedFalse(entity.getId()).isPresent());
    }

    @Test
    void testFindByStatus() {
        createIndex("creating", "CREATING");
        createIndex("active", "ACTIVE");

        List<SemanticVectorIndexEntity> activeIndexes = repository.findByStatusAndIsDeletedFalse("ACTIVE");
        assertFalse(activeIndexes.isEmpty());
    }
}
