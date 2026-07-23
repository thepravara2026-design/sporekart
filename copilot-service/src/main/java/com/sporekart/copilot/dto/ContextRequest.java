package com.sporekart.copilot.dto;

public record ContextRequest(
    String pageUrl,
    String pageTitle,
    String section,
    String entityType,
    String entityId
) {}
