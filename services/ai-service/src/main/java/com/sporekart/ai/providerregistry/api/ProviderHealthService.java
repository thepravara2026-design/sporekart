package com.sporekart.ai.providerregistry.api;

import com.sporekart.ai.providerregistry.domain.ProviderHealthStatus;

import java.util.List;
import java.util.Optional;

public interface ProviderHealthService {

    void recordHealth(String providerId, ProviderHealthStatus status);

    Optional<ProviderHealthStatus> getHealth(String providerId);

    List<ProviderHealthStatus> getHealthHistory(String providerId);
}
