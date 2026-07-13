package com.sporekart.ai.workflow.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface WorkflowStepRepository extends JpaRepository<WorkflowStepEntity, UUID> {
    List<WorkflowStepEntity> findByWorkflowIdAndIsDeletedFalseOrderByOrderIndexAsc(UUID workflowId);
    java.util.Optional<WorkflowStepEntity> findByIdAndWorkflowIdAndIsDeletedFalse(UUID id, UUID workflowId);
}
