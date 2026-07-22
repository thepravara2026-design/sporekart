package com.sporekart.prompt.repository;

import com.sporekart.prompt.domain.ApprovalStatus;
import com.sporekart.prompt.entity.PromptApprovalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PromptApprovalRepository extends JpaRepository<PromptApprovalEntity, UUID> {
    List<PromptApprovalEntity> findByVersionIdOrderByRequestedAtAsc(UUID versionId);
    List<PromptApprovalEntity> findByTemplateIdOrderByRequestedAtDesc(UUID templateId);
    List<PromptApprovalEntity> findByApproverAndStatus(UUID approver, ApprovalStatus status);
    List<PromptApprovalEntity> findByStatus(ApprovalStatus status);
    Optional<PromptApprovalEntity> findTopByVersionIdAndStepOrderByRequestedAtDesc(UUID versionId, String step);
    long countByVersionIdAndStatus(UUID versionId, ApprovalStatus status);
}
