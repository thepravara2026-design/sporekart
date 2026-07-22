package com.sporekart.ai.conversation.application;

import static org.junit.jupiter.api.Assertions.*;

import com.sporekart.ai.conversation.domain.*;
import com.sporekart.ai.conversation.infrastructure.persistence.InMemoryMemoryRepository;
import java.time.Duration;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MemoryManager")
class MemoryManagerTest {

    private MemoryManager manager;
    private InMemoryMemoryRepository repo;

    @BeforeEach
    void setUp() {
        repo = new InMemoryMemoryRepository();
        manager = new MemoryManager(repo);
    }

    @Test
    void shouldStoreAndRetrieve() {
        manager.store(MemoryLayer.CONVERSATION, "user_name", "Alice", Map.of("userId", "u1"));
        var result = manager.retrieve("user_name", MemoryLayer.CONVERSATION);
        assertTrue(result.isPresent());
        assertEquals("Alice", result.get().value());
        assertEquals(MemoryLayer.CONVERSATION, result.get().layer());
    }

    @Test
    void shouldStoreWithTTL() throws InterruptedException {
        manager.store(MemoryLayer.IMMEDIATE, "temp_key", "temp_value", Map.of(), Duration.ofMillis(1));
        Thread.sleep(5);
        var expired = repo.findExpired();
        assertEquals(1, expired.size());
        var result = manager.retrieve("temp_key", MemoryLayer.IMMEDIATE);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldRetrieveByLayer() {
        manager.store(MemoryLayer.WORKSPACE, "task_1", "task data", Map.of());
        manager.store(MemoryLayer.WORKSPACE, "task_2", "more data", Map.of());
        manager.store(MemoryLayer.BUSINESS, "concept", "semantic data", Map.of());
        var workingEntries = manager.retrieveByLayer(MemoryLayer.WORKSPACE);
        var semanticEntries = manager.retrieveByLayer(MemoryLayer.BUSINESS);
        assertEquals(2, workingEntries.size());
        assertEquals(1, semanticEntries.size());
    }

    @Test
    void shouldQueryMemory() {
        manager.store(MemoryLayer.CONVERSATION, "user_email", "alice@example.com", Map.of());
        manager.store(MemoryLayer.CONVERSATION, "user_phone", "1234567890", Map.of());
        manager.store(MemoryLayer.CONVERSATION, "admin_email", "admin@example.com", Map.of());
        var emailResults = manager.query("email", MemoryLayer.CONVERSATION);
        assertEquals(2, emailResults.size());
    }

    @Test
    void shouldClearLayer() {
        manager.store(MemoryLayer.WORKSPACE, "task_1", "hello", Map.of());
        manager.store(MemoryLayer.WORKSPACE, "task_2", "world", Map.of());
        manager.store(MemoryLayer.BUSINESS, "config", "config data", Map.of());
        manager.clear(MemoryLayer.WORKSPACE);
        assertEquals(0, manager.retrieveByLayer(MemoryLayer.WORKSPACE).size());
        assertEquals(1, manager.retrieveByLayer(MemoryLayer.BUSINESS).size());
    }

    @Test
    void shouldPromoteMemory() {
        manager.store(MemoryLayer.CONVERSATION, "important_info", "key data", Map.of("userId", "u1"));
        var original = manager.retrieve("important_info", MemoryLayer.CONVERSATION).orElseThrow();
        manager.promote(original, MemoryLayer.LONG_TERM);
        var inShortTerm = manager.retrieve("important_info", MemoryLayer.CONVERSATION);
        var inLongTerm = repo.findByLayer(MemoryLayer.LONG_TERM).stream()
            .filter(e -> "important_info".equals(e.key()))
            .findFirst();
        assertTrue(inShortTerm.isPresent());
        assertTrue(inLongTerm.isPresent());
        assertEquals("key data", inLongTerm.get().value());
    }

    @Test
    void shouldHandleMissingKey() {
        var result = manager.retrieve("non_existent_key", MemoryLayer.CONVERSATION);
        assertTrue(result.isEmpty());
    }
}
