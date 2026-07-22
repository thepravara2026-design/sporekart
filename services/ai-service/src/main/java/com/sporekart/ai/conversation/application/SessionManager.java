package com.sporekart.ai.conversation.application;

import com.sporekart.ai.conversation.api.ConversationRepository;
import com.sporekart.ai.conversation.api.SessionRepository;
import com.sporekart.ai.conversation.api.SessionService;
import com.sporekart.ai.conversation.domain.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class SessionManager implements SessionService {
    private final SessionRepository sessionRepository;
    private final ConversationRepository conversationRepository;

    public SessionManager(SessionRepository sessionRepository, ConversationRepository conversationRepository) {
        this.sessionRepository = sessionRepository;
        this.conversationRepository = conversationRepository;
    }

    @Override
    public Session createSession(String userId, WorkspaceId workspaceId, Duration idleTimeout) {
        var id = SessionId.random();
        var now = Instant.now();
        var timeout = idleTimeout != null ? idleTimeout : Duration.ofHours(8);
        var expiresAt = now.plus(Duration.ofHours(24));
        var session = new Session(id, userId, workspaceId, "active", new HashMap<>(), new ArrayList<>(),
            now, now, expiresAt, timeout);
        return sessionRepository.save(session);
    }

    @Override
    public Optional<Session> getSession(SessionId id) {
        var session = sessionRepository.findById(id);
        session.ifPresent(s -> {
            if (s.isExpired()) {
                throw new IllegalStateException("Session expired: " + id);
            }
        });
        return session;
    }

    @Override
    public List<Session> listSessions(String userId) {
        return sessionRepository.findByUserId(userId);
    }

    @Override
    public Session recordActivity(SessionId id) {
        var session = sessionRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Session not found: " + id));
        session.recordActivity();
        return sessionRepository.save(session);
    }

    @Override
    public void closeSession(SessionId id) {
        var session = sessionRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Session not found: " + id));
        session.close();
        sessionRepository.save(session);
    }

    @Override
    public List<Session> getExpiredSessions() {
        return sessionRepository.findAll().stream()
            .filter(Session::isExpired)
            .toList();
    }

    @Override
    public List<Session> getIdleSessions(Duration idleThreshold) {
        var now = Instant.now();
        return sessionRepository.findAll().stream()
            .filter(s -> {
                var last = s.getLastActivityAt();
                return last != null && Duration.between(last, now).compareTo(idleThreshold) > 0;
            })
            .toList();
    }
}
