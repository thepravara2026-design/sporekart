package com.sporekart.ai.promptregistry.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PromptVersionHistoryRepository extends JpaRepository<PromptVersionHistoryEntity, UUID> {

    List<PromptVersionHistoryEntity> findByPromptIdOrderByVersionDesc(String promptId);

    Optional<PromptVersionHistoryEntity> findByPromptIdAndVersion(String promptId, int version);

    int countByPromptId(String promptId);
}
