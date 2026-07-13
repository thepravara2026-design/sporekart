package com.sporekart.ai.content.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record ContentSEOResult(
    UUID id,
    UUID requestId,
    String title,
    String metaDescription,
    List<String> keywords,
    String slug,
    double readabilityScore,
    double seoScore,
    List<String> suggestions,
    OffsetDateTime generatedAt) {}
