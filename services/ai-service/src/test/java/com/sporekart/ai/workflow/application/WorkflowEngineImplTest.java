package com.sporekart.ai.workflow.application;

import com.sporekart.ai.workflow.api.WorkflowStepDispatcher;
import com.sporekart.ai.workflow.domain.*;
import com.sporekart.ai.workflow.infrastructure.kafka.WorkflowKafkaEventPublisher;
import com.sporekart.ai.workflow.infrastructure.persistence.*;
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
class WorkflowEngineImplTest {

    @Mock
    private WorkflowDefinitionRepository definitionRepository;

    @Mock
    private WorkflowExecutionRepository executionRepository;

    @Mock
    private WorkflowStepRepository stepRepository;

    @Mock
    private WorkflowStepDispatcher stepDispatcher;

    @Mock
    private WorkflowKafkaEventPublisher eventPublisher;

    private WorkflowEngineImpl engine;

    @BeforeEach
    void setUp() {
        engine = new WorkflowEngineImpl(definitionRepository, executionRepository,
                stepRepository, stepDispatcher, eventPublisher);
    }

    @Test
    void shouldExecuteActiveWorkflow() {
        var workflowId = UUID.randomUUID();
        var startedBy = UUID.randomUUID();
        var definition = new WorkflowDefinitionEntity();
        definition.setId(workflowId);
        definition.setName("Test");
        definition.setStatus(WorkflowStatus.ACTIVE);
        definition.setVersion("1.0.0");
        definition.setTriggerType(WorkflowTriggerType.REST_API);
        definition.setCreatedBy(UUID.randomUUID());
        definition.setCreatedAt(OffsetDateTime.now());
        definition.setUpdatedAt(OffsetDateTime.now());

        when(definitionRepository.findByIdAndIsDeletedFalse(workflowId)).thenReturn(Optional.of(definition));
        when(executionRepository.save(any())).thenAnswer(inv -> {
            var e = inv.<WorkflowExecutionEntity>getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID());
            return e;
        });
        when(stepRepository.findByWorkflowIdAndIsDeletedFalseOrderByOrderIndexAsc(workflowId))
                .thenReturn(List.of());

        var execution = engine.execute(workflowId, "{}", startedBy);

        assertNotNull(execution);
        assertEquals(WorkflowExecutionStatus.COMPLETED, execution.status());
        verify(eventPublisher).publishWorkflowExecutionStarted(any(), eq(workflowId));
        verify(eventPublisher).publishWorkflowExecutionCompleted(any(), eq(workflowId));
    }

    @Test
    void shouldThrowWhenExecutingInactiveWorkflow() {
        var workflowId = UUID.randomUUID();
        var definition = new WorkflowDefinitionEntity();
        definition.setId(workflowId);
        definition.setStatus(WorkflowStatus.DRAFT);
        when(definitionRepository.findByIdAndIsDeletedFalse(workflowId)).thenReturn(Optional.of(definition));

        assertThrows(WorkflowException.class,
                () -> engine.execute(workflowId, "{}", UUID.randomUUID()));
    }

    @Test
    void shouldThrowWhenExecutingNonExistentWorkflow() {
        var workflowId = UUID.randomUUID();
        when(definitionRepository.findByIdAndIsDeletedFalse(workflowId)).thenReturn(Optional.empty());

        assertThrows(WorkflowException.class,
                () -> engine.execute(workflowId, "{}", UUID.randomUUID()));
    }

    @Test
    void shouldPauseExecution() {
        var executionId = UUID.randomUUID();
        var entity = new WorkflowExecutionEntity();
        entity.setId(executionId);
        entity.setWorkflowId(UUID.randomUUID());
        entity.setStatus(WorkflowExecutionStatus.RUNNING);
        entity.setStartedBy(UUID.randomUUID());
        entity.setStartedAt(OffsetDateTime.now());
        entity.setCreatedAt(OffsetDateTime.now());
        when(executionRepository.findById(executionId)).thenReturn(Optional.of(entity));
        when(executionRepository.save(any())).thenReturn(entity);

        engine.pause(executionId);

        assertEquals(WorkflowExecutionStatus.PAUSED, entity.getStatus());
        verify(eventPublisher).publishWorkflowExecutionPaused(executionId, entity.getWorkflowId());
    }

    @Test
    void shouldThrowWhenPausingNonExistentExecution() {
        var executionId = UUID.randomUUID();
        when(executionRepository.findById(executionId)).thenReturn(Optional.empty());

        assertThrows(WorkflowException.class, () -> engine.pause(executionId));
    }

    @Test
    void shouldResumeExecution() {
        var executionId = UUID.randomUUID();
        var entity = new WorkflowExecutionEntity();
        entity.setId(executionId);
        entity.setWorkflowId(UUID.randomUUID());
        entity.setStatus(WorkflowExecutionStatus.PAUSED);
        entity.setStartedBy(UUID.randomUUID());
        entity.setStartedAt(OffsetDateTime.now());
        entity.setCreatedAt(OffsetDateTime.now());
        when(executionRepository.findById(executionId)).thenReturn(Optional.of(entity));
        when(executionRepository.save(any())).thenReturn(entity);

        engine.resume(executionId);

        assertEquals(WorkflowExecutionStatus.RUNNING, entity.getStatus());
        verify(eventPublisher).publishWorkflowExecutionResumed(executionId, entity.getWorkflowId());
    }

    @Test
    void shouldThrowWhenResumingNonExistentExecution() {
        var executionId = UUID.randomUUID();
        when(executionRepository.findById(executionId)).thenReturn(Optional.empty());

        assertThrows(WorkflowException.class, () -> engine.resume(executionId));
    }

    @Test
    void shouldThrowWhenResumingInvalidTransition() {
        var executionId = UUID.randomUUID();
        var entity = new WorkflowExecutionEntity();
        entity.setId(executionId);
        entity.setWorkflowId(UUID.randomUUID());
        entity.setStatus(WorkflowExecutionStatus.COMPLETED);
        entity.setStartedBy(UUID.randomUUID());
        entity.setStartedAt(OffsetDateTime.now());
        entity.setCreatedAt(OffsetDateTime.now());
        when(executionRepository.findById(executionId)).thenReturn(Optional.of(entity));

        assertThrows(IllegalStateException.class, () -> engine.resume(executionId));
    }

    @Test
    void shouldCancelExecution() {
        var executionId = UUID.randomUUID();
        var entity = new WorkflowExecutionEntity();
        entity.setId(executionId);
        entity.setWorkflowId(UUID.randomUUID());
        entity.setStatus(WorkflowExecutionStatus.RUNNING);
        entity.setStartedBy(UUID.randomUUID());
        entity.setStartedAt(OffsetDateTime.now());
        entity.setCreatedAt(OffsetDateTime.now());
        when(executionRepository.findById(executionId)).thenReturn(Optional.of(entity));
        when(executionRepository.save(any())).thenReturn(entity);

        engine.cancel(executionId);

        assertEquals(WorkflowExecutionStatus.CANCELLED, entity.getStatus());
        assertNotNull(entity.getCompletedAt());
        verify(eventPublisher).publishWorkflowExecutionCancelled(executionId, entity.getWorkflowId());
    }

    @Test
    void shouldThrowWhenCancellingNonExistentExecution() {
        var executionId = UUID.randomUUID();
        when(executionRepository.findById(executionId)).thenReturn(Optional.empty());

        assertThrows(WorkflowException.class, () -> engine.cancel(executionId));
    }

    @Test
    void shouldGetStatus() {
        var executionId = UUID.randomUUID();
        var entity = new WorkflowExecutionEntity();
        entity.setId(executionId);
        entity.setStatus(WorkflowExecutionStatus.RUNNING);
        when(executionRepository.findById(executionId)).thenReturn(Optional.of(entity));

        var status = engine.getStatus(executionId);

        assertEquals(WorkflowExecutionStatus.RUNNING, status);
    }

    @Test
    void shouldThrowWhenGettingStatusForNonExistentExecution() {
        var executionId = UUID.randomUUID();
        when(executionRepository.findById(executionId)).thenReturn(Optional.empty());

        assertThrows(WorkflowException.class, () -> engine.getStatus(executionId));
    }
}
