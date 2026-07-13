package com.sporekart.ai.semantic.interfaces.rest.dto;

import java.util.Map;

public record SearchResultItem(
        String documentId,
        String content,
        double score,
        int rank,
        Map<String, String> metadata) {}
