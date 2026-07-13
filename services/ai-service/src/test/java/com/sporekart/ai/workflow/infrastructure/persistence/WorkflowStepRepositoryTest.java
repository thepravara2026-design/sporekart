package com.sporekart.ai.workflow.infrastructure.persistence;

import com.sporekart.ai.workflow.domain.WorkflowStepType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class WorkflowStepRepositoryTest {

    @Autowired
    private WorkflowStepRepository repository;

    @Test
    void shouldSaveAndFindStep() {
        var workflowId = UUID.randomUUID();
        var entity = new WorkflowStepEntity();
        entity.setWorkflowId(workflowId);
        entity.setName("Step 1");
        entity.setStepType(WorkflowStepType.AI_GATEWAY);
        entity.setOrderIndex(1);
        entity.setConfig("{}");
        entity.setOptional(false);
        entity.setTimeoutMs(5000);
        entity.setMaxRetries(3);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setDeleted(false);
        var saved = repository.save(entity);

        var steps = repository.findByWorkflowIdAndIsDeletedFalseOrderByOrderIndexAsc(workflowId);

        assertFalse(steps.isEmpty());
        assertEquals("Step 1", steps.get(0).getName());
        assertEquals(1, steps.get(0).getOrderIndex());
    }

    @Test
    void shouldFindStepsByWorkflowIdOrderedByOrderIndex() {
        var workflowId = UUID.randomUUID();
        var step1 = new WorkflowStepEntity();
        step1.setWorkflowId(workflowId);
        step1.setName("Step 1");
        step1.setStepType(WorkflowStepType.LOGGING);
        step1.setOrderIndex(2);
        step1.setCreatedAt(OffsetDateTime.now());
        step1.setDeleted(false);

        var step2 = new WorkflowStepEntity();
        step2.setWorkflowId(workflowId);
        step2.setName("Step 2");
        step2.setStepType(WorkflowStepType.NOTIFICATION);
        step2.setOrderIndex(1);
        step2.setCreatedAt(OffsetDateTime.now());
        step2.setDeleted(false);

        repository.save(step1);
        repository.save(step2);

        var steps = repository.findByWorkflowIdAndIsDeletedFalseOrderByOrderIndexAsc(workflowId);

        assertEquals(2, steps.size());
        assertEquals("Step 2", steps.get(0).getName());
        assertEquals("Step 1", steps.get(1).getName());
    }

    @Test
    void shouldReturnEmptyWhenNoStepsForWorkflow() {
        var steps = repository.findByWorkflowIdAndIsDeletedFalseOrderByOrderIndexAsc(UUID.randomUUID());

        assertTrue(steps.isEmpty());
    }

    @Test
    void shouldFindStepByIdAndWorkflowId() {
        var workflowId = UUID.randomUUID();
        var entity = new WorkflowStepEntity();
        entity.setWorkflowId(workflowId);
        entity.setName("Step 1");
        entity.setStepType(WorkflowStepType.DECISION);
        entity.setOrderIndex(1);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setDeleted(false);
        var saved = repository.save(entity);

        var found = repository.findByIdAndWorkflowIdAndIsDeletedFalse(saved.getId(), workflowId);

        assertTrue(found.isPresent());
        assertEquals("Step 1", found.get().getName());
    }

    @Test
    void shouldRespectSoftDeleteWhenFindingById() {
        var workflowId = UUID.randomUUID();
        var entity = new WorkflowStepEntity();
        entity.setWorkflowId(workflowId);
        entity.setName("Deleted Step");
        entity.setStepType(WorkflowStepType.LOOP);
        entity.setOrderIndex(1);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setDeleted(true);
        var saved = repository.save(entity);

        var found = repository.findByIdAndWorkflowIdAndIsDeletedFalse(saved.getId(), workflowId);

        assertTrue(found.isEmpty());
    }

    @Test
    void shouldRespectSoftDeleteInFindByWorkflowId() {
        var workflowId = UUID.randomUUID();
        var entity = new WorkflowStepEntity();
        entity.setWorkflowId(workflowId);
        entity.setName("Deleted Step");
        entity.setStepType(WorkflowStepType.RETRY);
        entity.setOrderIndex(1);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setDeleted(true);
        repository.save(entity);

        var steps = repository.findByWorkflowIdAndIsDeletedFalseOrderByOrderIndexAsc(workflowId);

        assertTrue(steps.isEmpty());
    }
}
