package com.sporekart.ai.semantic.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SemanticSearchHistoryRepository extends JpaRepository<SemanticSearchHistoryEntity, UUID> {
    List<SemanticSearchHistoryEntity> findByIsDeletedFalse();
    Optional<SemanticSearchHistoryEntity> findByIdAndIsDeletedFalse(UUID id);
    List<SemanticSearchHistoryEntity> findBySearchTypeAndIsDeletedFalse(String searchType);
    List<SemanticSearchHistoryEntity> findByCreatedByAndIsDeletedFalse(UUID createdBy);
    List<SemanticSearchHistoryEntity> findByIsDeletedFalseOrderByCreatedAtDesc();
}
