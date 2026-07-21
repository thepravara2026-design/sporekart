package com.sporekart.ai.providers;

import com.sporekart.ai.providers.adapter.ProviderAdapter;
import com.sporekart.ai.providers.client.ProviderClient;
import com.sporekart.ai.providers.model.ProviderRequest;
import com.sporekart.ai.providers.model.ProviderResponse;

public interface ProviderContext {
    String correlationId();
    String tenantId();
    String userId();
    ProviderRequest request();
    ProviderAdapter adapter();
    ProviderClient client();
    long startTimeNanos();
}
