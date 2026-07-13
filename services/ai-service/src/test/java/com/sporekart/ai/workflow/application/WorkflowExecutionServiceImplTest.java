package com.sporekart.ai.workflow.application;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.workflow.api.WorkflowEngine;
import com.sporekart.ai.workflow.api.WorkflowExecutionService;
import com.sporekart.ai.workflow.domain.WorkflowExecution;
import com.sporekart.ai.workflow.domain.WorkflowExecutionStatus;
import com.sporekart.ai.workflow.infrastructure.persistence.WorkflowExecutionEntity;
import com.sporekart.ai.workflow.infrastructure.persistence.WorkflowStateEntity;
import com.sporekart.ai.workflow.infrastructure.persistence.WorkflowExecutionRepository;
import com.sporekart.ai.workflow.infrastructure.persistence.WorkflowStateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkflowExecutionServiceImplTest {

    @Mock
    private WorkflowEngine workflowEngine;

    @Mock
    private WorkflowExecutionRepository executionRepository;

    @Mock
    private WorkflowStateRepository stateRepository;

    private ObjectMapper objectMapper;

    private WorkflowExecutionServiceImpl service;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        service = new WorkflowExecutionServiceImpl(workflowEngine, executionRepository, stateRepository, objectMapper);
    }

    @Test
    void shouldStartExecution() {
        var workflowId = UUID.randomUUID();
        var startedBy = UUID.randomUUID();
        var inputData = Map.<String, Object>of("key", "value");
        var execution = new WorkflowExecution(UUID.randomUUID(), workflowId, "1.0.0",
                WorkflowExecutionStatus.PENDING, "REST_API", "{\"key\":\"value\"}",
                startedBy, OffsetDateTime.now(), null, null, 0);
        when(workflowEngine.execute(any(), any(), any())).thenReturn(execution);

        var result = service.startExecution(workflowId, inputData, startedBy);

        assertNotNull(result);
        assertEquals(workflowId, result.workflowId());
        verify(workflowEngine).execute(eq(workflowId), anyString(), eq(startedBy));
    }

    @Test
    void shouldThrowOnJsonSerializationFailure() throws Exception {
        var objectMapperSpy = mock(ObjectMapper.class);
        var serviceWithBrokenMapper = new WorkflowExecutionServiceImpl(
                workflowEngine, executionRepository, stateRepository, objectMapperSpy);
        when(objectMapperSpy.writeValueAsString(any())).thenThrow(new JsonParseException(null, "JSON error"));

        assertThrows(WorkflowException.class,
                () -> serviceWithBrokenMapper.startExecution(UUID.randomUUID(), Map.of("key", "value"), UUID.randomUUID()));
    }

    @Test
    void shouldExecuteWorkflow() {
        var workflowId = UUID.randomUUID();
        var startedBy = UUID.randomUUID();
        var execution = new WorkflowExecution(UUID.randomUUID(), workflowId, "1.0.0",
                WorkflowExecutionStatus.RUNNING, "REST_API", "{}",
                startedBy, OffsetDateTime.now(), null, null, 0);
        when(workflowEngine.execute(workflowId, "{}", startedBy)).thenReturn(execution);

        var result = service.executeWorkflow(workflowId, "{}", startedBy);

        assertNotNull(result);
        assertEquals(WorkflowExecutionStatus.RUNNING, result.status());
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
        doNothing().when(workflowEngine).pause(executionId);
        when(executionRepository.findById(executionId)).thenReturn(Optional.of(entity));

        var result = service.pauseExecution(executionId);

        assertNotNull(result);
        verify(workflowEngine).pause(executionId);
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
        doNothing().when(workflowEngine).resume(executionId);
        when(executionRepository.findById(executionId)).thenReturn(Optional.of(entity));

        var result = service.resumeExecution(executionId);

        assertNotNull(result);
        verify(workflowEngine).resume(executionId);
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
        doNothing().when(workflowEngine).cancel(executionId);
        when(executionRepository.findById(executionId)).thenReturn(Optional.of(entity));

        var result = service.cancelExecution(executionId);

        assertNotNull(result);
        verify(workflowEngine).cancel(executionId);
    }

    @Test
    void shouldGetExecution() {
        var executionId = UUID.randomUUID();
        var entity = new WorkflowExecutionEntity();
        entity.setId(executionId);
        entity.setWorkflowId(UUID.randomUUID());
        entity.setStatus(WorkflowExecutionStatus.RUNNING);
        entity.setStartedBy(UUID.randomUUID());
        entity.setStartedAt(OffsetDateTime.now());
        entity.setCreatedAt(OffsetDateTime.now());
        when(executionRepository.findById(executionId)).thenReturn(Optional.of(entity));

        var result = service.getExecution(executionId);

        assertTrue(result.isPresent());
        assertEquals(executionId, result.get().id());
    }

    @Test
    void shouldReturnEmptyForNonExistentExecution() {
        var executionId = UUID.randomUUID();
        when(executionRepository.findById(executionId)).thenReturn(Optional.empty());

        var result = service.getExecution(executionId);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldListExecutions() {
        var workflowId = UUID.randomUUID();
        var entity = new WorkflowExecutionEntity();
        entity.setId(UUID.randomUUID());
        entity.setWorkflowId(workflowId);
        entity.setStatus(WorkflowExecutionStatus.RUNNING);
        entity.setStartedBy(UUID.randomUUID());
        entity.setStartedAt(OffsetDateTime.now());
        entity.setCreatedAt(OffsetDateTime.now());
        when(executionRepository.findByWorkflowIdAndIsDeletedFalseOrderByStartedAtDesc(workflowId))
                .thenReturn(List.of(entity));

        var executions = service.listExecutions(workflowId);

        assertEquals(1, executions.size());
    }

    @Test
    void shouldReturnEmptyListWhenNoExecutions() {
        var workflowId = UUID.randomUUID();
        when(executionRepository.findByWorkflowIdAndIsDeletedFalseOrderByStartedAtDesc(workflowId))
                .thenReturn(List.of());

        var executions = service.listExecutions(workflowId);

        assertTrue(executions.isEmpty());
    }

    @Test
    void shouldGetExecutionState() {
        var executionId = UUID.randomUUID();
        var entity = new WorkflowStateEntity();
        entity.setId(UUID.randomUUID());
        entity.setExecutionId(executionId);
        entity.setWorkflowId(UUID.randomUUID());
        entity.setCurrentStep("step1");
        entity.setStatus(WorkflowExecutionStatus.RUNNING);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        when(stateRepository.findByExecutionId(executionId)).thenReturn(Optional.of(entity));

        var result = service.getExecutionState(executionId);

        assertTrue(result.isPresent());
        assertEquals("step1", result.get().currentStep());
    }

    @Test
    void shouldReturnEmptyStateWhenNotFound() {
        var executionId = UUID.randomUUID();
        when(stateRepository.findByExecutionId(executionId)).thenReturn(Optional.empty());

        var result = service.getExecutionState(executionId);

        assertTrue(result.isEmpty());
    }
}
