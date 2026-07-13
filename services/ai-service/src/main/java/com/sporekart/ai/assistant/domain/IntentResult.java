package com.sporekart.ai.assistant.domain;

import java.util.List;
import java.util.Map;

public record IntentResult(
    String intent,
    double confidence,
    IntentPriority priority,
    Map<String, Object> metadata,
    List<String> entities,
    boolean isFallback
) {}
