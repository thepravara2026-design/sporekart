package com.sporekart.ai.prompt.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PromptTemplateRepository extends JpaRepository<PromptTemplateEntity, UUID> {
    List<PromptTemplateEntity> findByIsDeletedFalse();
    Optional<PromptTemplateEntity> findByIdAndIsDeletedFalse(UUID id);
    List<PromptTemplateEntity> findByCategoryIdAndIsDeletedFalse(UUID categoryId);
    List<PromptTemplateEntity> findByStatusAndIsDeletedFalse(String status);
    List<PromptTemplateEntity> findByIsDeletedFalseAndIsActiveTrue();
    Optional<PromptTemplateEntity> findByNameAndCategoryIdAndIsDeletedFalse(String name, UUID categoryId);

    @Query("SELECT t FROM PromptTemplateEntity t WHERE t.isDeleted = false AND " +
           "(LOWER(t.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(t.description) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<PromptTemplateEntity> search(@Param("query") String query);

    boolean existsByNameAndCategoryIdAndIsDeletedFalse(String name, UUID categoryId);
}
