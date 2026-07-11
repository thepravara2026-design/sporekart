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
import java.util.UUID;

@Service
public class PromptLifecycleService {

    private final PromptTemplateRepository templateRepository;
    private final PromptVersionRepository versionRepository;
    private final PromptAuditRepository auditRepository;

    public PromptLifecycleService(PromptTemplateRepository templateRepository,
                                  PromptVersionRepository versionRepository,
                                  PromptAuditRepository auditRepository) {
        this.templateRepository = templateRepository;
        this.versionRepository = versionRepository;
        this.auditRepository = auditRepository;
    }

    @Transactional
    public PromptTemplateEntity submitForApproval(UUID templateId, UUID submittedBy) {
        PromptTemplateEntity template = templateRepository.findByIdAndIsDeletedFalse(templateId)
                .orElseThrow(() -> new PromptNotFoundException("Template not found: " + templateId));
        if (!"DRAFT".equals(template.getStatus())) {
            throw new PromptLifecycleException("Only DRAFT templates can be submitted for approval");
        }
        template.setStatus("PENDING_APPROVAL");
        template.setUpdatedBy(submittedBy);
        template.setUpdatedAt(OffsetDateTime.now());
        PromptTemplateEntity saved = templateRepository.save(template);

        auditRepository.save(new com.sporekart.ai.prompt.infrastructure.persistence.PromptAuditEntity(
                templateId, null, AuditAction.STATUS_CHANGED.name(), "TEMPLATE",
                templateId, "{\"status\":\"DRAFT\"}", "{\"status\":\"PENDING_APPROVAL\"}",
                submittedBy, "Submitted for approval"));
        return saved;
    }

    @Transactional
    public PromptTemplateEntity approve(UUID templateId, UUID approvedBy) {
        PromptTemplateEntity template = templateRepository.findByIdAndIsDeletedFalse(templateId)
                .orElseThrow(() -> new PromptNotFoundException("Template not found: " + templateId));
        if (!"PENDING_APPROVAL".equals(template.getStatus())) {
            throw new PromptLifecycleException("Only PENDING_APPROVAL templates can be approved");
        }
        template.setStatus("APPROVED");
        template.setUpdatedBy(approvedBy);
        template.setUpdatedAt(OffsetDateTime.now());
        PromptTemplateEntity saved = templateRepository.save(template);

        auditRepository.save(new com.sporekart.ai.prompt.infrastructure.persistence.PromptAuditEntity(
                templateId, null, AuditAction.STATUS_CHANGED.name(), "TEMPLATE",
                templateId, "{\"status\":\"PENDING_APPROVAL\"}", "{\"status\":\"APPROVED\"}",
                approvedBy, "Approved by " + approvedBy));
        return saved;
    }

    @Transactional
    public PromptTemplateEntity reject(UUID templateId, UUID rejectedBy, String reason) {
        PromptTemplateEntity template = templateRepository.findByIdAndIsDeletedFalse(templateId)
                .orElseThrow(() -> new PromptNotFoundException("Template not found: " + templateId));
        if (!"PENDING_APPROVAL".equals(template.getStatus())) {
            throw new PromptLifecycleException("Only PENDING_APPROVAL templates can be rejected");
        }
        template.setStatus("DRAFT");
        template.setUpdatedBy(rejectedBy);
        template.setUpdatedAt(OffsetDateTime.now());
        PromptTemplateEntity saved = templateRepository.save(template);

        auditRepository.save(new com.sporekart.ai.prompt.infrastructure.persistence.PromptAuditEntity(
                templateId, null, AuditAction.STATUS_CHANGED.name(), "TEMPLATE",
                templateId, "{\"status\":\"PENDING_APPROVAL\"}", "{\"status\":\"DRAFT\"}",
                rejectedBy, "Rejected: " + (reason != null ? reason : "No reason provided")));
        return saved;
    }

    @Transactional
    public PromptTemplateEntity publish(UUID templateId, UUID publishedBy) {
        PromptTemplateEntity template = templateRepository.findByIdAndIsDeletedFalse(templateId)
                .orElseThrow(() -> new PromptNotFoundException("Template not found: " + templateId));
        if (!"APPROVED".equals(template.getStatus()) && !"DRAFT".equals(template.getStatus())) {
            throw new PromptLifecycleException("Only APPROVED or DRAFT templates can be published");
        }
        template.setStatus("PUBLISHED");
        template.setUpdatedBy(publishedBy);
        template.setUpdatedAt(OffsetDateTime.now());
        PromptTemplateEntity saved = templateRepository.save(template);

        var latestDraftOpt = versionRepository
                .findTopByTemplateIdAndStatusAndIsDeletedFalseOrderByVersionNumberDesc(templateId, "DRAFT");
        if (latestDraftOpt.isPresent()) {
            PromptVersionEntity latestDraft = latestDraftOpt.get();
            latestDraft.setStatus("PUBLISHED");
            latestDraft.setActivationDate(OffsetDateTime.now());
            latestDraft.setApprovedBy(publishedBy);
            latestDraft.setApprovedAt(OffsetDateTime.now());
            versionRepository.save(latestDraft);
        }

        auditRepository.save(new com.sporekart.ai.prompt.infrastructure.persistence.PromptAuditEntity(
                templateId, null, AuditAction.STATUS_CHANGED.name(), "TEMPLATE",
                templateId, "{\"status\":\"" + template.getStatus() + "\"}", "{\"status\":\"PUBLISHED\"}",
                publishedBy, "Published template"));
        return saved;
    }

    @Transactional
    public PromptTemplateEntity deprecate(UUID templateId, UUID deprecatedBy) {
        PromptTemplateEntity template = templateRepository.findByIdAndIsDeletedFalse(templateId)
                .orElseThrow(() -> new PromptNotFoundException("Template not found: " + templateId));
        template.setStatus("DEPRECATED");
        template.setUpdatedBy(deprecatedBy);
        template.setUpdatedAt(OffsetDateTime.now());
        PromptTemplateEntity saved = templateRepository.save(template);

        auditRepository.save(new com.sporekart.ai.prompt.infrastructure.persistence.PromptAuditEntity(
                templateId, null, AuditAction.VERSION_DEPRECATED.name(), "TEMPLATE",
                templateId, null, null, deprecatedBy, "Deprecated template"));
        return saved;
    }

    @Transactional
    public PromptTemplateEntity archive(UUID templateId, UUID archivedBy) {
        PromptTemplateEntity template = templateRepository.findByIdAndIsDeletedFalse(templateId)
                .orElseThrow(() -> new PromptNotFoundException("Template not found: " + templateId));
        template.setStatus("ARCHIVED");
        template.setActive(false);
        template.setUpdatedBy(archivedBy);
        template.setUpdatedAt(OffsetDateTime.now());
        PromptTemplateEntity saved = templateRepository.save(template);

        auditRepository.save(new com.sporekart.ai.prompt.infrastructure.persistence.PromptAuditEntity(
                templateId, null, AuditAction.VERSION_ARCHIVED.name(), "TEMPLATE",
                templateId, null, null, archivedBy, "Archived template"));
        return saved;
    }
}
