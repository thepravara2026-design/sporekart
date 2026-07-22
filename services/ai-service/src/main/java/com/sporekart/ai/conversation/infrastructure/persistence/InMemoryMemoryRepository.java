package com.sporekart.ai.conversation.infrastructure.persistence;

import com.sporekart.ai.conversation.api.MemoryRepository;
import com.sporekart.ai.conversation.domain.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryMemoryRepository implements MemoryRepository {

    private final ConcurrentHashMap<String, MemoryEntry> store = new ConcurrentHashMap<>();

    @Override
    public List<MemoryEntry> findByLayer(MemoryLayer layer) {
        List<MemoryEntry> result = new ArrayList<>();
        for (MemoryEntry e : store.values()) {
            if (layer == e.layer()) {
                result.add(e);
            }
        }
        return result;
    }

    @Override
    public List<MemoryEntry> findByKey(String key) {
        List<MemoryEntry> result = new ArrayList<>();
        for (MemoryEntry e : store.values()) {
            if (key.equals(e.key())) {
                result.add(e);
            }
        }
        return result;
    }

    @Override
    public List<MemoryEntry> findByLayerAndWorkspace(MemoryLayer layer, WorkspaceId workspaceId) {
        List<MemoryEntry> result = new ArrayList<>();
        for (MemoryEntry e : store.values()) {
            if (layer == e.layer() && workspaceId.toString().equals(e.metadata().get("workspaceId"))) {
                result.add(e);
            }
        }
        return result;
    }

    @Override
    public List<MemoryEntry> findByLayerAndUser(MemoryLayer layer, String userId) {
        List<MemoryEntry> result = new ArrayList<>();
        for (MemoryEntry e : store.values()) {
            if (layer == e.layer() && userId.equals(e.metadata().get("userId"))) {
                result.add(e);
            }
        }
        return result;
    }

    @Override
    public List<MemoryEntry> findExpired() {
        Instant now = Instant.now();
        List<MemoryEntry> result = new ArrayList<>();
        for (MemoryEntry e : store.values()) {
            if (e.expiresAt() != null && e.expiresAt().isBefore(now)) {
                result.add(e);
            }
        }
        return result;
    }

    @Override
    public MemoryEntry save(MemoryEntry entry) {
        store.put(entry.id(), entry);
        return entry;
    }

    @Override
    public List<MemoryEntry> saveAll(List<MemoryEntry> entries) {
        List<MemoryEntry> saved = new ArrayList<>(entries.size());
        for (MemoryEntry e : entries) {
            saved.add(save(e));
        }
        return saved;
    }

    @Override
    public void delete(String id) {
        store.remove(id);
    }

    @Override
    public void clearByLayer(MemoryLayer layer) {
        store.entrySet().removeIf(e -> layer == e.getValue().layer());
    }

    @Override
    public void clearByWorkspace(WorkspaceId workspaceId) {
        store.entrySet().removeIf(e -> workspaceId.toString().equals(e.getValue().metadata().get("workspaceId")));
    }

    @Override
    public boolean exists(String key, MemoryLayer layer) {
        for (MemoryEntry e : store.values()) {
            if (key.equals(e.key()) && layer == e.layer()) {
                return true;
            }
        }
        return false;
    }
}
