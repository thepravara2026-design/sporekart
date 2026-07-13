package com.sporekart.ai.semantic.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SemanticIndexStatisticsRepository extends JpaRepository<SemanticIndexStatisticsEntity, UUID> {
    List<SemanticIndexStatisticsEntity> findByIsDeletedFalse();
    Optional<SemanticIndexStatisticsEntity> findByIdAndIsDeletedFalse(UUID id);
    List<SemanticIndexStatisticsEntity> findByIndexNameAndIsDeletedFalse(String indexName);
    List<SemanticIndexStatisticsEntity> findByIndexNameAndStatKeyAndIsDeletedFalse(String indexName, String statKey);
    List<SemanticIndexStatisticsEntity> findByIndexNameAndIsDeletedFalseOrderByRecordedAtDesc(String indexName);
}
