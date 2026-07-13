package com.sporekart.ai.semantic.interfaces.rest.dto;

public record IndexResponse(
        String id,
        String name,
        String status,
        int vectorCount,
        int dimensions,
        String createdAt) {}
