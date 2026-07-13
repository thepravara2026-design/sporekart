package com.sporekart.ai.configregistry.application;

import com.sporekart.ai.configregistry.api.ConfigRegistryService;
import com.sporekart.ai.configregistry.api.ConfigSnapshotService;
import com.sporekart.ai.configregistry.domain.ConfigSnapshot;
import com.sporekart.ai.configregistry.infrastructure.persistence.ConfigSnapshotEntity;
import com.sporekart.ai.configregistry.infrastructure.persistence.ConfigSnapshotRepository;
import com.sporekart.ai.configregistry.infrastructure.persistence.ConfigurationEntity;
import com.sporekart.ai.configregistry.infrastructure.persistence.ConfigurationRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ConfigSnapshotServiceImpl implements ConfigSnapshotService {

    private final ConfigSnapshotRepository snapshotRepository;
    private final ConfigurationRepository configurationRepository;

    public ConfigSnapshotServiceImpl(ConfigSnapshotRepository snapshotRepository,
            ConfigurationRepository configurationRepository) {
        this.snapshotRepository = snapshotRepository;
        this.configurationRepository = configurationRepository;
    }

    @Override
    public ConfigSnapshot createSnapshot(String name, String description, String createdBy) {
        Map<String, String> configurations = new LinkedHashMap<>();
        for (ConfigurationEntity entity : configurationRepository.findAll()) {
            configurations.put(entity.getConfigKey(), entity.getConfigValue());
        }
        int version = (int) snapshotRepository.count() + 1;
        ConfigSnapshotEntity entity = new ConfigSnapshotEntity(
                UUID.randomUUID().toString(),
                name,
                description,
                configurations,
                version,
                Instant.now(),
                createdBy);
        ConfigSnapshotEntity saved = snapshotRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public List<ConfigSnapshot> listSnapshots() {
        List<ConfigSnapshot> result = new ArrayList<>();
        for (ConfigSnapshotEntity entity : snapshotRepository.findAll()) {
            result.add(toDomain(entity));
        }
        return result;
    }

    @Override
    public void rollbackToSnapshot(String snapshotId) {
        Optional<ConfigSnapshotEntity> optional = snapshotRepository.findById(snapshotId);
        if (optional.isEmpty()) {
            throw new IllegalArgumentException("Snapshot not found: " + snapshotId);
        }
        ConfigSnapshotEntity snapshot = optional.get();
        Instant now = Instant.now();
        for (Map.Entry<String, String> entry : snapshot.getConfigurations().entrySet()) {
            Optional<ConfigurationEntity> existing =
                    configurationRepository.findByConfigKey(entry.getKey());
            if (existing.isPresent()) {
                ConfigurationEntity entity = existing.get();
                entity.setConfigValue(entry.getValue());
                entity.setVersion(entity.getVersion() + 1);
                entity.setSnapshotId(snapshotId);
                entity.setUpdatedAt(now);
                configurationRepository.save(entity);
            } else {
                ConfigurationEntity entity = new ConfigurationEntity(
                        UUID.randomUUID().toString(),
                        entry.getKey(),
                        entry.getValue(),
                        null,
                        "Restored from snapshot " + snapshotId,
                        1,
                        null,
                        null,
                        new LinkedHashMap<>(),
                        snapshotId,
                        true,
                        now,
                        now,
                        snapshot.getCreatedBy());
                configurationRepository.save(entity);
            }
        }
    }

    @Override
    public Map<String, String> compareSnapshots(String id1, String id2) {
        Optional<ConfigSnapshotEntity> first = snapshotRepository.findById(id1);
        Optional<ConfigSnapshotEntity> second = snapshotRepository.findById(id2);
        if (first.isEmpty() || second.isEmpty()) {
            throw new IllegalArgumentException("One or both snapshots not found");
        }
        Map<String, String> diff = new LinkedHashMap<>();
        Map<String, String> left = first.get().getConfigurations();
        Map<String, String> right = second.get().getConfigurations();
        for (Map.Entry<String, String> entry : left.entrySet()) {
            String key = entry.getKey();
            String other = right.get(key);
            if (other == null) {
                diff.put(key, "REMOVED (present in " + id1 + ")");
            } else if (!other.equals(entry.getValue())) {
                diff.put(key, "CHANGED: [" + entry.getValue() + "] -> [" + other + "]");
            }
        }
        for (Map.Entry<String, String> entry : right.entrySet()) {
            if (!left.containsKey(entry.getKey())) {
                diff.put(entry.getKey(), "ADDED (present in " + id2 + ")");
            }
        }
        return diff;
    }

    private ConfigSnapshot toDomain(ConfigSnapshotEntity entity) {
        ConfigSnapshot snapshot = new ConfigSnapshot();
        snapshot.setSnapshotId(entity.getSnapshotId());
        snapshot.setName(entity.getName());
        snapshot.setDescription(entity.getDescription());
        snapshot.setConfigurations(entity.getConfigurations());
        snapshot.setVersion(entity.getVersion());
        snapshot.setCreatedAt(entity.getCreatedAt());
        snapshot.setCreatedBy(entity.getCreatedBy());
        return snapshot;
    }
}
