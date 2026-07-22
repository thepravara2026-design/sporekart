package com.sporekart.prompt.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.sporekart.prompt.domain.ApprovalStatus;
import com.sporekart.prompt.domain.AuditAction;
import com.sporekart.prompt.entity.PromptApprovalEntity;
import com.sporekart.prompt.repository.PromptApprovalRepository;
import com.sporekart.prompt.repository.PromptTemplateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class PromptApprovalServiceTest {

    @Mock
    private PromptApprovalRepository approvalRepository;

    @Mock
    private PromptTemplateRepository templateRepository;

    @Mock
    private PromptAuditService auditService;

    private PromptApprovalService approvalService;
    private UUID versionId;
    private UUID templateId;
    private UUID approver;

    @BeforeEach
    void setUp() {
        approvalService = new PromptApprovalService(approvalRepository, templateRepository, auditService);
        versionId = UUID.randomUUID();
        templateId = UUID.randomUUID();
        approver = UUID.randomUUID();
    }

    @Test
    void shouldRequestApproval() {
        when(approvalRepository.save(any())).thenAnswer(inv -> {
            var e = inv.<PromptApprovalEntity>getArgument(0);
            e.setId(UUID.randomUUID());
            return e;
        });

        var result = approvalService.requestApproval(versionId, templateId, approver, "REVIEWER", null);

        assertThat(result.status()).isEqualTo(ApprovalStatus.PENDING);
        assertThat(result.step()).isEqualTo("REVIEWER");
        verify(auditService).record(eq(templateId), eq(versionId), eq(AuditAction.REVIEW_REQUESTED), eq(approver), anyString());
    }

    @Test
    void shouldApprove() {
        var entity = createApprovalEntity(ApprovalStatus.PENDING, "REVIEWER");
        when(approvalRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        when(approvalRepository.save(any())).thenReturn(entity);

        var result = approvalService.approve(entity.getId(), "approve", "Looks good", approver);

        assertThat(result.status()).isEqualTo(ApprovalStatus.APPROVED);
    }

    @Test
    void shouldReject() {
        var entity = createApprovalEntity(ApprovalStatus.PENDING, "REVIEWER");
        when(approvalRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        when(approvalRepository.save(any())).thenReturn(entity);

        var result = approvalService.approve(entity.getId(), "reject", "Not good", approver);

        assertThat(result.status()).isEqualTo(ApprovalStatus.REJECTED);
    }

    @Test
    void shouldThrowForInvalidDecision() {
        var entity = createApprovalEntity(ApprovalStatus.PENDING, "REVIEWER");
        when(approvalRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

        assertThrows(IllegalArgumentException.class,
                () -> approvalService.approve(entity.getId(), "invalid", null, approver));
    }

    @Test
    void shouldThrowWhenApprovalNotFound() {
        var id = UUID.randomUUID();
        when(approvalRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> approvalService.approve(id, "approve", null, approver));
    }

    @Test
    void shouldGetApprovalsForVersion() {
        when(approvalRepository.findByVersionIdOrderByRequestedAtAsc(versionId))
                .thenReturn(List.of(createApprovalEntity(ApprovalStatus.PENDING, "REVIEWER")));

        var approvals = approvalService.getApprovalsForVersion(versionId);
        assertThat(approvals).hasSize(1);
    }

    @Test
    void shouldGetApprovalsForTemplate() {
        when(approvalRepository.findByTemplateIdOrderByRequestedAtDesc(templateId))
                .thenReturn(List.of(createApprovalEntity(ApprovalStatus.PENDING, "REVIEWER")));

        var approvals = approvalService.getApprovalsForTemplate(templateId);
        assertThat(approvals).hasSize(1);
    }

    @Test
    void shouldGetPendingApprovals() {
        when(approvalRepository.findByApproverAndStatus(approver, ApprovalStatus.PENDING))
                .thenReturn(List.of(createApprovalEntity(ApprovalStatus.PENDING, "REVIEWER")));

        var pending = approvalService.getPendingApprovals(approver);
        assertThat(pending).hasSize(1);
    }

    private PromptApprovalEntity createApprovalEntity(ApprovalStatus status, String step) {
        var entity = new PromptApprovalEntity();
        entity.setId(UUID.randomUUID());
        entity.setVersionId(versionId);
        entity.setTemplateId(templateId);
        entity.setApprover(approver);
        entity.setStatus(status);
        entity.setStep(step);
        entity.setRequestedAt(OffsetDateTime.now());
        return entity;
    }
}
