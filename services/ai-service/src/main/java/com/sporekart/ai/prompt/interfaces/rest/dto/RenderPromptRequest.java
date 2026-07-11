package com.sporekart.ai.prompt.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.util.Map;
import java.util.UUID;

@Schema(description = "Request to render a prompt template with variables")
public record RenderPromptRequest(
        @NotBlank String templateText,
        Map<String, Object> variables) {}
