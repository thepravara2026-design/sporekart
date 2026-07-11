package com.sporekart.ai.prompt.application;

import com.sporekart.ai.prompt.infrastructure.persistence.PromptAuditRepository;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptCategoryEntity;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptTemplateEntity;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptTemplateRepository;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptVersionEntity;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptVersionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PromptVersionServiceTest {

    @Mock
    private PromptVersionRepository versionRepository;
    @Mock
    private PromptTemplateRepository templateRepository;
    @Mock
    private PromptAuditRepository auditRepository;

    private PromptVersionService versionService;

    @BeforeEach
    void setUp() {
        versionService = new PromptVersionService(versionRepository, templateRepository, auditRepository);
    }

    @Test
    void shouldCreateNewVersion() {
        UUID templateId = UUID.randomUUID();
        PromptTemplateEntity template = new PromptTemplateEntity();
        template.setId(templateId);
        template.setTemplateText("Original text");
        template.setCurrentVersion(1);

        when(templateRepository.findByIdAndIsDeletedFalse(templateId)).thenReturn(Optional.of(template));
        when(versionRepository.countByTemplateIdAndIsDeletedFalse(templateId)).thenReturn(1);
        when(versionRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(templateRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(auditRepository.save(any())).thenReturn(null);

        PromptVersionEntity version = versionService.createVersion(templateId, "Updated text", "Bug fix", UUID.randomUUID());
        assertNotNull(version);
        assertEquals(2, version.getVersionNumber());
        assertEquals("Updated text", version.getTemplateText());
    }

    @Test
    void shouldThrowWhenTemplateNotFound() {
        when(templateRepository.findByIdAndIsDeletedFalse(any())).thenReturn(Optional.empty());
        assertThrows(PromptNotFoundException.class,
                () -> versionService.createVersion(UUID.randomUUID(), "text", "notes", UUID.randomUUID()));
    }

    @Test
    void shouldPublishVersion() {
        UUID versionId = UUID.randomUUID();
        PromptVersionEntity version = new PromptVersionEntity();
        version.setId(versionId);
        version.setVersionNumber(1);
        version.setStatus("DRAFT");
        version.setTemplateText("Text");
        PromptTemplateEntity template = new PromptTemplateEntity();
        template.setId(UUID.randomUUID());
        version.setTemplate(template);

        when(versionRepository.findByIdAndIsDeletedFalse(versionId)).thenReturn(Optional.of(version));
        when(versionRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(templateRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(auditRepository.save(any())).thenReturn(null);

        PromptVersionEntity published = versionService.publishVersion(versionId, UUID.randomUUID());
        assertEquals("PUBLISHED", published.getStatus());
        assertNotNull(published.getActivationDate());
    }

    @Test
    void shouldRollbackToPreviousVersion() {
        UUID templateId = UUID.randomUUID();
        PromptTemplateEntity template = new PromptTemplateEntity();
        template.setId(templateId);
        template.setTemplateText("v1 text");
        template.setCurrentVersion(2);

        PromptVersionEntity targetVersion = new PromptVersionEntity();
        targetVersion.setTemplate(template);
        targetVersion.setVersionNumber(1);
        targetVersion.setTemplateText("v1 text");

        when(versionRepository.findByTemplateIdAndVersionNumberAndIsDeletedFalse(templateId, 1))
                .thenReturn(Optional.of(targetVersion));
        when(versionRepository.countByTemplateIdAndIsDeletedFalse(templateId)).thenReturn(2);
        when(versionRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(templateRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(auditRepository.save(any())).thenReturn(null);

        PromptVersionEntity rolledBack = versionService.rollback(templateId, 1, UUID.randomUUID());
        assertNotNull(rolledBack);
        assertEquals("PUBLISHED", rolledBack.getStatus());
        assertEquals("v1 text", rolledBack.getTemplateText());
    }

    @Test
    void shouldListVersions() {
        UUID templateId = UUID.randomUUID();
        PromptVersionEntity v1 = new PromptVersionEntity();
        v1.setVersionNumber(2);
        PromptVersionEntity v2 = new PromptVersionEntity();
        v2.setVersionNumber(1);
        when(versionRepository.findByTemplateIdAndIsDeletedFalseOrderByVersionNumberDesc(templateId))
                .thenReturn(List.of(v1, v2));

        List<PromptVersionEntity> versions = versionService.listVersions(templateId);
        assertEquals(2, versions.size());
    }

    @Test
    void shouldThrowOnVersionNotFound() {
        when(versionRepository.findByTemplateIdAndVersionNumberAndIsDeletedFalse(any(), anyInt()))
                .thenReturn(Optional.empty());
        assertThrows(PromptNotFoundException.class,
                () -> versionService.findVersion(UUID.randomUUID(), 99));
    }

    @Test
    void shouldDeprecateVersion() {
        UUID versionId = UUID.randomUUID();
        PromptVersionEntity version = new PromptVersionEntity();
        version.setId(versionId);
        version.setStatus("PUBLISHED");
        PromptTemplateEntity template = new PromptTemplateEntity();
        version.setTemplate(template);

        when(versionRepository.findByIdAndIsDeletedFalse(versionId)).thenReturn(Optional.of(version));
        when(versionRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(auditRepository.save(any())).thenReturn(null);

        PromptVersionEntity deprecated = versionService.deprecateVersion(versionId, UUID.randomUUID());
        assertEquals("DEPRECATED", deprecated.getStatus());
    }
}
