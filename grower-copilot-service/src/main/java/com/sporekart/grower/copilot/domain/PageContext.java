package com.sporekart.grower.copilot.domain;

public record PageContext(
    String pageUrl,
    String pageTitle,
    String section,
    String entityType,
    String entityId
) {}
