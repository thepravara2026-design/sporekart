package com.sporekart.ai.workflow.application;

import com.sporekart.ai.workflow.api.WorkflowEngine;
import com.sporekart.ai.workflow.api.WorkflowStepDispatcher;
import com.sporekart.ai.workflow.domain.*;
import com.sporekart.ai.workflow.infrastructure.kafka.WorkflowKafkaEventPublisher;
import com.sporekart.ai.workflow.infrastructure.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class WorkflowEngineImpl implements WorkflowEngine {

    private static final Logger log = LoggerFactory.getLogger(WorkflowEngineImpl.class);

    private final WorkflowDefinitionRepository definitionRepository;
    private final WorkflowExecutionRepository executionRepository;
    private final WorkflowStepRepository stepRepository;
    private final WorkflowStepDispatcher stepDispatcher;
    private final WorkflowKafkaEventPublisher eventPublisher;

    public WorkflowEngineImpl(WorkflowDefinitionRepository definitionRepository,
                              WorkflowExecutionRepository executionRepository,
                              WorkflowStepRepository stepRepository,
                              WorkflowStepDispatcher stepDispatcher,
                              WorkflowKafkaEventPublisher eventPublisher) {
        this.definitionRepository = definitionRepository;
        this.executionRepository = executionRepository;
        this.stepRepository = stepRepository;
        this.stepDispatcher = stepDispatcher;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public WorkflowExecution execute(UUID workflowId, String triggerData, UUID startedBy) {
        var definition = definitionRepository.findByIdAndIsDeletedFalse(workflowId)
                .orElseThrow(() -> new WorkflowException("Workflow definition not found: " + workflowId));

        if (definition.getStatus() != WorkflowStatus.ACTIVE) {
            throw new WorkflowException("Workflow definition " + workflowId + " is not active");
        }

        var executionEntity = new WorkflowExecutionEntity();
        executionEntity.setWorkflowId(workflowId);
        executionEntity.setWorkflowVersion(definition.getVersion());
        executionEntity.setStatus(WorkflowExecutionStatus.PENDING);
        executionEntity.setTriggerType(definition.getTriggerType().name());
        executionEntity.setTriggerData(triggerData);
        executionEntity.setStartedBy(startedBy);
        executionEntity.setStartedAt(OffsetDateTime.now());
        executionEntity.setRetryCount(0);
        var savedExecution = executionRepository.save(executionEntity);

        WorkflowStateMachine.transition(WorkflowExecutionStatus.PENDING, WorkflowExecutionStatus.RUNNING);
        savedExecution.setStatus(WorkflowExecutionStatus.RUNNING);
        executionRepository.save(savedExecution);

        eventPublisher.publishWorkflowExecutionStarted(savedExecution.getId(), workflowId);
        log.info("Workflow execution {} started for workflow {}", savedExecution.getId(), workflowId);

        var steps = stepRepository.findByWorkflowIdAndIsDeletedFalseOrderByOrderIndexAsc(workflowId);

        try {
            for (WorkflowStepEntity stepEntity : steps) {
                if (savedExecution.getStatus() != WorkflowExecutionStatus.RUNNING) {
                    log.info("Execution {} is no longer running, stopping step processing", savedExecution.getId());
                    break;
                }

                var step = toDomainStep(stepEntity);
                log.info("Dispatching step {} ({}) for execution {}", step.name(), step.type(), savedExecution.getId());
                var stepStatus = stepDispatcher.dispatch(savedExecution.getId(), step);

                if (stepStatus == WorkflowStepStatus.FAILED && !step.isOptional()) {
                    savedExecution.setStatus(WorkflowExecutionStatus.FAILED);
                    savedExecution.setErrorMessage("Step " + step.name() + " failed");
                    savedExecution.setCompletedAt(OffsetDateTime.now());
                    executionRepository.save(savedExecution);
                    eventPublisher.publishWorkflowExecutionFailed(savedExecution.getId(), workflowId, "Step " + step.name() + " failed");
                    log.error("Workflow execution {} failed at step {}", savedExecution.getId(), step.name());
                    return toDomain(savedExecution);
                }
            }

            if (savedExecution.getStatus() == WorkflowExecutionStatus.RUNNING) {
                savedExecution.setStatus(WorkflowExecutionStatus.COMPLETED);
                savedExecution.setCompletedAt(OffsetDateTime.now());
                executionRepository.save(savedExecution);
                eventPublisher.publishWorkflowExecutionCompleted(savedExecution.getId(), workflowId);
                log.info("Workflow execution {} completed successfully", savedExecution.getId());
            }
        } catch (Exception e) {
            savedExecution.setStatus(WorkflowExecutionStatus.FAILED);
            savedExecution.setErrorMessage(e.getMessage());
            savedExecution.setCompletedAt(OffsetDateTime.now());
            executionRepository.save(savedExecution);
            eventPublisher.publishWorkflowExecutionFailed(savedExecution.getId(), workflowId, e.getMessage());
            log.error("Workflow execution {} failed: {}", savedExecution.getId(), e.getMessage());
        }

        return toDomain(savedExecution);
    }

    @Override
    @Transactional
    public void pause(UUID executionId) {
        var entity = executionRepository.findById(executionId)
                .orElseThrow(() -> new WorkflowException("Execution not found: " + executionId));
        WorkflowStateMachine.transition(entity.getStatus(), WorkflowExecutionStatus.PAUSED);
        entity.setStatus(WorkflowExecutionStatus.PAUSED);
        executionRepository.save(entity);
        eventPublisher.publishWorkflowExecutionPaused(executionId, entity.getWorkflowId());
        log.info("Paused execution {}", executionId);
    }

    @Override
    @Transactional
    public void resume(UUID executionId) {
        var entity = executionRepository.findById(executionId)
                .orElseThrow(() -> new WorkflowException("Execution not found: " + executionId));
        WorkflowStateMachine.transition(entity.getStatus(), WorkflowExecutionStatus.RUNNING);
        entity.setStatus(WorkflowExecutionStatus.RUNNING);
        executionRepository.save(entity);
        eventPublisher.publishWorkflowExecutionResumed(executionId, entity.getWorkflowId());
        log.info("Resumed execution {}", executionId);
    }

    @Override
    @Transactional
    public void cancel(UUID executionId) {
        var entity = executionRepository.findById(executionId)
                .orElseThrow(() -> new WorkflowException("Execution not found: " + executionId));
        WorkflowStateMachine.transition(entity.getStatus(), WorkflowExecutionStatus.CANCELLED);
        entity.setStatus(WorkflowExecutionStatus.CANCELLED);
        entity.setCompletedAt(OffsetDateTime.now());
        executionRepository.save(entity);
        eventPublisher.publishWorkflowExecutionCancelled(executionId, entity.getWorkflowId());
        log.info("Cancelled execution {}", executionId);
    }

    @Override
    public WorkflowExecutionStatus getStatus(UUID executionId) {
        return executionRepository.findById(executionId)
                .map(WorkflowExecutionEntity::getStatus)
                .orElseThrow(() -> new WorkflowException("Execution not found: " + executionId));
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

    private WorkflowStep toDomainStep(WorkflowStepEntity entity) {
        return new WorkflowStep(
                entity.getId(),
                entity.getWorkflowId(),
                entity.getName(),
                entity.getStepType(),
                entity.getOrderIndex(),
                entity.getConfig(),
                null,
                entity.isOptional(),
                entity.getTimeoutMs(),
                entity.getMaxRetries()
        );
    }
}
