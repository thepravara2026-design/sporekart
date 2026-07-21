package com.sporekart.ai.memory.application;

import com.sporekart.ai.memory.api.MemoryIndexService;
import com.sporekart.ai.memory.domain.MemoryEntry;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MemoryIndexServiceImpl implements MemoryIndexService {

    @Override
    public void index(MemoryEntry entry) {
    }

    @Override
    public void reindex(UUID id) {
    }

    @Override
    public void reindexAll() {
    }

    @Override
    public void removeFromIndex(UUID id) {
    }

    @Override
    public List<MemoryEntry> searchByVector(float[] vector, int maxResults) {
        return List.of();
    }

    @Override
    public List<MemoryEntry> searchByKeywords(String keywords, int maxResults) {
        return List.of();
    }
}
