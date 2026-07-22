package com.sporekart.ai.conversation.application;

import static org.junit.jupiter.api.Assertions.*;

import com.sporekart.ai.conversation.domain.*;
import com.sporekart.ai.conversation.infrastructure.persistence.InMemoryConversationRepository;
import com.sporekart.ai.conversation.infrastructure.persistence.InMemoryMessageRepository;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ConversationManager")
class ConversationManagerTest {

    private ConversationManager manager;
    private InMemoryConversationRepository conversationRepo;
    private InMemoryMessageRepository messageRepo;

    @BeforeEach
    void setUp() {
        conversationRepo = new InMemoryConversationRepository();
        messageRepo = new InMemoryMessageRepository();
        manager = new ConversationManager(conversationRepo, messageRepo);
    }

    @Test
    @DisplayName("should create conversation with valid id, status, title, and userId")
    void shouldCreateConversation() {
        var sessionId = SessionId.random();
        var workspaceId = WorkspaceId.random();
        var conversation = manager.createConversation("Test Conversation", sessionId, workspaceId, "user1");

        assertNotNull(conversation.getId());
        assertEquals(ConversationStatus.ACTIVE, conversation.getStatus());
        assertEquals("Test Conversation", conversation.getTitle());
        assertEquals("user1", conversation.getUserId());
    }

    @Test
    @DisplayName("should close conversation and set CLOSED status with closedAt")
    void shouldCloseConversation() {
        var conversation = manager.createConversation("To Close", SessionId.random(), WorkspaceId.random(), "user1");

        var closed = manager.closeConversation(conversation.getId());

        assertEquals(ConversationStatus.CLOSED, closed.getStatus());
        assertNotNull(closed.getClosedAt());
    }

    @Test
    @DisplayName("should archive conversation and set ARCHIVED status")
    void shouldArchiveConversation() {
        var conversation = manager.createConversation("To Archive", SessionId.random(), WorkspaceId.random(), "user1");

        var archived = manager.archiveConversation(conversation.getId());

        assertEquals(ConversationStatus.ARCHIVED, archived.getStatus());
    }

    @Test
    @DisplayName("should restore archived conversation back to ACTIVE")
    void shouldRestoreArchivedConversation() {
        var conversation = manager.createConversation("To Restore", SessionId.random(), WorkspaceId.random(), "user1");
        manager.archiveConversation(conversation.getId());

        var restored = manager.restoreConversation(conversation.getId());

        assertEquals(ConversationStatus.ACTIVE, restored.getStatus());
    }

    @Test
    @DisplayName("should delete conversation and set DELETED status")
    void shouldDeleteConversation() {
        var conversation = manager.createConversation("To Delete", SessionId.random(), WorkspaceId.random(), "user1");

        manager.deleteConversation(conversation.getId());

        var deleted = conversationRepo.findById(conversation.getId()).orElseThrow();
        assertEquals(ConversationStatus.DELETED, deleted.getStatus());
    }

    @Test
    @DisplayName("should retrieve conversation by ID")
    void shouldGetConversation() {
        var conversation = manager.createConversation("Get Me", SessionId.random(), WorkspaceId.random(), "user1");

        var found = manager.getConversation(conversation.getId());

        assertEquals(conversation.getId(), found.getId());
        assertEquals(conversation.getTitle(), found.getTitle());
        assertEquals(conversation.getUserId(), found.getUserId());
    }

    @Test
    @DisplayName("should throw NoSuchElementException for missing conversation")
    void shouldThrowOnMissingConversation() {
        var missingId = ConversationId.random();

        assertThrows(NoSuchElementException.class, () -> manager.getConversation(missingId));
    }

    @Test
    @DisplayName("should list conversations filtered by userId")
    void shouldListConversationsByUser() {
        var sessionId = SessionId.random();
        var ws = WorkspaceId.random();
        manager.createConversation("User1-1", sessionId, ws, "user1");
        manager.createConversation("User1-2", sessionId, ws, "user1");
        manager.createConversation("User2-1", sessionId, ws, "user2");

        var user1Convs = manager.listConversations("user1", null, null);
        var user2Convs = manager.listConversations("user2", null, null);

        assertEquals(2, user1Convs.size());
        assertEquals(1, user2Convs.size());
    }

    @Test
    @DisplayName("should list conversations filtered by status")
    void shouldListConversationsByStatus() {
        var ws = WorkspaceId.random();
        var sessionId = SessionId.random();
        var active = manager.createConversation("Active", sessionId, ws, "user1");
        manager.createConversation("To Close", sessionId, ws, "user1");
        manager.closeConversation(ConversationId.fromString(manager.listConversations("user1", null, null).stream()
            .filter(c -> c.getTitle().equals("To Close")).findFirst().orElseThrow().getId().toString()));

        var closedList = manager.listConversations("user1", null, ConversationStatus.CLOSED);

        assertEquals(1, closedList.size());
        assertEquals(ConversationStatus.CLOSED, closedList.get(0).getStatus());
    }

    @Test
    @DisplayName("should increment message count when adding a message")
    void shouldAddMessageIncrementsCount() {
        var ws = WorkspaceId.random();
        var sessionId = SessionId.random();
        var conversation = manager.createConversation("Count Test", sessionId, ws, "user1");
        var message = new Message(MessageId.random(), conversation.getId(), MessageType.USER, "hello", "user",
            Map.of(), List.of(), List.of(), null, null, null, null, null, "active", Instant.now(), Instant.now());

        var updated = manager.addMessage(conversation.getId(), message);

        assertEquals(1, updated.getMessageCount());
    }
}
