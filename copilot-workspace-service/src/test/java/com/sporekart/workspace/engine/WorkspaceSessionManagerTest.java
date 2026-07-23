package com.sporekart.workspace.engine;

import com.sporekart.workspace.domain.WorkspaceSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WorkspaceSessionManagerTest {

    private WorkspaceSessionManager sessionManager;

    @BeforeEach
    void setUp() {
        sessionManager = new WorkspaceSessionManager();
    }

    @Test
    void createSession_ShouldCreateNewSession() {
        WorkspaceSession session = sessionManager.createSession("ws-1", "user-1");
        assertNotNull(session.sessionId());
        assertEquals("ws-1", session.workspaceId());
        assertEquals("user-1", session.userId());
        assertEquals(WorkspaceSession.STATUS_ACTIVE, session.status());
    }

    @Test
    void getSession_ShouldReturnExistingSession() {
        WorkspaceSession created = sessionManager.createSession("ws-1", "user-1");
        WorkspaceSession found = sessionManager.getSession(created.sessionId());
        assertEquals(created.sessionId(), found.sessionId());
    }

    @Test
    void getSession_ShouldReturnNull_WhenSessionDoesNotExist() {
        assertNull(sessionManager.getSession("nonexistent-session"));
    }

    @Test
    void updateSession_ShouldModifySession() {
        WorkspaceSession session = sessionManager.createSession("ws-1", "user-1");
        WorkspaceSession updated = sessionManager.updateSession(session.sessionId(), "copilot-2", WorkspaceSession.STATUS_IDLE);
        assertEquals("copilot-2", updated.activeCopilotId());
        assertEquals(WorkspaceSession.STATUS_IDLE, updated.status());
    }

    @Test
    void closeSession_ShouldMarkAsClosed() {
        WorkspaceSession session = sessionManager.createSession("ws-1", "user-1");
        sessionManager.closeSession(session.sessionId());
        WorkspaceSession closed = sessionManager.getSession(session.sessionId());
        assertEquals(WorkspaceSession.STATUS_CLOSED, closed.status());
    }

    @Test
    void getActiveSessions_ShouldReturnActiveForWorkspace() {
        sessionManager.createSession("ws-1", "user-1");
        sessionManager.createSession("ws-1", "user-2");
        sessionManager.createSession("ws-2", "user-3");

        List<WorkspaceSession> active = sessionManager.getActiveSessions("ws-1");
        assertThat(active).hasSize(2);
    }

    @Test
    void getAllActiveSessions_ShouldReturnAllActive() {
        sessionManager.createSession("ws-1", "user-1");
        sessionManager.createSession("ws-2", "user-2");

        List<WorkspaceSession> allActive = sessionManager.getAllActiveSessions();
        assertThat(allActive).hasSize(2);
    }

    @Test
    void cleanupIdleSessions_ShouldCloseIdleSessions() {
        WorkspaceSession session = sessionManager.createSession("ws-1", "user-1");
        sessionManager.updateSession(session.sessionId(), null, WorkspaceSession.STATUS_IDLE);
        sessionManager.cleanupIdleSessions();

        WorkspaceSession cleaned = sessionManager.getSession(session.sessionId());
        assertEquals(WorkspaceSession.STATUS_CLOSED, cleaned.status());
    }

    @Test
    void validateSessionLimit_ShouldReturnTrueWithinLimit() {
        boolean withinLimit = sessionManager.validateSessionLimit("ws-1", 10);
        assertTrue(withinLimit);
    }

    @Test
    void touchSession_ShouldUpdateLastActivity() {
        WorkspaceSession session = sessionManager.createSession("ws-1", "user-1");
        OffsetDateTime before = session.lastActivityAt();
        sessionManager.touchSession(session.sessionId());
        WorkspaceSession touched = sessionManager.getSession(session.sessionId());
        assertThat(touched.lastActivityAt()).isAfter(before);
    }
}
