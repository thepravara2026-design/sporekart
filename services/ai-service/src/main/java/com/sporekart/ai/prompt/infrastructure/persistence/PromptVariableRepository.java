package com.sporekart.ai.prompt.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PromptVariableRepository extends JpaRepository<PromptVariableEntity, UUID> {
    List<PromptVariableEntity> findByTemplateIdAndIsDeletedFalseOrderByDisplayOrder(UUID templateId);
    boolean existsByNameAndTemplateIdAndIsDeletedFalse(String name, UUID templateId);
}
