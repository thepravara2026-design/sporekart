package com.sporekart.ai.workflow.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkflowExecutionHistoryRepository extends JpaRepository<WorkflowExecutionHistoryEntity, UUID> {
    List<WorkflowExecutionHistoryEntity> findByExecutionIdOrderByCreatedAtAsc(UUID executionId);
    List<WorkflowExecutionHistoryEntity> findByWorkflowIdOrderByCreatedAtDesc(UUID workflowId);
}
