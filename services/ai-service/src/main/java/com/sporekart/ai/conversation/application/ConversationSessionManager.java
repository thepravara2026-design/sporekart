package com.sporekart.ai.conversation.application;

import com.sporekart.ai.conversation.api.SessionManager;
import com.sporekart.ai.conversation.domain.ConversationSession;
import com.sporekart.ai.conversation.domain.ConversationStatus;
import com.sporekart.ai.conversation.infrastructure.persistence.ConversationSessionEntity;
import com.sporekart.ai.conversation.infrastructure.persistence.ConversationSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConversationSessionManager implements SessionManager {

    private static final Logger log = LoggerFactory.getLogger(ConversationSessionManager.class);

    private final ConversationSessionRepository repository;

    public ConversationSessionManager(ConversationSessionRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public ConversationSession createSession(String userId, String title) {
        var entity = new ConversationSessionEntity();
        entity.setUserId(userId);
        entity.setTitle(title);
        entity.setStatus(ConversationStatus.ACTIVE);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        entity.setDeleted(false);
        var saved = repository.save(entity);
        log.info("Created session {} for user {}", saved.getId(), userId);
        return toDomain(saved);
    }

    @Override
    public Optional<ConversationSession> getSession(UUID sessionId) {
        return repository.findByIdAndIsDeletedFalse(sessionId).map(this::toDomain);
    }

    @Override
    public List<ConversationSession> getUserSessions(String userId) {
        return repository.findByUserIdAndIsDeletedFalse(userId).stream().map(this::toDomain).toList();
    }

    @Override
    @Transactional
    public ConversationSession updateStatus(UUID sessionId, ConversationStatus status) {
        var entity = repository.findByIdAndIsDeletedFalse(sessionId)
                .orElseThrow(() -> new ConversationException("Session not found: " + sessionId));
        entity.setStatus(status);
        entity.setUpdatedAt(OffsetDateTime.now());
        var saved = repository.save(entity);
        log.info("Updated session {} status to {}", sessionId, status);
        return toDomain(saved);
    }

    @Override
    @Transactional
    public void deleteSession(UUID sessionId) {
        var entity = repository.findByIdAndIsDeletedFalse(sessionId)
                .orElseThrow(() -> new ConversationException("Session not found: " + sessionId));
        entity.setDeleted(true);
        entity.setUpdatedAt(OffsetDateTime.now());
        repository.save(entity);
        log.info("Deleted session {}", sessionId);
    }

    @Override
    @Transactional
    public ConversationSession suspendSession(UUID sessionId) {
        return updateStatus(sessionId, ConversationStatus.ARCHIVED);
    }

    @Override
    @Transactional
    public ConversationSession resumeSession(UUID sessionId) {
        return updateStatus(sessionId, ConversationStatus.ACTIVE);
    }

    @Override
    @Transactional
    public ConversationSession closeSession(UUID sessionId) {
        return updateStatus(sessionId, ConversationStatus.CLOSED);
    }

    @Override
    public boolean isSessionActive(UUID sessionId) {
        return repository.findByIdAndIsDeletedFalse(sessionId)
                .map(e -> e.getStatus() == ConversationStatus.ACTIVE)
                .orElse(false);
    }

    private ConversationSession toDomain(ConversationSessionEntity entity) {
        return new ConversationSession(
                entity.getId(),
                entity.getUserId(),
                entity.getTitle(),
                entity.getStatus(),
                null,
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getExpiresAt()
        );
    }
}
