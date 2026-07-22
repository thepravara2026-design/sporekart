package com.sporekart.ai.conversation.application;

import com.sporekart.ai.conversation.api.ConversationRepository;
import com.sporekart.ai.conversation.api.ConversationService;
import com.sporekart.ai.conversation.api.MessageRepository;
import com.sporekart.ai.conversation.domain.*;
import java.time.Instant;
import java.util.*;

public class ConversationManager implements ConversationService {
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

    public ConversationManager(ConversationRepository conversationRepository, MessageRepository messageRepository) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public Conversation createConversation(String title, SessionId sessionId, WorkspaceId workspaceId, String userId) {
        var id = ConversationId.random();
        var now = Instant.now();
        var conversation = new Conversation(id, title, ConversationStatus.ACTIVE, sessionId, workspaceId, userId,
            new ArrayList<>(), new HashMap<>(), new HashSet<>(), now, now, null, 0, 0);
        return conversationRepository.save(conversation);
    }

    @Override
    public Conversation getConversation(ConversationId id) {
        return conversationRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Conversation not found: " + id));
    }

    @Override
    public List<Conversation> listConversations(String userId, WorkspaceId workspaceId, ConversationStatus status) {
        var all = conversationRepository.findByUserId(userId);
        if (workspaceId != null) all = all.stream().filter(c -> c.getWorkspaceId().equals(workspaceId)).toList();
        if (status != null) all = all.stream().filter(c -> c.getStatus() == status).toList();
        return all;
    }

    @Override
    public Conversation closeConversation(ConversationId id) {
        var conv = getConversation(id);
        conv.close();
        return conversationRepository.save(conv);
    }

    @Override
    public Conversation archiveConversation(ConversationId id) {
        var conv = getConversation(id);
        conv.archive();
        return conversationRepository.save(conv);
    }

    @Override
    public Conversation restoreConversation(ConversationId id) {
        var conv = getConversation(id);
        conv.restore();
        return conversationRepository.save(conv);
    }

    @Override
    public void deleteConversation(ConversationId id) {
        var conv = getConversation(id);
        conv.delete();
        conversationRepository.save(conv);
    }

    @Override
    public Conversation updateMetadata(ConversationId id, Map<String, String> metadata) {
        var conv = getConversation(id);
        var updated = new HashMap<>(conv.getMetadata());
        updated.putAll(metadata);
        var result = new Conversation(conv.getId(), conv.getTitle(), conv.getStatus(), conv.getSessionId(),
            conv.getWorkspaceId(), conv.getUserId(), conv.getParticipantIds(), updated, conv.getPermissions(),
            conv.getCreatedAt(), Instant.now(), conv.getClosedAt(), conv.getMessageCount(), conv.getTotalTokenUsage());
        return conversationRepository.save(result);
    }

    @Override
    public Conversation addMessage(ConversationId id, Message message) {
        var conv = getConversation(id);
        conv.incrementMessageCount();
        conv.addTokenUsage(message.getTokenUsage() != null ? message.getTokenUsage() : 0);
        messageRepository.save(message);
        return conversationRepository.save(conv);
    }
}
