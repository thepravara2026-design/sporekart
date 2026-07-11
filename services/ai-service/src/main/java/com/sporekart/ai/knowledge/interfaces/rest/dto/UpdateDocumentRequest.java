package com.sporekart.ai.knowledge.interfaces.rest.dto;

import java.util.UUID;

public record UpdateDocumentRequest(
        UUID categoryId,
        String title,
        String content,
        String description,
        String language,
        String source,
        String visibility,
        String businessModule,
        String region) {}
