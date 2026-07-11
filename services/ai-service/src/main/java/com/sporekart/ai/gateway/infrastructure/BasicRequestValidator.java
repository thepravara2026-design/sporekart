package com.sporekart.ai.gateway.infrastructure;

import com.sporekart.ai.core.application.exception.AIValidationException;
import com.sporekart.ai.core.domain.AiRequest;
import com.sporekart.ai.gateway.api.RequestValidator;

public class BasicRequestValidator implements RequestValidator {
    @Override
    public void validate(AiRequest request) {
        if (request == null) {
            throw new AIValidationException("Request must not be null");
        }
        if (request.prompt() == null || request.prompt().isBlank()) {
            throw new AIValidationException("Prompt must not be blank");
        }
    }
}
