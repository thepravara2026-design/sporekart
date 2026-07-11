package com.sporekart.ai.knowledge.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface KnowledgeDocumentRepository extends JpaRepository<KnowledgeDocumentEntity, UUID> {
    List<KnowledgeDocumentEntity> findByIsDeletedFalse();
    Optional<KnowledgeDocumentEntity> findByIdAndIsDeletedFalse(UUID id);
    List<KnowledgeDocumentEntity> findByCategoryIdAndIsDeletedFalse(UUID categoryId);
    List<KnowledgeDocumentEntity> findByStatusAndIsDeletedFalse(String status);
    List<KnowledgeDocumentEntity> findByVisibilityAndIsActiveTrue(String visibility);
    List<KnowledgeDocumentEntity> findByBusinessModuleAndIsDeletedFalse(String businessModule);
    @Query("SELECT d FROM KnowledgeDocumentEntity d WHERE d.isDeleted = false AND d.isActive = true " +
           "AND (LOWER(d.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(d.description) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(d.content) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<KnowledgeDocumentEntity> search(@Param("query") String query);
}
