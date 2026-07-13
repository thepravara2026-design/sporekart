package com.sporekart.ai.workflow.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.workflow.api.WorkflowEngine;
import com.sporekart.ai.workflow.api.WorkflowExecutionService;
import com.sporekart.ai.workflow.domain.WorkflowExecution;
import com.sporekart.ai.workflow.domain.WorkflowExecutionState;
import com.sporekart.ai.workflow.infrastructure.persistence.WorkflowExecutionEntity;
import com.sporekart.ai.workflow.infrastructure.persistence.WorkflowExecutionRepository;
import com.sporekart.ai.workflow.infrastructure.persistence.WorkflowStateEntity;
import com.sporekart.ai.workflow.infrastructure.persistence.WorkflowStateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class WorkflowExecutionServiceImpl implements WorkflowExecutionService {

    private static final Logger log = LoggerFactory.getLogger(WorkflowExecutionServiceImpl.class);

    private final WorkflowEngine workflowEngine;
    private final WorkflowExecutionRepository executionRepository;
    private final WorkflowStateRepository stateRepository;
    private final ObjectMapper objectMapper;

    public WorkflowExecutionServiceImpl(WorkflowEngine workflowEngine,
                                        WorkflowExecutionRepository executionRepository,
                                        WorkflowStateRepository stateRepository,
                                        ObjectMapper objectMapper) {
        this.workflowEngine = workflowEngine;
        this.executionRepository = executionRepository;
        this.stateRepository = stateRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public WorkflowExecution executeWorkflow(UUID workflowId, String triggerData, UUID startedBy) {
        log.info("Executing workflow {} triggered by {}", workflowId, startedBy);
        return workflowEngine.execute(workflowId, triggerData, startedBy);
    }

    @Override
    @Transactional
    public WorkflowExecution pauseExecution(UUID executionId) {
        log.info("Pausing execution {}", executionId);
        workflowEngine.pause(executionId);
        return getExecution(executionId)
                .orElseThrow(() -> new WorkflowException("Execution not found: " + executionId));
    }

    @Override
    @Transactional
    public WorkflowExecution resumeExecution(UUID executionId) {
        log.info("Resuming execution {}", executionId);
        workflowEngine.resume(executionId);
        return getExecution(executionId)
                .orElseThrow(() -> new WorkflowException("Execution not found: " + executionId));
    }

    @Override
    @Transactional
    public WorkflowExecution cancelExecution(UUID executionId) {
        log.info("Cancelling execution {}", executionId);
        workflowEngine.cancel(executionId);
        return getExecution(executionId)
                .orElseThrow(() -> new WorkflowException("Execution not found: " + executionId));
    }

    @Override
    public Optional<WorkflowExecution> getExecution(UUID executionId) {
        return executionRepository.findById(executionId).map(this::toDomain);
    }

    @Override
    public List<WorkflowExecution> listExecutions(UUID workflowId) {
        return executionRepository.findByWorkflowIdAndIsDeletedFalseOrderByStartedAtDesc(workflowId)
                .stream().map(this::toDomain).toList();
    }

    @Override
    public List<WorkflowExecution> listAllExecutions() {
        return executionRepository.findAllByIsDeletedFalseOrderByStartedAtDesc()
                .stream().map(this::toDomain).toList();
    }

    @Override
    @Transactional
    public WorkflowExecution startExecution(UUID workflowId, Map<String, Object> inputData, UUID startedBy) {
        try {
            var triggerData = objectMapper.writeValueAsString(inputData);
            return executeWorkflow(workflowId, triggerData, startedBy);
        } catch (JsonProcessingException e) {
            throw new WorkflowException("Failed to serialize input data", e);
        }
    }

    @Override
    public Optional<WorkflowExecutionState> getExecutionState(UUID executionId) {
        return stateRepository.findByExecutionId(executionId).map(this::toStateDomain);
    }

    private WorkflowExecutionState toStateDomain(WorkflowStateEntity entity) {
        return new WorkflowExecutionState(
                entity.getId(),
                entity.getExecutionId(),
                entity.getWorkflowId(),
                entity.getCurrentStep(),
                null,
                null,
                entity.getStatus().name(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    private WorkflowExecution toDomain(WorkflowExecutionEntity entity) {
        return new WorkflowExecution(
                entity.getId(),
                entity.getWorkflowId(),
                entity.getWorkflowVersion(),
                entity.getStatus(),
                entity.getTriggerType(),
                entity.getTriggerData(),
                entity.getStartedBy(),
                entity.getStartedAt(),
                entity.getCompletedAt(),
                entity.getErrorMessage(),
                entity.getRetryCount()
        );
    }
}
