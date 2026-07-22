package com.sporekart.ai.conversation.api;
import com.sporekart.ai.conversation.domain.*;
import java.util.*;

public interface ContextWindowService {
    ContextWindow buildWindow(ConversationId conversationId, List<Message> messages);
    ContextWindow compress(ContextWindow window);
    ContextWindow trim(ContextWindow window, int maxTokens);
    ContextWindow prioritize(ContextWindow window, List<String> priorityKeys);
    int estimateTokens(List<Message> messages);
    boolean needsCompression(ContextWindow window);
}
