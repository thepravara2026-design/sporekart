package com.sporekart.ai.approval.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApprovalReviewerRepository extends JpaRepository<ApprovalReviewerEntity, UUID> {
    Optional<ApprovalReviewerEntity> findByIdAndIsDeletedFalse(UUID id);
    List<ApprovalReviewerEntity> findByUserIdAndIsDeletedFalse(String userId);
    List<ApprovalReviewerEntity> findByDepartmentAndIsDeletedFalse(String department);
    List<ApprovalReviewerEntity> findByIsAvailableTrue();
}
