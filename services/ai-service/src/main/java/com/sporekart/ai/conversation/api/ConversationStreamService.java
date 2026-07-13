package com.sporekart.ai.conversation.api;

import com.sporekart.ai.conversation.domain.ConversationMessage;

import java.util.UUID;
import java.util.function.Consumer;

public interface ConversationStreamService {
    void streamResponse(UUID sessionId, String userMessage, Consumer<String> onChunk, Runnable onComplete, Consumer<Throwable> onError);
    void sendMessageStream(UUID sessionId, String userMessage, Consumer<ConversationMessage> onMessage, Consumer<Throwable> onError);
}
