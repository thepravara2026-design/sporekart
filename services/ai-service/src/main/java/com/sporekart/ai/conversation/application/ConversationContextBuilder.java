package com.sporekart.ai.conversation.application;

import com.sporekart.ai.conversation.api.ContextBuilder;
import com.sporekart.ai.conversation.domain.ContextEntry;
import com.sporekart.ai.conversation.infrastructure.persistence.ConversationContextRepository;
import com.sporekart.ai.conversation.infrastructure.persistence.ConversationMessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ConversationContextBuilder implements ContextBuilder {

    private static final Logger log = LoggerFactory.getLogger(ConversationContextBuilder.class);

    private final ConversationContextRepository contextRepository;
    private final ConversationMessageRepository messageRepository;

    public ConversationContextBuilder(ConversationContextRepository contextRepository,
                                      ConversationMessageRepository messageRepository) {
        this.contextRepository = contextRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public List<ContextEntry> buildContext(UUID sessionId) {
        return buildContext(sessionId, null);
    }

    @Override
    public List<ContextEntry> buildContext(UUID sessionId, String userQuery) {
        var stored = contextRepository.findBySessionIdAndIsDeletedFalse(sessionId).stream()
                .map(e -> new ContextEntry(e.getSessionId(), e.getSource(), e.getContent(), e.getWeight()))
                .collect(Collectors.toList());

        var recentMessages = messageRepository.findBySessionIdAndIsDeletedFalseOrderByCreatedAtAsc(sessionId);
        int msgCount = recentMessages.size();
        if (msgCount > 0) {
            var lastMessages = recentMessages.subList(Math.max(0, msgCount - 5), msgCount);
            for (var msg : lastMessages) {
                stored.add(new ContextEntry(sessionId, "conversation_history", msg.getRole() + ": " + msg.getContent(), 0.8));
            }
        }

        if (userQuery != null && !userQuery.isBlank()) {
            stored.add(new ContextEntry(sessionId, "user_query", userQuery, 1.0));
        }

        log.debug("Built context for session {} with {} entries", sessionId, stored.size());
        return stored;
    }

    @Override
    public String buildContextString(UUID sessionId) {
        return buildContextString(sessionId, null);
    }

    @Override
    public String buildContextString(UUID sessionId, String userQuery) {
        var entries = buildContext(sessionId, userQuery);
        return entries.stream()
                .map(e -> "[" + e.source() + "] " + e.content())
                .collect(Collectors.joining("\n"));
    }

    @Override
    public Map<String, Double> getContextSources(UUID sessionId) {
        var entries = contextRepository.findBySessionIdAndIsDeletedFalse(sessionId);
        Map<String, Double> sources = new HashMap<>();
        for (var e : entries) {
            sources.merge(e.getSource(), e.getWeight(), Double::sum);
        }
        return sources;
    }

    @Override
    @Transactional
    public void refreshContext(UUID sessionId) {
        contextRepository.deleteBySessionIdAndIsDeletedFalse(sessionId);
        log.info("Refreshed context for session {}", sessionId);
    }
}
