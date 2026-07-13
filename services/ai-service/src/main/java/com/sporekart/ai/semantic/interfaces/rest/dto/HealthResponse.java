package com.sporekart.ai.semantic.interfaces.rest.dto;

public record HealthResponse(
        String status,
        String indexStatus,
        long embeddingCount,
        double cacheHitRatio,
        long latencyMs) {}
