package com.sporekart.workspace.dto;

import com.sporekart.copilot.domain.Suggestion;
import java.util.List;
import java.util.Map;

public record ChatResponse(
    String sessionId,
    String message,
    String copilotId,
    boolean handoffRequired,
    String handoffCopilotId,
    List<CollaborationResponse> collaborationResponses,
    List<Suggestion> suggestions,
    Map<String, Object> context,
    boolean streaming
) {

    public record CollaborationResponse(
        String copilotId,
        String copilotName,
        String message,
        String status,
        Map<String, Object> metadata
    ) {}
}
