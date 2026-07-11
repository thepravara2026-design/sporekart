package com.sporekart.ai.chat.api;

import com.sporekart.ai.chat.domain.ConversationMessage;
import java.util.List;

public interface ChatMemoryPort {
    void store(String sessionId, ConversationMessage message);
    List<ConversationMessage> retrieve(String sessionId, int limit);
    void clear(String sessionId);
}
