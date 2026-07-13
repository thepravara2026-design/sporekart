package com.sporekart.ai.semantic.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SemanticVectorIndexRepository extends JpaRepository<SemanticVectorIndexEntity, UUID> {
    List<SemanticVectorIndexEntity> findByIsDeletedFalse();
    Optional<SemanticVectorIndexEntity> findByIdAndIsDeletedFalse(UUID id);
    Optional<SemanticVectorIndexEntity> findByNameAndIsDeletedFalse(String name);
    List<SemanticVectorIndexEntity> findByStatusAndIsDeletedFalse(String status);
}
