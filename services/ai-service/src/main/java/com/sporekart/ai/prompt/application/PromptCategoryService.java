package com.sporekart.ai.prompt.application;

import com.sporekart.ai.prompt.domain.AuditAction;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptAuditRepository;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptCategoryEntity;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PromptCategoryService {

    private final PromptCategoryRepository categoryRepository;
    private final PromptAuditRepository auditRepository;

    public PromptCategoryService(PromptCategoryRepository categoryRepository,
                                 PromptAuditRepository auditRepository) {
        this.categoryRepository = categoryRepository;
        this.auditRepository = auditRepository;
    }

    public List<PromptCategoryEntity> listAll() {
        return categoryRepository.findByIsDeletedFalseOrderByDisplayOrder();
    }

    public List<PromptCategoryEntity> listActive() {
        return categoryRepository.findByIsDeletedFalseAndIsActiveTrueOrderByDisplayOrder();
    }

    public PromptCategoryEntity findById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new PromptNotFoundException("Category not found: " + id));
    }

    @Transactional
    public PromptCategoryEntity create(String name, String description, String icon, int displayOrder, UUID createdBy) {
        if (categoryRepository.existsByNameAndIsDeletedFalse(name)) {
            throw new PromptValidationException("Category already exists: " + name);
        }
        PromptCategoryEntity entity = new PromptCategoryEntity(name, description, icon, displayOrder);
        entity.setCreatedBy(createdBy);
        PromptCategoryEntity saved = categoryRepository.save(entity);
        auditRepository.save(new com.sporekart.ai.prompt.infrastructure.persistence.PromptAuditEntity(
                null, null, AuditAction.CATEGORY_CREATED.name(), "CATEGORY",
                saved.getId(), null, null, createdBy, "Created category: " + name));
        return saved;
    }

    @Transactional
    public PromptCategoryEntity update(UUID id, String name, String description, String icon,
                                        Integer displayOrder, Boolean isActive, UUID updatedBy) {
        PromptCategoryEntity entity = findById(id);
        if (name != null && !name.equals(entity.getName())) {
            if (categoryRepository.existsByNameAndIsDeletedFalse(name)) {
                throw new PromptValidationException("Category already exists: " + name);
            }
            entity.setName(name);
        }
        if (description != null) entity.setDescription(description);
        if (icon != null) entity.setIcon(icon);
        if (displayOrder != null) entity.setDisplayOrder(displayOrder);
        if (isActive != null) entity.setActive(isActive);
        entity.setUpdatedBy(updatedBy);
        entity.setUpdatedAt(OffsetDateTime.now());
        PromptCategoryEntity saved = categoryRepository.save(entity);
        auditRepository.save(new com.sporekart.ai.prompt.infrastructure.persistence.PromptAuditEntity(
                null, null, AuditAction.CATEGORY_UPDATED.name(), "CATEGORY",
                saved.getId(), null, null, updatedBy, "Updated category: " + name));
        return saved;
    }

    @Transactional
    public void delete(UUID id, UUID deletedBy) {
        PromptCategoryEntity entity = findById(id);
        entity.setDeleted(true);
        entity.setDeletedAt(OffsetDateTime.now());
        entity.setUpdatedBy(deletedBy);
        categoryRepository.save(entity);
        auditRepository.save(new com.sporekart.ai.prompt.infrastructure.persistence.PromptAuditEntity(
                null, null, AuditAction.CATEGORY_DELETED.name(), "CATEGORY",
                entity.getId(), null, null, deletedBy, "Deleted category: " + entity.getName()));
    }
}
