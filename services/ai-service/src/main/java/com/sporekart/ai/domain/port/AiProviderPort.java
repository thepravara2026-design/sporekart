package com.sporekart.ai.domain.port;

import com.sporekart.ai.domain.model.ProviderType;
import com.sporekart.ai.domain.model.ai.AiProvider;

public interface AiProviderPort {
    boolean supports(ProviderType providerType);

    String generateResponse(String prompt, AiProvider provider);
}
