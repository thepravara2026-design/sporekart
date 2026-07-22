package com.sporekart.ai.knowledge.infrastructure.persistence;

import com.sporekart.ai.knowledge.api.KnowledgeSourceRepository;
import com.sporekart.ai.knowledge.domain.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryKnowledgeSourceRepository implements KnowledgeSourceRepository {

    private final ConcurrentHashMap<KnowledgeSourceId, KnowledgeSource> store = new ConcurrentHashMap<>();

    @Override
    public Optional<KnowledgeSource> findById(KnowledgeSourceId id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<KnowledgeSource> findByWorkspaceId(String workspaceId) {
        return store.values().stream()
                .filter(s -> s.workspaceId().equals(workspaceId))
                .collect(Collectors.toList());
    }

    @Override
    public List<KnowledgeSource> findByOwner(String owner) {
        return store.values().stream()
                .filter(s -> s.owner().equals(owner))
                .collect(Collectors.toList());
    }

    @Override
    public List<KnowledgeSource> findByStatus(DocumentStatus status) {
        return store.values().stream()
                .filter(s -> s.status() == status)
                .collect(Collectors.toList());
    }

    @Override
    public List<KnowledgeSource> findAll() {
        return List.copyOf(store.values());
    }

    @Override
    public KnowledgeSource save(KnowledgeSource source) {
        store.put(source.id(), source);
        return source;
    }

    @Override
    public void delete(KnowledgeSourceId id) {
        store.remove(id);
    }

    @Override
    public boolean exists(KnowledgeSourceId id) {
        return store.containsKey(id);
    }
}
