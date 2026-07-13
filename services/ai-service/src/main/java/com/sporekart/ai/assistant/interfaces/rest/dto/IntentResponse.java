package com.sporekart.ai.assistant.interfaces.rest.dto;

import com.sporekart.ai.assistant.domain.IntentResult;
import java.util.List;
import java.util.Map;

public record IntentResponse(
        String intent,
        double confidence,
        String priority,
        List<String> entities,
        Map<String, Object> metadata,
        boolean isFallback
) {
    public static IntentResponse from(IntentResult result) {
        return new IntentResponse(
                result.intent(),
                result.confidence(),
                result.priority().name(),
                result.entities(),
                result.metadata(),
                result.isFallback()
        );
    }
}
