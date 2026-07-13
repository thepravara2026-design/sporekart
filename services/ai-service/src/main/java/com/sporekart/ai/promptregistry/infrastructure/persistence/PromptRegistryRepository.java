package com.sporekart.ai.promptregistry.infrastructure.persistence;

import com.sporekart.ai.promptregistry.domain.PromptStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromptRegistryRepository extends JpaRepository<PromptRegistryEntity, java.util.UUID> {

    List<PromptRegistryEntity> findByPromptNameContaining(String name);

    List<PromptRegistryEntity> findByStatus(PromptStatus status);

    List<PromptRegistryEntity> findByTagsContaining(String tag);

    java.util.Optional<PromptRegistryEntity> findByPromptId(String promptId);
}
