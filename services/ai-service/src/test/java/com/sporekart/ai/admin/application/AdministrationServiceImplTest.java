package com.sporekart.ai.admin.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.admin.api.AdministrationAuditService;
import com.sporekart.ai.admin.api.AdministrationMetricsService;
import com.sporekart.ai.admin.domain.AdminOperationType;
import com.sporekart.ai.admin.infrastructure.persistence.AdminOperationEntity;
import com.sporekart.ai.admin.infrastructure.persistence.AdminOperationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdministrationServiceImplTest {

    @Mock private AdminOperationRepository adminOperationRepository;
    @Mock private AdministrationAuditService administrationAuditService;
    @Mock private AdministrationMetricsService administrationMetricsService;

    private AdministrationServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new AdministrationServiceImpl(adminOperationRepository, administrationAuditService,
                administrationMetricsService, new ObjectMapper());
    }

    @Test
    void testPerformOperation() {
        var entity = new AdminOperationEntity();
        entity.setId(UUID.randomUUID());
        entity.setType(AdminOperationType.CONFIG_UPDATE.name());
        entity.setDescription("test operation");
        entity.setDetails("{}");
        entity.setPerformedBy(UUID.randomUUID());
        entity.setIpAddress("127.0.0.1");
        entity.setSuccessful(true);
        entity.setPerformedAt(LocalDateTime.now());

        when(adminOperationRepository.save(any())).thenReturn(entity);

        var result = service.performOperation(AdminOperationType.CONFIG_UPDATE, "test operation",
                Map.of(), entity.getPerformedBy(), "127.0.0.1");

        assertNotNull(result);
        assertEquals(AdminOperationType.CONFIG_UPDATE, result.type());
        assertEquals("test operation", result.description());
        assertTrue(result.successful());
        verify(adminOperationRepository).save(any());
        verify(administrationAuditService).recordAudit(anyString(), anyString(), any(), any(), any(), any());
        verify(administrationMetricsService).getStatistics();
    }

    @Test
    void testGetOperationHistory() {
        var entity = new AdminOperationEntity();
        entity.setId(UUID.randomUUID());
        entity.setType(AdminOperationType.FEATURE_FLAG_CHANGE.name());
        entity.setDescription("flag change");
        entity.setDetails("{}");
        entity.setPerformedBy(UUID.randomUUID());
        entity.setSuccessful(true);
        entity.setPerformedAt(LocalDateTime.now());

        when(adminOperationRepository.findAll()).thenReturn(List.of(entity));

        var result = service.getOperationHistory();
        assertEquals(1, result.size());
        assertEquals(AdminOperationType.FEATURE_FLAG_CHANGE, result.get(0).type());
    }

    @Test
    void testGetOperationsByType() {
        var entity = new AdminOperationEntity();
        entity.setId(UUID.randomUUID());
        entity.setType(AdminOperationType.MODULE_ENABLE.name());
        entity.setDescription("module enabled");
        entity.setDetails("{}");
        entity.setPerformedBy(UUID.randomUUID());
        entity.setSuccessful(true);
        entity.setPerformedAt(LocalDateTime.now());

        when(adminOperationRepository.findByType(AdminOperationType.MODULE_ENABLE.name())).thenReturn(List.of(entity));

        var result = service.getOperationsByType(AdminOperationType.MODULE_ENABLE);
        assertEquals(1, result.size());
        assertEquals(AdminOperationType.MODULE_ENABLE, result.get(0).type());
    }

    @Test
    void testGetOperationsByType_NotFound() {
        when(adminOperationRepository.findByType(anyString())).thenReturn(List.of());
        var result = service.getOperationsByType(AdminOperationType.MAINTENANCE_START);
        assertTrue(result.isEmpty());
    }
}
