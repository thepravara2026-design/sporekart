package com.sporekart.ai.workflow.application;

import com.sporekart.ai.workflow.api.WorkflowService;
import com.sporekart.ai.workflow.domain.*;
import com.sporekart.ai.workflow.infrastructure.kafka.WorkflowKafkaEventPublisher;
import com.sporekart.ai.workflow.infrastructure.persistence.WorkflowDefinitionEntity;
import com.sporekart.ai.workflow.infrastructure.persistence.WorkflowDefinitionRepository;
import com.sporekart.ai.workflow.infrastructure.persistence.WorkflowStepEntity;
import com.sporekart.ai.workflow.infrastructure.persistence.WorkflowStepRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WorkflowServiceImpl implements WorkflowService {

    private static final Logger log = LoggerFactory.getLogger(WorkflowServiceImpl.class);

    private final WorkflowDefinitionRepository repository;
    private final WorkflowStepRepository stepRepository;
    private final WorkflowKafkaEventPublisher eventPublisher;

    public WorkflowServiceImpl(WorkflowDefinitionRepository repository,
                               WorkflowStepRepository stepRepository,
                               WorkflowKafkaEventPublisher eventPublisher) {
        this.repository = repository;
        this.stepRepository = stepRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public WorkflowDefinition createDefinition(String name, String description, String category,
                                               WorkflowTriggerType triggerType, String triggerConfig, UUID createdBy) {
        var entity = new WorkflowDefinitionEntity();
        entity.setName(name);
        entity.setDescription(description);
        entity.setCategory(category);
        entity.setStatus(WorkflowStatus.DRAFT);
        entity.setVersion("1.0.0");
        entity.setTriggerType(triggerType);
        entity.setTriggerConfig(triggerConfig);
        entity.setCreatedBy(createdBy);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        entity.setDeleted(false);
        var saved = repository.save(entity);
        eventPublisher.publishWorkflowCreated(saved.getId(), name, createdBy);
        log.info("Created workflow definition {}: {}", saved.getId(), name);
        return toDomain(saved);
    }

    @Override
    public Optional<WorkflowDefinition> getDefinition(UUID id) {
        return repository.findByIdAndIsDeletedFalse(id).map(this::toDomain);
    }

    @Override
    public List<WorkflowDefinition> listDefinitions() {
        return repository.findByIsDeletedFalse().stream().map(this::toDomain).toList();
    }

    @Override
    @Transactional
    public WorkflowDefinition updateDefinition(UUID id, String name, String description, String category,
                                               WorkflowTriggerType triggerType, String triggerConfig) {
        var entity = repository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new WorkflowException("Workflow definition not found: " + id));
        entity.setName(name);
        entity.setDescription(description);
        entity.setCategory(category);
        entity.setTriggerType(triggerType);
        entity.setTriggerConfig(triggerConfig);
        entity.setUpdatedAt(OffsetDateTime.now());
        var saved = repository.save(entity);
        log.info("Updated workflow definition {}", id);
        return toDomain(saved);
    }

    @Override
    @Transactional
    public void deleteDefinition(UUID id) {
        var entity = repository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new WorkflowException("Workflow definition not found: " + id));
        entity.setDeleted(true);
        entity.setUpdatedAt(OffsetDateTime.now());
        repository.save(entity);
        log.info("Deleted workflow definition {}", id);
    }

    @Override
    @Transactional
    public WorkflowDefinition publishDefinition(UUID id) {
        var entity = repository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new WorkflowException("Workflow definition not found: " + id));
        entity.setStatus(WorkflowStatus.ACTIVE);
        entity.setUpdatedAt(OffsetDateTime.now());
        var saved = repository.save(entity);
        eventPublisher.publishWorkflowPublished(saved.getId());
        log.info("Published workflow definition {}", id);
        return toDomain(saved);
    }

    @Override
    @Transactional
    public WorkflowDefinition deactivateDefinition(UUID id) {
        var entity = repository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new WorkflowException("Workflow definition not found: " + id));
        entity.setStatus(WorkflowStatus.DEACTIVATED);
        entity.setUpdatedAt(OffsetDateTime.now());
        var saved = repository.save(entity);
        eventPublisher.publishWorkflowDeactivated(saved.getId());
        log.info("Deactivated workflow definition {}", id);
        return toDomain(saved);
    }

    @Override
    @Transactional
    public WorkflowDefinition cloneDefinition(UUID id, String newName) {
        var source = repository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new WorkflowException("Workflow definition not found: " + id));
        var entity = new WorkflowDefinitionEntity();
        entity.setName(newName);
        entity.setDescription(source.getDescription());
        entity.setCategory(source.getCategory());
        entity.setStatus(WorkflowStatus.DRAFT);
        entity.setVersion("1.0.0");
        entity.setTriggerType(source.getTriggerType());
        entity.setTriggerConfig(source.getTriggerConfig());
        entity.setCreatedBy(source.getCreatedBy());
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        entity.setDeleted(false);
        var saved = repository.save(entity);
        log.info("Cloned workflow definition {} to {} as {}", id, saved.getId(), newName);
        return toDomain(saved);
    }

    @Override
    @Transactional
    public WorkflowStep addStep(UUID workflowId, String name, WorkflowStepType stepType,
                                int orderIndex, String config, boolean isOptional,
                                long timeoutMs, int maxRetries) {
        var definition = repository.findByIdAndIsDeletedFalse(workflowId)
                .orElseThrow(() -> new WorkflowException("Workflow definition not found: " + workflowId));
        var entity = new WorkflowStepEntity();
        entity.setWorkflowId(workflowId);
        entity.setName(name);
        entity.setStepType(stepType);
        entity.setOrderIndex(orderIndex);
        entity.setConfig(config);
        entity.setOptional(isOptional);
        entity.setTimeoutMs(timeoutMs);
        entity.setMaxRetries(maxRetries);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setDeleted(false);
        var saved = stepRepository.save(entity);
        log.info("Added step {} to workflow {}", saved.getId(), workflowId);
        return toStepDomain(saved);
    }

    @Override
    public List<WorkflowStep> getSteps(UUID workflowId) {
        return stepRepository.findByWorkflowIdAndIsDeletedFalseOrderByOrderIndexAsc(workflowId)
                .stream().map(this::toStepDomain).toList();
    }

    @Override
    @Transactional
    public void removeStep(UUID workflowId, UUID stepId) {
        var entity = stepRepository.findByIdAndWorkflowIdAndIsDeletedFalse(stepId, workflowId)
                .orElseThrow(() -> new WorkflowException("Step not found: " + stepId));
        entity.setDeleted(true);
        stepRepository.save(entity);
        log.info("Removed step {} from workflow {}", stepId, workflowId);
    }

    private WorkflowStep toStepDomain(WorkflowStepEntity entity) {
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

    private WorkflowDefinition toDomain(WorkflowDefinitionEntity entity) {
        return new WorkflowDefinition(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCategory(),
                entity.getStatus(),
                entity.getVersion(),
                entity.getTriggerType(),
                entity.getTriggerConfig(),
                null,
                entity.isTemplate(),
                entity.getCreatedBy(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
