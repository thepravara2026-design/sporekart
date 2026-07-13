package com.sporekart.ai.content.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface ContentHistoryRepository extends JpaRepository<ContentHistoryEntity, UUID> {
    List<ContentHistoryEntity> findByUserIdOrderByCreatedAtDesc(UUID userId);
    List<ContentHistoryEntity> findByRequestId(UUID requestId);
}
