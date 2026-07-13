package com.sporekart.ai.content.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record ContentModerateRequest(
        @NotBlank String content,
        String contentType,
        boolean checkPii,
        boolean checkProfanity,
        boolean checkToxicity
) {}
