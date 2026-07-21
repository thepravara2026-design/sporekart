package com.sporekart.ai.configuration.validation;

import com.sporekart.ai.configuration.domain.ConfigKey;
import com.sporekart.ai.configuration.domain.ProviderConfiguration;
import com.sporekart.ai.configuration.model.provider.OpenAiConfig;

public final class ConfigValidator {

    private ConfigValidator() {}

    public static ConfigValidationResult validateStringNotBlank(ConfigKey key, String value) {
        if (value == null || value.isBlank()) {
            return ConfigValidationResult.failed(key.key(),
                ConfigValidationError.error(key.key(), key.description() + " must not be blank"));
        }
        return ConfigValidationResult.success(key.key());
    }

    public static ConfigValidationResult validatePositive(ConfigKey key, Number value) {
        if (value == null || value.doubleValue() <= 0) {
            return ConfigValidationResult.failed(key.key(),
                ConfigValidationError.error(key.key(), key.description() + " must be positive"));
        }
        return ConfigValidationResult.success(key.key());
    }

    public static ConfigValidationResult validateRange(ConfigKey key, Number value, double min, double max) {
        if (value == null || value.doubleValue() < min || value.doubleValue() > max) {
            return ConfigValidationResult.failed(key.key(),
                ConfigValidationError.error(key.key(),
                    key.description() + " must be between " + min + " and " + max));
        }
        return ConfigValidationResult.success(key.key());
    }

    public static ConfigValidationResult validateUrl(ConfigKey key, String url) {
        if (url == null || url.isBlank()) {
            return ConfigValidationResult.failed(key.key(),
                ConfigValidationError.error(key.key(), key.description() + " must not be blank"));
        }
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            return ConfigValidationResult.failed(key.key(),
                ConfigValidationError.error(key.key(), key.description() + " must be a valid HTTP(S) URL"));
        }
        return ConfigValidationResult.success(key.key());
    }

    public static ConfigValidationResult validateProviderConfiguration(ProviderConfiguration config) {
        if (!config.enabled()) {
            return ConfigValidationResult.success(config.providerName());
        }
        return validateUrl(
            ConfigKey.of("provider." + config.providerName() + ".apiUrl", String.class)
                .withDescription(config.providerName() + " API URL"),
            config.apiUrl()
        );
    }

    public static ConfigValidationResult validateOpenAiConfig(OpenAiConfig config) {
        if (!config.enabled()) {
            return ConfigValidationResult.success("openai");
        }
        var urlResult = validateUrl(
            ConfigKey.of("provider.openai.apiUrl", String.class).withDescription("OpenAI API URL"),
            config.apiUrl()
        );
        if (!urlResult.isValid()) {
            return urlResult;
        }
        return validateRange(
            ConfigKey.of("provider.openai.timeout", Integer.class).withDescription("OpenAI timeout"),
            (int) config.timeout().toSeconds(), 1, 300
        );
    }
}
