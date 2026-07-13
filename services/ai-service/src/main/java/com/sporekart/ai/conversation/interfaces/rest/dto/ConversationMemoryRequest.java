package com.sporekart.ai.conversation.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ConversationMemoryRequest(
    @NotBlank(message = "Memory type is required")
    String memoryType,

    @Size(max = 5000, message = "Summary must be at most 5000 characters")
    String summary,

    @Size(max = 1000, message = "Keywords must be at most 1000 characters")
    String keywords,

    double relevanceScore
) {}
