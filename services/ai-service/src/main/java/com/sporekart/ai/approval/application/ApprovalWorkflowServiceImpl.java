package com.sporekart.ai.approval.application;

import com.sporekart.ai.approval.api.ApprovalWorkflowService;
import com.sporekart.ai.approval.domain.*;
import com.sporekart.ai.approval.infrastructure.persistence.ApprovalWorkflowEntity;
import com.sporekart.ai.approval.infrastructure.persistence.ApprovalWorkflowRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApprovalWorkflowServiceImpl implements ApprovalWorkflowService {

    private final ApprovalWorkflowRepository workflowRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public ApprovalWorkflow createWorkflow(ApprovalWorkflow workflow) {
        var entity = new ApprovalWorkflowEntity();
        entity.setId(UUID.randomUUID());
        entity.setName(workflow.name());
        entity.setDescription(workflow.description());
        entity.setModule(workflow.module());
        entity.setAllowedTransitions(toJson(workflow.allowedTransitions()));
        entity.setMaxLevels(workflow.maxLevels());
        entity.setParallelEnabled(workflow.parallelEnabled());
        entity.setSequentialEnabled(workflow.sequentialEnabled());
        entity.setStrategy(workflow.strategy().name());
        entity.setSlaMinutes(workflow.slaMinutes());
        entity.setConfig(toJson(workflow.config()));
        entity.setIsActive(workflow.isActive());
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        entity.setIsDeleted(false);
        var saved = workflowRepository.save(entity);
        log.info("Workflow created: {}", saved.getId());
        return toDomain(saved);
    }

    @Override
    @Transactional
    public ApprovalWorkflow updateWorkflow(ApprovalWorkflow workflow) {
        var entity = workflowRepository.findByIdAndIsDeletedFalse(workflow.id())
            .orElseThrow(() -> new RuntimeException("Workflow not found: " + workflow.id()));
        entity.setName(workflow.name());
        entity.setDescription(workflow.description());
        entity.setModule(workflow.module());
        entity.setAllowedTransitions(toJson(workflow.allowedTransitions()));
        entity.setMaxLevels(workflow.maxLevels());
        entity.setParallelEnabled(workflow.parallelEnabled());
        entity.setSequentialEnabled(workflow.sequentialEnabled());
        entity.setStrategy(workflow.strategy().name());
        entity.setSlaMinutes(workflow.slaMinutes());
        entity.setConfig(toJson(workflow.config()));
        entity.setIsActive(workflow.isActive());
        entity.setUpdatedAt(OffsetDateTime.now());
        var saved = workflowRepository.save(entity);
        log.info("Workflow updated: {}", saved.getId());
        return toDomain(saved);
    }

    @Override
    @Transactional
    public void deleteWorkflow(UUID id) {
        var entity = workflowRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new RuntimeException("Workflow not found: " + id));
        entity.setIsDeleted(true);
        entity.setUpdatedAt(OffsetDateTime.now());
        workflowRepository.save(entity);
        log.info("Workflow deleted: {}", id);
    }

    @Override
    public Optional<ApprovalWorkflow> getWorkflow(UUID id) {
        return workflowRepository.findByIdAndIsDeletedFalse(id).map(this::toDomain);
    }

    @Override
    public List<ApprovalWorkflow> listWorkflows() {
        return workflowRepository.findByIsDeletedFalse().stream().map(this::toDomain).toList();
    }

    @Override
    public List<ApprovalWorkflow> findWorkflowsByModule(String module) {
        return workflowRepository.findByModuleAndIsDeletedFalse(module).stream().map(this::toDomain).toList();
    }

    @Override
    public boolean canTransition(ApprovalStatus from, ApprovalStatus to, UUID workflowId) {
        return workflowRepository.findByIdAndIsDeletedFalse(workflowId)
            .map(entity -> {
                var transitions = fromJson(entity.getAllowedTransitions(),
                    new TypeReference<List<ApprovalStatus>>() {});
                return transitions != null && transitions.contains(to);
            })
            .orElse(false);
    }

    private ApprovalWorkflow toDomain(ApprovalWorkflowEntity entity) {
        return new ApprovalWorkflow(
            entity.getId(), entity.getName(), entity.getDescription(), entity.getModule(),
            fromJson(entity.getAllowedTransitions(), new TypeReference<List<ApprovalStatus>>() {}),
            entity.getMaxLevels() != null ? entity.getMaxLevels() : 0,
            entity.getParallelEnabled() != null && entity.getParallelEnabled(),
            entity.getSequentialEnabled() != null && entity.getSequentialEnabled(),
            AssignmentStrategy.valueOf(entity.getStrategy()),
            entity.getSlaMinutes() != null ? entity.getSlaMinutes() : 0,
            fromJson(entity.getConfig(), new TypeReference<java.util.Map<String, Object>>() {}),
            entity.getIsActive() != null && entity.getIsActive(),
            entity.getCreatedAt(), entity.getUpdatedAt()
        );
    }

    private String toJson(Object value) {
        try {
            return value == null ? null : objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            throw new RuntimeException("JSON conversion error", e);
        }
    }

    private <T> T fromJson(String json, TypeReference<T> type) {
        try {
            return json == null ? null : objectMapper.readValue(json, type);
        } catch (Exception e) {
            return null;
        }
    }
}
