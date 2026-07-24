package com.sporekart.identity.application.service;

import com.sporekart.identity.domain.model.Session;
import com.sporekart.identity.domain.repository.SessionRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class SessionServiceTest {

    private SessionRepositoryPort sessionRepository;
    private SessionService sessionService;

    @BeforeEach
    void setUp() {
        sessionRepository = mock(SessionRepositoryPort.class);
        sessionService = new SessionService(sessionRepository);
    }

    @Test
    void shouldCreateSession() {
        when(sessionRepository.countActiveByUserId(anyString())).thenReturn(0L);
        when(sessionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var session = sessionService.createSession("user-1", "device-1", "127.0.0.1", "fp-1", "workspace-1");

        assertNotNull(session);
        assertEquals("user-1", session.getUserId());
        assertEquals("device-1", session.getDeviceId());
        assertEquals("127.0.0.1", session.getIpAddress());
        assertNotNull(session.getSessionId());
        assertFalse(session.isExpired());
        verify(sessionRepository).save(any(Session.class));
    }

    @Test
    void shouldRevokeSession() {
        sessionService.revokeSession("session-1");

        verify(sessionRepository).revokeById("session-1");
    }

    @Test
    void shouldRevokeAllSessions() {
        sessionService.revokeAllSessions("user-1");

        verify(sessionRepository).revokeAllByUserId("user-1");
    }

    @Test
    void shouldGetActiveSessions() {
        var session = new Session("s-1", "user-1", "d-1", "ip", "fp", "ws",
                Instant.now(), Instant.now().plusSeconds(3600));
        when(sessionRepository.findActiveByUserId("user-1")).thenReturn(List.of(session));

        var sessions = sessionService.getActiveSessions("user-1");

        assertEquals(1, sessions.size());
        assertEquals("s-1", sessions.getFirst().getSessionId());
    }

    @Test
    void shouldEnforceMaxConcurrentSessions() {
        when(sessionRepository.countActiveByUserId("user-1")).thenReturn(10L);
        var oldestSession = new Session("old-session", "user-1", "d-1", "ip", "fp", "ws",
                Instant.now().minusSeconds(7200), Instant.now().plusSeconds(3600));
        when(sessionRepository.findActiveByUserId("user-1")).thenReturn(List.of(oldestSession));
        when(sessionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var session = sessionService.createSession("user-1", "device-2", "10.0.0.1", "fp-2", "ws-2");

        assertNotNull(session);
        verify(sessionRepository).revokeById("old-session");
        verify(sessionRepository).save(any(Session.class));
    }

    @Test
    void shouldTouchSession() {
        var session = new Session("s-1", "user-1", "d-1", "ip", "fp", "ws",
                Instant.now(), Instant.now().plusSeconds(3600));
        when(sessionRepository.findById("s-1")).thenReturn(Optional.of(session));
        when(sessionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        sessionService.touchSession("s-1");

        verify(sessionRepository).save(session);
    }
}
