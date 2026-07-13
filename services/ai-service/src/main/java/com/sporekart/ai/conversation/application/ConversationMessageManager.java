package com.sporekart.ai.conversation.application;

import com.sporekart.ai.conversation.api.MessageService;
import com.sporekart.ai.conversation.domain.ConversationMessage;
import com.sporekart.ai.conversation.domain.MessageRole;
import com.sporekart.ai.conversation.domain.MessageStatus;
import com.sporekart.ai.conversation.infrastructure.persistence.ConversationMessageEntity;
import com.sporekart.ai.conversation.infrastructure.persistence.ConversationMessageRepository;
import com.sporekart.ai.conversation.infrastructure.persistence.ConversationSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConversationMessageManager implements MessageService {

    private static final Logger log = LoggerFactory.getLogger(ConversationMessageManager.class);

    private final ConversationMessageRepository messageRepository;
    private final ConversationSessionRepository sessionRepository;

    public ConversationMessageManager(ConversationMessageRepository messageRepository,
                                      ConversationSessionRepository sessionRepository) {
        this.messageRepository = messageRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    @Transactional
    public ConversationMessage sendMessage(UUID sessionId, MessageRole role, String content) {
        return sendMessage(sessionId, role, content, null);
    }

    @Override
    @Transactional
    public ConversationMessage sendMessage(UUID sessionId, MessageRole role, String content, String metadataJson) {
        var session = sessionRepository.findByIdAndIsDeletedFalse(sessionId)
                .orElseThrow(() -> new ConversationException("Session not found: " + sessionId));

        var entity = new ConversationMessageEntity();
        entity.setSessionId(sessionId);
        entity.setRole(role);
        entity.setContent(content);
        entity.setMetadata(metadataJson);
        entity.setStatus(MessageStatus.SENT);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setDeleted(false);
        var saved = messageRepository.save(entity);
        log.info("Sent {} message in session {}", role, sessionId);
        return toDomain(saved);
    }

    @Override
    public Optional<ConversationMessage> getMessage(UUID messageId) {
        return messageRepository.findByIdAndIsDeletedFalse(messageId).map(this::toDomain);
    }

    @Override
    public List<ConversationMessage> getSessionMessages(UUID sessionId) {
        return messageRepository.findBySessionIdAndIsDeletedFalseOrderByCreatedAtAsc(sessionId)
                .stream().map(this::toDomain).toList();
    }

    @Override
    public List<ConversationMessage> getSessionMessages(UUID sessionId, int limit, int offset) {
        return messageRepository
                .findBySessionIdAndIsDeletedFalseOrderByCreatedAtAsc(sessionId, PageRequest.of(offset / limit, limit))
                .stream().map(this::toDomain).toList();
    }

    @Override
    @Transactional
    public ConversationMessage updateStatus(UUID messageId, String status) {
        var entity = messageRepository.findByIdAndIsDeletedFalse(messageId)
                .orElseThrow(() -> new ConversationException("Message not found: " + messageId));
        entity.setStatus(MessageStatus.valueOf(status.toUpperCase()));
        var saved = messageRepository.save(entity);
        log.info("Updated message {} status to {}", messageId, status);
        return toDomain(saved);
    }

    @Override
    @Transactional
    public void deleteMessage(UUID messageId) {
        var entity = messageRepository.findByIdAndIsDeletedFalse(messageId)
                .orElseThrow(() -> new ConversationException("Message not found: " + messageId));
        entity.setDeleted(true);
        messageRepository.save(entity);
        log.info("Deleted message {}", messageId);
    }

    @Override
    @Transactional
    public void deleteSessionMessages(UUID sessionId) {
        var messages = messageRepository.findBySessionIdAndIsDeletedFalseOrderByCreatedAtAsc(sessionId);
        messages.forEach(m -> m.setDeleted(true));
        messageRepository.saveAll(messages);
        log.info("Deleted all messages for session {}", sessionId);
    }

    @Override
    public int getMessageCount(UUID sessionId) {
        return (int) messageRepository.countBySessionIdAndIsDeletedFalse(sessionId);
    }

    private ConversationMessage toDomain(ConversationMessageEntity entity) {
        return new ConversationMessage(
                entity.getId(),
                entity.getSessionId(),
                entity.getRole(),
                entity.getContent(),
                null,
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
