package com.sporekart.ai.prompt.application;

import com.sporekart.ai.prompt.domain.AuditAction;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptAuditRepository;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptTemplateEntity;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptTemplateRepository;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptVersionEntity;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptVersionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PromptVersionService {

    private final PromptVersionRepository versionRepository;
    private final PromptTemplateRepository templateRepository;
    private final PromptAuditRepository auditRepository;

    public PromptVersionService(PromptVersionRepository versionRepository,
                                PromptTemplateRepository templateRepository,
                                PromptAuditRepository auditRepository) {
        this.versionRepository = versionRepository;
        this.templateRepository = templateRepository;
        this.auditRepository = auditRepository;
    }

    public List<PromptVersionEntity> listVersions(UUID templateId) {
        return versionRepository.findByTemplateIdAndIsDeletedFalseOrderByVersionNumberDesc(templateId);
    }

    public PromptVersionEntity findVersion(UUID templateId, int versionNumber) {
        return versionRepository.findByTemplateIdAndVersionNumberAndIsDeletedFalse(templateId, versionNumber)
                .orElseThrow(() -> new PromptNotFoundException("Version not found: " + templateId + " v" + versionNumber));
    }

    @Transactional
    public PromptVersionEntity createVersion(UUID templateId, String templateText, String changeNotes, UUID createdBy) {
        PromptTemplateEntity template = templateRepository.findByIdAndIsDeletedFalse(templateId)
                .orElseThrow(() -> new PromptNotFoundException("Template not found: " + templateId));

        int nextVersion = versionRepository.countByTemplateIdAndIsDeletedFalse(templateId) + 1;

        PromptVersionEntity version = new PromptVersionEntity();
        version.setTemplate(template);
        version.setVersionNumber(nextVersion);
        version.setTemplateText(templateText != null ? templateText : template.getTemplateText());
        version.setStatus("DRAFT");
        version.setChangeNotes(changeNotes);
        version.setCreatedBy(createdBy);
        version.setCreatedAt(OffsetDateTime.now());

        PromptVersionEntity saved = versionRepository.save(version);

        template.setTemplateText(version.getTemplateText());
        template.setCurrentVersion(nextVersion);
        template.setUpdatedBy(createdBy);
        template.setUpdatedAt(OffsetDateTime.now());
        templateRepository.save(template);

        auditRepository.save(new com.sporekart.ai.prompt.infrastructure.persistence.PromptAuditEntity(
                templateId, saved.getId(), AuditAction.VERSION_CREATED.name(), "VERSION",
                saved.getId(), null, null, createdBy, "Created version " + nextVersion));
        return saved;
    }

    @Transactional
    public PromptVersionEntity publishVersion(UUID versionId, UUID approvedBy) {
        PromptVersionEntity version = versionRepository.findByIdAndIsDeletedFalse(versionId)
                .orElseThrow(() -> new PromptNotFoundException("Version not found: " + versionId));
        version.setStatus("PUBLISHED");
        version.setApprovedBy(approvedBy);
        version.setApprovedAt(OffsetDateTime.now());
        version.setActivationDate(OffsetDateTime.now());
        PromptVersionEntity saved = versionRepository.save(version);

        PromptTemplateEntity template = saved.getTemplate();
        template.setStatus("PUBLISHED");
        template.setTemplateText(saved.getTemplateText());
        template.setCurrentVersion(saved.getVersionNumber());
        template.setUpdatedBy(approvedBy);
        template.setUpdatedAt(OffsetDateTime.now());
        templateRepository.save(template);

        auditRepository.save(new com.sporekart.ai.prompt.infrastructure.persistence.PromptAuditEntity(
                template.getId(), saved.getId(), AuditAction.VERSION_PUBLISHED.name(), "VERSION",
                saved.getId(), null, null, approvedBy, "Published version " + saved.getVersionNumber()));
        return saved;
    }

    @Transactional
    public PromptVersionEntity deprecateVersion(UUID versionId, UUID deprecatedBy) {
        PromptVersionEntity version = versionRepository.findByIdAndIsDeletedFalse(versionId)
                .orElseThrow(() -> new PromptNotFoundException("Version not found: " + versionId));
        version.setStatus("DEPRECATED");
        PromptVersionEntity saved = versionRepository.save(version);

        auditRepository.save(new com.sporekart.ai.prompt.infrastructure.persistence.PromptAuditEntity(
                version.getTemplate().getId(), saved.getId(), AuditAction.VERSION_DEPRECATED.name(), "VERSION",
                saved.getId(), null, null, deprecatedBy, "Deprecated version " + saved.getVersionNumber()));
        return saved;
    }

    @Transactional
    public PromptVersionEntity rollback(UUID templateId, int targetVersion, UUID rolledBackBy) {
        PromptVersionEntity target = findVersion(templateId, targetVersion);
        PromptTemplateEntity template = target.getTemplate();

        PromptVersionEntity rollbackVersion = new PromptVersionEntity();
        rollbackVersion.setTemplate(template);
        rollbackVersion.setVersionNumber(versionRepository.countByTemplateIdAndIsDeletedFalse(templateId) + 1);
        rollbackVersion.setTemplateText(target.getTemplateText());
        rollbackVersion.setStatus("PUBLISHED");
        rollbackVersion.setChangeNotes("Rollback to version " + targetVersion);
        rollbackVersion.setCreatedBy(rolledBackBy);
        rollbackVersion.setCreatedAt(OffsetDateTime.now());
        rollbackVersion.setApprovedBy(rolledBackBy);
        rollbackVersion.setApprovedAt(OffsetDateTime.now());
        rollbackVersion.setActivationDate(OffsetDateTime.now());

        PromptVersionEntity saved = versionRepository.save(rollbackVersion);

        template.setTemplateText(target.getTemplateText());
        template.setCurrentVersion(saved.getVersionNumber());
        template.setStatus("PUBLISHED");
        template.setUpdatedBy(rolledBackBy);
        template.setUpdatedAt(OffsetDateTime.now());
        templateRepository.save(template);

        auditRepository.save(new com.sporekart.ai.prompt.infrastructure.persistence.PromptAuditEntity(
                templateId, saved.getId(), AuditAction.VERSION_ROLLED_BACK.name(), "VERSION",
                saved.getId(), null, null, rolledBackBy, "Rolled back to version " + targetVersion));
        return saved;
    }

    @Transactional
    public void archiveVersion(UUID versionId, UUID archivedBy) {
        PromptVersionEntity version = versionRepository.findByIdAndIsDeletedFalse(versionId)
                .orElseThrow(() -> new PromptNotFoundException("Version not found: " + versionId));
        version.setStatus("ARCHIVED");
        version.setDeleted(true);
        version.setDeletedAt(OffsetDateTime.now());
        versionRepository.save(version);

        auditRepository.save(new com.sporekart.ai.prompt.infrastructure.persistence.PromptAuditEntity(
                version.getTemplate().getId(), versionId, AuditAction.VERSION_ARCHIVED.name(), "VERSION",
                versionId, null, null, archivedBy, "Archived version " + version.getVersionNumber()));
    }
}
