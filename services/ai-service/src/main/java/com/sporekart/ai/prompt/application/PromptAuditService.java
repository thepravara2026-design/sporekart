package com.sporekart.ai.prompt.application;

import com.sporekart.ai.prompt.infrastructure.persistence.PromptAuditEntity;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptAuditRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PromptAuditService {

    private final PromptAuditRepository auditRepository;

    public PromptAuditService(PromptAuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    public List<PromptAuditEntity> getHistoryForTemplate(UUID templateId) {
        return auditRepository.findByTemplateIdOrderByChangedAtDesc(templateId);
    }

    public Page<PromptAuditEntity> getAllHistory(int page, int size) {
        return auditRepository.findAllByOrderByChangedAtDesc(PageRequest.of(page, size));
    }

    public void record(UUID templateId, UUID versionId, String action, String entityType,
                       UUID entityId, String previousValue, String newValue,
                       UUID changedBy, String details) {
        auditRepository.save(new PromptAuditEntity(
                templateId, versionId, action, entityType, entityId,
                previousValue, newValue, changedBy, details));
    }
}
