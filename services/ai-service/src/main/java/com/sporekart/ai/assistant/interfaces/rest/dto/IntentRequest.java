package com.sporekart.ai.assistant.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record IntentRequest(
        @NotBlank String message
) {}
