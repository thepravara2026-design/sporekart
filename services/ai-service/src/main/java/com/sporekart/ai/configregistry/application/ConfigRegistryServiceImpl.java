package com.sporekart.ai.configregistry.application;

import com.sporekart.ai.configregistry.api.ConfigRegistryService;
import com.sporekart.ai.configregistry.domain.ConfigurationEntry;
import com.sporekart.ai.configregistry.domain.ConfigType;
import com.sporekart.ai.configregistry.infrastructure.persistence.ConfigurationEntity;
import com.sporekart.ai.configregistry.infrastructure.persistence.ConfigurationRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ConfigRegistryServiceImpl implements ConfigRegistryService {

    private final ConfigurationRepository configurationRepository;

    public ConfigRegistryServiceImpl(ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    @Override
    public ConfigurationEntry getConfig(String key) {
        Optional<ConfigurationEntity> entity = configurationRepository.findByConfigKey(key);
        return entity.map(this::toDomain).orElse(null);
    }

    @Override
    public void setConfig(ConfigurationEntry entry) {
        Instant now = Instant.now();
        Optional<ConfigurationEntity> existing = configurationRepository.findByConfigKey(entry.getConfigKey());
        ConfigurationEntity entity;
        if (existing.isPresent()) {
            entity = existing.get();
            entity.setConfigValue(entry.getConfigValue());
            entity.setConfigType(entry.getConfigType());
            entity.setDescription(entry.getDescription());
            entity.setModule(entry.getModule());
            entity.setEnvironment(entry.getEnvironment());
            entity.setMetadata(entry.getMetadata());
            entity.setSnapshotId(entry.getSnapshotId());
            entity.setValid(entry.isValid());
            entity.setCreatedBy(entry.getCreatedBy());
            entity.setVersion(entity.getVersion() + 1);
            entity.setUpdatedAt(now);
        } else {
            entity = new ConfigurationEntity(
                    entry.getConfigId() != null ? entry.getConfigId() : UUID.randomUUID().toString(),
                    entry.getConfigKey(),
                    entry.getConfigValue(),
                    entry.getConfigType(),
                    entry.getDescription(),
                    entry.getVersion() > 0 ? entry.getVersion() : 1,
                    entry.getModule(),
                    entry.getEnvironment(),
                    entry.getMetadata(),
                    entry.getSnapshotId(),
                    entry.isValid(),
                    now,
                    now,
                    entry.getCreatedBy());
        }
        configurationRepository.save(entity);
    }

    @Override
    public void deleteConfig(String key) {
        configurationRepository.findByConfigKey(key).ifPresent(configurationRepository::delete);
    }

    @Override
    public List<ConfigurationEntry> listByType(ConfigType type) {
        List<ConfigurationEntity> entities = configurationRepository.findByConfigType(type);
        return toDomainList(entities);
    }

    @Override
    public List<ConfigurationEntry> listByModule(String module) {
        List<ConfigurationEntity> entities = configurationRepository.findByModule(module);
        return toDomainList(entities);
    }

    @Override
    public List<ConfigurationEntry> searchConfig(String query) {
        List<ConfigurationEntry> all = toDomainList(configurationRepository.findAll());
        if (query == null || query.isBlank()) {
            return all;
        }
        String q = query.toLowerCase();
        List<ConfigurationEntry> result = new ArrayList<>();
        for (ConfigurationEntry entry : all) {
            if (matches(entry, q)) {
                result.add(entry);
            }
        }
        return result;
    }

    private boolean matches(ConfigurationEntry entry, String q) {
        return (entry.getConfigKey() != null && entry.getConfigKey().toLowerCase().contains(q))
                || (entry.getConfigValue() != null && entry.getConfigValue().toLowerCase().contains(q))
                || (entry.getDescription() != null && entry.getDescription().toLowerCase().contains(q))
                || (entry.getModule() != null && entry.getModule().toLowerCase().contains(q))
                || (entry.getEnvironment() != null && entry.getEnvironment().toLowerCase().contains(q));
    }

    private ConfigurationEntry toDomain(ConfigurationEntity entity) {
        ConfigurationEntry entry = new ConfigurationEntry();
        entry.setConfigId(entity.getConfigId());
        entry.setConfigKey(entity.getConfigKey());
        entry.setConfigValue(entity.getConfigValue());
        entry.setConfigType(entity.getConfigType());
        entry.setDescription(entity.getDescription());
        entry.setVersion(entity.getVersion());
        entry.setModule(entity.getModule());
        entry.setEnvironment(entity.getEnvironment());
        entry.setMetadata(entity.getMetadata());
        entry.setSnapshotId(entity.getSnapshotId());
        entry.setValid(entity.isValid());
        entry.setCreatedAt(entity.getCreatedAt());
        entry.setUpdatedAt(entity.getUpdatedAt());
        entry.setCreatedBy(entity.getCreatedBy());
        return entry;
    }

    private List<ConfigurationEntry> toDomainList(List<ConfigurationEntity> entities) {
        List<ConfigurationEntry> result = new ArrayList<>();
        for (ConfigurationEntity entity : entities) {
            result.add(toDomain(entity));
        }
        return result;
    }
}
