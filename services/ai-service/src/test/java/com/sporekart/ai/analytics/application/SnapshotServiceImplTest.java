package com.sporekart.ai.analytics.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.analytics.domain.GovernanceSnapshot;
import com.sporekart.ai.analytics.infrastructure.persistence.GovernanceSnapshotEntity;
import com.sporekart.ai.analytics.infrastructure.persistence.GovernanceSnapshotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SnapshotServiceImplTest {

    @Mock private GovernanceSnapshotRepository repository;
    private ObjectMapper objectMapper;
    private SnapshotServiceImpl service;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        service = new SnapshotServiceImpl(repository, objectMapper);
    }

    @Test
    void testCreateSnapshot() {
        var entity = createEntity();
        when(repository.save(any(GovernanceSnapshotEntity.class))).thenReturn(entity);

        GovernanceSnapshot snapshot = service.createSnapshot("test-snapshot", Map.of("key", "value"));

        assertNotNull(snapshot);
        assertEquals("test-snapshot", snapshot.name());
    }

    @Test
    void testGetSnapshot() {
        var entity = createEntity();
        when(repository.findById(entity.getId())).thenReturn(Optional.of(entity));

        GovernanceSnapshot snapshot = service.getSnapshot(entity.getId());

        assertNotNull(snapshot);
        assertEquals(entity.getId(), snapshot.id());
    }

    @Test
    void testGetSnapshotsByName() {
        var entity = createEntity();
        when(repository.findByName("test-snapshot")).thenReturn(List.of(entity));

        List<GovernanceSnapshot> snapshots = service.getSnapshotsByName("test-snapshot");

        assertEquals(1, snapshots.size());
        assertEquals("test-snapshot", snapshots.get(0).name());
    }

    private GovernanceSnapshotEntity createEntity() {
        var entity = new GovernanceSnapshotEntity();
        entity.setId(UUID.randomUUID());
        entity.setName("test-snapshot");
        entity.setData("{}");
        entity.setCapturedAt(OffsetDateTime.now());
        entity.setCreatedAt(OffsetDateTime.now());
        return entity;
    }
}
