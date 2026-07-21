package com.sporekart.ai.memory.api;

import com.sporekart.ai.memory.domain.MemoryEntry;

import java.util.List;
import java.util.UUID;

public interface MemoryIndexService {
    void index(MemoryEntry entry);
    void reindex(UUID id);
    void reindexAll();
    void removeFromIndex(UUID id);
    List<MemoryEntry> searchByVector(float[] vector, int maxResults);
    List<MemoryEntry> searchByKeywords(String keywords, int maxResults);
}
