package com.sporekart.ai.gateway.application;

import com.sporekart.ai.core.domain.AiRequest;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.sporekart.ai.core.application.exception.AIValidationException;

class GatewayRequestValidatorTest {

    private final GatewayRequestValidator validator = new GatewayRequestValidator();

    @Test
    void shouldAcceptValidRequest() {
        AiRequest request = new AiRequest("What is my order status?");
        validator.validate(request);
    }

    @Test
    void shouldRejectNullRequest() {
        assertThrows(AIValidationException.class, () -> validator.validate(null));
    }

    @Test
    void shouldRejectBlankPrompt() {
        AiRequest request = new AiRequest("   ");
        assertThrows(AIValidationException.class, () -> validator.validate(request));
    }

    @Test
    void shouldRejectExcessivelyLongPrompt() {
        String longPrompt = "A".repeat(32001);
        AiRequest request = new AiRequest(longPrompt);
        assertThrows(AIValidationException.class, () -> validator.validate(request));
    }

    @Test
    void shouldReturnDetailedErrors() {
        var result = validator.validateDetailed(null);
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrors()).contains("Request must not be null");

        result = validator.validateDetailed(new AiRequest(""));
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrors()).contains("Prompt must not be blank");
    }
}
