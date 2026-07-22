package com.sporekart.ai.knowledge.infrastructure.persistence;

import com.sporekart.ai.knowledge.api.KnowledgeCollectionRepository;
import com.sporekart.ai.knowledge.domain.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryKnowledgeCollectionRepository implements KnowledgeCollectionRepository {

    private final ConcurrentHashMap<KnowledgeCollectionId, KnowledgeCollection> store = new ConcurrentHashMap<>();

    @Override
    public Optional<KnowledgeCollection> findById(KnowledgeCollectionId id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<KnowledgeCollection> findByWorkspaceId(String workspaceId) {
        return store.values().stream()
                .filter(c -> c.workspaceId().equals(workspaceId))
                .collect(Collectors.toList());
    }

    @Override
    public List<KnowledgeCollection> findByOwner(String owner) {
        return store.values().stream()
                .filter(c -> c.owner().equals(owner))
                .collect(Collectors.toList());
    }

    @Override
    public List<KnowledgeCollection> findAll() {
        return List.copyOf(store.values());
    }

    @Override
    public KnowledgeCollection save(KnowledgeCollection collection) {
        store.put(collection.id(), collection);
        return collection;
    }

    @Override
    public void delete(KnowledgeCollectionId id) {
        store.remove(id);
    }

    @Override
    public boolean exists(KnowledgeCollectionId id) {
        return store.containsKey(id);
    }
}
