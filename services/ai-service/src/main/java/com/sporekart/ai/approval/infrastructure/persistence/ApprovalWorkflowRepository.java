package com.sporekart.ai.approval.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApprovalWorkflowRepository extends JpaRepository<ApprovalWorkflowEntity, UUID> {
    Optional<ApprovalWorkflowEntity> findByIdAndIsDeletedFalse(UUID id);
    List<ApprovalWorkflowEntity> findByIsDeletedFalse();
    List<ApprovalWorkflowEntity> findByModuleAndIsDeletedFalse(String module);
}
