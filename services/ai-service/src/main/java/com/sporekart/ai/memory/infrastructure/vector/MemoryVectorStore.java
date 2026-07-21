package com.sporekart.ai.memory.infrastructure.vector;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class MemoryVectorStore {

    public void storeEmbedding(UUID memoryId, float[] embedding) {
    }

    public List<UUID> searchSimilar(float[] queryVector, int maxResults) {
        return List.of();
    }

    public void removeEmbedding(UUID memoryId) {
    }

    public void rebuildIndex() {
    }
}
