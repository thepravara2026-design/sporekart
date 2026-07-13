package com.sporekart.ai.configregistry.api;

import com.sporekart.ai.configregistry.domain.ConfigurationEntry;
import com.sporekart.ai.configregistry.domain.ConfigType;
import com.sporekart.ai.configregistry.domain.ConfigValidationResult;

public interface ConfigValidationService {

    ConfigValidationResult validateConfig(ConfigurationEntry entry);

    ConfigValidationResult validateAll();

    ConfigValidationResult validateByType(ConfigType type);
}
