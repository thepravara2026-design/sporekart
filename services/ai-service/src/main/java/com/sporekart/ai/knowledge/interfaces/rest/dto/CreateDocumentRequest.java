package com.sporekart.ai.knowledge.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.UUID;

public record CreateDocumentRequest(
        UUID categoryId,
        @NotBlank String title,
        String description,
        String content,
        String language,
        String author,
        String source,
        String visibility,
        String businessModule,
        String region,
        List<String> tags) {}
