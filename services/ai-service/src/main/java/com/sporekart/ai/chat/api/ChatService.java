package com.sporekart.ai.chat.api;

import com.sporekart.ai.chat.domain.ChatSession;
import com.sporekart.ai.chat.domain.Conversation;
import com.sporekart.ai.chat.domain.ConversationMessage;
import com.sporekart.ai.core.domain.AiResponse;
import java.util.List;
import java.util.Optional;

public interface ChatService {
    Conversation startConversation(String owner, String context);
    AiResponse sendMessage(String sessionId, String message);
    List<ConversationMessage> getHistory(String sessionId);
    Optional<Conversation> findById(String sessionId);
    List<Conversation> listByOwner(String owner);
    void deleteConversation(String sessionId);
}
