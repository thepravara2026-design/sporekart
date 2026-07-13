package com.sporekart.ai.assistant.interfaces.rest.dto;

import com.sporekart.ai.assistant.domain.AssistantFeedback;
import java.util.UUID;

public record FeedbackResponse(
        UUID id,
        int rating,
        String message
) {
    public static FeedbackResponse from(AssistantFeedback feedback) {
        return new FeedbackResponse(
                feedback.id(),
                feedback.rating(),
                "Feedback recorded successfully"
        );
    }
}
