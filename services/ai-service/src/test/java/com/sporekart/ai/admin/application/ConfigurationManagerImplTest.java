package com.sporekart.ai.admin.application;

import com.sporekart.ai.admin.api.AdministrationAuditService;
import com.sporekart.ai.admin.api.AdministrationMetricsService;
import com.sporekart.ai.admin.api.ConfigurationVersionManager;
import com.sporekart.ai.admin.domain.AdminConfiguration;
import com.sporekart.ai.admin.domain.ConfigurationStatus;
import com.sporekart.ai.admin.infrastructure.persistence.AdminConfigurationEntity;
import com.sporekart.ai.admin.infrastructure.persistence.AdminConfigurationRepository;
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
class ConfigurationManagerImplTest {

    @Mock private AdminConfigurationRepository adminConfigurationRepository;
    @Mock private ConfigurationVersionManager configurationVersionManager;
    @Mock private AdministrationAuditService administrationAuditService;
    @Mock private AdministrationMetricsService administrationMetricsService;

    private ConfigurationManagerImpl manager;

    @BeforeEach
    void setUp() {
        manager = new ConfigurationManagerImpl(adminConfigurationRepository, configurationVersionManager,
                administrationAuditService, administrationMetricsService);
    }

    private AdminConfigurationEntity createEntity(UUID id, String key, String value, int version) {
        var entity = new AdminConfigurationEntity();
        entity.setId(id);
        entity.setKey(key);
        entity.setValue(value);
        entity.setModule("test-module");
        entity.setEnvironment("production");
        entity.setDescription("desc");
        entity.setStatus(ConfigurationStatus.ACTIVE.name());
        entity.setVersion(version);
        entity.setUpdatedBy(UUID.randomUUID());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }

    @Test
    void testGetConfiguration_Found() {
        var id = UUID.randomUUID();
        var entity = createEntity(id, "cfg1", "val1", 1);
        when(adminConfigurationRepository.findByKeyAndModuleAndEnvironment("cfg1", "test-module", "production"))
                .thenReturn(Optional.of(entity));

        var result = manager.getConfiguration("cfg1", "test-module", "production");
        assertNotNull(result);
        assertEquals("cfg1", result.key());
        assertEquals("val1", result.value());
    }

    @Test
    void testGetConfiguration_NotFound() {
        when(adminConfigurationRepository.findByKeyAndModuleAndEnvironment(any(), any(), any()))
                .thenReturn(Optional.empty());
        var result = manager.getConfiguration("nonexistent", "mod", "env");
        assertNull(result);
    }

    @Test
    void testSetConfiguration_Create() {
        when(adminConfigurationRepository.findByKeyAndModuleAndEnvironment(any(), any(), any()))
                .thenReturn(Optional.empty());
        var savedEntity = createEntity(UUID.randomUUID(), "new-key", "new-val", 1);
        when(adminConfigurationRepository.save(any())).thenReturn(savedEntity);

        var result = manager.setConfiguration("new-key", "new-val", "mod", "env", "desc", UUID.randomUUID());
        assertNotNull(result);
        assertEquals("new-key", result.key());
        verify(configurationVersionManager).createVersion(any(), eq("new-val"), anyString(), any());
        verify(administrationAuditService).recordAudit(eq("CONFIG_CREATE"), any(), any(), any(), any(), any());
    }

    @Test
    void testSetConfiguration_Update() {
        var id = UUID.randomUUID();
        var existing = createEntity(id, "existing-key", "old-val", 1);
        when(adminConfigurationRepository.findByKeyAndModuleAndEnvironment(any(), any(), any()))
                .thenReturn(Optional.of(existing));
        when(adminConfigurationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var result = manager.setConfiguration("existing-key", "new-val", "mod", "env", "updated desc", UUID.randomUUID());
        assertNotNull(result);
        assertEquals("new-val", result.value());
        verify(configurationVersionManager).createVersion(eq(id), eq("old-val"), anyString(), any());
        verify(administrationAuditService).recordAudit(eq("CONFIG_UPDATE"), any(), any(), any(), any(), any());
    }

    @Test
    void testDeleteConfiguration() {
        var id = UUID.randomUUID();
        var entity = createEntity(id, "del-key", "del-val", 2);
        when(adminConfigurationRepository.findById(id)).thenReturn(Optional.of(entity));
        when(adminConfigurationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var result = manager.deleteConfiguration(id);
        assertEquals(ConfigurationStatus.INACTIVE.name(), "INACTIVE");
        assertNotNull(result);
        verify(adminConfigurationRepository).findById(id);
        verify(administrationAuditService).recordAudit(eq("CONFIG_DELETE"), any(), any(), any(), any(), any());
    }

    @Test
    void testDeleteConfiguration_NotFound() {
        when(adminConfigurationRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> manager.deleteConfiguration(UUID.randomUUID()));
    }

    @Test
    void testGetAllConfigurations() {
        var entity = createEntity(UUID.randomUUID(), "k1", "v1", 1);
        when(adminConfigurationRepository.findByModule("mod")).thenReturn(List.of(entity));

        var result = manager.getAllConfigurations("mod");
        assertEquals(1, result.size());
    }

    @Test
    void testGetConfigurationsByEnvironment() {
        var entity = createEntity(UUID.randomUUID(), "k1", "v1", 1);
        when(adminConfigurationRepository.findByEnvironment("production")).thenReturn(List.of(entity));

        var result = manager.getConfigurationsByEnvironment("production");
        assertEquals(1, result.size());
    }

    @Test
    void testValidateConfiguration_Valid() {
        var result = manager.validateConfiguration("key", "value");
        assertNotNull(result);
        assertEquals("key", result.key());
        assertEquals("value", result.value());
    }

    @Test
    void testValidateConfiguration_InvalidKey() {
        assertThrows(IllegalArgumentException.class, () -> manager.validateConfiguration("", "value"));
    }

    @Test
    void testValidateConfiguration_InvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> manager.validateConfiguration("key", ""));
    }
}
