package com.sporekart.ai.providers.adapter;

import com.sporekart.ai.providers.model.ProviderRequest;
import com.sporekart.ai.providers.model.ProviderResponse;

public interface ProviderAdapter {
    String adapterId();
    String providerId();
    ProviderResponse adaptRequest(ProviderRequest request);
    ProviderResponse adaptResponse(ProviderResponse response);
    boolean supports(String model);
}
