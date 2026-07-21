package com.sporekart.ai.providers.registry;

import com.sporekart.ai.providers.AIProvider;

import java.util.List;

public interface ProviderVersionTracker {
    String getCurrentVersion(String providerId);
    List<String> getVersionHistory(String providerId);
    void trackVersion(String providerId, String version);
    boolean isLatestVersion(String providerId);
    String getLatestVersion();
}
