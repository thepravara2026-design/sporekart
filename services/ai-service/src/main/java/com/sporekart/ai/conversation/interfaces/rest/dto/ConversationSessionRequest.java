package com.sporekart.ai.conversation.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ConversationSessionRequest(
    @NotBlank(message = "User ID is required")
    @Size(max = 255, message = "User ID must be at most 255 characters")
    String userId,

    @Size(max = 500, message = "Title must be at most 500 characters")
    String title
) {}
