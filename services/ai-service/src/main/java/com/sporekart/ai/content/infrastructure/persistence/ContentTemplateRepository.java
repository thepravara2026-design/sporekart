package com.sporekart.ai.content.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContentTemplateRepository extends JpaRepository<ContentTemplateEntity, UUID> {
    Optional<ContentTemplateEntity> findByIdAndIsDeletedFalse(UUID id);
    List<ContentTemplateEntity> findByCategoryAndIsDeletedFalse(String category);
    List<ContentTemplateEntity> findByIsActiveAndIsDeletedFalse(boolean isActive);
    List<ContentTemplateEntity> findByIsDeletedFalse();
}
