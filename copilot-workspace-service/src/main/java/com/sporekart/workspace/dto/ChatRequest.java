package com.sporekart.workspace.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.Map;

public record ChatRequest(
    @NotBlank String message,
    String sessionId,
    String workspaceId,
    String preferredCopilot,
    String pageUrl,
    String pageTitle,
    boolean enableCollaboration
) {}
