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
class SemanticEmbeddingRepositoryTest {

    @Autowired
    private SemanticEmbeddingRepository repository;

    private SemanticEmbeddingEntity createEntity(String content, String provider, String status) {
        SemanticEmbeddingEntity entity = new SemanticEmbeddingEntity();
        entity.setContent(content);
        entity.setEmbedding("[0.1,0.2,0.3]");
        entity.setProvider(provider);
        entity.setModel("text-embedding-3-small");
        entity.setDimensions(1536);
        entity.setStatus(status);
        entity.setVersion(1);
        entity.setCreatedAt(OffsetDateTime.now());
        return repository.save(entity);
    }

    @Test
    void testSaveAndFindById() {
        SemanticEmbeddingEntity saved = createEntity("test content", "OPENAI", "COMPLETED");
        Optional<SemanticEmbeddingEntity> found = repository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("test content", found.get().getContent());
    }

    @Test
    void testFindByIsDeletedFalse() {
        createEntity("active", "OPENAI", "COMPLETED");
        SemanticEmbeddingEntity deleted = createEntity("deleted", "OPENAI", "COMPLETED");
        deleted.setDeleted(true);
        deleted.setDeletedAt(OffsetDateTime.now());
        repository.save(deleted);

        List<SemanticEmbeddingEntity> active = repository.findByIsDeletedFalse();
        assertFalse(active.isEmpty());
        assertTrue(active.stream().noneMatch(SemanticEmbeddingEntity::isDeleted));
    }

    @Test
    void testSoftDelete() {
        SemanticEmbeddingEntity entity = createEntity("delete me", "OPENAI", "COMPLETED");
        entity.setDeleted(true);
        entity.setDeletedAt(OffsetDateTime.now());
        repository.save(entity);

        assertFalse(repository.findByIdAndIsDeletedFalse(entity.getId()).isPresent());
    }

    @Test
    void testFindByStatus() {
        createEntity("pending", "OPENAI", "PENDING");
        createEntity("completed", "OPENAI", "COMPLETED");

        List<SemanticEmbeddingEntity> completed = repository.findByStatusAndIsDeletedFalse("COMPLETED");
        assertFalse(completed.isEmpty());
        assertTrue(completed.stream().allMatch(e -> "COMPLETED".equals(e.getStatus())));
    }

    @Test
    void testFindByProvider() {
        createEntity("openai doc", "OPENAI", "COMPLETED");
        createEntity("gemini doc", "GEMINI", "COMPLETED");

        List<SemanticEmbeddingEntity> openaiDocs = repository.findByProviderAndIsDeletedFalse("OPENAI");
        assertFalse(openaiDocs.isEmpty());
        assertTrue(openaiDocs.stream().allMatch(e -> "OPENAI".equals(e.getProvider())));
    }

    @Test
    void testFindByContent() {
        createEntity("unique content", "OPENAI", "COMPLETED");
        Optional<SemanticEmbeddingEntity> found = repository.findByContentAndIsDeletedFalse("unique content");
        assertTrue(found.isPresent());
    }

    @Test
    void testFindByProviderAndModel() {
        createEntity("test", "OPENAI", "COMPLETED");
        List<SemanticEmbeddingEntity> results = repository.findByProviderAndModelAndIsDeletedFalse("OPENAI", "text-embedding-3-small");
        assertFalse(results.isEmpty());
    }
}
