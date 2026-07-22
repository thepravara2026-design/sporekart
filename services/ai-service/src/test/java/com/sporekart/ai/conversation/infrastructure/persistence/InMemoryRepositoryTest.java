package com.sporekart.ai.conversation.infrastructure.persistence;

import com.sporekart.ai.conversation.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryRepositoryTest {

    private InMemoryConversationRepository conversationRepo;
    private InMemoryMessageRepository messageRepo;
    private InMemoryMemoryRepository memoryRepo;
    private InMemorySessionRepository sessionRepo;
    private InMemorySummaryRepository summaryRepo;

    @BeforeEach
    void setUp() {
        conversationRepo = new InMemoryConversationRepository();
        messageRepo = new InMemoryMessageRepository();
        memoryRepo = new InMemoryMemoryRepository();
        sessionRepo = new InMemorySessionRepository();
        summaryRepo = new InMemorySummaryRepository();
    }

    @Test
    void shouldSaveAndFindConversation() {
        var id = ConversationId.random();
        var conv = new Conversation(id, "Test", ConversationStatus.ACTIVE, SessionId.random(),
            WorkspaceId.random(), "user1", List.of(), Map.of(), Set.of(),
            Instant.now(), Instant.now(), null, 0, 0);
        conversationRepo.save(conv);
        assertTrue(conversationRepo.findById(id).isPresent());
        assertEquals("Test", conversationRepo.findById(id).get().getTitle());
    }

    @Test
    void shouldSaveAndFindMessages() {
        var convId = ConversationId.random();
        var msgId = MessageId.random();
        var msg = new Message(msgId, convId, MessageType.USER, "Hello", "user", Map.of(),
            List.of(), List.of(), null, null, null, null, null, "sent", Instant.now(), Instant.now());
        messageRepo.save(msg);
        var found = messageRepo.findByConversationId(convId);
        assertEquals(1, found.size());
        assertEquals("Hello", found.getFirst().getContent());
    }

    @Test
    void shouldStoreAndRetrieveMemory() {
        var entry = new MemoryEntry("mem1", MemoryLayer.WORKSPACE, "key1", "value1", Map.of(), Instant.now(), null);
        memoryRepo.save(entry);
        var found = memoryRepo.findByKey("key1");
        assertEquals(1, found.size());
        assertEquals("value1", found.getFirst().value());
    }

    @Test
    void shouldSaveAndFindSession() {
        var id = SessionId.random();
        var session = new Session(id, "user1", WorkspaceId.random(), "active", Map.of(), List.of(),
            Instant.now(), Instant.now(), Instant.now().plusSeconds(3600), java.time.Duration.ofMinutes(30));
        sessionRepo.save(session);
        assertTrue(sessionRepo.findById(id).isPresent());
        assertEquals("user1", sessionRepo.findById(id).get().getUserId());
    }

    @Test
    void shouldSaveAndFindSummary() {
        var convId = ConversationId.random();
        var summary = new Summary("sum1", convId, "Test summary", "sliding_window",
            1000, 200, 80, Instant.now());
        summaryRepo.save(summary);
        assertTrue(summaryRepo.findByConversationId(convId).isPresent());
    }

    @Test
    void shouldDeleteConversation() {
        var id = ConversationId.random();
        var conv = new Conversation(id, "Test", ConversationStatus.ACTIVE, SessionId.random(),
            WorkspaceId.random(), "user1", List.of(), Map.of(), Set.of(),
            Instant.now(), Instant.now(), null, 0, 0);
        conversationRepo.save(conv);
        assertTrue(conversationRepo.exists(id));
        conversationRepo.delete(id);
        assertFalse(conversationRepo.findById(id).isPresent());
    }
}
