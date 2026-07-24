package com.sporekart.search.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Map;

public record IndexDocumentRequest(
        @NotBlank @Size(max = 50) String entityType,
        @NotBlank @Size(max = 100) String entityId,
        @NotBlank @Size(max = 500) String title,
        @Size(max = 2000) String description,
        @Size(max = 10000) String content,
        List<String> tags,
        Map<String, String> metadata) {
}