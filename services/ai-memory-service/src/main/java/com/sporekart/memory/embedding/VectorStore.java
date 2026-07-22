package com.sporekart.memory.embedding;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VectorStore {
    String storeName();
    void store(UUID id, float[] vector, String metadata);
    void storeBatch(List<VectorStoreEntry> entries);
    Optional<float[]> retrieve(UUID id);
    void delete(UUID id);
    void update(UUID id, float[] vector, String metadata);
    List<VectorSearchResult> search(float[] queryVector, int maxResults, double minScore);
    boolean isAvailable();
    long size();

    record VectorStoreEntry(UUID id, float[] vector, String metadata) {}
    record VectorSearchResult(UUID id, double score, String metadata) {}
}
