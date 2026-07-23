package com.sporekart.workspace.service;

import com.sporekart.workspace.domain.ContextSnapshot;
import com.sporekart.workspace.domain.WorkspaceEvent;
import com.sporekart.workspace.domain.WorkspaceSession;
import com.sporekart.workspace.dto.ChatRequest;
import com.sporekart.workspace.dto.ChatResponse;
import com.sporekart.workspace.dto.ContextResponse;
import com.sporekart.workspace.dto.CopilotListResponse;
import com.sporekart.workspace.dto.HandoffRequest;
import com.sporekart.workspace.dto.HandoffResponse;
import com.sporekart.workspace.dto.HistoryResponse;
import com.sporekart.workspace.dto.SwitchCopilotRequest;
import com.sporekart.workspace.dto.SwitchCopilotResponse;
import com.sporekart.workspace.dto.WorkspaceStatusResponse;
import com.sporekart.workspace.domain.CopilotInfo;
import com.sporekart.workspace.engine.ContextBroker;
import com.sporekart.workspace.engine.EventBus;
import com.sporekart.workspace.engine.SharedMemoryBroker;
import com.sporekart.workspace.engine.WorkspaceSessionManager;
import com.sporekart.workspace.infrastructure.client.CopilotServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class WorkspaceOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(WorkspaceOrchestrator.class);

    private final CopilotRouterEngine copilotRouterEngine;
    private final CollaborationEngine collaborationEngine;
    private final HandoffEngine handoffEngine;
    private final CopilotRegistryService copilotRegistryService;
    private final ContextBroker contextBroker;
    private final SharedMemoryBroker sharedMemoryBroker;
    private final WorkspaceSessionManager workspaceSessionManager;
    private final EventBus eventBus;
    private final CopilotServiceClient copilotServiceClient;

    public WorkspaceOrchestrator(
            CopilotRouterEngine copilotRouterEngine,
            CollaborationEngine collaborationEngine,
            HandoffEngine handoffEngine,
            CopilotRegistryService copilotRegistryService,
            ContextBroker contextBroker,
            SharedMemoryBroker sharedMemoryBroker,
            WorkspaceSessionManager workspaceSessionManager,
            EventBus eventBus,
            CopilotServiceClient copilotServiceClient) {
        this.copilotRouterEngine = copilotRouterEngine;
        this.collaborationEngine = collaborationEngine;
        this.handoffEngine = handoffEngine;
        this.copilotRegistryService = copilotRegistryService;
        this.contextBroker = contextBroker;
        this.sharedMemoryBroker = sharedMemoryBroker;
        this.workspaceSessionManager = workspaceSessionManager;
        this.eventBus = eventBus;
        this.copilotServiceClient = copilotServiceClient;
    }

    public ChatResponse processMessage(ChatRequest request, Map<String, Object> userContext, Map<String, Object> pageContext) {
        String sessionId = resolveSessionId(request, userContext);
        WorkspaceSession session = workspaceSessionManager.getSession(sessionId)
                .orElseThrow(() -> new IllegalStateException("Session not found: " + sessionId));

        String copilotId = determineCopilot(request, session);
        if (request.enableCollaboration()) {
            collaborationEngine.initiateCollaboration(request.message(), copilotId, List.of(), Map.of());
        }

        eventBus.publish(new WorkspaceEvent(
                UUID.randomUUID().toString(),
                WorkspaceEvent.TYPE_CONVERSATION_STARTED,
                "WorkspaceOrchestrator",
                sessionId,
                copilotId,
                Map.of("message", request.message()),
                OffsetDateTime.now()
        ));

        ChatResponse response = copilotServiceClient.sendChat(copilotId, request);

        contextBroker.updateConversationContext(sessionId, "lastMessage", request.message());
        contextBroker.updateConversationContext(sessionId, "lastResponse", response.message());

        eventBus.publish(new WorkspaceEvent(
                UUID.randomUUID().toString(),
                WorkspaceEvent.TYPE_CONVERSATION_FINISHED,
                "WorkspaceOrchestrator",
                sessionId,
                copilotId,
                Map.of("responseLength", response.message() != null ? response.message().length() : 0),
                OffsetDateTime.now()
        ));

        return response;
    }

    public SseEmitter processStreamMessage(ChatRequest request, Map<String, Object> userContext, Map<String, Object> pageContext) {
        String sessionId = resolveSessionId(request, userContext);
        WorkspaceSession session = workspaceSessionManager.getSession(sessionId)
                .orElseThrow(() -> new IllegalStateException("Session not found: " + sessionId));

        String copilotId = determineCopilot(request, session);

        SseEmitter emitter = new SseEmitter(300000L);

        eventBus.publish(new WorkspaceEvent(
                UUID.randomUUID().toString(),
                WorkspaceEvent.TYPE_CONVERSATION_STARTED,
                "WorkspaceOrchestrator",
                sessionId,
                copilotId,
                Map.of("message", request.message(), "streaming", true),
                OffsetDateTime.now()
        ));

        copilotServiceClient.streamChat(copilotId, request)
                .subscribe(
                        chunk -> {
                            try {
                                emitter.send(SseEmitter.event()
                                        .name("message")
                                        .data(chunk));
                            } catch (Exception e) {
                                log.error("Error sending SSE chunk", e);
                                emitter.completeWithError(e);
                            }
                        },
                        error -> {
                            log.error("Stream error for copilot {}", copilotId, error);
                            emitter.completeWithError(error);
                        },
                        () -> {
                            contextBroker.updateConversationContext(sessionId, "lastMessage", request.message());
                            eventBus.publish(new WorkspaceEvent(
                                    UUID.randomUUID().toString(),
                                    WorkspaceEvent.TYPE_CONVERSATION_FINISHED,
                                    "WorkspaceOrchestrator",
                                    sessionId,
                                    copilotId,
                                    Map.of("streaming", true),
                                    OffsetDateTime.now()
                            ));
                            emitter.complete();
                        }
                );

        return emitter;
    }

    public SwitchCopilotResponse switchCopilot(SwitchCopilotRequest request) {
        WorkspaceSession session = workspaceSessionManager.getSession(request.sessionId())
                .orElseThrow(() -> new IllegalArgumentException("Session not found: " + request.sessionId()));

        String previousCopilotId = session.activeCopilotId();

        if (request.preserveContext()) {
            contextBroker.exportContext(request.sessionId());
        }

        WorkspaceSession updated = workspaceSessionManager.updateSession(request.sessionId(), new WorkspaceSession(
                request.sessionId(),
                session.workspaceId(),
                session.userId(),
                request.targetCopilotId(),
                WorkspaceSession.STATUS_ACTIVE,
                session.conversationHistory(),
                session.sharedContext(),
                session.startedAt(),
                OffsetDateTime.now()
        ));

        eventBus.publish(new WorkspaceEvent(
                UUID.randomUUID().toString(),
                WorkspaceEvent.TYPE_COPILOT_SWITCHED,
                "WorkspaceOrchestrator",
                request.sessionId(),
                request.targetCopilotId(),
                Map.of("previousCopilotId", previousCopilotId, "reason", request.reason()),
                OffsetDateTime.now()
        ));

        log.info("Switched copilot from {} to {} for session {}", previousCopilotId, request.targetCopilotId(), request.sessionId());

        return new SwitchCopilotResponse(
                request.sessionId(),
                previousCopilotId,
                request.targetCopilotId(),
                "SUCCESS",
                "Switched to " + request.targetCopilotId(),
                contextBroker.getFullContext(request.sessionId())
        );
    }

    public HandoffResponse handoff(HandoffRequest request) {
        return handoffEngine.initiateHandoff(
                request.sessionId(),
                request.fromCopilotId(),
                request.toCopilotId(),
                request.reason(),
                request.contextSummary()
        );
    }

    public ContextResponse getContext(String sessionId) {
        ContextSnapshot snapshot = contextBroker.buildContextSnapshot(sessionId);
        return new ContextResponse(
                sessionId,
                snapshot.userContext(),
                snapshot.conversationContext(),
                snapshot.businessContext(),
                snapshot.knowledgeContext(),
                snapshot.timestamp()
        );
    }

    public HistoryResponse getHistory(String sessionId, int page, int size) {
        WorkspaceSession session = workspaceSessionManager.getSession(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found: " + sessionId));

        List<WorkspaceSession.ConversationTurn> history = session.conversationHistory();
        int total = history.size();
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, total);

        List<WorkspaceSession.ConversationTurn> pageTurns = fromIndex < total
                ? history.subList(fromIndex, toIndex)
                : List.of();

        return new HistoryResponse(pageTurns, total, page, size);
    }

    public WorkspaceStatusResponse getWorkspaceStatus(String workspaceId) {
        List<WorkspaceSession> activeSessions = workspaceSessionManager.getActiveSessions(workspaceId);
        int totalSessions = activeSessions.size();
        int activeCount = (int) activeSessions.stream()
                .filter(s -> WorkspaceSession.STATUS_ACTIVE.equals(s.status()))
                .count();

        Map<String, Object> analytics = new HashMap<>();
        analytics.put("totalSessions", totalSessions);
        analytics.put("activeSessions", activeCount);
        analytics.put("idleSessions", totalSessions - activeCount);
        analytics.put("timestamp", OffsetDateTime.now().toString());

        return new WorkspaceStatusResponse(
                workspaceId,
                "Workspace " + workspaceId,
                "ACTIVE",
                activeCount,
                totalSessions,
                List.of(),
                analytics
        );
    }

    public CopilotListResponse discoverCopilots() {
        List<CopilotInfo> copilots = copilotRegistryService.getAllCopilots();
        int total = copilots.size();
        int enabled = (int) copilots.stream().filter(CopilotInfo::enabled).count();
        return new CopilotListResponse(copilots, total, (int) copilots.stream().filter(c -> CopilotInfo.STATUS_ACTIVE.equals(c.status())).count(), enabled, total - enabled);
    }

    private String resolveSessionId(ChatRequest request, Map<String, Object> userContext) {
        if (request.sessionId() != null) {
            workspaceSessionManager.touchSession(request.sessionId());
            return request.sessionId();
        }
        String userId = userContext != null ? (String) userContext.getOrDefault("userId", "anonymous") : "anonymous";
        String workspaceId = request.workspaceId() != null ? request.workspaceId() : "default";
        WorkspaceSession session = workspaceSessionManager.createSession(userId, workspaceId);
        return session.sessionId();
    }

    private String determineCopilot(ChatRequest request, WorkspaceSession session) {
        if (request.preferredCopilot() != null) {
            return request.preferredCopilot();
        }
        if (session.activeCopilotId() != null) {
            return session.activeCopilotId();
        }
        return copilotRouterEngine.routeMessage(request.message(), session.sessionId(), Map.of()).copilotId();
    }
}
