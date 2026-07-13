package com.sporekart.ai.analytics.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.analytics.api.SnapshotService;
import com.sporekart.ai.analytics.domain.GovernanceSnapshot;
import com.sporekart.ai.analytics.infrastructure.persistence.GovernanceSnapshotEntity;
import com.sporekart.ai.analytics.infrastructure.persistence.GovernanceSnapshotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SnapshotServiceImpl implements SnapshotService {

    private final GovernanceSnapshotRepository repository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public GovernanceSnapshot createSnapshot(String name, Map<String, Object> data) {
        var entity = new GovernanceSnapshotEntity();
        entity.setId(UUID.randomUUID());
        entity.setName(name);
        try {
            entity.setData(objectMapper.writeValueAsString(data != null ? data : Map.of()));
        } catch (Exception e) {
            entity.setData("{}");
        }
        entity.setCapturedAt(java.time.OffsetDateTime.now());
        entity.setCreatedAt(java.time.OffsetDateTime.now());

        var saved = repository.save(entity);
        log.info("Created snapshot '{}' with id {}", name, saved.getId());
        return toDomain(saved);
    }

    @Override
    public GovernanceSnapshot getSnapshot(UUID id) {
        return repository.findById(id).map(this::toDomain).orElse(null);
    }

    @Override
    public List<GovernanceSnapshot> getSnapshotsByName(String name) {
        return repository.findByName(name).stream().map(this::toDomain).toList();
    }

    @Override
    public List<GovernanceSnapshot> getSnapshotsByDateRange(Instant from, Instant to) {
        var fromLdt = LocalDateTime.ofInstant(from, ZoneId.systemDefault());
        var toLdt = LocalDateTime.ofInstant(to, ZoneId.systemDefault());
        return repository.findByCapturedAtBetween(fromLdt, toLdt).stream().map(this::toDomain).toList();
    }

    @SuppressWarnings("unchecked")
    private GovernanceSnapshot toDomain(GovernanceSnapshotEntity entity) {
        Map<String, Object> dataMap = Map.of();
        try {
            dataMap = objectMapper.readValue(entity.getData(), HashMap.class);
        } catch (Exception e) {
            log.warn("Failed to deserialize snapshot data for {}: {}", entity.getId(), e.getMessage());
        }
        return new GovernanceSnapshot(
                entity.getId(),
                entity.getName(),
                dataMap,
                entity.getCapturedAt().toInstant()
        );
    }
}
