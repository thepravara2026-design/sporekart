package com.sporekart.ai.prompt.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PromptCategoryRepository extends JpaRepository<PromptCategoryEntity, UUID> {
    Optional<PromptCategoryEntity> findByNameAndIsDeletedFalse(String name);
    List<PromptCategoryEntity> findByIsDeletedFalseOrderByDisplayOrder();
    List<PromptCategoryEntity> findByIsDeletedFalseAndIsActiveTrueOrderByDisplayOrder();
    boolean existsByNameAndIsDeletedFalse(String name);
}
