package com.sporekart.ai.content.domain;

import java.util.List;
import java.util.UUID;

public record ContentClassificationRequest(
    UUID id,
    String text,
    List<String> categories,
    int maxCategories) {}
