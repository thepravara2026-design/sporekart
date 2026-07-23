package com.sporekart.copilot.core;

import com.sporekart.copilot.capability.Capability;
import com.sporekart.copilot.capability.CapabilityRegistry;
import com.sporekart.copilot.context.ContextAssembler;
import com.sporekart.copilot.context.CopilotContext;
import com.sporekart.copilot.context.PageContext;
import com.sporekart.copilot.context.UserContext;
import com.sporekart.copilot.domain.*;
import com.sporekart.copilot.event.CopilotEvent;
import com.sporekart.copilot.event.EventBus;
import com.sporekart.copilot.memory.MemoryEntry;
import com.sporekart.copilot.memory.MemoryStore;
import com.sporekart.copilot.memory.MemoryType;
import com.sporekart.copilot.permission.PermissionChecker;
import com.sporekart.copilot.persona.Persona;
import com.sporekart.copilot.persona.PersonaEngine;
import com.sporekart.copilot.streaming.StreamingEngine;
import com.sporekart.copilot.tool.ToolExecutionContext;
import com.sporekart.copilot.tool.ToolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CopilotEngineImpl implements CopilotEngine {

    private static final Logger log = LoggerFactory.getLogger(CopilotEngineImpl.class);

    private final ContextAssembler contextAssembler;
    private final PersonaEngine personaEngine;
    private final PermissionChecker permissionChecker;
    private final CapabilityRegistry capabilityRegistry;
    private final ToolExecutor toolExecutor;
    private final MemoryStore memoryStore;
    private final EventBus eventBus;
    private final StreamingEngine streamingEngine;

    private final ConcurrentMap<SessionId, CopilotStatus> sessionStatuses = new ConcurrentHashMap<>();
    private final ConcurrentMap<SessionId, CopilotType> sessionTypes = new ConcurrentHashMap<>();

    public CopilotEngineImpl(
        ContextAssembler contextAssembler,
        PersonaEngine personaEngine,
        PermissionChecker permissionChecker,
        CapabilityRegistry capabilityRegistry,
        ToolExecutor toolExecutor,
        MemoryStore memoryStore,
        EventBus eventBus,
        StreamingEngine streamingEngine
    ) {
        this.contextAssembler = contextAssembler;
        this.personaEngine = personaEngine;
        this.permissionChecker = permissionChecker;
        this.capabilityRegistry = capabilityRegistry;
        this.toolExecutor = toolExecutor;
        this.memoryStore = memoryStore;
        this.eventBus = eventBus;
        this.streamingEngine = streamingEngine;
    }

    @Override
    public CompletableFuture<CopilotResponse> processMessage(
        SessionId sessionId, String message, UserContext user, PageContext page) {

        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();
            log.info("Processing message for sessionId={}, userId={}", sessionId, user.userId());

            CopilotType copilotType = sessionTypes.getOrDefault(sessionId, CopilotType.CUSTOMER);
            CopilotContext context = contextAssembler.assembleContext(user, page, copilotType, sessionId);

            Persona persona = personaEngine.resolvePersona(copilotType, user);

            storeConversationMessage(sessionId, MessageRole.USER, message, user);

            List<Capability> availableCapabilities = resolveCapabilities(copilotType, user);

            Map<String, Object> contextData = buildResponseContext(persona, availableCapabilities);

            List<Suggestion> suggestions = generateSuggestions(persona, availableCapabilities, message);

            String responseMessage = generateResponse(persona, availableCapabilities, message, context);

            storeConversationMessage(sessionId, MessageRole.ASSISTANT, responseMessage, user);

            CopilotResponse response = new CopilotResponse(
                responseMessage,
                copilotType,
                contextData,
                false,
                suggestions,
                OffsetDateTime.now()
            );

            publishMessageProcessedEvent(sessionId, copilotType, user, response, System.currentTimeMillis() - startTime);

            log.info("Message processed in {}ms for sessionId={}", System.currentTimeMillis() - startTime, sessionId);
            return response;
        });
    }

    @Override
    public void startSession(SessionId sessionId, CopilotType type, UserContext user) {
        log.info("Starting session: sessionId={}, type={}, userId={}", sessionId, type, user.userId());
        sessionStatuses.put(sessionId, CopilotStatus.STARTING);
        sessionTypes.put(sessionId, type);

        memoryStore.store(MemoryEntry.of(MemoryType.SESSION, "session:" + sessionId,
            Map.of("type", type.name(), "userId", user.userId(), "startedAt", OffsetDateTime.now().toString())));

        sessionStatuses.put(sessionId, CopilotStatus.ACTIVE);

        eventBus.publish(CopilotEvent.of(
            "session.started",
            type,
            sessionId,
            Map.of("userId", user.userId(), "copilotType", type.name())
        ));

        log.info("Session started: sessionId={}", sessionId);
    }

    @Override
    public void endSession(SessionId sessionId) {
        CopilotType copilotType = sessionTypes.get(sessionId);
        log.info("Ending session: sessionId={}", sessionId);

        sessionStatuses.put(sessionId, CopilotStatus.STOPPED);
        sessionStatuses.remove(sessionId);
        sessionTypes.remove(sessionId);

        eventBus.publish(CopilotEvent.of(
            "session.ended",
            copilotType != null ? copilotType : CopilotType.CUSTOMER,
            sessionId
        ));

        if (streamingEngine.isActive(sessionId)) {
            streamingEngine.interrupt(sessionId);
        }

        log.info("Session ended: sessionId={}", sessionId);
    }

    @Override
    public CopilotStatus getStatus(SessionId sessionId) {
        return sessionStatuses.getOrDefault(sessionId, CopilotStatus.INACTIVE);
    }

    private void storeConversationMessage(SessionId sessionId, MessageRole role, String content, UserContext user) {
        ConversationMessage message = ConversationMessage.of(sessionId, role, content,
            Map.of("userId", user.userId()));
        memoryStore.store(MemoryEntry.of(
            MemoryType.SESSION,
            "conversation:" + sessionId + ":" + OffsetDateTime.now().toString(),
            message
        ));
    }

    private List<Capability> resolveCapabilities(CopilotType copilotType, UserContext user) {
        List<Capability> capabilities = capabilityRegistry.findByCopilotType(copilotType);
        log.debug("Resolved {} capabilities for copilotType={}", capabilities.size(), copilotType);
        return capabilities;
    }

    private Map<String, Object> buildResponseContext(Persona persona, List<Capability> capabilities) {
        Map<String, Object> contextData = new HashMap<>();
        contextData.put("persona", persona.name());
        contextData.put("role", persona.role());
        contextData.put("tone", persona.tone());
        contextData.put("capabilities", capabilities.stream().map(Capability::id).toList());
        return contextData;
    }

    private List<Suggestion> generateSuggestions(Persona persona, List<Capability> capabilities, String message) {
        List<Suggestion> suggestions = new ArrayList<>();
        for (Capability cap : capabilities) {
            suggestions.add(Suggestion.of(
                "Use " + cap.name(),
                cap.id(),
                Map.of("source", "capability", "description", cap.description())
            ));
        }
        return suggestions.size() > 5 ? suggestions.subList(0, 5) : suggestions;
    }

    private String generateResponse(Persona persona, List<Capability> capabilities, String message, CopilotContext context) {
        StringBuilder response = new StringBuilder();
        response.append("[").append(persona.role()).append("] ");
        response.append("I understand you're asking about \"").append(message).append("\". ");

        if (!capabilities.isEmpty()) {
            Capability primary = capabilities.get(0);
            response.append("I can help you with ").append(primary.name().toLowerCase()).append(". ");
        }

        response.append("How would you like me to proceed?");
        return response.toString();
    }

    private void publishMessageProcessedEvent(SessionId sessionId, CopilotType copilotType,
                                               UserContext user, CopilotResponse response, long processingTimeMs) {
        eventBus.publish(CopilotEvent.of(
            "message.processed",
            copilotType,
            sessionId,
            Map.of(
                "userId", user.userId(),
                "responseLength", response.message().length(),
                "suggestionCount", response.suggestions().size(),
                "processingTimeMs", processingTimeMs
            )
        ));
    }
}
