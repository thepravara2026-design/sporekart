package com.sporekart.ai.knowledge.infrastructure.persistence;

import com.sporekart.ai.knowledge.api.KnowledgeDocumentRepository;
import com.sporekart.ai.knowledge.domain.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryKnowledgeDocumentRepository implements KnowledgeDocumentRepository {

    private final ConcurrentHashMap<KnowledgeDocumentId, KnowledgeDocument> store = new ConcurrentHashMap<>();

    @Override
    public Optional<KnowledgeDocument> findById(KnowledgeDocumentId id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<KnowledgeDocument> findBySourceId(KnowledgeSourceId sourceId) {
        return store.values().stream()
                .filter(d -> d.sourceId().equals(sourceId))
                .collect(Collectors.toList());
    }

    @Override
    public List<KnowledgeDocument> findByCollectionId(KnowledgeCollectionId collectionId) {
        return store.values().stream()
                .filter(d -> d.collectionId().equals(collectionId))
                .collect(Collectors.toList());
    }

    @Override
    public List<KnowledgeDocument> findByStatus(DocumentStatus status) {
        return store.values().stream()
                .filter(d -> d.status() == status)
                .collect(Collectors.toList());
    }

    @Override
    public List<KnowledgeDocument> findByWorkspaceId(String workspaceId) {
        return List.of();
    }

    @Override
    public List<KnowledgeDocument> findAll() {
        return List.copyOf(store.values());
    }

    @Override
    public KnowledgeDocument save(KnowledgeDocument document) {
        store.put(document.id(), document);
        return document;
    }

    @Override
    public void delete(KnowledgeDocumentId id) {
        store.remove(id);
    }

    @Override
    public boolean exists(KnowledgeDocumentId id) {
        return store.containsKey(id);
    }
}
