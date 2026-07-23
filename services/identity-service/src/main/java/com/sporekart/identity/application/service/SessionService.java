package com.sporekart.identity.application.service;

import com.sporekart.identity.domain.model.Session;
import com.sporekart.identity.domain.repository.SessionRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SessionService {

    private final SessionRepositoryPort sessionRepository;
    private static final long SESSION_DURATION_MINUTES = 60;
    private static final long MAX_CONCURRENT_SESSIONS = 10;

    public SessionService(SessionRepositoryPort sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Transactional
    public Session createSession(String userId, String deviceId, String ipAddress,
                                  String deviceFingerprint, String workspaceContext) {
        var activeCount = sessionRepository.countActiveByUserId(userId);
        if (activeCount >= MAX_CONCURRENT_SESSIONS) {
            var oldest = sessionRepository.findActiveByUserId(userId).stream()
                    .min((a, b) -> a.getLastActivityAt().compareTo(b.getLastActivityAt()));
            oldest.ifPresent(s -> sessionRepository.revokeById(s.getSessionId()));
        }
        var sessionId = UUID.randomUUID().toString();
        var now = Instant.now();
        var session = new Session(
                sessionId, userId, deviceId, ipAddress, deviceFingerprint,
                workspaceContext, now, now.plusSeconds(SESSION_DURATION_MINUTES * 60));
        return sessionRepository.save(session);
    }

    public Optional<Session> getSession(String sessionId) {
        return sessionRepository.findById(sessionId);
    }

    public List<Session> getUserSessions(String userId) {
        return sessionRepository.findByUserId(userId);
    }

    public List<Session> getActiveSessions(String userId) {
        return sessionRepository.findActiveByUserId(userId);
    }

    @Transactional
    public void revokeSession(String sessionId) {
        sessionRepository.revokeById(sessionId);
    }

    @Transactional
    public void revokeAllSessions(String userId) {
        sessionRepository.revokeAllByUserId(userId);
    }

    @Transactional
    public void touchSession(String sessionId) {
        sessionRepository.findById(sessionId).ifPresent(session -> {
            session.touch();
            sessionRepository.save(session);
        });
    }

    @Transactional
    public void cleanupExpired() {
        sessionRepository.deleteExpired();
    }
}
