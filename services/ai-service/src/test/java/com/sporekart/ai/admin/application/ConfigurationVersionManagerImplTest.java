package com.sporekart.ai.admin.application;

import com.sporekart.ai.admin.api.AdministrationAuditService;
import com.sporekart.ai.admin.domain.ConfigurationStatus;
import com.sporekart.ai.admin.infrastructure.persistence.AdminConfigurationEntity;
import com.sporekart.ai.admin.infrastructure.persistence.AdminConfigurationRepository;
import com.sporekart.ai.admin.infrastructure.persistence.ConfigurationVersionEntity;
import com.sporekart.ai.admin.infrastructure.persistence.ConfigurationVersionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfigurationVersionManagerImplTest {

    @Mock private ConfigurationVersionRepository configurationVersionRepository;
    @Mock private AdminConfigurationRepository adminConfigurationRepository;
    @Mock private AdministrationAuditService administrationAuditService;

    private ConfigurationVersionManagerImpl manager;

    @BeforeEach
    void setUp() {
        manager = new ConfigurationVersionManagerImpl(configurationVersionRepository,
                adminConfigurationRepository, administrationAuditService);
    }

    private AdminConfigurationEntity createConfigEntity(UUID id, int version) {
        var entity = new AdminConfigurationEntity();
        entity.setId(id);
        entity.setKey("test-key");
        entity.setValue("val-v" + version);
        entity.setModule("mod");
        entity.setEnvironment("env");
        entity.setStatus(ConfigurationStatus.ACTIVE.name());
        entity.setVersion(version);
        entity.setUpdatedBy(UUID.randomUUID());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }

    private ConfigurationVersionEntity createVersionEntity(UUID id, UUID configId, int version) {
        var entity = new ConfigurationVersionEntity();
        entity.setId(id);
        entity.setConfigId(configId);
        entity.setVersion(version);
        entity.setValue("val-v" + version);
        entity.setChangeReason("reason");
        entity.setChangedBy(UUID.randomUUID());
        entity.setChangedAt(LocalDateTime.now());
        return entity;
    }

    @Test
    void testCreateVersion() {
        var configId = UUID.randomUUID();
        var configEntity = createConfigEntity(configId, 2);
        when(adminConfigurationRepository.findById(configId)).thenReturn(Optional.of(configEntity));
        var versionEntity = createVersionEntity(UUID.randomUUID(), configId, 2);
        when(configurationVersionRepository.save(any())).thenReturn(versionEntity);

        var result = manager.createVersion(configId, "new-value", "change reason", UUID.randomUUID());
        assertNotNull(result);
        assertEquals(2, result.version());
        assertEquals("new-value", result.value());
    }

    @Test
    void testCreateVersion_ConfigNotFound() {
        when(adminConfigurationRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,
                () -> manager.createVersion(UUID.randomUUID(), "val", "reason", UUID.randomUUID()));
    }

    @Test
    void testGetVersions() {
        var configId = UUID.randomUUID();
        var v1 = createVersionEntity(UUID.randomUUID(), configId, 1);
        var v2 = createVersionEntity(UUID.randomUUID(), configId, 2);
        when(configurationVersionRepository.findByConfigIdOrderByVersionDesc(configId)).thenReturn(List.of(v2, v1));

        var result = manager.getVersions(configId);
        assertEquals(2, result.size());
    }

    @Test
    void testGetVersion() {
        var configId = UUID.randomUUID();
        var v1 = createVersionEntity(UUID.randomUUID(), configId, 1);
        var v2 = createVersionEntity(UUID.randomUUID(), configId, 2);
        when(configurationVersionRepository.findByConfigIdOrderByVersionDesc(configId)).thenReturn(List.of(v2, v1));

        var result = manager.getVersion(configId, 1);
        assertNotNull(result);
        assertEquals(1, result.version());
    }

    @Test
    void testGetVersion_NotFound() {
        var configId = UUID.randomUUID();
        when(configurationVersionRepository.findByConfigIdOrderByVersionDesc(configId)).thenReturn(List.of());
        assertNull(manager.getVersion(configId, 99));
    }

    @Test
    void testRollback() {
        var configId = UUID.randomUUID();
        var v1 = createVersionEntity(UUID.randomUUID(), configId, 1);
        createVersionEntity(UUID.randomUUID(), configId, 2);
        when(configurationVersionRepository.findByConfigIdOrderByVersionDesc(configId)).thenReturn(List.of(v1));
        var configEntity = createConfigEntity(configId, 2);
        when(adminConfigurationRepository.findById(configId)).thenReturn(Optional.of(configEntity));
        when(configurationVersionRepository.save(any())).thenReturn(createVersionEntity(UUID.randomUUID(), configId, 3));
        when(adminConfigurationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var result = manager.rollback(configId, 1, UUID.randomUUID());
        assertNotNull(result);
        assertEquals("val-v1", result.value());
        verify(administrationAuditService).recordAudit(eq("CONFIG_ROLLBACK"), any(), any(), any(), any(), any());
    }

    @Test
    void testRollback_VersionNotFound() {
        var configId = UUID.randomUUID();
        when(configurationVersionRepository.findByConfigIdOrderByVersionDesc(configId)).thenReturn(List.of());
        assertThrows(IllegalArgumentException.class, () -> manager.rollback(configId, 99, UUID.randomUUID()));
    }

    @Test
    void testRollback_ConfigNotFound() {
        var configId = UUID.randomUUID();
        var v1 = createVersionEntity(UUID.randomUUID(), configId, 1);
        when(configurationVersionRepository.findByConfigIdOrderByVersionDesc(configId)).thenReturn(List.of(v1));
        when(adminConfigurationRepository.findById(configId)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> manager.rollback(configId, 1, UUID.randomUUID()));
    }
}
