package com.sporekart.ai.workflow.application;

import com.sporekart.ai.workflow.domain.*;
import com.sporekart.ai.workflow.infrastructure.kafka.WorkflowKafkaEventPublisher;
import com.sporekart.ai.workflow.infrastructure.persistence.WorkflowDefinitionEntity;
import com.sporekart.ai.workflow.infrastructure.persistence.WorkflowDefinitionRepository;
import com.sporekart.ai.workflow.infrastructure.persistence.WorkflowStepEntity;
import com.sporekart.ai.workflow.infrastructure.persistence.WorkflowStepRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkflowServiceImplTest {

    @Mock
    private WorkflowDefinitionRepository repository;

    @Mock
    private WorkflowStepRepository stepRepository;

    @Mock
    private WorkflowKafkaEventPublisher eventPublisher;

    private WorkflowServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new WorkflowServiceImpl(repository, stepRepository, eventPublisher);
    }

    @Test
    void shouldCreateDefinition() {
        var name = "Test Workflow";
        var description = "Test description";
        var category = "general";
        var triggerType = WorkflowTriggerType.REST_API;
        var triggerConfig = "{\"endpoint\":\"/test\"}";
        var createdBy = UUID.randomUUID();

        when(repository.save(any())).thenAnswer(invocation -> {
            var entity = invocation.<WorkflowDefinitionEntity>getArgument(0);
            entity.setId(UUID.randomUUID());
            return entity;
        });

        var def = service.createDefinition(name, description, category, triggerType, triggerConfig, createdBy);

        assertNotNull(def.id());
        assertEquals(name, def.name());
        assertEquals(description, def.description());
        assertEquals(category, def.category());
        assertEquals(triggerType, def.triggerType());
        assertEquals(WorkflowStatus.DRAFT, def.status());
        verify(repository).save(any());
        verify(eventPublisher).publishWorkflowCreated(any(), eq(name), eq(createdBy));
    }

    @Test
    void shouldGetDefinition() {
        var id = UUID.randomUUID();
        var entity = new WorkflowDefinitionEntity();
        entity.setId(id);
        entity.setName("Test");
        entity.setStatus(WorkflowStatus.DRAFT);
        entity.setVersion("1.0.0");
        entity.setTriggerType(WorkflowTriggerType.REST_API);
        entity.setCreatedBy(UUID.randomUUID());
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        when(repository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(entity));

        var result = service.getDefinition(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().id());
    }

    @Test
    void shouldReturnEmptyForNonExistentDefinition() {
        var id = UUID.randomUUID();
        when(repository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.empty());

        var result = service.getDefinition(id);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldListDefinitions() {
        var entity = new WorkflowDefinitionEntity();
        entity.setId(UUID.randomUUID());
        entity.setName("Test");
        entity.setStatus(WorkflowStatus.DRAFT);
        entity.setVersion("1.0.0");
        entity.setTriggerType(WorkflowTriggerType.REST_API);
        entity.setCreatedBy(UUID.randomUUID());
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        when(repository.findByIsDeletedFalse()).thenReturn(List.of(entity));

        var defs = service.listDefinitions();

        assertEquals(1, defs.size());
    }

    @Test
    void shouldReturnEmptyListWhenNoDefinitions() {
        when(repository.findByIsDeletedFalse()).thenReturn(List.of());

        var defs = service.listDefinitions();

        assertTrue(defs.isEmpty());
    }

    @Test
    void shouldUpdateDefinition() {
        var id = UUID.randomUUID();
        var entity = new WorkflowDefinitionEntity();
        entity.setId(id);
        entity.setName("Original");
        entity.setStatus(WorkflowStatus.DRAFT);
        entity.setVersion("1.0.0");
        entity.setTriggerType(WorkflowTriggerType.REST_API);
        entity.setCreatedBy(UUID.randomUUID());
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        when(repository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(entity));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var def = service.updateDefinition(id, "Updated", "desc", "cat",
                WorkflowTriggerType.SCHEDULED, "{}");

        assertEquals("Updated", def.name());
        assertEquals("desc", def.description());
        assertEquals("cat", def.category());
        assertEquals(WorkflowTriggerType.SCHEDULED, def.triggerType());
        verify(repository).save(entity);
    }

    @Test
    void shouldThrowWhenUpdatingNonExistentDefinition() {
        var id = UUID.randomUUID();
        when(repository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.empty());

        assertThrows(WorkflowException.class,
                () -> service.updateDefinition(id, "name", "desc", "cat",
                        WorkflowTriggerType.REST_API, "{}"));
    }

    @Test
    void shouldDeleteDefinition() {
        var id = UUID.randomUUID();
        var entity = new WorkflowDefinitionEntity();
        entity.setId(id);
        entity.setDeleted(false);
        when(repository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(entity));

        service.deleteDefinition(id);

        assertTrue(entity.isDeleted());
        verify(repository).save(entity);
    }

    @Test
    void shouldThrowWhenDeletingNonExistentDefinition() {
        var id = UUID.randomUUID();
        when(repository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.empty());

        assertThrows(WorkflowException.class, () -> service.deleteDefinition(id));
    }

    @Test
    void shouldPublishDefinition() {
        var id = UUID.randomUUID();
        var entity = new WorkflowDefinitionEntity();
        entity.setId(id);
        entity.setStatus(WorkflowStatus.DRAFT);
        entity.setCreatedBy(UUID.randomUUID());
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        when(repository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(entity));
        when(repository.save(any())).thenReturn(entity);

        var def = service.publishDefinition(id);

        assertEquals(WorkflowStatus.ACTIVE, def.status());
        verify(eventPublisher).publishWorkflowPublished(id);
    }

    @Test
    void shouldThrowWhenPublishingNonExistentDefinition() {
        var id = UUID.randomUUID();
        when(repository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.empty());

        assertThrows(WorkflowException.class, () -> service.publishDefinition(id));
    }

    @Test
    void shouldDeactivateDefinition() {
        var id = UUID.randomUUID();
        var entity = new WorkflowDefinitionEntity();
        entity.setId(id);
        entity.setStatus(WorkflowStatus.ACTIVE);
        entity.setCreatedBy(UUID.randomUUID());
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        when(repository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(entity));
        when(repository.save(any())).thenReturn(entity);

        var def = service.deactivateDefinition(id);

        assertEquals(WorkflowStatus.DEACTIVATED, def.status());
        verify(eventPublisher).publishWorkflowDeactivated(id);
    }

    @Test
    void shouldThrowWhenDeactivatingNonExistentDefinition() {
        var id = UUID.randomUUID();
        when(repository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.empty());

        assertThrows(WorkflowException.class, () -> service.deactivateDefinition(id));
    }

    @Test
    void shouldCloneDefinition() {
        var id = UUID.randomUUID();
        var source = new WorkflowDefinitionEntity();
        source.setId(id);
        source.setName("Source");
        source.setDescription("desc");
        source.setCategory("cat");
        source.setStatus(WorkflowStatus.ACTIVE);
        source.setVersion("2.0.0");
        source.setTriggerType(WorkflowTriggerType.REST_API);
        source.setTriggerConfig("{}");
        source.setCreatedBy(UUID.randomUUID());
        source.setCreatedAt(OffsetDateTime.now());
        source.setUpdatedAt(OffsetDateTime.now());
        when(repository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(source));
        when(repository.save(any())).thenAnswer(inv -> {
            var e = inv.<WorkflowDefinitionEntity>getArgument(0);
            e.setId(UUID.randomUUID());
            return e;
        });

        var cloned = service.cloneDefinition(id, "Cloned");

        assertEquals("Cloned", cloned.name());
        assertEquals("1.0.0", cloned.version());
        assertEquals(WorkflowStatus.DRAFT, cloned.status());
        assertEquals(WorkflowTriggerType.REST_API, cloned.triggerType());
    }

    @Test
    void shouldThrowWhenCloningNonExistentDefinition() {
        var id = UUID.randomUUID();
        when(repository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.empty());

        assertThrows(WorkflowException.class, () -> service.cloneDefinition(id, "new"));
    }

    @Test
    void shouldAddStep() {
        var workflowId = UUID.randomUUID();
        var definition = new WorkflowDefinitionEntity();
        definition.setId(workflowId);
        definition.setName("Test");
        definition.setVersion("1.0.0");
        definition.setStatus(WorkflowStatus.DRAFT);
        definition.setTriggerType(WorkflowTriggerType.REST_API);
        definition.setCreatedBy(UUID.randomUUID());
        definition.setCreatedAt(OffsetDateTime.now());
        definition.setUpdatedAt(OffsetDateTime.now());
        when(repository.findByIdAndIsDeletedFalse(workflowId)).thenReturn(Optional.of(definition));
        when(stepRepository.save(any())).thenAnswer(inv -> {
            var e = inv.<WorkflowStepEntity>getArgument(0);
            e.setId(UUID.randomUUID());
            return e;
        });

        var step = service.addStep(workflowId, "Step1", WorkflowStepType.NOTIFICATION, 1, "{}", false, 5000, 3);

        assertNotNull(step.id());
        assertEquals("Step1", step.name());
        assertEquals(WorkflowStepType.NOTIFICATION, step.type());
        assertEquals(1, step.orderIndex());
        assertEquals("{}", step.config());
        assertEquals(5000, step.timeoutMs());
        assertEquals(3, step.maxRetries());
        verify(stepRepository).save(any());
    }

    @Test
    void shouldThrowWhenAddingStepToNonExistentWorkflow() {
        var workflowId = UUID.randomUUID();
        when(repository.findByIdAndIsDeletedFalse(workflowId)).thenReturn(Optional.empty());

        assertThrows(WorkflowException.class,
                () -> service.addStep(workflowId, "Step1", WorkflowStepType.AUDIT, 1, "{}", false, 5000, 3));
    }

    @Test
    void shouldGetSteps() {
        var workflowId = UUID.randomUUID();
        var entity = new WorkflowStepEntity();
        entity.setId(UUID.randomUUID());
        entity.setWorkflowId(workflowId);
        entity.setName("Step1");
        entity.setStepType(WorkflowStepType.LOGGING);
        entity.setOrderIndex(1);
        entity.setCreatedAt(OffsetDateTime.now());
        when(stepRepository.findByWorkflowIdAndIsDeletedFalseOrderByOrderIndexAsc(workflowId))
                .thenReturn(List.of(entity));

        var steps = service.getSteps(workflowId);

        assertEquals(1, steps.size());
        assertEquals("Step1", steps.get(0).name());
    }

    @Test
    void shouldReturnEmptyStepsWhenNoneExist() {
        var workflowId = UUID.randomUUID();
        when(stepRepository.findByWorkflowIdAndIsDeletedFalseOrderByOrderIndexAsc(workflowId))
                .thenReturn(List.of());

        var steps = service.getSteps(workflowId);

        assertTrue(steps.isEmpty());
    }

    @Test
    void shouldRemoveStep() {
        var workflowId = UUID.randomUUID();
        var stepId = UUID.randomUUID();
        var entity = new WorkflowStepEntity();
        entity.setId(stepId);
        entity.setWorkflowId(workflowId);
        entity.setDeleted(false);
        when(stepRepository.findByIdAndWorkflowIdAndIsDeletedFalse(stepId, workflowId))
                .thenReturn(Optional.of(entity));

        service.removeStep(workflowId, stepId);

        assertTrue(entity.isDeleted());
        verify(stepRepository).save(entity);
    }

    @Test
    void shouldThrowWhenRemovingNonExistentStep() {
        var workflowId = UUID.randomUUID();
        var stepId = UUID.randomUUID();
        when(stepRepository.findByIdAndWorkflowIdAndIsDeletedFalse(stepId, workflowId))
                .thenReturn(Optional.empty());

        assertThrows(WorkflowException.class, () -> service.removeStep(workflowId, stepId));
    }
}
