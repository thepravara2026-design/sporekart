package com.sporekart.ai.semantic.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record EmbedRequest(
        @NotBlank String content,
        String provider,
        String model) {}
