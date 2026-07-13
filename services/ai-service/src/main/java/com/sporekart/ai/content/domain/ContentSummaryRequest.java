package com.sporekart.ai.content.domain;

import java.util.UUID;

public record ContentSummaryRequest(
    UUID id,
    String sourceText,
    int maxLength,
    String language,
    boolean preserveKeyPoints) {}
