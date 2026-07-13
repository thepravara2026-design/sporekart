package com.sporekart.ai.content.application;

import com.sporekart.ai.content.api.ContentTemplateService;
import com.sporekart.ai.content.domain.ContentCategory;
import com.sporekart.ai.content.domain.ContentTemplate;
import com.sporekart.ai.content.infrastructure.persistence.ContentTemplateEntity;
import com.sporekart.ai.content.infrastructure.persistence.ContentTemplateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ContentTemplateServiceImpl implements ContentTemplateService {

    private static final Logger log = LoggerFactory.getLogger(ContentTemplateServiceImpl.class);
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{\\{([^}]+)}}");

    private final ContentTemplateRepository repository;

    public ContentTemplateServiceImpl(ContentTemplateRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public ContentTemplate createTemplate(ContentTemplate template) {
        log.info("Creating template: {}", template.name());
        var entity = toEntity(template);
        entity.setId(UUID.randomUUID());
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        entity.setDeleted(false);
        entity.setActive(true);
        entity.setVersion(1);
        var saved = repository.save(entity);
        log.info("Created template {}: {}", saved.getId(), saved.getName());
        return toDomain(saved);
    }

    @Override
    @Transactional
    public ContentTemplate updateTemplate(ContentTemplate template) {
        log.info("Updating template: {}", template.id());
        var entity = repository.findByIdAndIsDeletedFalse(template.id())
                .orElseThrow(() -> new ContentException("Template not found: " + template.id()));
        entity.setName(template.name());
        entity.setDescription(template.description());
        entity.setCategory(template.category().name());
        entity.setContentType(template.contentType().name());
        entity.setTemplateContent(template.templateContent());
        entity.setVariables(template.variables() != null ? String.join(",", template.variables()) : null);
        entity.setActive(template.isActive());
        entity.setVersion(entity.getVersion() + 1);
        entity.setUpdatedAt(OffsetDateTime.now());
        var saved = repository.save(entity);
        log.info("Updated template {} to version {}", saved.getId(), saved.getVersion());
        return toDomain(saved);
    }

    @Override
    @Transactional
    public void deleteTemplate(UUID id) {
        log.info("Deleting template: {}", id);
        var entity = repository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ContentException("Template not found: " + id));
        entity.setDeleted(true);
        entity.setUpdatedAt(OffsetDateTime.now());
        repository.save(entity);
        log.info("Deleted template {}", id);
    }

    @Override
    public Optional<ContentTemplate> getTemplate(UUID id) {
        return repository.findByIdAndIsDeletedFalse(id).map(this::toDomain);
    }

    @Override
    public List<ContentTemplate> listTemplates(ContentCategory category) {
        return repository.findByCategoryAndIsDeletedFalse(category.name())
                .stream().map(this::toDomain).toList();
    }

    @Override
    public ContentTemplate renderTemplate(UUID templateId, Map<String, Object> variables) {
        log.info("Rendering template: {}", templateId);
        var template = repository.findByIdAndIsDeletedFalse(templateId)
                .orElseThrow(() -> new ContentException("Template not found: " + templateId));
        var renderedContent = template.getTemplateContent();
        var varBuilder = new StringBuilder(renderedContent);
        var matcher = VARIABLE_PATTERN.matcher(varBuilder);
        var result = new StringBuffer();
        while (matcher.find()) {
            var varName = matcher.group(1).trim();
            var value = variables.getOrDefault(varName, "{{" + varName + "}}");
            matcher.appendReplacement(result, Matcher.quoteReplacement(String.valueOf(value)));
        }
        matcher.appendTail(result);
        var rendered = toDomain(template);
        return new ContentTemplate(
                rendered.id(),
                rendered.name(),
                rendered.description(),
                rendered.category(),
                rendered.contentType(),
                result.toString(),
                rendered.variables(),
                rendered.isActive(),
                rendered.version(),
                rendered.createdBy(),
                rendered.createdAt(),
                OffsetDateTime.now()
        );
    }

    private ContentTemplateEntity toEntity(ContentTemplate template) {
        var entity = new ContentTemplateEntity();
        if (template.id() != null) {
            entity.setId(template.id());
        }
        entity.setName(template.name());
        entity.setDescription(template.description());
        entity.setCategory(template.category().name());
        entity.setContentType(template.contentType().name());
        entity.setTemplateContent(template.templateContent());
        entity.setVariables(template.variables() != null ? String.join(",", template.variables()) : null);
        entity.setActive(template.isActive());
        entity.setVersion(template.version());
        entity.setCreatedBy(template.createdBy());
        return entity;
    }

    private ContentTemplate toDomain(ContentTemplateEntity entity) {
        return new ContentTemplate(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                ContentCategory.valueOf(entity.getCategory()),
                com.sporekart.ai.core.domain.ContentType.valueOf(entity.getContentType()),
                entity.getTemplateContent(),
                entity.getVariables() != null ? List.of(entity.getVariables().split(",")) : List.of(),
                entity.isActive(),
                entity.getVersion(),
                entity.getCreatedBy(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
