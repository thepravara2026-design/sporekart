package com.sporekart.ai.configuration.api;

import com.sporekart.ai.configuration.domain.ConfigKey;
import com.sporekart.ai.configuration.validation.ConfigValidationResult;

import java.util.List;

public interface ConfigurationValidator {
    <T> ConfigValidationResult validate(ConfigKey key, T value);
    ConfigValidationResult validateAll();
    boolean isValid(ConfigKey key);
    List<ConfigValidationResult> getErrors();
    List<ConfigValidationResult> getWarnings();
    void clear();
}
