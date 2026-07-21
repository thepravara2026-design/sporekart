package com.sporekart.ai.configuration.api;

import com.sporekart.ai.configuration.model.environment.Environment;
import com.sporekart.ai.configuration.model.environment.EnvironmentVariable;

import java.util.List;
import java.util.Optional;

public interface EnvironmentProvider {
    Environment current();
    String getActiveProfile();
    List<String> getActiveProfiles();
    Optional<String> getVariable(EnvironmentVariable variable);
    String getVariableOrDefault(EnvironmentVariable variable);
    boolean hasVariable(EnvironmentVariable variable);
    boolean isProduction();
    boolean isDevelopment();
    List<EnvironmentVariable> getRequiredVariables();
}
