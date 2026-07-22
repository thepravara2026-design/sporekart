package com.sporekart.prompt.service;

import com.sporekart.prompt.domain.AuditAction;
import com.sporekart.prompt.domain.PromptStatus;
import com.sporekart.prompt.dto.request.CreatePromptRequest;
import com.sporekart.prompt.dto.request.UpdatePromptRequest;
import com.sporekart.prompt.dto.response.DashboardResponse;
import com.sporekart.prompt.dto.response.PromptTemplateResponse;
import com.sporekart.prompt.entity.PromptTagEntity;
import com.sporekart.prompt.entity.PromptTemplateEntity;
import com.sporekart.prompt.events.PromptEventPublisher;
import com.sporekart.prompt.repository.PromptTagRepository;
import com.sporekart.prompt.repository.PromptTemplateRepository;
import com.sporekart.prompt.repository.PromptUsageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PromptService {

    private final PromptTemplateRepository templateRepository;
    private final PromptTagRepository tagRepository;
    private final PromptUsageRepository usageRepository;
    private final PromptVersionService versionService;
    private final PromptAuditService auditService;
    private final PromptEventPublisher eventPublisher;

    public PromptService(PromptTemplateRepository templateRepository,
                         PromptTagRepository tagRepository,
                         PromptUsageRepository usageRepository,
                         PromptVersionService versionService,
                         PromptAuditService auditService,
                         PromptEventPublisher eventPublisher) {
        this.templateRepository = templateRepository;
        this.tagRepository = tagRepository;
        this.usageRepository = usageRepository;
        this.versionService = versionService;
        this.auditService = auditService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public PromptTemplateResponse createTemplate(CreatePromptRequest request) {
        if (templateRepository.existsBySlugAndIsDeletedFalse(request.slug())) {
            throw new IllegalArgumentException("Slug already exists: " + request.slug());
        }

        var entity = new PromptTemplateEntity();
        entity.setName(request.name());
        entity.setSlug(request.slug());
        entity.setDescription(request.description());
        entity.setCategory(request.category());
        entity.setScope(request.scope());
        entity.setStatus(PromptStatus.DRAFT);
        entity.setOwner(request.owner());
        entity.setCreatedBy(request.createdBy());
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        entity.setActive(true);
        entity.setDeleted(false);

        if (request.tags() != null && !request.tags().isEmpty()) {
            var tags = resolveTags(request.tags());
            entity.setTags(new HashSet<>(tags));
        }

        var saved = templateRepository.save(entity);

        auditService.record(saved.getId(), null, AuditAction.CREATED,
                request.createdBy(), "Created prompt template: " + request.name());

        eventPublisher.publishPromptCreated(saved.getId(), request.name(), request.createdBy());

        return toResponse(saved);
    }

    public Optional<PromptTemplateResponse> getTemplate(UUID id) {
        return templateRepository.findByIdAndIsDeletedFalse(id).map(this::toResponse);
    }

    public List<PromptTemplateResponse> listTemplates() {
        return templateRepository.findByIsDeletedFalse().stream().map(this::toResponse).toList();
    }

    @Transactional
    public PromptTemplateResponse updateTemplate(UUID id, UpdatePromptRequest request) {
        var entity = templateRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("Template not found: " + id));

        if (request.name() != null) entity.setName(request.name());
        if (request.slug() != null) {
            if (!request.slug().equals(entity.getSlug()) &&
                    templateRepository.existsBySlugAndIsDeletedFalse(request.slug())) {
                throw new IllegalArgumentException("Slug already exists: " + request.slug());
            }
            entity.setSlug(request.slug());
        }
        if (request.description() != null) entity.setDescription(request.description());
        if (request.category() != null) entity.setCategory(request.category());
        if (request.scope() != null) entity.setScope(request.scope());
        if (request.owner() != null) entity.setOwner(request.owner());
        entity.setUpdatedBy(request.updatedBy());
        entity.setUpdatedAt(OffsetDateTime.now());

        if (request.tags() != null) {
            var tags = resolveTags(request.tags());
            entity.setTags(new HashSet<>(tags));
        }

        var saved = templateRepository.save(entity);

        auditService.record(id, null, AuditAction.UPDATED,
                request.updatedBy(), "Updated prompt template");

        eventPublisher.promptUpdated(saved.getId(), request.updatedBy());

        return toResponse(saved);
    }

    @Transactional
    public void archiveTemplate(UUID id, UUID performedBy) {
        var entity = templateRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("Template not found: " + id));
        entity.setStatus(PromptStatus.ARCHIVED);
        entity.setActive(false);
        entity.setUpdatedAt(OffsetDateTime.now());
        templateRepository.save(entity);

        auditService.record(id, null, AuditAction.ARCHIVED, performedBy, "Archived prompt template");
        eventPublisher.promptArchived(id, performedBy);
    }

    @Transactional
    public void deleteTemplate(UUID id, UUID performedBy) {
        var entity = templateRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("Template not found: " + id));
        entity.setDeleted(true);
        entity.setUpdatedAt(OffsetDateTime.now());
        templateRepository.save(entity);

        auditService.record(id, null, AuditAction.DELETED, performedBy, "Deleted prompt template");
    }

    @Transactional
    public PromptTemplateResponse publishTemplate(UUID templateId, UUID versionId, UUID performedBy) {
        var entity = templateRepository.findByIdAndIsDeletedFalse(templateId)
                .orElseThrow(() -> new IllegalArgumentException("Template not found: " + templateId));

        var versionResponse = versionService.publishVersion(versionId, performedBy);

        entity.setStatus(PromptStatus.PUBLISHED);
        entity.setUpdatedAt(OffsetDateTime.now());
        var saved = templateRepository.save(entity);

        auditService.record(templateId, versionId, AuditAction.PUBLISHED,
                performedBy, "Published version " + versionResponse.version());

        eventPublisher.promptPublished(templateId, versionId, performedBy);

        return toResponse(saved);
    }

    @Transactional
    public PromptTemplateResponse rollbackTemplate(UUID templateId, Integer targetVersion, UUID performedBy, String reason) {
        versionService.rollback(templateId, targetVersion, performedBy, reason);

        var entity = templateRepository.findByIdAndIsDeletedFalse(templateId)
                .orElseThrow(() -> new IllegalArgumentException("Template not found: " + templateId));
        entity.setStatus(PromptStatus.PUBLISHED);
        entity.setUpdatedAt(OffsetDateTime.now());
        var saved = templateRepository.save(entity);

        eventPublisher.promptRollback(templateId, targetVersion, performedBy);

        return toResponse(saved);
    }

    public List<PromptTemplateResponse> search(String query) {
        return templateRepository.search(query).stream().map(this::toResponse).toList();
    }

    public List<PromptTemplateResponse> filterByCategory(String category) {
        var cat = Arrays.stream(com.sporekart.prompt.domain.PromptCategory.values())
                .filter(c -> c.name().equalsIgnoreCase(category))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid category: " + category));
        return templateRepository.findByCategoryAndIsDeletedFalse(cat).stream()
                .map(this::toResponse).toList();
    }

    public List<PromptTemplateResponse> filterByStatus(String status) {
        var stat = Arrays.stream(PromptStatus.values())
                .filter(s -> s.name().equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid status: " + status));
        return templateRepository.findByStatusAndIsDeletedFalse(stat).stream()
                .map(this::toResponse).toList();
    }

    public List<PromptTemplateResponse> filterByOwner(UUID owner) {
        return templateRepository.findByOwnerAndIsDeletedFalse(owner).stream()
                .map(this::toResponse).toList();
    }

    public DashboardResponse getDashboard() {
        var allTemplates = templateRepository.findByIsDeletedFalse();
        long total = allTemplates.size();
        long published = allTemplates.stream().filter(t -> t.getStatus() == PromptStatus.PUBLISHED).count();
        long drafts = allTemplates.stream().filter(t -> t.getStatus() == PromptStatus.DRAFT).count();
        long totalExecutions = usageRepository.countExecutionsSince(OffsetDateTime.now().minusDays(30));

        var recent = allTemplates.stream()
                .sorted(Comparator.comparing(PromptTemplateEntity::getUpdatedAt).reversed())
                .limit(10)
                .map(this::toResponse)
                .toList();

        var mostUsed = usageRepository.findMostUsedPrompts().stream()
                .limit(5)
                .map(obj -> templateRepository.findByIdAndIsDeletedFalse((UUID) obj[0]).map(this::toResponse).orElse(null))
                .filter(Objects::nonNull)
                .toList();

        var leastUsed = usageRepository.findLeastUsedPrompts().stream()
                .limit(5)
                .map(obj -> templateRepository.findByIdAndIsDeletedFalse((UUID) obj[0]).map(this::toResponse).orElse(null))
                .filter(Objects::nonNull)
                .toList();

        return new DashboardResponse(total, published, drafts, 0, totalExecutions, recent, mostUsed, leastUsed);
    }

    private List<PromptTagEntity> resolveTags(List<String> tagNames) {
        return tagNames.stream().map(name -> {
            var normalized = name.trim().toLowerCase();
            return tagRepository.findByName(normalized)
                    .orElseGet(() -> {
                        var tag = new PromptTagEntity();
                        tag.setName(normalized);
                        return tagRepository.save(tag);
                    });
        }).collect(Collectors.toList());
    }

    public PromptTemplateResponse toResponse(PromptTemplateEntity e) {
        var latestVer = versionService.getLatestVersion(e.getId());
        var publishedVer = versionService.getPublishedVersion(e.getId());

        return PromptTemplateResponse.builder()
                .id(e.getId())
                .name(e.getName())
                .slug(e.getSlug())
                .description(e.getDescription())
                .category(e.getCategory())
                .scope(e.getScope())
                .status(e.getStatus())
                .owner(e.getOwner())
                .createdBy(e.getCreatedBy())
                .updatedBy(e.getUpdatedBy())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .isActive(e.isActive())
                .tags(e.getTags().stream().map(PromptTagEntity::getName).toList())
                .latestVersion(latestVer.map(com.sporekart.prompt.dto.response.PromptVersionResponse::version).orElse(null))
                .latestVersionId(latestVer.map(com.sporekart.prompt.dto.response.PromptVersionResponse::id).orElse(null))
                .isPublished(publishedVer.isPresent())
                .build();
    }
}
