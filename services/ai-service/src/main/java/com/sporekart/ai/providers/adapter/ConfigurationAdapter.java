package com.sporekart.ai.providers.adapter;

import com.sporekart.ai.providers.ProviderConfiguration;
import com.sporekart.ai.providers.ProviderContext;

public interface ConfigurationAdapter {
    ProviderConfiguration adaptConfiguration(ProviderConfiguration config, ProviderContext context);
    boolean supports(String providerId);
}
