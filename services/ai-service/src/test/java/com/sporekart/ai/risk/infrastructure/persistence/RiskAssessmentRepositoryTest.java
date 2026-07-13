package com.sporekart.ai.risk.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.UUID;

@DataJpaTest
@ActiveProfiles("test")
class RiskAssessmentRepositoryTest {

    @Autowired
    private RiskAssessmentRepository repository;

    @Test
    void saveShouldPersistEntity() {
        var entity = new RiskAssessmentEntity();
        entity.setId(UUID.randomUUID());
        entity.setModule("prompt");
        entity.setAction("generate");
        entity.setStatus("PENDING");
        entity.setContext("{\"key\":\"value\"}");

        var saved = repository.save(entity);

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals("prompt", saved.getModule());
        assertEquals("generate", saved.getAction());
        assertEquals("PENDING", saved.getStatus());
    }

    @Test
    void findByModuleShouldReturnMatchingEntities() {
        var module = "search";
        var entity = new RiskAssessmentEntity();
        entity.setId(UUID.randomUUID());
        entity.setModule(module);
        entity.setAction("query");
        entity.setStatus("COMPLETED");
        entity.setContext("{}");

        repository.save(entity);

        var results = repository.findByModule(module);

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(module, results.get(0).getModule());
    }

    @Test
    void findByModuleShouldReturnEmptyWhenNoneMatch() {
        var results = repository.findByModule("nonexistent");
        assertTrue(results.isEmpty());
    }

    @Test
    void findByStatusShouldReturnMatchingEntities() {
        var entity = new RiskAssessmentEntity();
        entity.setId(UUID.randomUUID());
        entity.setModule("test");
        entity.setAction("run");
        entity.setStatus("PENDING");
        entity.setContext("{}");

        repository.save(entity);

        var results = repository.findByStatus("PENDING");

        assertFalse(results.isEmpty());
        assertEquals("PENDING", results.get(0).getStatus());
    }

    @Test
    void findByIdShouldReturnEntity() {
        var entity = new RiskAssessmentEntity();
        entity.setId(UUID.randomUUID());
        entity.setModule("test");
        entity.setAction("run");
        entity.setStatus("PENDING");
        entity.setContext("{}");

        repository.save(entity);

        var found = repository.findById(entity.getId());

        assertTrue(found.isPresent());
        assertEquals(entity.getId(), found.get().getId());
    }

    @Test
    void findByModuleAndStatusShouldFilter() {
        var module = "prompt";
        var entity = new RiskAssessmentEntity();
        entity.setId(UUID.randomUUID());
        entity.setModule(module);
        entity.setAction("generate");
        entity.setStatus("COMPLETED");
        entity.setContext("{}");

        repository.save(entity);

        var results = repository.findByModuleAndStatus(module, "COMPLETED");

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
    }
}
