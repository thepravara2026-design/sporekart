package com.sporekart.ai.content.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface ContentFeedbackRepository extends JpaRepository<ContentFeedbackEntity, UUID> {
    List<ContentFeedbackEntity> findByRequestId(UUID requestId);
    List<ContentFeedbackEntity> findByUserId(UUID userId);
}
