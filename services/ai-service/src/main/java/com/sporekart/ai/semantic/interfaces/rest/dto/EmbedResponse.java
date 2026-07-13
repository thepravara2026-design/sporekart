package com.sporekart.ai.semantic.interfaces.rest.dto;

public record EmbedResponse(
        String id,
        String status,
        int dimensions,
        String provider,
        String model,
        String createdAt) {}
