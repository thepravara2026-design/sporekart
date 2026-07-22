package com.sporekart.prompt.repository;

import com.sporekart.prompt.entity.PromptVersionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PromptVersionRepository extends JpaRepository<PromptVersionEntity, UUID> {
    List<PromptVersionEntity> findByTemplateIdOrderByVersionDesc(UUID templateId);
    Optional<PromptVersionEntity> findByTemplateIdAndVersion(UUID templateId, Integer version);
    Optional<PromptVersionEntity> findTopByTemplateIdAndIsPublishedTrueOrderByVersionDesc(UUID templateId);
    Optional<PromptVersionEntity> findTopByTemplateIdOrderByVersionDesc(UUID templateId);
    int countByTemplateId(UUID templateId);
    List<PromptVersionEntity> findByTemplateIdAndIsPublishedTrueOrderByVersionDesc(UUID templateId);
}
