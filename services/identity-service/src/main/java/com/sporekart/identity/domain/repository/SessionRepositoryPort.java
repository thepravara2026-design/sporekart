package com.sporekart.identity.domain.repository;

import com.sporekart.identity.domain.model.Session;
import java.util.List;
import java.util.Optional;

public interface SessionRepositoryPort {
    Session save(Session session);
    Optional<Session> findById(String sessionId);
    List<Session> findByUserId(String userId);
    List<Session> findActiveByUserId(String userId);
    void revokeById(String sessionId);
    void revokeAllByUserId(String userId);
    void deleteExpired();
    long countActiveByUserId(String userId);
}
