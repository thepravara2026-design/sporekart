package com.sporekart.ai.prompt.application;

import com.sporekart.ai.prompt.infrastructure.persistence.PromptAuditEntity;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptAuditRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PromptAuditServiceTest {

    @Mock
    private PromptAuditRepository auditRepository;

    private PromptAuditService auditService;

    @BeforeEach
    void setUp() {
        auditService = new PromptAuditService(auditRepository);
    }

    @Test
    void shouldGetHistoryForTemplate() {
        UUID templateId = UUID.randomUUID();
        when(auditRepository.findByTemplateIdOrderByChangedAtDesc(templateId))
                .thenReturn(List.of(new PromptAuditEntity(templateId, null, "CREATED", "TEMPLATE",
                        templateId, null, null, UUID.randomUUID(), "Created")));
        assertEquals(1, auditService.getHistoryForTemplate(templateId).size());
    }

    @Test
    void shouldGetAllHistory() {
        when(auditRepository.findAllByOrderByChangedAtDesc(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(new PromptAuditEntity())));
        Page<PromptAuditEntity> result = auditService.getAllHistory(0, 50);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void shouldRecordAudit() {
        when(auditRepository.save(any())).thenReturn(null);
        assertDoesNotThrow(() -> auditService.record(
                UUID.randomUUID(), null, "TEST", "TEMPLATE",
                UUID.randomUUID(), null, null, UUID.randomUUID(), "Test entry"));
    }
}
