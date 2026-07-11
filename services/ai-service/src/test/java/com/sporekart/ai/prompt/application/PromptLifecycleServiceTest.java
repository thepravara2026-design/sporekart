package com.sporekart.ai.prompt.application;

import com.sporekart.ai.prompt.infrastructure.persistence.PromptAuditRepository;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptTemplateEntity;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptTemplateRepository;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptVersionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PromptLifecycleServiceTest {

    @Mock
    private PromptTemplateRepository templateRepository;
    @Mock
    private PromptVersionRepository versionRepository;
    @Mock
    private PromptAuditRepository auditRepository;

    private PromptLifecycleService lifecycleService;

    @BeforeEach
    void setUp() {
        lifecycleService = new PromptLifecycleService(templateRepository, versionRepository, auditRepository);
    }

    @Test
    void shouldSubmitForApproval() {
        UUID templateId = UUID.randomUUID();
        PromptTemplateEntity template = new PromptTemplateEntity();
        template.setId(templateId);
        template.setStatus("DRAFT");

        when(templateRepository.findByIdAndIsDeletedFalse(templateId)).thenReturn(Optional.of(template));
        when(templateRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(auditRepository.save(any())).thenReturn(null);

        PromptTemplateEntity result = lifecycleService.submitForApproval(templateId, UUID.randomUUID());
        assertEquals("PENDING_APPROVAL", result.getStatus());
    }

    @Test
    void shouldRejectSubmitWhenNotDraft() {
        UUID templateId = UUID.randomUUID();
        PromptTemplateEntity template = new PromptTemplateEntity();
        template.setId(templateId);
        template.setStatus("PUBLISHED");

        when(templateRepository.findByIdAndIsDeletedFalse(templateId)).thenReturn(Optional.of(template));

        assertThrows(PromptLifecycleException.class,
                () -> lifecycleService.submitForApproval(templateId, UUID.randomUUID()));
    }

    @Test
    void shouldApprovePendingTemplate() {
        UUID templateId = UUID.randomUUID();
        PromptTemplateEntity template = new PromptTemplateEntity();
        template.setId(templateId);
        template.setStatus("PENDING_APPROVAL");

        when(templateRepository.findByIdAndIsDeletedFalse(templateId)).thenReturn(Optional.of(template));
        when(templateRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(auditRepository.save(any())).thenReturn(null);

        PromptTemplateEntity result = lifecycleService.approve(templateId, UUID.randomUUID());
        assertEquals("APPROVED", result.getStatus());
    }

    @Test
    void shouldRejectApproveWhenNotPending() {
        UUID templateId = UUID.randomUUID();
        PromptTemplateEntity template = new PromptTemplateEntity();
        template.setId(templateId);
        template.setStatus("DRAFT");

        when(templateRepository.findByIdAndIsDeletedFalse(templateId)).thenReturn(Optional.of(template));

        assertThrows(PromptLifecycleException.class,
                () -> lifecycleService.approve(templateId, UUID.randomUUID()));
    }

    @Test
    void shouldRejectPendingTemplate() {
        UUID templateId = UUID.randomUUID();
        PromptTemplateEntity template = new PromptTemplateEntity();
        template.setId(templateId);
        template.setStatus("PENDING_APPROVAL");

        when(templateRepository.findByIdAndIsDeletedFalse(templateId)).thenReturn(Optional.of(template));
        when(templateRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(auditRepository.save(any())).thenReturn(null);

        PromptTemplateEntity result = lifecycleService.reject(templateId, UUID.randomUUID(), "Not suitable");
        assertEquals("DRAFT", result.getStatus());
    }

    @Test
    void shouldPublishApprovedTemplate() {
        UUID templateId = UUID.randomUUID();
        PromptTemplateEntity template = new PromptTemplateEntity();
        template.setId(templateId);
        template.setStatus("APPROVED");

        when(templateRepository.findByIdAndIsDeletedFalse(templateId)).thenReturn(Optional.of(template));
        when(templateRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(auditRepository.save(any())).thenReturn(null);

        PromptTemplateEntity result = lifecycleService.publish(templateId, UUID.randomUUID());
        assertEquals("PUBLISHED", result.getStatus());
    }

    @Test
    void shouldDeprecateTemplate() {
        UUID templateId = UUID.randomUUID();
        PromptTemplateEntity template = new PromptTemplateEntity();
        template.setId(templateId);
        template.setStatus("PUBLISHED");

        when(templateRepository.findByIdAndIsDeletedFalse(templateId)).thenReturn(Optional.of(template));
        when(templateRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(auditRepository.save(any())).thenReturn(null);

        PromptTemplateEntity result = lifecycleService.deprecate(templateId, UUID.randomUUID());
        assertEquals("DEPRECATED", result.getStatus());
    }

    @Test
    void shouldArchiveTemplate() {
        UUID templateId = UUID.randomUUID();
        PromptTemplateEntity template = new PromptTemplateEntity();
        template.setId(templateId);
        template.setStatus("DEPRECATED");
        template.setActive(true);

        when(templateRepository.findByIdAndIsDeletedFalse(templateId)).thenReturn(Optional.of(template));
        when(templateRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(auditRepository.save(any())).thenReturn(null);

        PromptTemplateEntity result = lifecycleService.archive(templateId, UUID.randomUUID());
        assertEquals("ARCHIVED", result.getStatus());
        assertFalse(result.isActive());
    }

    @Test
    void shouldThrowWhenTemplateNotFound() {
        when(templateRepository.findByIdAndIsDeletedFalse(any())).thenReturn(Optional.empty());
        assertThrows(PromptNotFoundException.class,
                () -> lifecycleService.submitForApproval(UUID.randomUUID(), UUID.randomUUID()));
    }
}
