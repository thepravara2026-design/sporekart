package com.sporekart.customer.copilot.service;

import com.sporekart.copilot.*;
import com.sporekart.customer.copilot.config.CustomerCopilotConfig;
import com.sporekart.customer.copilot.domain.ConversationMessage;
import com.sporekart.customer.copilot.dto.ChatResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CustomerCopilotOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(CustomerCopilotOrchestrator.class);

    private final CopilotEngine copilotEngine;
    private final CustomerContextService contextService;
    private final CustomerCopilotConfig config;

    private final Map<String, SessionId> activeSessions = new ConcurrentHashMap<>();

    public CustomerCopilotOrchestrator(CopilotEngine copilotEngine,
                                       CustomerContextService contextService,
                                       CustomerCopilotConfig config) {
        this.copilotEngine = copilotEngine;
        this.contextService = contextService;
        this.config = config;
    }

    public SessionId createSession(UserContext userContext) {
        var sessionId = new SessionId();
        copilotEngine.startSession(sessionId, CopilotType.CUSTOMER, userContext);
        activeSessions.put(sessionId.toString(), sessionId);

        var profile = contextService.getOrCreateProfile(userContext);
        log.info("Created session {} for user {} ({})", sessionId, userContext.userId(), profile.userName());

        return sessionId;
    }

    public void endSession(String sessionIdStr) {
        var sessionId = activeSessions.remove(sessionIdStr);
        if (sessionId != null) {
            copilotEngine.endSession(sessionId);
            log.info("Ended session: {}", sessionIdStr);
        }
    }

    public Optional<SessionId> getSession(String sessionIdStr) {
        return Optional.ofNullable(activeSessions.get(sessionIdStr));
    }

    public ChatResponse processMessage(String sessionIdStr, String message, UserContext userContext, PageContext pageContext) {
        var sessionId = getSession(sessionIdStr).orElseGet(() -> createSession(userContext));

        var userMessage = new ConversationMessage(
            UUID.randomUUID().toString(),
            "user",
            message,
            Map.of("sessionId", sessionIdStr, "pageUrl", pageContext != null ? pageContext.pageUrl() : ""),
            OffsetDateTime.now().toString()
        );
        contextService.addConversationMessage(sessionIdStr, userMessage);

        log.info("Processing message for session {}: {}...", sessionIdStr, message.length() > 50 ? message.substring(0, 50) + "..." : message);

        try {
            CompletableFuture<CopilotResponse> future = copilotEngine.processMessage(sessionId, message, userContext, pageContext);
            CopilotResponse copilotResponse = future.join();

            var assistantMessage = new ConversationMessage(
                UUID.randomUUID().toString(),
                "assistant",
                copilotResponse.message(),
                copilotResponse.context(),
                copilotResponse.timestamp() != null ? copilotResponse.timestamp().toString() : OffsetDateTime.now().toString()
            );
            contextService.addConversationMessage(sessionIdStr, assistantMessage);

            return new ChatResponse(
                sessionIdStr,
                copilotResponse.message() != null ? copilotResponse.message() : "",
                copilotResponse.suggestions() != null ? copilotResponse.suggestions() : List.of(),
                copilotResponse.context() != null ? copilotResponse.context() : Map.of(),
                copilotResponse.streaming()
            );
        } catch (Exception e) {
            log.error("Error processing message for session {}: {}", sessionIdStr, e.getMessage(), e);
            return new ChatResponse(
                sessionIdStr,
                "I apologize, but I encountered an error processing your request. Please try again.",
                List.of(Suggestion.of("Try again", "retry")),
                Map.of("error", e.getMessage()),
                false
            );
        }
    }

    public CopilotStatus getSessionStatus(String sessionIdStr) {
        var sessionId = activeSessions.get(sessionIdStr);
        if (sessionId == null) {
            return CopilotStatus.STOPPED;
        }
        try {
            return copilotEngine.getStatus(sessionId);
        } catch (Exception e) {
            log.warn("Could not get status for session {}: {}", sessionIdStr, e.getMessage());
            return CopilotStatus.ERROR;
        }
    }

    public List<ConversationMessage> getConversationHistory(String sessionIdStr) {
        return contextService.getConversationHistory(sessionIdStr);
    }

    public CustomerContextService getContextService() {
        return contextService;
    }
}
