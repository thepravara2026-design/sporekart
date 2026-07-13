package com.sporekart.ai.workflow.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkflowConditionRepository extends JpaRepository<WorkflowConditionEntity, UUID> {
    List<WorkflowConditionEntity> findByWorkflowIdAndIsDeletedFalse(UUID workflowId);
    List<WorkflowConditionEntity> findByStepIdAndIsDeletedFalse(UUID stepId);
}
