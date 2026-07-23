package com.sporekart.identity.infrastructure.persistence.repository;

import com.sporekart.identity.domain.model.Session;
import com.sporekart.identity.domain.repository.SessionRepositoryPort;
import com.sporekart.identity.infrastructure.persistence.entity.SessionEntity;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaSessionRepositoryAdapter implements SessionRepositoryPort {

    private final SpringDataSessionRepository repository;

    public JpaSessionRepositoryAdapter(SpringDataSessionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Session save(Session session) {
        return toDomain(repository.save(toEntity(session)));
    }

    @Override
    public Optional<Session> findById(String sessionId) {
        return repository.findById(sessionId).map(this::toDomain);
    }

    @Override
    public List<Session> findByUserId(String userId) {
        return repository.findByUserId(userId).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Session> findActiveByUserId(String userId) {
        return repository.findByUserIdAndRevokedFalseAndExpiresAtAfter(userId, Instant.now())
                .stream().map(this::toDomain).toList();
    }

    @Override
    public void revokeById(String sessionId) {
        repository.findById(sessionId).ifPresent(entity -> {
            entity.setRevoked(true);
            repository.save(entity);
        });
    }

    @Override
    public void revokeAllByUserId(String userId) {
        var sessions = repository.findByUserIdAndRevokedFalseAndExpiresAtAfter(userId, Instant.now());
        sessions.forEach(s -> s.setRevoked(true));
        repository.saveAll(sessions);
    }

    @Override
    public void deleteExpired() {
        repository.deleteByExpiresAtBefore(Instant.now());
    }

    @Override
    public long countActiveByUserId(String userId) {
        return repository.countByUserIdAndRevokedFalseAndExpiresAtAfter(userId, Instant.now());
    }

    private Session toDomain(SessionEntity entity) {
        var session = new Session(
                entity.getSessionId(), entity.getUserId(), entity.getDeviceId(),
                entity.getIpAddress(), entity.getDeviceFingerprint(),
                entity.getWorkspaceContext(), entity.getIssuedAt(), entity.getExpiresAt());
        if (entity.isRevoked()) session.revoke();
        return session;
    }

    private SessionEntity toEntity(Session session) {
        var entity = new SessionEntity();
        entity.setSessionId(session.getSessionId());
        entity.setUserId(session.getUserId());
        entity.setDeviceId(session.getDeviceId());
        entity.setIpAddress(session.getIpAddress());
        entity.setDeviceFingerprint(session.getDeviceFingerprint());
        entity.setWorkspaceContext(session.getWorkspaceContext());
        entity.setIssuedAt(session.getIssuedAt());
        entity.setExpiresAt(session.getExpiresAt());
        entity.setLastActivityAt(session.getLastActivityAt());
        entity.setRevoked(session.isRevoked());
        return entity;
    }
}
