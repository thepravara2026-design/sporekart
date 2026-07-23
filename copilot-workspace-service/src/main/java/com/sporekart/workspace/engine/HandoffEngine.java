package com.sporekart.workspace.engine;

import com.sporekart.workspace.domain.ContextSnapshot;
import com.sporekart.workspace.domain.HandoffRequest;
import com.sporekart.workspace.dto.HandoffResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Component
public class HandoffEngine {

    private static final Logger log = LoggerFactory.getLogger(HandoffEngine.class);

    private final Map<String, HandoffRequest> handoffRequests = new ConcurrentHashMap<>();
    private final Map<String, List<HandoffRequest>> sessionHandoffHistory = new ConcurrentHashMap<>();
    private final Map<String, ContextSnapshot> sessionContexts = new ConcurrentHashMap<>();

    public HandoffResponse initiateHandoff(String sessionId, String fromCopilot, String toCopilot, String reason, String contextSummary) {
        var handoffId = UUID.randomUUID().toString();
        var request = new HandoffRequest(
            handoffId,
            sessionId,
            fromCopilot,
            toCopilot,
            reason,
            contextSummary,
            null,
            HandoffRequest.STATUS_PENDING,
            OffsetDateTime.now()
        );
        handoffRequests.put(handoffId, request);
        sessionHandoffHistory.computeIfAbsent(sessionId, k -> new CopyOnWriteArrayList<>()).add(request);

        var transferredContext = buildTransferredContext(sessionId, contextSummary);

        log.info("Handoff initiated: {} -> {} in session {} (reason: {})", fromCopilot, toCopilot, sessionId, reason);
        return new HandoffResponse(handoffId, sessionId, fromCopilot, toCopilot, HandoffRequest.STATUS_PENDING, "Handoff initiated", transferredContext);
    }

    public HandoffResponse acceptHandoff(String handoffId) {
        var request = handoffRequests.get(handoffId);
        if (request == null) {
            log.warn("Handoff not found: {}", handoffId);
            return null;
        }
        var updated = new HandoffRequest(
            request.handoffId(),
            request.sessionId(),
            request.fromCopilotId(),
            request.toCopilotId(),
            request.reason(),
            request.contextSummary(),
            request.userMessage(),
            HandoffRequest.STATUS_ACCEPTED,
            request.createdAt()
        );
        handoffRequests.put(handoffId, updated);
        log.info("Handoff {} accepted by {}", handoffId, request.toCopilotId());
        return buildHandoffResponse(updated);
    }

    public HandoffResponse rejectHandoff(String handoffId, String reason) {
        var request = handoffRequests.get(handoffId);
        if (request == null) {
            log.warn("Handoff not found: {}", handoffId);
            return null;
        }
        var updated = new HandoffRequest(
            request.handoffId(),
            request.sessionId(),
            request.fromCopilotId(),
            request.toCopilotId(),
            request.reason(),
            request.contextSummary(),
            request.userMessage(),
            HandoffRequest.STATUS_REJECTED,
            request.createdAt()
        );
        handoffRequests.put(handoffId, updated);
        log.info("Handoff {} rejected by {}: {}", handoffId, request.toCopilotId(), reason);
        return buildHandoffResponse(updated);
    }

    public HandoffResponse completeHandoff(String handoffId, String finalMessage) {
        var request = handoffRequests.get(handoffId);
        if (request == null) {
            log.warn("Handoff not found: {}", handoffId);
            return null;
        }
        var updated = new HandoffRequest(
            request.handoffId(),
            request.sessionId(),
            request.fromCopilotId(),
            request.toCopilotId(),
            request.reason(),
            request.contextSummary(),
            finalMessage,
            HandoffRequest.STATUS_COMPLETED,
            request.createdAt()
        );
        handoffRequests.put(handoffId, updated);
        log.info("Handoff {} completed by {}", handoffId, request.toCopilotId());
        return buildHandoffResponse(updated);
    }

    public HandoffResponse getHandoffStatus(String handoffId) {
        var request = handoffRequests.get(handoffId);
        if (request == null) {
            log.warn("Handoff not found: {}", handoffId);
            return null;
        }
        return buildHandoffResponse(request);
    }

    public List<HandoffResponse> getHandoffHistory(String sessionId) {
        var history = sessionHandoffHistory.getOrDefault(sessionId, List.of());
        return history.stream().map(this::buildHandoffResponse).collect(Collectors.toList());
    }

    public String getSuggestedHandoff(String sessionId, String query, String currentCopilot) {
        if (query == null || query.isBlank()) {
            return currentCopilot;
        }
        var lower = query.toLowerCase();

        if (!"CUSTOMER".equals(currentCopilot) && (lower.contains("order") || lower.contains("buy") || lower.contains("cart") || lower.contains("checkout") || lower.contains("payment") || lower.contains("shipping"))) {
            return "CUSTOMER";
        }
        if (!"ADMIN".equals(currentCopilot) && (lower.contains("revenue") || lower.contains("report") || lower.contains("dashboard") || lower.contains("analytics") || lower.contains("kpi") || lower.contains("financial"))) {
            return "ADMIN";
        }
        if (!"TRAINER".equals(currentCopilot) && (lower.contains("lesson") || lower.contains("student") || lower.contains("attendance") || lower.contains("assessment") || lower.contains("training") || lower.contains("certification"))) {
            return "TRAINER";
        }
        if (!"GROWER".equals(currentCopilot) && (lower.contains("cultivat") || lower.contains("spawn") || lower.contains("substrate") || lower.contains("disease") || lower.contains("yield") || lower.contains("harvest") || lower.contains("mushroom"))) {
            return "GROWER";
        }
        return currentCopilot;
    }

    private HandoffResponse buildHandoffResponse(HandoffRequest request) {
        var transferredContext = buildTransferredContext(request.sessionId(), request.contextSummary());
        return new HandoffResponse(
            request.handoffId(),
            request.sessionId(),
            request.fromCopilotId(),
            request.toCopilotId(),
            request.status(),
            request.userMessage(),
            transferredContext
        );
    }

    private Map<String, Object> buildTransferredContext(String sessionId, String contextSummary) {
        var context = new HashMap<String, Object>();
        context.put("sessionId", sessionId);
        context.put("contextSummary", contextSummary);
        context.put("transferredAt", OffsetDateTime.now().toString());
        var snapshot = sessionContexts.get(sessionId);
        if (snapshot != null) {
            context.put("userContext", snapshot.userContext());
            context.put("conversationContext", snapshot.conversationContext());
            context.put("businessContext", snapshot.businessContext());
            context.put("knowledgeContext", snapshot.knowledgeContext());
            context.put("memoryContext", snapshot.memoryContext());
        }
        return context;
    }
}
