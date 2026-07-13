package com.sporekart.ai.content.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContentClassificationRepository extends JpaRepository<ContentClassificationEntity, UUID> {
    Optional<ContentClassificationEntity> findByRequestId(UUID requestId);
    List<ContentClassificationEntity> findByPrimaryCategory(String primaryCategory);
}
