package com.sporekart.ai.gateway.application;

import com.sporekart.ai.core.application.exception.AIValidationException;
import com.sporekart.ai.core.application.validation.ValidationResult;
import com.sporekart.ai.core.domain.AiRequest;
import com.sporekart.ai.gateway.api.RequestValidator;
import org.springframework.stereotype.Service;

@Service
public class GatewayRequestValidator implements RequestValidator {
    private static final int MAX_PROMPT_LENGTH = 32000;

    @Override
    public void validate(AiRequest request) {
        ValidationResult result = validateDetailed(request);
        if (!result.isValid()) {
            throw new AIValidationException(String.join("; ", result.getErrors()));
        }
    }

    public ValidationResult validateDetailed(AiRequest request) {
        ValidationResult.Builder builder = new ValidationResult.Builder();

        if (request == null) {
            return ValidationResult.withError("Request must not be null");
        }

        if (request.prompt() == null || request.prompt().isBlank()) {
            builder.addError("Prompt must not be blank");
        } else if (request.prompt().length() > MAX_PROMPT_LENGTH) {
            builder.addError("Prompt exceeds maximum length of " + MAX_PROMPT_LENGTH);
        }

        return builder.build();
    }
}
