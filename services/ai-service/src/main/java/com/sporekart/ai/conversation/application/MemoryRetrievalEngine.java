package com.sporekart.ai.conversation.application;

import com.sporekart.ai.conversation.api.MemoryRepository;
import com.sporekart.ai.conversation.api.SummaryRepository;
import com.sporekart.ai.conversation.domain.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MemoryRetrievalEngine {
    private final MemoryRepository memoryRepository;
    private final SummaryRepository summaryRepository;

    public MemoryRetrievalEngine(MemoryRepository memoryRepository, SummaryRepository summaryRepository) {
        this.memoryRepository = memoryRepository;
        this.summaryRepository = summaryRepository;
    }

    public List<MemoryEntry> getRecentHistory(String userId, int limit) {
        var immediate = memoryRepository.findByLayerAndUser(MemoryLayer.IMMEDIATE, userId);
        var conversation = memoryRepository.findByLayerAndUser(MemoryLayer.CONVERSATION, userId);
        return Stream.concat(immediate.stream(), conversation.stream())
            .limit(limit)
            .toList();
    }

    public List<MemoryEntry> getConversationHistory(ConversationId conversationId) {
        return memoryRepository.findByLayer(MemoryLayer.CONVERSATION).stream()
            .filter(e -> {
                var meta = e.metadata();
                return meta != null && conversationId.toString().equals(meta.get("conversationId"));
            })
            .toList();
    }

    public List<MemoryEntry> getWorkspaceHistory(WorkspaceId workspaceId) {
        return memoryRepository.findByLayerAndWorkspace(MemoryLayer.WORKSPACE, workspaceId);
    }

    public List<MemoryEntry> getUserPreferences(String userId) {
        return memoryRepository.findByLayerAndUser(MemoryLayer.SESSION, userId);
    }

    public List<MemoryEntry> getBusinessMemory(WorkspaceId workspaceId) {
        var business = memoryRepository.findByLayerAndWorkspace(MemoryLayer.BUSINESS, workspaceId);
        var longTerm = memoryRepository.findByLayerAndWorkspace(MemoryLayer.LONG_TERM, workspaceId);
        return Stream.concat(business.stream(), longTerm.stream()).toList();
    }

    public List<Summary> getRelevantSummaries(ConversationId conversationId, int limit) {
        return summaryRepository.findByConversationIdOrderByCreatedAtDesc(conversationId).stream()
            .limit(limit)
            .toList();
    }

    public List<MemoryEntry> searchMemory(String query, MemoryLayer layer) {
        var q = query.toLowerCase();
        return memoryRepository.findByLayer(layer).stream()
            .filter(e -> e.key().toLowerCase().contains(q) || e.value().toLowerCase().contains(q))
            .toList();
    }

    public Map<String, Object> getConversationContext(ConversationId conversationId, WorkspaceId workspaceId, String userId) {
        var messages = memoryRepository.findByLayer(MemoryLayer.CONVERSATION).stream()
            .filter(e -> {
                var meta = e.metadata();
                return meta != null && conversationId.toString().equals(meta.get("conversationId"));
            })
            .toList();
        var summaries = getRelevantSummaries(conversationId, 5);
        var userPreferences = memoryRepository.findByLayerAndUser(MemoryLayer.SESSION, userId);
        var workspaceMemory = memoryRepository.findByLayerAndWorkspace(MemoryLayer.WORKSPACE, workspaceId);
        var businessMemory = Stream.concat(
            memoryRepository.findByLayerAndWorkspace(MemoryLayer.BUSINESS, workspaceId).stream(),
            memoryRepository.findByLayerAndWorkspace(MemoryLayer.LONG_TERM, workspaceId).stream()
        ).toList();

        var context = new HashMap<String, Object>();
        context.put("messages", messages);
        context.put("summaries", summaries);
        context.put("user_preferences", userPreferences);
        context.put("workspace_memory", workspaceMemory);
        context.put("business_memory", businessMemory);
        return context;
    }

    public MemoryEntry getConversationMetadata(ConversationId conversationId) {
        return memoryRepository.findByLayer(MemoryLayer.CONVERSATION).stream()
            .filter(e -> ("conversation:" + conversationId.toString()).equals(e.key()))
            .findFirst()
            .orElse(null);
    }
}
