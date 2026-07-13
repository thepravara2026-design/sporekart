package com.sporekart.ai.admin.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.admin.api.AdministrationAuditService;
import com.sporekart.ai.admin.domain.ConfigurationSnapshot;
import com.sporekart.ai.admin.domain.ConfigurationStatus;
import com.sporekart.ai.admin.infrastructure.persistence.AdminConfigurationEntity;
import com.sporekart.ai.admin.infrastructure.persistence.AdminConfigurationRepository;
import com.sporekart.ai.admin.infrastructure.persistence.ConfigurationSnapshotEntity;
import com.sporekart.ai.admin.infrastructure.persistence.ConfigurationSnapshotRepository;
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
class ConfigurationSnapshotServiceImplTest {

    @Mock private ConfigurationSnapshotRepository configurationSnapshotRepository;
    @Mock private AdminConfigurationRepository adminConfigurationRepository;
    @Mock private AdministrationAuditService administrationAuditService;

    private ConfigurationSnapshotServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ConfigurationSnapshotServiceImpl(configurationSnapshotRepository,
                adminConfigurationRepository, administrationAuditService, new ObjectMapper());
    }

    private AdminConfigurationEntity createConfigEntity(String key, String value) {
        var entity = new AdminConfigurationEntity();
        entity.setId(UUID.randomUUID());
        entity.setKey(key);
        entity.setValue(value);
        entity.setModule("*");
        entity.setEnvironment("production");
        entity.setStatus(ConfigurationStatus.ACTIVE.name());
        entity.setVersion(1);
        entity.setUpdatedBy(UUID.randomUUID());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }

    @Test
    void testCreateSnapshot() {
        var configs = List.of(createConfigEntity("key1", "val1"), createConfigEntity("key2", "val2"));
        when(adminConfigurationRepository.findByEnvironment("production")).thenReturn(configs);
        var savedEntity = new ConfigurationSnapshotEntity();
        savedEntity.setId(UUID.randomUUID());
        savedEntity.setName("snap1");
        savedEntity.setConfiguration("{\"key1\":\"val1\",\"key2\":\"val2\"}");
        savedEntity.setEnvironment("production");
        savedEntity.setDescription("test snapshot");
        savedEntity.setCapturedAt(LocalDateTime.now());
        savedEntity.setCapturedBy(UUID.randomUUID());
        when(configurationSnapshotRepository.save(any())).thenReturn(savedEntity);

        var result = service.createSnapshot("snap1", "production", "test snapshot", UUID.randomUUID());
        assertNotNull(result);
        assertEquals("snap1", result.name());
        assertEquals("production", result.environment());
        verify(administrationAuditService).recordAudit(eq("SNAPSHOT_CREATE"), any(), any(), any(), any(), any());
    }

    @Test
    void testGetSnapshot_Found() {
        var id = UUID.randomUUID();
        var entity = new ConfigurationSnapshotEntity();
        entity.setId(id);
        entity.setName("snap1");
        entity.setConfiguration("{}");
        entity.setEnvironment("production");
        entity.setDescription("desc");
        entity.setCapturedAt(LocalDateTime.now());
        entity.setCapturedBy(UUID.randomUUID());
        when(configurationSnapshotRepository.findById(id)).thenReturn(Optional.of(entity));

        var result = service.getSnapshot(id);
        assertNotNull(result);
        assertEquals("snap1", result.name());
    }

    @Test
    void testGetSnapshot_NotFound() {
        when(configurationSnapshotRepository.findById(any())).thenReturn(Optional.empty());
        assertNull(service.getSnapshot(UUID.randomUUID()));
    }

    @Test
    void testGetSnapshotsByEnvironment() {
        var entity = new ConfigurationSnapshotEntity();
        entity.setId(UUID.randomUUID());
        entity.setName("snap1");
        entity.setConfiguration("{}");
        entity.setEnvironment("staging");
        entity.setDescription("desc");
        entity.setCapturedAt(LocalDateTime.now());
        entity.setCapturedBy(UUID.randomUUID());
        when(configurationSnapshotRepository.findByEnvironment("staging")).thenReturn(List.of(entity));

        var result = service.getSnapshotsByEnvironment("staging");
        assertEquals(1, result.size());
        assertEquals("staging", result.get(0).environment());
    }

    @Test
    void testRestoreSnapshot() {
        var snapshotId = UUID.randomUUID();
        var snapshotEntity = new ConfigurationSnapshotEntity();
        snapshotEntity.setId(snapshotId);
        snapshotEntity.setName("snap1");
        snapshotEntity.setConfiguration("{\"key1\":\"restored-val\"}");
        snapshotEntity.setEnvironment("production");
        snapshotEntity.setDescription("desc");
        snapshotEntity.setCapturedAt(LocalDateTime.now());
        snapshotEntity.setCapturedBy(UUID.randomUUID());
        when(configurationSnapshotRepository.findById(snapshotId)).thenReturn(Optional.of(snapshotEntity));

        var existingConfig = createConfigEntity("key1", "old-val");
        when(adminConfigurationRepository.findByKeyAndModuleAndEnvironment("key1", "*", "production"))
                .thenReturn(Optional.of(existingConfig));
        when(adminConfigurationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var result = service.restoreSnapshot(snapshotId, UUID.randomUUID());
        assertNotNull(result);
        assertEquals("restored-val", result.value());
        verify(administrationAuditService).recordAudit(eq("SNAPSHOT_RESTORE"), any(), any(), any(), any(), any());
    }

    @Test
    void testRestoreSnapshot_NotFound() {
        when(configurationSnapshotRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.restoreSnapshot(UUID.randomUUID(), UUID.randomUUID()));
    }
}
