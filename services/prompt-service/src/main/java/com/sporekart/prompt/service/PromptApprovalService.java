package com.sporekart.prompt.service;

import com.sporekart.prompt.domain.ApprovalStatus;
import com.sporekart.prompt.domain.AuditAction;
import com.sporekart.prompt.dto.response.PromptApprovalResponse;
import com.sporekart.prompt.entity.PromptApprovalEntity;
import com.sporekart.prompt.repository.PromptApprovalRepository;
import com.sporekart.prompt.repository.PromptTemplateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PromptApprovalService {

    private final PromptApprovalRepository approvalRepository;
    private final PromptTemplateRepository templateRepository;
    private final PromptAuditService auditService;

    private static final List<String> APPROVAL_STEPS = List.of("REVIEWER", "AI_LEAD");

    public PromptApprovalService(PromptApprovalRepository approvalRepository,
                                 PromptTemplateRepository templateRepository,
                                 PromptAuditService auditService) {
        this.approvalRepository = approvalRepository;
        this.templateRepository = templateRepository;
        this.auditService = auditService;
    }

    @Transactional
    public PromptApprovalResponse requestApproval(UUID versionId, UUID templateId, UUID approver, String step, String comments) {
        var entity = new PromptApprovalEntity();
        entity.setVersionId(versionId);
        entity.setTemplateId(templateId);
        entity.setApprover(approver);
        entity.setStatus(ApprovalStatus.PENDING);
        entity.setComments(comments);
        entity.setRequestedAt(OffsetDateTime.now());
        entity.setStep(step);
        var saved = approvalRepository.save(entity);

        auditService.record(templateId, versionId, AuditAction.REVIEW_REQUESTED,
                approver, "Approval requested for step: " + step);

        return toResponse(saved);
    }

    @Transactional
    public PromptApprovalResponse approve(UUID approvalId, String decision, String comments, UUID performedBy) {
        var entity = approvalRepository.findById(approvalId)
                .orElseThrow(() -> new IllegalArgumentException("Approval not found: " + approvalId));

        var newStatus = switch (decision.toLowerCase()) {
            case "approve" -> ApprovalStatus.APPROVED;
            case "reject" -> ApprovalStatus.REJECTED;
            default -> throw new IllegalArgumentException("Invalid decision: " + decision);
        };

        entity.setStatus(newStatus);
        entity.setComments(comments != null ? comments : entity.getComments());
        entity.setApprovedAt(OffsetDateTime.now());
        var saved = approvalRepository.save(entity);

        var action = newStatus == ApprovalStatus.APPROVED ? AuditAction.APPROVED : AuditAction.REJECTED;
        auditService.record(entity.getTemplateId(), entity.getVersionId(), action,
                performedBy, "Step " + entity.getStep() + " " + decision + (comments != null ? ": " + comments : ""));

        if (newStatus == ApprovalStatus.APPROVED && isFullyApproved(entity.getVersionId())) {
            auditService.record(entity.getTemplateId(), entity.getVersionId(), AuditAction.APPROVED,
                    performedBy, "All approval steps completed");
        }

        return toResponse(saved);
    }

    public boolean isFullyApproved(UUID versionId) {
        for (var step : APPROVAL_STEPS) {
            var approval = approvalRepository.findTopByVersionIdAndStepOrderByRequestedAtDesc(versionId, step);
            if (approval.isEmpty() || approval.get().getStatus() != ApprovalStatus.APPROVED) {
                return false;
            }
        }
        return true;
    }

    public List<PromptApprovalResponse> getApprovalsForVersion(UUID versionId) {
        return approvalRepository.findByVersionIdOrderByRequestedAtAsc(versionId)
                .stream().map(this::toResponse).toList();
    }

    public List<PromptApprovalResponse> getApprovalsForTemplate(UUID templateId) {
        return approvalRepository.findByTemplateIdOrderByRequestedAtDesc(templateId)
                .stream().map(this::toResponse).toList();
    }

    public List<PromptApprovalResponse> getPendingApprovals(UUID approver) {
        return approvalRepository.findByApproverAndStatus(approver, ApprovalStatus.PENDING)
                .stream().map(this::toResponse).toList();
    }

    private PromptApprovalResponse toResponse(PromptApprovalEntity e) {
        return PromptApprovalResponse.from(
                e.getId(), e.getVersionId(), e.getTemplateId(), e.getApprover(),
                e.getStatus(), e.getComments(), e.getApprovedAt(),
                e.getRequestedAt(), e.getStep());
    }
}
