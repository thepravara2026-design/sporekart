package com.sporekart.prompt.repository;

import com.sporekart.prompt.domain.PromptCategory;
import com.sporekart.prompt.domain.PromptStatus;
import com.sporekart.prompt.entity.PromptTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PromptTemplateRepository extends JpaRepository<PromptTemplateEntity, UUID> {
    Optional<PromptTemplateEntity> findByIdAndIsDeletedFalse(UUID id);
    List<PromptTemplateEntity> findByIsDeletedFalse();
    Optional<PromptTemplateEntity> findBySlugAndIsDeletedFalse(String slug);
    List<PromptTemplateEntity> findByCategoryAndIsDeletedFalse(PromptCategory category);
    List<PromptTemplateEntity> findByStatusAndIsDeletedFalse(PromptStatus status);
    List<PromptTemplateEntity> findByOwnerAndIsDeletedFalse(UUID owner);
    List<PromptTemplateEntity> findByCreatedByAndIsDeletedFalse(UUID createdBy);
    boolean existsBySlugAndIsDeletedFalse(String slug);

    @Query("SELECT t FROM PromptTemplateEntity t JOIN t.tags tag WHERE tag.name IN :tags AND t.isDeleted = false")
    List<PromptTemplateEntity> findByTagNames(@Param("tags") List<String> tags);

    @Query("SELECT t FROM PromptTemplateEntity t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(t.description) LIKE LOWER(CONCAT('%', :query, '%')) AND t.isDeleted = false")
    List<PromptTemplateEntity> search(@Param("query") String query);
}
