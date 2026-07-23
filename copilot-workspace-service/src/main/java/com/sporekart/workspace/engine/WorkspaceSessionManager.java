package com.sporekart.workspace.engine;

import com.sporekart.workspace.domain.WorkspaceSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class WorkspaceSessionManager {

    private static final Logger log = LoggerFactory.getLogger(WorkspaceSessionManager.class);
    private static final int MAX_SESSIONS_PER_USER = 10;

    private final ConcurrentHashMap<String, WorkspaceSession> sessions = new ConcurrentHashMap<>();

    public WorkspaceSession createSession(String userId, String workspaceId) {
        if (!validateSessionLimit(userId)) {
            throw new IllegalStateException("Session limit exceeded for user: " + userId);
        }
        String sessionId = UUID.randomUUID().toString();
        WorkspaceSession session = new WorkspaceSession(
            sessionId,
            workspaceId,
            userId,
            null,
            WorkspaceSession.STATUS_ACTIVE,
            List.of(),
            Collections.emptyMap(),
            OffsetDateTime.now(),
            OffsetDateTime.now()
        );
        sessions.put(sessionId, session);
        log.info("Created session {} for user {} in workspace {}", sessionId, userId, workspaceId);
        return session;
    }

    public Optional<WorkspaceSession> getSession(String sessionId) {
        return Optional.ofNullable(sessions.get(sessionId));
    }

    public WorkspaceSession updateSession(String sessionId, WorkspaceSession updates) {
        WorkspaceSession existing = sessions.get(sessionId);
        if (existing == null) {
            throw new IllegalArgumentException("Session not found: " + sessionId);
        }
        WorkspaceSession merged = new WorkspaceSession(
            sessionId,
            updates.workspaceId() != null ? updates.workspaceId() : existing.workspaceId(),
            updates.userId() != null ? updates.userId() : existing.userId(),
            updates.activeCopilotId() != null ? updates.activeCopilotId() : existing.activeCopilotId(),
            updates.status() != null ? updates.status() : existing.status(),
            updates.conversationHistory() != null ? updates.conversationHistory() : existing.conversationHistory(),
            updates.sharedContext() != null ? updates.sharedContext() : existing.sharedContext(),
            existing.startedAt(),
            OffsetDateTime.now()
        );
        sessions.put(sessionId, merged);
        return merged;
    }

    public void closeSession(String sessionId) {
        WorkspaceSession session = sessions.get(sessionId);
        if (session != null) {
            WorkspaceSession closed = new WorkspaceSession(
                session.sessionId(),
                session.workspaceId(),
                session.userId(),
                session.activeCopilotId(),
                WorkspaceSession.STATUS_CLOSED,
                session.conversationHistory(),
                session.sharedContext(),
                session.startedAt(),
                OffsetDateTime.now()
            );
            sessions.put(sessionId, closed);
            log.info("Closed session {}", sessionId);
        }
    }

    public List<WorkspaceSession> getActiveSessions(String workspaceId) {
        return sessions.values().stream()
            .filter(s -> s.workspaceId().equals(workspaceId) && !WorkspaceSession.STATUS_CLOSED.equals(s.status()))
            .collect(Collectors.toList());
    }

    public List<WorkspaceSession> getAllActiveSessions() {
        return sessions.values().stream()
            .filter(s -> !WorkspaceSession.STATUS_CLOSED.equals(s.status()))
            .collect(Collectors.toList());
    }

    public List<String> getIdleSessions(int idleMinutesThreshold) {
        OffsetDateTime threshold = OffsetDateTime.now().minusMinutes(idleMinutesThreshold);
        return sessions.values().stream()
            .filter(s -> !WorkspaceSession.STATUS_CLOSED.equals(s.status()))
            .filter(s -> s.lastActivityAt().isBefore(threshold))
            .map(WorkspaceSession::sessionId)
            .collect(Collectors.toList());
    }

    public int cleanupIdleSessions(int idleMinutesThreshold) {
        List<String> idle = getIdleSessions(idleMinutesThreshold);
        for (String sessionId : idle) {
            closeSession(sessionId);
        }
        log.info("Cleaned up {} idle sessions", idle.size());
        return idle.size();
    }

    public boolean validateSessionLimit(String userId) {
        long activeCount = sessions.values().stream()
            .filter(s -> userId.equals(s.userId()))
            .filter(s -> !WorkspaceSession.STATUS_CLOSED.equals(s.status()))
            .count();
        return activeCount < MAX_SESSIONS_PER_USER;
    }

    public void touchSession(String sessionId) {
        WorkspaceSession session = sessions.get(sessionId);
        if (session != null) {
            WorkspaceSession touched = new WorkspaceSession(
                session.sessionId(),
                session.workspaceId(),
                session.userId(),
                session.activeCopilotId(),
                session.status(),
                session.conversationHistory(),
                session.sharedContext(),
                session.startedAt(),
                OffsetDateTime.now()
            );
            sessions.put(sessionId, touched);
        }
    }
}