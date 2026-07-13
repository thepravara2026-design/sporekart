package com.sporekart.ai.content.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContentRequestRepository extends JpaRepository<ContentRequestEntity, UUID> {
    List<ContentRequestEntity> findByUserIdAndIsDeletedFalse(UUID userId);
    List<ContentRequestEntity> findByStatusAndIsDeletedFalse(String status);
    Optional<ContentRequestEntity> findByIdAndIsDeletedFalse(UUID id);
    List<ContentRequestEntity> findByUserIdOrderByCreatedAtDesc(UUID userId);
}
