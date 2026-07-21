package com.sporekart.ai.provider.configuration;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class EnvironmentConfigProvider {

    private static final String API_KEY_SUFFIX = "_API_KEY";
    private static final String ENDPOINT_SUFFIX = "_ENDPOINT";
    private static final String TIMEOUT_SUFFIX = "_TIMEOUT_MS";
    private static final String RETRIES_SUFFIX = "_MAX_RETRIES";
    private static final String ENABLED_SUFFIX = "_ENABLED";
    private static final String RPM_SUFFIX = "_RPM";
    private static final String TPM_SUFFIX = "_TPM";

    private final Map<String, ProviderConfig> configCache = new ConcurrentHashMap<>();

    public ProviderConfig loadConfig(String providerType) {
        return configCache.computeIfAbsent(providerType.toUpperCase(), this::buildConfig);
    }

    public void refreshConfig(String providerType) {
        configCache.remove(providerType.toUpperCase());
    }

    public void refreshAll() {
        configCache.clear();
    }

    private ProviderConfig buildConfig(String providerType) {
        String prefix = "SPOREKART_AI_" + providerType;
        return new ProviderConfig(
                providerType.toLowerCase() + "-provider",
                providerType,
                getEnv(prefix + API_KEY_SUFFIX),
                getEnv(prefix + ENDPOINT_SUFFIX),
                getEnvInt(prefix + TIMEOUT_SUFFIX, 30000),
                getEnvInt(prefix + RETRIES_SUFFIX, 3),
                Map.of(),
                getEnv(prefix + "_ORG_ID"),
                getEnv(prefix + "_PROJECT_ID"),
                getEnv(prefix + "_DEPLOYMENT_NAME"),
                getEnv(prefix + "_MODEL_OVERRIDE"),
                getEnvInt(prefix + RPM_SUFFIX, 60),
                getEnvInt(prefix + TPM_SUFFIX, 100000),
                getEnvBool(prefix + ENABLED_SUFFIX, false)
        );
    }

    private static String getEnv(String key) {
        String value = System.getenv(key);
        if (value != null && !value.isBlank()) return value;

        String dotEnvKey = key.replace('_', '.').toLowerCase();
        value = System.getProperty(dotEnvKey);
        if (value != null && !value.isBlank()) return value;

        String camelKey = toCamelCase(key);
        value = System.getProperty(camelKey);
        return (value != null && !value.isBlank()) ? value : null;
    }

    private static int getEnvInt(String key, int defaultValue) {
        String value = getEnv(key);
        if (value == null) return defaultValue;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private static boolean getEnvBool(String key, boolean defaultValue) {
        String value = getEnv(key);
        if (value == null) return defaultValue;
        return "true".equalsIgnoreCase(value) || "1".equals(value) || "yes".equalsIgnoreCase(value);
    }

    private static String toCamelCase(String snakeCase) {
        var parts = snakeCase.toLowerCase().split("_");
        var result = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            if (!parts[i].isEmpty()) {
                result.append(Character.toUpperCase(parts[i].charAt(0)));
                result.append(parts[i].substring(1));
            }
        }
        return result.toString();
    }
}
