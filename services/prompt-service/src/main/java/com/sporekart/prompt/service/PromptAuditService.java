package com.sporekart.prompt.service;

import com.sporekart.prompt.domain.AuditAction;
import com.sporekart.prompt.dto.response.AuditEntryResponse;
import com.sporekart.prompt.entity.PromptAuditEntity;
import com.sporekart.prompt.repository.PromptAuditRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PromptAuditService {

    private final PromptAuditRepository auditRepository;

    public PromptAuditService(PromptAuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    public void record(UUID templateId, UUID versionId, AuditAction action, UUID performedBy, String details) {
        var entity = new PromptAuditEntity();
        entity.setTemplateId(templateId);
        entity.setVersionId(versionId);
        entity.setAction(action);
        entity.setPerformedBy(performedBy);
        entity.setDetails(details);
        entity.setCreatedAt(OffsetDateTime.now());
        auditRepository.save(entity);
    }

    public List<AuditEntryResponse> getAuditLog(UUID templateId) {
        return auditRepository.findByTemplateIdOrderByCreatedAtDesc(templateId).stream()
                .map(e -> new AuditEntryResponse(e.getId(), e.getTemplateId(), e.getVersionId(),
                        e.getAction(), e.getPerformedBy(), e.getDetails(), e.getCreatedAt()))
                .toList();
    }

    public List<AuditEntryResponse> getAuditLogForVersion(UUID templateId, UUID versionId) {
        return auditRepository.findByTemplateIdAndVersionIdOrderByCreatedAtDesc(templateId, versionId).stream()
                .map(e -> new AuditEntryResponse(e.getId(), e.getTemplateId(), e.getVersionId(),
                        e.getAction(), e.getPerformedBy(), e.getDetails(), e.getCreatedAt()))
                .toList();
    }
}
