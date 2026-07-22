package com.sporekart.ai.conversation.application;

import com.sporekart.ai.conversation.api.MemoryRepository;
import com.sporekart.ai.conversation.api.MemoryService;
import com.sporekart.ai.conversation.domain.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class MemoryManager implements MemoryService {
    private final MemoryRepository memoryRepository;

    public MemoryManager(MemoryRepository memoryRepository) {
        this.memoryRepository = memoryRepository;
    }

    @Override
    public void store(MemoryLayer layer, String key, String value, Map<String, String> metadata) {
        var entry = new MemoryEntry(UUID.randomUUID().toString(), layer, key, value, metadata, Instant.now(), null);
        memoryRepository.save(entry);
    }

    @Override
    public void store(MemoryLayer layer, String key, String value, Map<String, String> metadata, Duration ttl) {
        var entry = new MemoryEntry(UUID.randomUUID().toString(), layer, key, value, metadata, Instant.now(),
            Instant.now().plus(ttl));
        memoryRepository.save(entry);
    }

    @Override
    public Optional<MemoryEntry> retrieve(String key, MemoryLayer layer) {
        return memoryRepository.findByKey(key).stream()
            .filter(e -> e.layer() == layer)
            .filter(e -> e.expiresAt() == null || e.expiresAt().isAfter(Instant.now()))
            .findFirst();
    }

    @Override
    public List<MemoryEntry> retrieveByLayer(MemoryLayer layer) {
        return memoryRepository.findByLayer(layer).stream()
            .filter(e -> e.expiresAt() == null || e.expiresAt().isAfter(Instant.now()))
            .toList();
    }

    @Override
    public List<MemoryEntry> retrieveByLayerAndWorkspace(MemoryLayer layer, WorkspaceId workspaceId) {
        return memoryRepository.findByLayerAndWorkspace(layer, workspaceId).stream()
            .filter(e -> e.expiresAt() == null || e.expiresAt().isAfter(Instant.now()))
            .toList();
    }

    @Override
    public List<MemoryEntry> query(String query, MemoryLayer layer) {
        var q = query.toLowerCase();
        return memoryRepository.findByLayer(layer).stream()
            .filter(e -> e.expiresAt() == null || e.expiresAt().isAfter(Instant.now()))
            .filter(e -> e.key().toLowerCase().contains(q) || e.value().toLowerCase().contains(q))
            .toList();
    }

    @Override
    public void clear(MemoryLayer layer) {
        memoryRepository.clearByLayer(layer);
    }

    @Override
    public void clearWorkspace(WorkspaceId workspaceId) {
        memoryRepository.clearByWorkspace(workspaceId);
    }

    @Override
    public void promote(MemoryEntry entry, MemoryLayer targetLayer) {
        var promoted = new MemoryEntry(UUID.randomUUID().toString(), targetLayer, entry.key(), entry.value(),
            entry.metadata(), Instant.now(), entry.expiresAt());
        memoryRepository.save(promoted);
    }
}
