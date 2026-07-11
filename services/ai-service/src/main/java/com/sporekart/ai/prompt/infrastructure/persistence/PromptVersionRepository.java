package com.sporekart.ai.prompt.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PromptVersionRepository extends JpaRepository<PromptVersionEntity, UUID> {
    List<PromptVersionEntity> findByTemplateIdAndIsDeletedFalseOrderByVersionNumberDesc(UUID templateId);
    Optional<PromptVersionEntity> findByTemplateIdAndVersionNumberAndIsDeletedFalse(UUID templateId, int versionNumber);
    Optional<PromptVersionEntity> findTopByTemplateIdAndStatusAndIsDeletedFalseOrderByVersionNumberDesc(UUID templateId, String status);
    Optional<PromptVersionEntity> findByIdAndIsDeletedFalse(UUID id);
    int countByTemplateIdAndIsDeletedFalse(UUID templateId);
}
