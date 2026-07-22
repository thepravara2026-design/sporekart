package com.sporekart.ai.knowledge.infrastructure.persistence;

import com.sporekart.ai.knowledge.api.KnowledgeChunkRepository;
import com.sporekart.ai.knowledge.domain.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryKnowledgeChunkRepository implements KnowledgeChunkRepository {

    private final ConcurrentHashMap<KnowledgeChunkId, KnowledgeChunk> store = new ConcurrentHashMap<>();

    @Override
    public Optional<KnowledgeChunk> findById(KnowledgeChunkId id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<KnowledgeChunk> findByDocumentId(KnowledgeDocumentId documentId) {
        return store.values().stream()
                .filter(c -> c.documentId().equals(documentId))
                .collect(Collectors.toList());
    }

    @Override
    public List<KnowledgeChunk> findByDocumentIdOrderBySequence(KnowledgeDocumentId documentId) {
        return store.values().stream()
                .filter(c -> c.documentId().equals(documentId))
                .sorted(Comparator.comparingInt(KnowledgeChunk::chunkIndex))
                .collect(Collectors.toList());
    }

    @Override
    public List<KnowledgeChunk> findByEmbeddingIsNotNull() {
        return store.values().stream()
                .filter(c -> c.embedding() != null)
                .collect(Collectors.toList());
    }

    @Override
    public List<KnowledgeChunk> findAll() {
        return List.copyOf(store.values());
    }

    @Override
    public KnowledgeChunk save(KnowledgeChunk chunk) {
        store.put(chunk.id(), chunk);
        return chunk;
    }

    @Override
    public List<KnowledgeChunk> saveAll(List<KnowledgeChunk> chunks) {
        for (KnowledgeChunk chunk : chunks) {
            store.put(chunk.id(), chunk);
        }
        return chunks;
    }

    @Override
    public void delete(KnowledgeChunkId id) {
        store.remove(id);
    }

    @Override
    public void deleteByDocumentId(KnowledgeDocumentId documentId) {
        store.values().removeIf(c -> c.documentId().equals(documentId));
    }
}
