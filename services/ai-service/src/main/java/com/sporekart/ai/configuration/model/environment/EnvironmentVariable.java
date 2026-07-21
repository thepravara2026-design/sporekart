package com.sporekart.ai.configuration.model.environment;

public record EnvironmentVariable(
    String variableName,
    String description,
    String defaultValue,
    boolean required,
    boolean secret,
    Environment targetEnvironment,
    String validationPattern
) {
    public static EnvironmentVariable of(String variableName, String description) {
        return new EnvironmentVariable(variableName, description, "", false, false, Environment.DEV, null);
    }

    public EnvironmentVariable withDefault(String defaultValue) {
        return new EnvironmentVariable(variableName, description, defaultValue, required, secret,
            targetEnvironment, validationPattern);
    }

    public EnvironmentVariable markRequired() {
        return new EnvironmentVariable(variableName, description, defaultValue, true, secret,
            targetEnvironment, validationPattern);
    }

    public EnvironmentVariable markSecret() {
        return new EnvironmentVariable(variableName, description, defaultValue, required, true,
            targetEnvironment, validationPattern);
    }
}
