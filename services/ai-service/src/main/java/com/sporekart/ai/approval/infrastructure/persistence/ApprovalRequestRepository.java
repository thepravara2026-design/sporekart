package com.sporekart.ai.approval.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApprovalRequestRepository extends JpaRepository<ApprovalRequestEntity, UUID> {
    Optional<ApprovalRequestEntity> findByIdAndIsDeletedFalse(UUID id);
    List<ApprovalRequestEntity> findByUserIdAndIsDeletedFalse(String userId);
    List<ApprovalRequestEntity> findByStatusAndIsDeletedFalse(String status);
    List<ApprovalRequestEntity> findByModuleAndIsDeletedFalse(String module);
}
