package com.sporekart.ai.admin.api;

import com.sporekart.ai.admin.domain.EnvironmentProfile;

import java.util.List;
import java.util.UUID;

public interface EnvironmentManager {
    EnvironmentProfile getEnvironment(String name);
    EnvironmentProfile createEnvironment(EnvironmentProfile profile);
    EnvironmentProfile updateEnvironment(UUID id, EnvironmentProfile profile);
    List<EnvironmentProfile> getAllEnvironments();
    EnvironmentProfile switchEnvironment(String name);
}
