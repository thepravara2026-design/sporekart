package com.sporekart.prompt.service;

import com.sporekart.prompt.dto.request.CreateVersionRequest;
import com.sporekart.prompt.dto.response.PromptVersionResponse;
import com.sporekart.prompt.entity.PromptVersionEntity;
import com.sporekart.prompt.repository.PromptVersionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PromptVersionService {

    private final PromptVersionRepository versionRepository;
    private final PromptAuditService auditService;

    public PromptVersionService(PromptVersionRepository versionRepository, PromptAuditService auditService) {
        this.versionRepository = versionRepository;
        this.auditService = auditService;
    }

    @Transactional
    public PromptVersionResponse createVersion(UUID templateId, CreateVersionRequest request) {
        var currentVersion = versionRepository.countByTemplateId(templateId);
        var entity = new PromptVersionEntity();
        entity.setTemplateId(templateId);
        entity.setVersion(currentVersion + 1);
        entity.setPromptBody(request.promptBody());
        entity.setSystemPrompt(request.systemPrompt());
        entity.setDeveloperPrompt(request.developerPrompt());
        entity.setUserPrompt(request.userPrompt());
        entity.setFewShotExamples(request.fewShotExamples());
        entity.setConversationInstructions(request.conversationInstructions());
        entity.setSafetyConstraints(request.safetyConstraints());
        entity.setProviderMetadata(request.providerMetadata());
        entity.setVariablesJson(request.variablesJson());
        entity.setProviderConstraints(request.providerConstraints());
        entity.setTemperature(request.temperature());
        entity.setTopP(request.topP());
        entity.setMaxTokens(request.maxTokens());
        entity.setPublished(false);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setCreatedBy(request.createdBy());
        entity.setChangeNotes(request.changeNotes());
        var saved = versionRepository.save(entity);

        auditService.record(templateId, saved.getId(),
                com.sporekart.prompt.domain.AuditAction.CREATED,
                request.createdBy(), "Created version " + saved.getVersion());

        return toResponse(saved);
    }

    public List<PromptVersionResponse> getVersions(UUID templateId) {
        return versionRepository.findByTemplateIdOrderByVersionDesc(templateId)
                .stream().map(this::toResponse).toList();
    }

    public Optional<PromptVersionResponse> getVersion(UUID templateId, Integer version) {
        return versionRepository.findByTemplateIdAndVersion(templateId, version)
                .map(this::toResponse);
    }

    public Optional<PromptVersionResponse> getPublishedVersion(UUID templateId) {
        return versionRepository.findTopByTemplateIdAndIsPublishedTrueOrderByVersionDesc(templateId)
                .map(this::toResponse);
    }

    public Optional<PromptVersionEntity> getLatestVersionEntity(UUID templateId) {
        return versionRepository.findTopByTemplateIdOrderByVersionDesc(templateId);
    }

    public Optional<PromptVersionResponse> getLatestVersion(UUID templateId) {
        return versionRepository.findTopByTemplateIdOrderByVersionDesc(templateId)
                .map(this::toResponse);
    }

    public int getNextVersionNumber(UUID templateId) {
        return versionRepository.countByTemplateId(templateId) + 1;
    }

    @Transactional
    public PromptVersionResponse publishVersion(UUID versionId, UUID performedBy) {
        var entity = versionRepository.findById(versionId)
                .orElseThrow(() -> new IllegalArgumentException("Version not found: " + versionId));

        var allVersions = versionRepository.findByTemplateIdAndIsPublishedTrueOrderByVersionDesc(entity.getTemplateId());
        for (var v : allVersions) {
            v.setPublished(false);
            versionRepository.save(v);
        }

        entity.setPublished(true);
        var saved = versionRepository.save(entity);

        auditService.record(entity.getTemplateId(), saved.getId(),
                com.sporekart.prompt.domain.AuditAction.PUBLISHED,
                performedBy, "Published version " + saved.getVersion());

        return toResponse(saved);
    }

    @Transactional
    public PromptVersionResponse rollback(UUID templateId, Integer targetVersion, UUID performedBy, String reason) {
        var targetOpt = versionRepository.findByTemplateIdAndVersion(templateId, targetVersion);
        if (targetOpt.isEmpty()) {
            throw new IllegalArgumentException("Target version not found: " + targetVersion);
        }
        var target = targetOpt.get();

        var currentVersion = versionRepository.countByTemplateId(templateId);
        var entity = new PromptVersionEntity();
        entity.setTemplateId(templateId);
        entity.setVersion(currentVersion + 1);
        entity.setPromptBody(target.getPromptBody());
        entity.setSystemPrompt(target.getSystemPrompt());
        entity.setDeveloperPrompt(target.getDeveloperPrompt());
        entity.setUserPrompt(target.getUserPrompt());
        entity.setFewShotExamples(target.getFewShotExamples());
        entity.setConversationInstructions(target.getConversationInstructions());
        entity.setSafetyConstraints(target.getSafetyConstraints());
        entity.setProviderMetadata(target.getProviderMetadata());
        entity.setVariablesJson(target.getVariablesJson());
        entity.setProviderConstraints(target.getProviderConstraints());
        entity.setTemperature(target.getTemperature());
        entity.setTopP(target.getTopP());
        entity.setMaxTokens(target.getMaxTokens());
        entity.setPublished(false);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setCreatedBy(performedBy);
        entity.setChangeNotes("Rollback to version " + targetVersion + (reason != null ? ": " + reason : ""));
        var saved = versionRepository.save(entity);

        var allVersions = versionRepository.findByTemplateIdAndIsPublishedTrueOrderByVersionDesc(templateId);
        for (var v : allVersions) {
            v.setPublished(false);
            versionRepository.save(v);
        }

        saved.setPublished(true);
        versionRepository.save(saved);

        auditService.record(templateId, saved.getId(),
                com.sporekart.prompt.domain.AuditAction.ROLLBACK,
                performedBy, "Rollback to version " + targetVersion + (reason != null ? ": " + reason : ""));

        return toResponse(saved);
    }

    private PromptVersionResponse toResponse(PromptVersionEntity e) {
        return PromptVersionResponse.builder()
                .id(e.getId())
                .templateId(e.getTemplateId())
                .version(e.getVersion())
                .promptBody(e.getPromptBody())
                .systemPrompt(e.getSystemPrompt())
                .developerPrompt(e.getDeveloperPrompt())
                .userPrompt(e.getUserPrompt())
                .fewShotExamples(e.getFewShotExamples())
                .conversationInstructions(e.getConversationInstructions())
                .safetyConstraints(e.getSafetyConstraints())
                .providerMetadata(e.getProviderMetadata())
                .variablesJson(e.getVariablesJson())
                .providerConstraints(e.getProviderConstraints())
                .temperature(e.getTemperature())
                .topP(e.getTopP())
                .maxTokens(e.getMaxTokens())
                .isPublished(e.isPublished())
                .createdAt(e.getCreatedAt())
                .createdBy(e.getCreatedBy())
                .changeNotes(e.getChangeNotes())
                .build();
    }
}
