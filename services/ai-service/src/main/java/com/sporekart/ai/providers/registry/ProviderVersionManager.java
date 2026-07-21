package com.sporekart.ai.providers.registry;

import java.util.List;

public interface ProviderVersionManager {
    void registerVersion(String providerId, String version);
    String getCurrentVersion(String providerId);
    List<String> getVersionHistory(String providerId);
    boolean isLatestVersion(String providerId);
    boolean isCompatible(String providerId, String version);
    List<String> getCompatibleVersions(String providerId);
    void deprecateVersion(String providerId, String version);
}
