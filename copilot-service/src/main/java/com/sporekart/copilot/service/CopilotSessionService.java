package com.sporekart.copilot.service;

import com.sporekart.copilot.context.UserContext;
import com.sporekart.copilot.domain.CopilotStatus;
import com.sporekart.copilot.domain.CopilotType;
import com.sporekart.copilot.domain.SessionId;
import com.sporekart.copilot.domain.CopilotSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class CopilotSessionService {

    private static final Logger log = LoggerFactory.getLogger(CopilotSessionService.class);

    private final Map<SessionId, CopilotSession> sessions = new ConcurrentHashMap<>();

    public CopilotSession createSession(CopilotType type, UserContext user) {
        var sessionId = new SessionId();
        var now = OffsetDateTime.now();
        var session = new CopilotSession(sessionId, type, user, CopilotStatus.ACTIVE, now, now);
        sessions.put(sessionId, session);
        log.info("Created session {} for user {} with type {}", sessionId, user.userId(), type);
        return session;
    }

    public Optional<CopilotSession> getSession(SessionId id) {
        return Optional.ofNullable(sessions.get(id));
    }

    public void closeSession(SessionId id) {
        var session = sessions.get(id);
        if (session == null) {
            throw new NoSuchElementException("Session not found: " + id);
        }
        sessions.remove(id);
        log.info("Closed session {}", id);
    }

    public List<CopilotSession> getUserSessions(String userId) {
        return sessions.values().stream()
            .filter(s -> s.user() != null && userId.equals(s.user().userId()))
            .collect(Collectors.toList());
    }

    public List<CopilotSession> listActiveSessions() {
        return sessions.values().stream()
            .filter(s -> CopilotStatus.ACTIVE.equals(s.status()))
            .collect(Collectors.toList());
    }
}
