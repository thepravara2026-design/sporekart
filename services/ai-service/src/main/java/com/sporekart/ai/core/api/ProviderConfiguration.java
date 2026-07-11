package com.sporekart.ai.core.api;

import java.util.Map;
import java.util.Optional;

public interface ProviderConfiguration {
    Optional<String> getProperty(String provider, String key);
    Map<String, String> getAllProperties(String provider);
    boolean isProviderEnabled(String provider);
    void setProperty(String provider, String key, String value);
    void reload();
}
