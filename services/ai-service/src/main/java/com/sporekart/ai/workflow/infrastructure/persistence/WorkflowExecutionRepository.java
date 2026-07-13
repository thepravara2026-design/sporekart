package com.sporekart.ai.workflow.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkflowExecutionRepository extends JpaRepository<WorkflowExecutionEntity, UUID> {
    List<WorkflowExecutionEntity> findByWorkflowIdAndIsDeletedFalseOrderByStartedAtDesc(UUID workflowId);
    List<WorkflowExecutionEntity> findAllByIsDeletedFalseOrderByStartedAtDesc();
    Optional<WorkflowExecutionEntity> findById(UUID id);
}
