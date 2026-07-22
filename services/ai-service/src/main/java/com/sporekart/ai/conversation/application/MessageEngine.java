package com.sporekart.ai.conversation.application;

import com.sporekart.ai.conversation.api.ConversationRepository;
import com.sporekart.ai.conversation.api.MessageRepository;
import com.sporekart.ai.conversation.domain.*;
import java.time.Instant;
import java.util.*;

public class MessageEngine {
    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;

    public MessageEngine(MessageRepository messageRepository, ConversationRepository conversationRepository) {
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
    }

    public Message createMessage(ConversationId conversationId, MessageType type, String content, String role, Map<String, Object> metadata) {
        var id = MessageId.random();
        var now = Instant.now();
        var message = new Message(id, conversationId, type, content, role, metadata, List.of(), List.of(), null,
            null, null, null, null, "active", now, now);
        return messageRepository.save(message);
    }

    public Message createSystemMessage(ConversationId conversationId, String content) {
        return createMessage(conversationId, MessageType.SYSTEM, content, "system", Map.of());
    }

    public Message createUserMessage(ConversationId conversationId, String content) {
        return createMessage(conversationId, MessageType.USER, content, "user", Map.of());
    }

    public Message createAssistantMessage(ConversationId conversationId, String content, String provider, Integer tokenUsage, Long latencyMs) {
        var id = MessageId.random();
        var now = Instant.now();
        var metadata = provider != null ? Map.<String, Object>of("provider", provider) : Map.<String, Object>of();
        var message = new Message(id, conversationId, MessageType.ASSISTANT, content, "assistant", metadata,
            List.of(), List.of(), null, null, tokenUsage, provider, latencyMs, "active", now, now);
        return messageRepository.save(message);
    }

    public Message createToolMessage(ConversationId conversationId, String content, ToolCall toolCall) {
        var id = MessageId.random();
        var now = Instant.now();
        var message = new Message(id, conversationId, MessageType.TOOL, content, "tool", Map.of(),
            List.of(), List.of(), toolCall, null, null, null, null, "active", now, now);
        return messageRepository.save(message);
    }

    public List<Message> getConversationMessages(ConversationId conversationId) {
        return messageRepository.findByConversationIdOrdered(conversationId);
    }

    public List<Message> getConversationMessagesAfter(ConversationId conversationId, Instant after) {
        return messageRepository.findByConversationIdAfter(conversationId, after);
    }

    public int getMessageCount(ConversationId conversationId) {
        return messageRepository.countByConversationId(conversationId);
    }

    public void deleteMessages(ConversationId conversationId) {
        messageRepository.deleteByConversationId(conversationId);
    }
}
