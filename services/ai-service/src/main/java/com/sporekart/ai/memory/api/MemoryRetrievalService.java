package com.sporekart.ai.memory.api;

import com.sporekart.ai.memory.domain.MemoryEntry;
import com.sporekart.ai.memory.domain.MemoryQuery;

import java.util.List;

public interface MemoryRetrievalService {
    List<MemoryEntry> semanticSearch(String query, int maxResults);
    List<MemoryEntry> contextualSearch(String contextId, MemoryQuery query);
    List<MemoryEntry> recentBySession(String sessionId, int limit);
    List<MemoryEntry> recentByAgent(String agentId, int limit);
    List<MemoryEntry> importantByUser(String userId, int minImportance, int limit);
}
