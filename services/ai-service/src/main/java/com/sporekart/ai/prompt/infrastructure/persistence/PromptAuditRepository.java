package com.sporekart.ai.prompt.infrastructure.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PromptAuditRepository extends JpaRepository<PromptAuditEntity, UUID> {
    List<PromptAuditEntity> findByTemplateIdOrderByChangedAtDesc(UUID templateId);
    Page<PromptAuditEntity> findAllByOrderByChangedAtDesc(Pageable pageable);
    List<PromptAuditEntity> findByActionOrderByChangedAtDesc(String action);
}
