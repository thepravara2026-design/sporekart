package com.sporekart.ai.semantic.interfaces.rest.dto;

public record SimilarityResultItem(
        String targetId,
        double similarity,
        String content) {}
