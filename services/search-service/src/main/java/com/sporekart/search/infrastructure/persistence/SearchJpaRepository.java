package com.sporekart.search.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SearchJpaRepository extends JpaRepository<SearchDocumentEntity, String> {
    List<SearchDocumentEntity> findByEntityType(String entityType);
    Optional<SearchDocumentEntity> findByEntityTypeAndEntityId(String entityType, String entityId);
    void deleteByEntityTypeAndEntityId(String entityType, String entityId);
}
