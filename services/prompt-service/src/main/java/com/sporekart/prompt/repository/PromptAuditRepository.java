package com.sporekart.prompt.repository;

import com.sporekart.prompt.entity.PromptAuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PromptAuditRepository extends JpaRepository<PromptAuditEntity, UUID> {
    List<PromptAuditEntity> findByTemplateIdOrderByCreatedAtDesc(UUID templateId);
    List<PromptAuditEntity> findByTemplateIdAndVersionIdOrderByCreatedAtDesc(UUID templateId, UUID versionId);
}
