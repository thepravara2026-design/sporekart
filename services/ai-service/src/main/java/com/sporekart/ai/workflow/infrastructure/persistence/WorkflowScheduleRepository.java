package com.sporekart.ai.workflow.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkflowScheduleRepository extends JpaRepository<WorkflowScheduleEntity, UUID> {
    List<WorkflowScheduleEntity> findByWorkflowId(UUID workflowId);
    List<WorkflowScheduleEntity> findByIsActiveTrue();
}
