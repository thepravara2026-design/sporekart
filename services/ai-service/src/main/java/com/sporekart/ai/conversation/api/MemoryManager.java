package com.sporekart.ai.conversation.api;

import com.sporekart.ai.conversation.domain.MemoryEntry;
import com.sporekart.ai.conversation.domain.MemoryType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemoryManager {
    MemoryEntry storeMemory(UUID sessionId, MemoryType type, String summary, String keywords, double relevanceScore);
    Optional<MemoryEntry> getMemory(UUID memoryId);
    List<MemoryEntry> getSessionMemories(UUID sessionId);
    List<MemoryEntry> getSessionMemoriesByType(UUID sessionId, MemoryType type);
    List<MemoryEntry> getRelevantMemories(UUID sessionId, String query, int limit);
    void deleteMemory(UUID memoryId);
    void deleteSessionMemories(UUID sessionId);
    MemoryEntry updateRelevance(UUID memoryId, double relevanceScore);
}
