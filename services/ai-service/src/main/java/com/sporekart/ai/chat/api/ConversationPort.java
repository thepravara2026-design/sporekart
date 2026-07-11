package com.sporekart.ai.chat.api;

import com.sporekart.ai.chat.domain.Conversation;
import com.sporekart.ai.chat.domain.ConversationMessage;
import java.util.List;
import java.util.Optional;

public interface ConversationPort {
    Conversation save(Conversation conversation);
    Optional<Conversation> findById(String id);
    List<Conversation> findByOwner(String owner);
    void deleteById(String id);
    ConversationMessage addMessage(String conversationId, ConversationMessage message);
    List<ConversationMessage> getMessages(String conversationId);
}
