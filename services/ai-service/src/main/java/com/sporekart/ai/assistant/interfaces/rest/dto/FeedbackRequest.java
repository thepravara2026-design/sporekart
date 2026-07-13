package com.sporekart.ai.assistant.interfaces.rest.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record FeedbackRequest(
        @NotNull UUID sessionId,
        @NotNull UUID userId,
        @Min(1) @Max(5) int rating,
        String comment,
        String category,
        boolean helpful
) {}
