package com.sporekart.copilot.service;

import com.sporekart.copilot.context.UserContext;
import com.sporekart.copilot.domain.CopilotStatus;
import com.sporekart.copilot.domain.CopilotType;
import com.sporekart.copilot.domain.SessionId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class CopilotSessionServiceTest {

    private CopilotSessionService service;

    @BeforeEach
    void setUp() {
        service = new CopilotSessionService();
    }

    @Test
    void createSessionShouldReturnActiveSession() {
        var user = UserContext.builder().userId("user1").userName("User1").email("").build();
        var session = service.createSession(CopilotType.CUSTOMER, user);

        assertNotNull(session.id());
        assertEquals(CopilotType.CUSTOMER, session.copilotType());
        assertEquals(CopilotStatus.ACTIVE, session.status());
        assertEquals("user1", session.user().userId());
    }

    @Test
    void getSessionShouldReturnSessionWhenExists() {
        var user = UserContext.builder().userId("user1").userName("User1").email("").build();
        var created = service.createSession(CopilotType.CUSTOMER, user);

        var found = service.getSession(created.id());
        assertTrue(found.isPresent());
        assertEquals(created.id(), found.get().id());
    }

    @Test
    void getSessionShouldReturnEmptyWhenNotExists() {
        var result = service.getSession(new SessionId());
        assertTrue(result.isEmpty());
    }

    @Test
    void closeSessionShouldRemoveSession() {
        var user = UserContext.builder().userId("user1").userName("User1").email("").build();
        var session = service.createSession(CopilotType.CUSTOMER, user);

        service.closeSession(session.id());
        assertTrue(service.getSession(session.id()).isEmpty());
    }

    @Test
    void closeSessionShouldThrowWhenNotFound() {
        assertThrows(NoSuchElementException.class, () -> service.closeSession(new SessionId()));
    }

    @Test
    void getUserSessionsShouldReturnSessionsForUser() {
        var user1 = UserContext.builder().userId("user1").userName("User1").email("").build();
        var user2 = UserContext.builder().userId("user2").userName("User2").email("").build();

        service.createSession(CopilotType.CUSTOMER, user1);
        service.createSession(CopilotType.GROWER, user1);
        service.createSession(CopilotType.ADMIN, user2);

        var user1Sessions = service.getUserSessions("user1");
        assertEquals(2, user1Sessions.size());

        var user2Sessions = service.getUserSessions("user2");
        assertEquals(1, user2Sessions.size());
    }

    @Test
    void getUserSessionsShouldReturnEmptyForUnknownUser() {
        var sessions = service.getUserSessions("unknown");
        assertTrue(sessions.isEmpty());
    }
}
