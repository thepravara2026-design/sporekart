package com.sporekart.prompt.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.sporekart.prompt.domain.AuditAction;
import com.sporekart.prompt.dto.request.CreateVersionRequest;
import com.sporekart.prompt.dto.response.PromptVersionResponse;
import com.sporekart.prompt.entity.PromptVersionEntity;
import com.sporekart.prompt.repository.PromptVersionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class PromptVersionServiceTest {

    @Mock
    private PromptVersionRepository versionRepository;

    @Mock
    private PromptAuditService auditService;

    private PromptVersionService versionService;
    private UUID templateId;

    @BeforeEach
    void setUp() {
        versionService = new PromptVersionService(versionRepository, auditService);
        templateId = UUID.randomUUID();
    }

    @Test
    void shouldCreateVersion() {
        when(versionRepository.countByTemplateId(templateId)).thenReturn(0);
        when(versionRepository.save(any())).thenAnswer(inv -> {
            var e = inv.<PromptVersionEntity>getArgument(0);
            e.setId(UUID.randomUUID());
            return e;
        });

        var request = new CreateVersionRequest(
                "Test prompt body", "system", null, null, null, null,
                null, null, "[\"var1\",\"var2\"]",
                null, null, null, 1000, UUID.randomUUID(), "Initial version");

        var result = versionService.createVersion(templateId, request);

        assertThat(result.promptBody()).isEqualTo("Test prompt body");
        assertThat(result.version()).isEqualTo(1);
        verify(auditService).record(eq(templateId), any(), eq(AuditAction.CREATED), any(), anyString());
    }

    @Test
    void shouldGetVersions() {
        var entity = createEntity(1);
        when(versionRepository.findByTemplateIdOrderByVersionDesc(templateId))
                .thenReturn(List.of(entity));

        var versions = versionService.getVersions(templateId);
        assertThat(versions).hasSize(1);
    }

    @Test
    void shouldGetVersionByNumber() {
        var entity = createEntity(1);
        when(versionRepository.findByTemplateIdAndVersion(templateId, 1))
                .thenReturn(Optional.of(entity));

        var result = versionService.getVersion(templateId, 1);
        assertThat(result).isPresent();
    }

    @Test
    void shouldReturnEmptyForNonExistentVersion() {
        when(versionRepository.findByTemplateIdAndVersion(templateId, 99))
                .thenReturn(Optional.empty());

        var result = versionService.getVersion(templateId, 99);
        assertThat(result).isEmpty();
    }

    @Test
    void shouldGetPublishedVersion() {
        var entity = createEntity(2);
        entity.setPublished(true);
        when(versionRepository.findTopByTemplateIdAndIsPublishedTrueOrderByVersionDesc(templateId))
                .thenReturn(Optional.of(entity));

        var result = versionService.getPublishedVersion(templateId);
        assertThat(result).isPresent();
        assertThat(result.get().isPublished()).isTrue();
    }

    @Test
    void shouldGetLatestVersion() {
        var entity = createEntity(3);
        when(versionRepository.findTopByTemplateIdOrderByVersionDesc(templateId))
                .thenReturn(Optional.of(entity));

        var result = versionService.getLatestVersion(templateId);
        assertThat(result).isPresent();
        assertThat(result.get().version()).isEqualTo(3);
    }

    @Test
    void shouldPublishVersion() {
        var entity = createEntity(1);
        entity.setId(UUID.randomUUID());
        when(versionRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        when(versionRepository.findByTemplateIdAndIsPublishedTrueOrderByVersionDesc(templateId))
                .thenReturn(List.of());
        when(versionRepository.save(any())).thenReturn(entity);

        var result = versionService.publishVersion(entity.getId(), UUID.randomUUID());
        assertThat(result.isPublished()).isTrue();
    }

    @Test
    void shouldThrowWhenPublishingNonExistent() {
        var id = UUID.randomUUID();
        when(versionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> versionService.publishVersion(id, UUID.randomUUID()));
    }

    @Test
    void shouldRollback() {
        var targetEntity = createEntity(1);
        when(versionRepository.findByTemplateIdAndVersion(templateId, 1))
                .thenReturn(Optional.of(targetEntity));
        when(versionRepository.countByTemplateId(templateId)).thenReturn(2);
        when(versionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(versionRepository.findByTemplateIdAndIsPublishedTrueOrderByVersionDesc(templateId))
                .thenReturn(List.of());

        var result = versionService.rollback(templateId, 1, UUID.randomUUID(), "Rollback test");
        assertThat(result.version()).isEqualTo(3);
        assertThat(result.isPublished()).isTrue();
    }

    @Test
    void shouldThrowWhenRollbackTargetNotFound() {
        when(versionRepository.findByTemplateIdAndVersion(templateId, 99))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> versionService.rollback(templateId, 99, UUID.randomUUID(), null));
    }

    @Test
    void shouldGetNextVersionNumber() {
        when(versionRepository.countByTemplateId(templateId)).thenReturn(5);
        assertThat(versionService.getNextVersionNumber(templateId)).isEqualTo(6);
    }

    private PromptVersionEntity createEntity(int version) {
        var entity = new PromptVersionEntity();
        entity.setId(UUID.randomUUID());
        entity.setTemplateId(templateId);
        entity.setVersion(version);
        entity.setPromptBody("Body v" + version);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setCreatedBy(UUID.randomUUID());
        return entity;
    }
}
