package com.sporekart.ai.workflow.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkflowActionRepository extends JpaRepository<WorkflowActionEntity, UUID> {
    List<WorkflowActionEntity> findByWorkflowIdAndIsDeletedFalse(UUID workflowId);
    List<WorkflowActionEntity> findByStepIdAndIsDeletedFalse(UUID stepId);
}
