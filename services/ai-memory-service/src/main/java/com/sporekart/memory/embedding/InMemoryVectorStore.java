package com.sporekart.memory.embedding;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(name = "sporekart.memory.vector-provider", havingValue = "MEMORY", matchIfMissing = true)
public class InMemoryVectorStore implements VectorStore {

    private final Map<UUID, float[]> vectors = new ConcurrentHashMap<>();
    private final Map<UUID, String> metadatas = new ConcurrentHashMap<>();

    @Override
    public String storeName() { return "IN_MEMORY"; }

    @Override
    public void store(UUID id, float[] vector, String metadata) {
        vectors.put(id, vector);
        metadatas.put(id, metadata);
    }

    @Override
    public void storeBatch(List<VectorStoreEntry> entries) {
        entries.forEach(e -> store(e.id(), e.vector(), e.metadata()));
    }

    @Override
    public Optional<float[]> retrieve(UUID id) {
        return Optional.ofNullable(vectors.get(id));
    }

    @Override
    public void delete(UUID id) {
        vectors.remove(id);
        metadatas.remove(id);
    }

    @Override
    public void update(UUID id, float[] vector, String metadata) {
        vectors.put(id, vector);
        metadatas.put(id, metadata);
    }

    @Override
    public List<VectorSearchResult> search(float[] queryVector, int maxResults, double minScore) {
        return vectors.entrySet().stream()
                .map(e -> {
                    double score = cosineSimilarity(queryVector, e.getValue());
                    return new VectorSearchResult(e.getKey(), score, metadatas.get(e.getKey()));
                })
                .filter(r -> r.score() >= minScore)
                .sorted(Comparator.comparingDouble(VectorSearchResult::score).reversed())
                .limit(maxResults)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAvailable() { return true; }

    @Override
    public long size() { return vectors.size(); }

    private double cosineSimilarity(float[] a, float[] b) {
        double dot = 0, normA = 0, normB = 0;
        for (int i = 0; i < Math.min(a.length, b.length); i++) {
            dot += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }
        return dot / (Math.sqrt(normA) * Math.sqrt(normB) + 1e-10);
    }
}
