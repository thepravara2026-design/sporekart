package com.sporekart.workspace.engine;

import com.sporekart.workspace.domain.CollaborationRequest;
import com.sporekart.workspace.dto.ChatResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class CollaborationEngine {

    private static final Logger log = LoggerFactory.getLogger(CollaborationEngine.class);

    private final Map<String, CollaborationRequest> activeCollaborations = new ConcurrentHashMap<>();

    public CollaborationRequest initiateCollaboration(String query, String primaryCopilotId, List<String> collaboratingCopilotIds, Map<String, Object> context) {
        var collaborationId = UUID.randomUUID().toString();
        var request = new CollaborationRequest(
            collaborationId,
            primaryCopilotId,
            collaboratingCopilotIds,
            query,
            new ArrayList<>(),
            CollaborationRequest.STATUS_IN_PROGRESS,
            OffsetDateTime.now()
        );
        activeCollaborations.put(collaborationId, request);
        log.info("Initiated collaboration {}: primary={}, collaborators={}", collaborationId, primaryCopilotId, collaboratingCopilotIds);
        return request;
    }

    public void addCollaboratingCopilot(String collaborationId, String copilotId) {
        var request = activeCollaborations.get(collaborationId);
        if (request == null) {
            log.warn("Collaboration not found: {}", collaborationId);
            return;
        }
        var updatedCollaborators = new ArrayList<>(request.collaboratingCopilotIds());
        if (!updatedCollaborators.contains(copilotId)) {
            updatedCollaborators.add(copilotId);
        }
        activeCollaborations.put(collaborationId, new CollaborationRequest(
            request.collaborationId(),
            request.primaryCopilotId(),
            updatedCollaborators,
            request.query(),
            request.partialResponses(),
            request.status(),
            request.createdAt()
        ));
        log.info("Added copilot {} to collaboration {}", copilotId, collaborationId);
    }

    public void submitPartialResponse(String collaborationId, String copilotId, String response) {
        var request = activeCollaborations.get(collaborationId);
        if (request == null) {
            log.warn("Collaboration not found: {}", collaborationId);
            return;
        }
        var updatedResponses = new ArrayList<>(request.partialResponses());
        updatedResponses.add("[" + copilotId + "] " + response);
        var newStatus = updatedResponses.size() >= request.collaboratingCopilotIds().size()
            ? CollaborationRequest.STATUS_COMPLETED
            : CollaborationRequest.STATUS_IN_PROGRESS;
        activeCollaborations.put(collaborationId, new CollaborationRequest(
            request.collaborationId(),
            request.primaryCopilotId(),
            request.collaboratingCopilotIds(),
            request.query(),
            updatedResponses,
            newStatus,
            request.createdAt()
        ));
        log.info("Partial response submitted by {} for collaboration {}. Status: {}", copilotId, collaborationId, newStatus);
    }

    public ChatResponse getMergedResponse(String collaborationId) {
        var request = activeCollaborations.get(collaborationId);
        if (request == null) {
            log.warn("Collaboration not found: {}", collaborationId);
            return null;
        }

        var collaborationResponses = request.partialResponses().stream()
            .map(partial -> {
                var copilotId = extractCopilotId(partial);
                var message = extractMessage(partial);
                return new ChatResponse.CollaborationResponse(copilotId, copilotId, message, "COMPLETED", Map.of());
            })
            .collect(Collectors.toList());

        var mergedMessage = new StringBuilder();
        mergedMessage.append("Here is the combined response from all collaborating copilots:\n\n");
        for (var resp : collaborationResponses) {
            mergedMessage.append("--- ").append(resp.copilotName()).append(" ---\n");
            mergedMessage.append(resp.message()).append("\n\n");
        }

        return new ChatResponse(
            null,
            mergedMessage.toString().trim(),
            request.primaryCopilotId(),
            false,
            null,
            collaborationResponses,
            List.of(),
            Map.of("collaborationId", collaborationId),
            false
        );
    }

    public CollaborationRequest getCollaborationStatus(String collaborationId) {
        return activeCollaborations.get(collaborationId);
    }

    public void cancelCollaboration(String collaborationId) {
        var request = activeCollaborations.remove(collaborationId);
        if (request != null) {
            log.info("Cancelled collaboration: {}", collaborationId);
        }
    }

    public List<CollaborationRequest> getAllActiveCollaborations() {
        return List.copyOf(activeCollaborations.values());
    }

    private String extractCopilotId(String partialResponse) {
        if (partialResponse == null || !partialResponse.startsWith("[")) {
            return "unknown";
        }
        var end = partialResponse.indexOf("]");
        return end > 0 ? partialResponse.substring(1, end) : "unknown";
    }

    private String extractMessage(String partialResponse) {
        if (partialResponse == null) {
            return "";
        }
        var end = partialResponse.indexOf("] ");
        return end > 0 ? partialResponse.substring(end + 2) : partialResponse;
    }
}
