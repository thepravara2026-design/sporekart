package com.sporekart.ai.conversation.application;

import static org.junit.jupiter.api.Assertions.*;

import com.sporekart.ai.conversation.domain.*;
import com.sporekart.ai.conversation.infrastructure.persistence.InMemoryMemoryRepository;
import com.sporekart.ai.conversation.infrastructure.persistence.InMemorySummaryRepository;
import java.time.Instant;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MemoryRetrievalEngineTest {

    private MemoryRetrievalEngine engine;
    private InMemoryMemoryRepository memoryRepo;
    private InMemorySummaryRepository summaryRepo;

    @BeforeEach
    void setUp() {
        memoryRepo = new InMemoryMemoryRepository();
        summaryRepo = new InMemorySummaryRepository();
        engine = new MemoryRetrievalEngine(memoryRepo, summaryRepo);
    }

    private MemoryEntry entry(MemoryLayer layer, String key, String value, Map<String, String> meta) {
        return new MemoryEntry(java.util.UUID.randomUUID().toString(), layer, key, value, meta,
            Instant.now(), null);
    }

    @Test
    void shouldGetRecentHistory() {
        memoryRepo.save(entry(MemoryLayer.IMMEDIATE, "immediate_1", "just now", Map.of("userId", "u1")));
        memoryRepo.save(entry(MemoryLayer.CONVERSATION, "recent_1", "recent memory", Map.of("userId", "u1")));
        memoryRepo.save(entry(MemoryLayer.IMMEDIATE, "immediate_2", "also now", Map.of("userId", "u1")));
        var history = engine.getRecentHistory("u1", 5);
        assertEquals(3, history.size());
    }

    @Test
    void shouldGetUserPreferences() {
        memoryRepo.save(entry(MemoryLayer.SESSION, "lang_pref", "en", Map.of("userId", "u1")));
        memoryRepo.save(entry(MemoryLayer.SESSION, "theme_pref", "dark", Map.of("userId", "u1")));
        memoryRepo.save(entry(MemoryLayer.SESSION, "lang_pref", "fr", Map.of("userId", "u2")));
        var prefs = engine.getUserPreferences("u1");
        assertEquals(2, prefs.size());
    }

    @Test
    void shouldGetBusinessMemory() {
        var wsId = WorkspaceId.random();
        memoryRepo.save(entry(MemoryLayer.BUSINESS, "company_policy", "policy data", Map.of("workspaceId", wsId.toString())));
        memoryRepo.save(entry(MemoryLayer.LONG_TERM, "company_facts", "fact data", Map.of("workspaceId", wsId.toString())));
        memoryRepo.save(entry(MemoryLayer.WORKSPACE, "temp", "temp data", Map.of("workspaceId", wsId.toString())));
        var businessMemory = engine.getBusinessMemory(wsId);
        assertEquals(2, businessMemory.size());
    }

    @Test
    void shouldGetConversationContext() {
        var convId = ConversationId.random();
        var wsId = WorkspaceId.random();
        var userId = "u1";
        var meta = Map.of("conversationId", convId.toString(), "workspaceId", wsId.toString(), "userId", userId);
        memoryRepo.save(entry(MemoryLayer.CONVERSATION, "msg", "hello", meta));
        memoryRepo.save(entry(MemoryLayer.SESSION, "pref", "dark", Map.of("userId", userId)));
        memoryRepo.save(entry(MemoryLayer.WORKSPACE, "ws_data", "workspace info", Map.of("workspaceId", wsId.toString())));
        memoryRepo.save(entry(MemoryLayer.BUSINESS, "business", "business rule", Map.of("workspaceId", wsId.toString())));
        var context = engine.getConversationContext(convId, wsId, userId);
        assertTrue(context.containsKey("messages"));
        assertTrue(context.containsKey("user_preferences"));
        assertTrue(context.containsKey("workspace_memory"));
        assertTrue(context.containsKey("business_memory"));
        assertTrue(context.containsKey("summaries"));
    }

    @Test
    void shouldSearchMemory() {
        memoryRepo.save(entry(MemoryLayer.CONVERSATION, "product_name", "SporeKart Platform", Map.of()));
        memoryRepo.save(entry(MemoryLayer.CONVERSATION, "product_version", "v2.1.0", Map.of()));
        memoryRepo.save(entry(MemoryLayer.CONVERSATION, "service_name", "Auth Service", Map.of()));
        var results = engine.searchMemory("product", MemoryLayer.CONVERSATION);
        assertEquals(2, results.size());
    }
}
