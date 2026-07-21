package com.sporekart.ai.providers.adapter;

import com.sporekart.ai.providers.model.ProviderRequest;
import com.sporekart.ai.providers.model.ProviderResponse;

public interface RequestAdapter {
    ProviderRequest adaptIncoming(ProviderRequest request);
    ProviderResponse adaptOutgoing(ProviderResponse response);
    String targetFormat();
    String sourceFormat();
}
