package com.sporekart.ai.application.service;

import com.sporekart.ai.domain.model.AssistantType;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class PromptOrchestrationServiceTest {

    @Test
    void shouldAssemblePromptWithDynamicVariables() {
        PromptOrchestrationService service = new PromptOrchestrationService();

        String prompt = service.buildPrompt(AssistantType.CUSTOMER, "support", "How do I return an order?",
                Map.of("topic", "returns"));

        assertThat(prompt).contains("customer");
        assertThat(prompt).contains("returns");
    }
}
