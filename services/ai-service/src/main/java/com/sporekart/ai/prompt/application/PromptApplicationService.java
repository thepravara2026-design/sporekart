package com.sporekart.ai.prompt.application;

import com.sporekart.ai.prompt.infrastructure.persistence.PromptTemplateEntity;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptTemplateRepository;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptCategoryEntity;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptCategoryRepository;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptVariableEntity;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptVariableRepository;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptAuditRepository;
import com.sporekart.ai.prompt.domain.AuditAction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PromptApplicationService {

    private final PromptTemplateRepository templateRepository;
    private final PromptCategoryRepository categoryRepository;
    private final PromptVariableRepository variableRepository;
    private final PromptAuditRepository auditRepository;
    private final PromptValidationService validationService;
    private final PromptRenderService renderService;
    private final PromptVersionService versionService;
    private final PromptLifecycleService lifecycleService;

    public PromptApplicationService(PromptTemplateRepository templateRepository,
                                    PromptCategoryRepository categoryRepository,
                                    PromptVariableRepository variableRepository,
                                    PromptAuditRepository auditRepository,
                                    PromptValidationService validationService,
                                    PromptRenderService renderService,
                                    PromptVersionService versionService,
                                    PromptLifecycleService lifecycleService) {
        this.templateRepository = templateRepository;
        this.categoryRepository = categoryRepository;
        this.variableRepository = variableRepository;
        this.auditRepository = auditRepository;
        this.validationService = validationService;
        this.renderService = renderService;
        this.versionService = versionService;
        this.lifecycleService = lifecycleService;
    }

    public List<PromptTemplateEntity> listTemplates() {
        return templateRepository.findByIsDeletedFalse();
    }

    public PromptTemplateEntity getTemplate(UUID id) {
        return templateRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new PromptNotFoundException("Template not found: " + id));
    }

    @Transactional
    public PromptTemplateEntity createTemplate(UUID categoryId, String name, String description,
                                                String templateText, List<PromptVariableInput> variables,
                                                UUID createdBy) {
        PromptCategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new PromptNotFoundException("Category not found: " + categoryId));
        if (templateRepository.existsByNameAndCategoryIdAndIsDeletedFalse(name, categoryId)) {
            throw new PromptValidationException("Template already exists in this category: " + name);
        }

        PromptTemplateEntity template = new PromptTemplateEntity();
        template.setCategory(category);
        template.setName(name);
        template.setDescription(description);
        template.setTemplateText(templateText);
        template.setStatus("DRAFT");
        template.setCreatedBy(createdBy);
        template.setCreatedAt(OffsetDateTime.now());
        validationService.validateTemplate(template);

        PromptTemplateEntity saved = templateRepository.save(template);

        if (variables != null) {
            int order = 0;
            for (PromptVariableInput var : variables) {
                PromptVariableEntity varEntity = new PromptVariableEntity();
                varEntity.setTemplate(saved);
                varEntity.setName(var.name());
                varEntity.setVarType(var.type() != null ? var.type() : "STRING");
                varEntity.setRequired(var.required());
                varEntity.setDefaultValue(var.defaultValue());
                varEntity.setDescription(var.description());
                varEntity.setValidationRegex(var.validationRegex());
                varEntity.setDisplayOrder(order++);
                varEntity.setCreatedAt(OffsetDateTime.now());
                variableRepository.save(varEntity);
            }
        }

        auditRepository.save(new com.sporekart.ai.prompt.infrastructure.persistence.PromptAuditEntity(
                saved.getId(), null, AuditAction.TEMPLATE_CREATED.name(), "TEMPLATE",
                saved.getId(), null, null, createdBy, "Created template: " + name));
        return saved;
    }

    @Transactional
    public PromptTemplateEntity updateTemplate(UUID id, UUID categoryId, String name, String description,
                                                String templateText, UUID updatedBy) {
        PromptTemplateEntity template = getTemplate(id);
        if (categoryId != null) {
            PromptCategoryEntity category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new PromptNotFoundException("Category not found: " + categoryId));
            template.setCategory(category);
        }
        if (name != null && !name.equals(template.getName())) {
            if (templateRepository.existsByNameAndCategoryIdAndIsDeletedFalse(name,
                    template.getCategory().getId())) {
                throw new PromptValidationException("Template already exists: " + name);
            }
            template.setName(name);
        }
        if (description != null) template.setDescription(description);
        if (templateText != null) {
            template.setTemplateText(templateText);
            validationService.validateTemplate(template);
        }
        template.setUpdatedBy(updatedBy);
        template.setUpdatedAt(OffsetDateTime.now());

        PromptTemplateEntity saved = templateRepository.save(template);

        auditRepository.save(new com.sporekart.ai.prompt.infrastructure.persistence.PromptAuditEntity(
                saved.getId(), null, AuditAction.TEMPLATE_UPDATED.name(), "TEMPLATE",
                saved.getId(), null, null, updatedBy, "Updated template: " + saved.getName()));
        return saved;
    }

    @Transactional
    public void deleteTemplate(UUID id, UUID deletedBy) {
        PromptTemplateEntity template = getTemplate(id);
        template.setDeleted(true);
        template.setDeletedAt(OffsetDateTime.now());
        template.setUpdatedBy(deletedBy);
        templateRepository.save(template);
        auditRepository.save(new com.sporekart.ai.prompt.infrastructure.persistence.PromptAuditEntity(
                id, null, AuditAction.TEMPLATE_DELETED.name(), "TEMPLATE",
                id, null, null, deletedBy, "Deleted template: " + template.getName()));
    }

    public String renderTemplate(UUID id, java.util.Map<String, Object> variables) {
        PromptTemplateEntity template = getTemplate(id);
        return renderService.render(template, variables);
    }

    public record PromptVariableInput(String name, String type, boolean required,
                                       String defaultValue, String description, String validationRegex) {}
}
