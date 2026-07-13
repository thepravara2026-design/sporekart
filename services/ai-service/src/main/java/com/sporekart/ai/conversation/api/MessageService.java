package com.sporekart.ai.conversation.api;

import com.sporekart.ai.conversation.domain.ConversationMessage;
import com.sporekart.ai.conversation.domain.MessageRole;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
    ConversationMessage sendMessage(UUID sessionId, MessageRole role, String content);
    ConversationMessage sendMessage(UUID sessionId, MessageRole role, String content, String metadataJson);
    Optional<ConversationMessage> getMessage(UUID messageId);
    List<ConversationMessage> getSessionMessages(UUID sessionId);
    List<ConversationMessage> getSessionMessages(UUID sessionId, int limit, int offset);
    ConversationMessage updateStatus(UUID messageId, String status);
    void deleteMessage(UUID messageId);
    void deleteSessionMessages(UUID sessionId);
    int getMessageCount(UUID sessionId);
}
