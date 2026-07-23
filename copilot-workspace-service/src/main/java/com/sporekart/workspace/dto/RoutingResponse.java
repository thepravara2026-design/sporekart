package com.sporekart.workspace.dto;

import java.util.List;

public record RoutingResponse(
    String copilotId,
    double confidence,
    String reasoning,
    boolean requiresHandoff,
    List<String> collaboratingCopilotIds
) {}
