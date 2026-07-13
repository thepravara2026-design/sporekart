package com.sporekart.ai.conversation.application;

import com.sporekart.ai.conversation.api.ConversationStreamService;
import com.sporekart.ai.conversation.api.MessageService;
import com.sporekart.ai.conversation.api.SessionManager;
import com.sporekart.ai.conversation.domain.ConversationMessage;
import com.sporekart.ai.conversation.domain.MessageRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.function.Consumer;

@Service
public class ConversationStreamServiceImpl implements ConversationStreamService {

    private static final Logger log = LoggerFactory.getLogger(ConversationStreamServiceImpl.class);

    private final SessionManager sessionManager;
    private final MessageService messageService;

    public ConversationStreamServiceImpl(SessionManager sessionManager, MessageService messageService) {
        this.sessionManager = sessionManager;
        this.messageService = messageService;
    }

    @Override
    public void streamResponse(UUID sessionId, String userMessage, Consumer<String> onChunk,
                               Runnable onComplete, Consumer<Throwable> onError) {
        if (!sessionManager.isSessionActive(sessionId)) {
            onError.accept(new ConversationException("Session is not active: " + sessionId));
            return;
        }
        try {
            messageService.sendMessage(sessionId, MessageRole.USER, userMessage);
            log.debug("Streaming response for session {}", sessionId);
            onComplete.run();
        } catch (Exception e) {
            log.error("Error streaming response for session {}", sessionId, e);
            onError.accept(e);
        }
    }

    @Override
    public void sendMessageStream(UUID sessionId, String userMessage,
                                  Consumer<ConversationMessage> onMessage, Consumer<Throwable> onError) {
        if (!sessionManager.isSessionActive(sessionId)) {
            onError.accept(new ConversationException("Session is not active: " + sessionId));
            return;
        }
        try {
            var userMsg = messageService.sendMessage(sessionId, MessageRole.USER, userMessage);
            onMessage.accept(userMsg);
            log.debug("Sent message stream for session {}", sessionId);
        } catch (Exception e) {
            log.error("Error sending message stream for session {}", sessionId, e);
            onError.accept(e);
        }
    }
}
