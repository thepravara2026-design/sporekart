package com.sporekart.ai.conversation.application;

import static org.junit.jupiter.api.Assertions.*;

import com.sporekart.ai.conversation.domain.*;
import com.sporekart.ai.conversation.infrastructure.persistence.InMemoryConversationRepository;
import com.sporekart.ai.conversation.infrastructure.persistence.InMemorySessionRepository;
import java.time.Duration;
import java.time.Instant;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SessionManager")
class SessionManagerTest {

    private SessionManager manager;
    private InMemorySessionRepository sessionRepo;

    @BeforeEach
    void setUp() {
        sessionRepo = new InMemorySessionRepository();
        var conversationRepo = new InMemoryConversationRepository();
        manager = new SessionManager(sessionRepo, conversationRepo);
    }

    @Test
    @DisplayName("should create session with id, userId, status, and workspaceId")
    void shouldCreateSession() {
        var wsId = WorkspaceId.random();

        var session = manager.createSession("user1", wsId, Duration.ofHours(1));

        assertNotNull(session.getId());
        assertEquals("user1", session.getUserId());
        assertEquals("active", session.getStatus());
        assertEquals(wsId, session.getWorkspaceId());
    }

    @Test
    @DisplayName("should record activity and update lastActivityAt")
    void shouldRecordActivity() throws InterruptedException {
        var session = manager.createSession("user1", WorkspaceId.random(), Duration.ofHours(1));
        var originalActivity = session.getLastActivityAt();
        Thread.sleep(1);

        var updated = manager.recordActivity(session.getId());

        assertTrue(updated.getLastActivityAt().isAfter(originalActivity));
    }

    @Test
    @DisplayName("should close session and set status to closed")
    void shouldCloseSession() {
        var session = manager.createSession("user1", WorkspaceId.random(), Duration.ofHours(1));

        manager.closeSession(session.getId());

        var found = sessionRepo.findById(session.getId()).orElseThrow();
        assertEquals("closed", found.getStatus());
    }

    @Test
    @DisplayName("should detect expired sessions")
    void shouldDetectExpiredSession() {
        var now = Instant.now();
        var expiredPast = now.minus(Duration.ofHours(2));
        var expiredSession = new Session(SessionId.random(), "user1", WorkspaceId.random(), "active",
            new java.util.HashMap<>(), new java.util.ArrayList<>(), now.minus(Duration.ofDays(1)),
            now.minus(Duration.ofDays(1)), expiredPast, Duration.ofHours(1));
        sessionRepo.save(expiredSession);

        var activeSession = manager.createSession("user1", WorkspaceId.random(), Duration.ofHours(1));

        var expiredSessions = manager.getExpiredSessions();

        assertEquals(1, expiredSessions.size());
        assertEquals(expiredSession.getId(), expiredSessions.get(0).getId());
    }

    @Test
    @DisplayName("should list sessions by userId")
    void shouldListSessionsByUser() {
        var ws = WorkspaceId.random();
        manager.createSession("user1", ws, Duration.ofHours(1));
        manager.createSession("user1", ws, Duration.ofHours(1));
        manager.createSession("user2", ws, Duration.ofHours(1));

        var user1Sessions = manager.listSessions("user1");
        var user2Sessions = manager.listSessions("user2");

        assertEquals(2, user1Sessions.size());
        assertEquals(1, user2Sessions.size());
    }
}
