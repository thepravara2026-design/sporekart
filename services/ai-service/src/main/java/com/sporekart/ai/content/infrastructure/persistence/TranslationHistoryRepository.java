package com.sporekart.ai.content.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TranslationHistoryRepository extends JpaRepository<TranslationHistoryEntity, UUID> {
    Optional<TranslationHistoryEntity> findByRequestId(UUID requestId);
    List<TranslationHistoryEntity> findByTargetLanguage(String targetLanguage);
}
