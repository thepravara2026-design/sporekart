package com.sporekart.ai.conversation.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ConversationMessageRequest(
    @NotBlank(message = "Role is required")
    String role,

    @NotBlank(message = "Content is required")
    @Size(max = 10000, message = "Content must be at most 10000 characters")
    String content
) {}
