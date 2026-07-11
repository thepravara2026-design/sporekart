package com.sporekart.ai.prompt.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "Request to update an existing prompt template")
public record UpdatePromptRequest(
        UUID categoryId,
        String name,
        String description,
        String templateText) {}
