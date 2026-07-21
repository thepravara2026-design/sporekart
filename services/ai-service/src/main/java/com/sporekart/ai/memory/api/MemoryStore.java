package com.sporekart.ai.memory.api;

import com.sporekart.ai.memory.domain.MemoryEntry;
import com.sporekart.ai.memory.domain.MemoryQuery;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemoryStore {
    MemoryEntry save(MemoryEntry entry);
    Optional<MemoryEntry> findById(UUID id);
    List<MemoryEntry> findAll(MemoryQuery query);
    MemoryEntry update(MemoryEntry entry);
    void deleteById(UUID id);
    void deleteBySession(String sessionId);
    void deleteByAgent(String agentId);
    long count();
    long countByType(String type);
}
