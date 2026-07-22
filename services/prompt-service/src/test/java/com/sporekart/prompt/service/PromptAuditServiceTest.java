package com.sporekart.prompt.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sporekart.prompt.domain.AuditAction;
import com.sporekart.prompt.entity.PromptAuditEntity;
import com.sporekart.prompt.repository.PromptAuditRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class PromptAuditServiceTest {

    @Mock
    private PromptAuditRepository auditRepository;

    private PromptAuditService auditService;
    private UUID templateId;

    @BeforeEach
    void setUp() {
        auditService = new PromptAuditService(auditRepository);
        templateId = UUID.randomUUID();
    }

    @Test
    void shouldRecordAuditEntry() {
        when(auditRepository.save(any())).thenAnswer(inv -> {
            var e = inv.<PromptAuditEntity>getArgument(0);
            e.setId(UUID.randomUUID());
            return e;
        });

        auditService.record(templateId, null, AuditAction.CREATED, UUID.randomUUID(), "Created");
    }

    @Test
    void shouldGetAuditLog() {
        var entity = new PromptAuditEntity();
        entity.setId(UUID.randomUUID());
        entity.setTemplateId(templateId);
        entity.setAction(AuditAction.CREATED);
        entity.setPerformedBy(UUID.randomUUID());
        entity.setCreatedAt(OffsetDateTime.now());

        when(auditRepository.findByTemplateIdOrderByCreatedAtDesc(templateId))
                .thenReturn(List.of(entity));

        var log = auditService.getAuditLog(templateId);
        assertThat(log).hasSize(1);
    }

    @Test
    void shouldGetAuditLogForVersion() {
        var versionId = UUID.randomUUID();
        var entity = new PromptAuditEntity();
        entity.setId(UUID.randomUUID());
        entity.setTemplateId(templateId);
        entity.setVersionId(versionId);
        entity.setAction(AuditAction.PUBLISHED);
        entity.setPerformedBy(UUID.randomUUID());
        entity.setCreatedAt(OffsetDateTime.now());

        when(auditRepository.findByTemplateIdAndVersionIdOrderByCreatedAtDesc(templateId, versionId))
                .thenReturn(List.of(entity));

        var log = auditService.getAuditLogForVersion(templateId, versionId);
        assertThat(log).hasSize(1);
    }
}
