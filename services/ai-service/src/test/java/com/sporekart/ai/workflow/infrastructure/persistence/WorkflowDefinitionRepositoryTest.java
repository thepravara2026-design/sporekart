package com.sporekart.ai.workflow.infrastructure.persistence;

import com.sporekart.ai.workflow.domain.WorkflowStatus;
import com.sporekart.ai.workflow.domain.WorkflowTriggerType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class WorkflowDefinitionRepositoryTest {

    @Autowired
    private WorkflowDefinitionRepository repository;

    @Test
    void shouldSaveAndFindDefinition() {
        var entity = new WorkflowDefinitionEntity();
        entity.setName("Test Workflow");
        entity.setDescription("Test description");
        entity.setCategory("general");
        entity.setStatus(WorkflowStatus.DRAFT);
        entity.setVersion("1.0.0");
        entity.setTriggerType(WorkflowTriggerType.REST_API);
        entity.setCreatedBy(UUID.randomUUID());
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        entity.setDeleted(false);
        var saved = repository.save(entity);

        var found = repository.findByIdAndIsDeletedFalse(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("Test Workflow", found.get().getName());
    }

    @Test
    void shouldFindByIsDeletedFalse() {
        var entity = new WorkflowDefinitionEntity();
        entity.setName("Test");
        entity.setStatus(WorkflowStatus.DRAFT);
        entity.setVersion("1.0.0");
        entity.setTriggerType(WorkflowTriggerType.REST_API);
        entity.setCreatedBy(UUID.randomUUID());
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        entity.setDeleted(false);
        repository.save(entity);

        var results = repository.findByIsDeletedFalse();

        assertFalse(results.isEmpty());
    }

    @Test
    void shouldRespectSoftDelete() {
        var entity = new WorkflowDefinitionEntity();
        entity.setName("Deleted");
        entity.setStatus(WorkflowStatus.DRAFT);
        entity.setVersion("1.0.0");
        entity.setTriggerType(WorkflowTriggerType.REST_API);
        entity.setCreatedBy(UUID.randomUUID());
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        entity.setDeleted(true);
        var saved = repository.save(entity);

        var found = repository.findByIdAndIsDeletedFalse(saved.getId());

        assertTrue(found.isEmpty());
    }

    @Test
    void shouldFindByStatusAndIsDeletedFalse() {
        var entity = new WorkflowDefinitionEntity();
        entity.setName("Active Workflow");
        entity.setStatus(WorkflowStatus.ACTIVE);
        entity.setVersion("1.0.0");
        entity.setTriggerType(WorkflowTriggerType.REST_API);
        entity.setCreatedBy(UUID.randomUUID());
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        entity.setDeleted(false);
        repository.save(entity);

        var results = repository.findByStatusAndIsDeletedFalse(WorkflowStatus.ACTIVE);

        assertFalse(results.isEmpty());
        assertEquals("Active Workflow", results.get(0).getName());
    }

    @Test
    void shouldFindByCreatedByAndIsDeletedFalse() {
        var createdBy = UUID.randomUUID();
        var entity = new WorkflowDefinitionEntity();
        entity.setName("My Workflow");
        entity.setStatus(WorkflowStatus.DRAFT);
        entity.setVersion("1.0.0");
        entity.setTriggerType(WorkflowTriggerType.REST_API);
        entity.setCreatedBy(createdBy);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        entity.setDeleted(false);
        repository.save(entity);

        var results = repository.findByCreatedByAndIsDeletedFalse(createdBy);

        assertFalse(results.isEmpty());
        assertEquals(createdBy, results.get(0).getCreatedBy());
    }
}
