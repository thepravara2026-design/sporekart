package com.sporekart.ai.assistant.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.Map;
import java.util.UUID;

public record WorkflowRequest(
        @NotBlank String intent,
        UUID sessionId,
        Map<String, Object> parameters
) {}
