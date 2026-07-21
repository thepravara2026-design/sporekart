package com.sporekart.ai.memory.api;

import com.sporekart.ai.memory.domain.MemoryEntry;
import com.sporekart.ai.memory.domain.MemoryQuery;
import com.sporekart.ai.memory.domain.MemoryStats;
import com.sporekart.ai.memory.domain.MemorySummary;
import com.sporekart.ai.memory.domain.ConsolidationPolicy;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemoryService {
    MemoryEntry store(MemoryEntry entry);
    Optional<MemoryEntry> retrieve(UUID id);
    List<MemoryEntry> search(MemoryQuery query);
    MemoryEntry update(MemoryEntry entry);
    void delete(UUID id);
    void deleteBySession(String sessionId);
    void deleteByAgent(String agentId);
    MemoryStats getStats();
    List<MemorySummary> getSummaries();
    void consolidate(ConsolidationPolicy policy);
    void prune(ConsolidationPolicy policy, int retentionDays);
}
