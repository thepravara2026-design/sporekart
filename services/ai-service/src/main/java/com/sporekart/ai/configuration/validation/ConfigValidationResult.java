package com.sporekart.ai.configuration.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ConfigValidationResult {
    private final String configKey;
    private final boolean valid;
    private final List<ConfigValidationError> errors;
    private final List<ConfigValidationError> warnings;

    public ConfigValidationResult(String configKey, boolean valid,
                                  List<ConfigValidationError> errors,
                                  List<ConfigValidationError> warnings) {
        this.configKey = configKey;
        this.valid = valid;
        this.errors = Collections.unmodifiableList(errors);
        this.warnings = Collections.unmodifiableList(warnings);
    }

    public static ConfigValidationResult success(String configKey) {
        return new ConfigValidationResult(configKey, true, List.of(), List.of());
    }

    public static ConfigValidationResult failed(String configKey, ConfigValidationError error) {
        return new ConfigValidationResult(configKey, false, List.of(error), List.of());
    }

    public boolean isValid() { return valid; }
    public String configKey() { return configKey; }
    public List<ConfigValidationError> errors() { return errors; }
    public List<ConfigValidationError> warnings() { return warnings; }
    public boolean hasErrors() { return !errors.isEmpty(); }
    public boolean hasWarnings() { return !warnings.isEmpty(); }
}
