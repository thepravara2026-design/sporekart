package com.sporekart.copilot.service;

import com.sporekart.copilot.context.ContextAssembler;
import com.sporekart.copilot.context.PageContext;
import com.sporekart.copilot.context.UserContext;
import com.sporekart.copilot.core.CopilotEngine;
import com.sporekart.copilot.domain.CopilotResponse;
import com.sporekart.copilot.domain.CopilotType;
import com.sporekart.copilot.domain.SessionId;
import com.sporekart.copilot.dto.ChatRequest;
import com.sporekart.copilot.dto.ChatResponse;
import com.sporekart.copilot.persona.PersonaEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class CopilotOrchestrationService {

    private static final Logger log = LoggerFactory.getLogger(CopilotOrchestrationService.class);

    private final CopilotEngine copilotEngine;
    private final CopilotRegistryService registryService;
    private final CopilotSessionService sessionService;
    private final PersonaEngine personaEngine;
    private final ContextAssembler contextAssembler;

    public CopilotOrchestrationService(
            CopilotEngine copilotEngine,
            CopilotRegistryService registryService,
            CopilotSessionService sessionService,
            PersonaEngine personaEngine,
            ContextAssembler contextAssembler) {
        this.copilotEngine = copilotEngine;
        this.registryService = registryService;
        this.sessionService = sessionService;
        this.personaEngine = personaEngine;
        this.contextAssembler = contextAssembler;
    }

    public ChatResponse processMessage(ChatRequest request, UserContext user, PageContext page) {
        SessionId sessionId = resolveSessionId(request.sessionId(), user);
        log.debug("Processing message for session {}", sessionId);

        var copilotType = CopilotType.CUSTOMER;
        var context = contextAssembler.assembleContext(user, page, copilotType, sessionId);

        CompletableFuture<CopilotResponse> future = copilotEngine.processMessage(
            sessionId, request.message(), user, page);

        CopilotResponse response = future.join();

        return new ChatResponse(
            sessionId.toString(),
            response.message(),
            response.suggestions(),
            response.context(),
            response.streaming()
        );
    }

    private SessionId resolveSessionId(String sessionIdStr, UserContext user) {
        if (sessionIdStr != null && !sessionIdStr.isBlank()) {
            var sid = SessionId.fromString(sessionIdStr);
            return sessionService.getSession(sid)
                .map(s -> sid)
                .orElseGet(() -> sessionService.createSession(CopilotType.CUSTOMER, user).id());
        }
        return sessionService.createSession(CopilotType.CUSTOMER, user).id();
    }
}
