package com.sporekart.ai.configregistry.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sporekart.ai.configregistry.domain.ConfigurationEntry;
import com.sporekart.ai.configregistry.domain.ConfigSnapshot;
import com.sporekart.ai.configregistry.infrastructure.persistence.ConfigurationEntity;
import com.sporekart.ai.configregistry.infrastructure.persistence.ConfigSnapshotEntity;
import com.sporekart.ai.configregistry.infrastructure.persistence.ConfigurationRepository;
import com.sporekart.ai.configregistry.infrastructure.persistence.ConfigSnapshotRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ConfigRegistryServiceTest {

    @Mock
    private ConfigurationRepository configurationRepository;

    @Mock
    private ConfigSnapshotRepository snapshotRepository;

    @InjectMocks
    private ConfigRegistryServiceImpl configService;

    @InjectMocks
    private ConfigSnapshotServiceImpl snapshotService;

    @Test
    void setConfigStoresEntry() {
        ConfigurationEntry entry = new ConfigurationEntry();
        entry.setConfigKey("k1");
        entry.setConfigValue("v1");
        when(configurationRepository.findByConfigKey("k1")).thenReturn(Optional.empty());
        when(configurationRepository.save(any(ConfigurationEntity.class))).thenAnswer(i -> i.getArgument(0));

        configService.setConfig(entry);

        verify(configurationRepository).save(any(ConfigurationEntity.class));
    }

    @Test
    void getConfigReturnsEntry() {
        ConfigurationEntity entity = new ConfigurationEntity();
        entity.setConfigKey("k1");
        entity.setConfigValue("v1");
        when(configurationRepository.findByConfigKey("k1")).thenReturn(Optional.of(entity));

        ConfigurationEntry result = configService.getConfig("k1");

        assertNotNull(result);
        assertEquals("v1", result.getConfigValue());
    }

    @Test
    void createSnapshotCapturesCurrentConfigurations() {
        ConfigurationEntity entity = new ConfigurationEntity();
        entity.setConfigKey("k1");
        entity.setConfigValue("v1");
        when(configurationRepository.findAll()).thenReturn(List.of(entity));
        when(snapshotRepository.count()).thenReturn(0L);
        when(snapshotRepository.save(any(ConfigSnapshotEntity.class))).thenAnswer(i -> i.getArgument(0));

        ConfigSnapshot snapshot = snapshotService.createSnapshot("snap1", "desc", "me");

        assertEquals("snap1", snapshot.getName());
        assertEquals("v1", snapshot.getConfigurations().get("k1"));
    }

    @Test
    void rollbackToSnapshotRestoresConfigurations() {
        Map<String, String> configurations = new LinkedHashMap<>();
        configurations.put("k1", "v2");
        ConfigSnapshotEntity snapshotEntity = new ConfigSnapshotEntity();
        snapshotEntity.setSnapshotId("sid");
        snapshotEntity.setConfigurations(configurations);
        snapshotEntity.setCreatedBy("me");

        ConfigurationEntity configEntity = new ConfigurationEntity();
        configEntity.setConfigKey("k1");
        configEntity.setConfigValue("v0");

        when(snapshotRepository.findById("sid")).thenReturn(Optional.of(snapshotEntity));
        when(configurationRepository.findByConfigKey("k1")).thenReturn(Optional.of(configEntity));
        when(configurationRepository.save(any(ConfigurationEntity.class))).thenAnswer(i -> i.getArgument(0));

        snapshotService.rollbackToSnapshot("sid");

        verify(configurationRepository).save(any(ConfigurationEntity.class));
    }
}
