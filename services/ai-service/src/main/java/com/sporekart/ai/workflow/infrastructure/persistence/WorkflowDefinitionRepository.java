package com.sporekart.ai.workflow.infrastructure.persistence;

import com.sporekart.ai.workflow.domain.WorkflowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkflowDefinitionRepository extends JpaRepository<WorkflowDefinitionEntity, UUID> {
    List<WorkflowDefinitionEntity> findByIsDeletedFalse();
    Optional<WorkflowDefinitionEntity> findByIdAndIsDeletedFalse(UUID id);
    List<WorkflowDefinitionEntity> findByStatusAndIsDeletedFalse(WorkflowStatus status);
    List<WorkflowDefinitionEntity> findByCreatedByAndIsDeletedFalse(UUID createdBy);
}
