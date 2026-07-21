package com.sporekart.ai.providers;

import com.sporekart.ai.providers.metadata.ProviderMetadata;

import java.util.List;
import java.util.Map;

public interface ProviderAdminService {
    void registerProvider(ProviderMetadata metadata);
    void unregisterProvider(String providerId);
    void activateProvider(String providerId);
    void deactivateProvider(String providerId);
    void setMaintenanceMode(String providerId, boolean enabled);
    void updatePriority(String providerId, int priority);
    Map<String, Object> getProviderStatus(String providerId);
    List<Map<String, Object>> getAllProviderStatuses();
}
