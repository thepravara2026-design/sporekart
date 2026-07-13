package com.sporekart.ai.admin.api;

import com.sporekart.ai.admin.domain.AdminConfiguration;

import java.util.List;
import java.util.UUID;

public interface ConfigurationManager {
    AdminConfiguration getConfiguration(String key, String module, String environment);
    AdminConfiguration setConfiguration(String key, String value, String module, String environment, String description, UUID updatedBy);
    List<AdminConfiguration> getAllConfigurations(String module);
    AdminConfiguration deleteConfiguration(UUID id);
    List<AdminConfiguration> getConfigurationsByEnvironment(String environment);
    AdminConfiguration validateConfiguration(String key, String value);
}
