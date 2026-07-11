package com.sporekart.ai.provider.api;

import com.sporekart.ai.provider.domain.Provider;
import java.util.List;

public interface ModelDiscovery {
    List<String> discoverModels(Provider provider);
    boolean supportsModel(Provider provider, String modelId);
}
