package com.sporekart.prompt.repository;

import com.sporekart.prompt.entity.PromptTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PromptTagRepository extends JpaRepository<PromptTagEntity, UUID> {
    Optional<PromptTagEntity> findByName(String name);
    boolean existsByName(String name);
}
