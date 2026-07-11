package com.sporekart.ai.infrastructure.provider;

import com.sporekart.ai.domain.model.ProviderType;
import com.sporekart.ai.domain.model.ai.AiProvider;
import com.sporekart.ai.domain.port.AiProviderPort;
import org.springframework.stereotype.Component;

@Component
public class OpenAiProvider implements AiProviderPort {
    @Override
    public boolean supports(ProviderType providerType) {
        return providerType == ProviderType.OPENAI;
    }

    @Override
    public String generateResponse(String prompt, AiProvider provider) {
        return "openai:" + prompt;
    }
}
