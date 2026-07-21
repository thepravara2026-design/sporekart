package com.sporekart.ai.memory.application;

import com.sporekart.ai.memory.api.MemoryRetrievalService;
import com.sporekart.ai.memory.domain.MemoryEntry;
import com.sporekart.ai.memory.domain.MemoryQuery;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemoryRetrievalServiceImpl implements MemoryRetrievalService {

    @Override
    public List<MemoryEntry> semanticSearch(String query, int maxResults) {
        return List.of();
    }

    @Override
    public List<MemoryEntry> contextualSearch(String contextId, MemoryQuery query) {
        return List.of();
    }

    @Override
    public List<MemoryEntry> recentBySession(String sessionId, int limit) {
        return List.of();
    }

    @Override
    public List<MemoryEntry> recentByAgent(String agentId, int limit) {
        return List.of();
    }

    @Override
    public List<MemoryEntry> importantByUser(String userId, int minImportance, int limit) {
        return List.of();
    }
}
