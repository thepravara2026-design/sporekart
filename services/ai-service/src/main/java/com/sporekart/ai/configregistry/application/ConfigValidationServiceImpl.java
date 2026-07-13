package com.sporekart.ai.configregistry.application;

import com.sporekart.ai.configregistry.api.ConfigRegistryService;
import com.sporekart.ai.configregistry.api.ConfigValidationService;
import com.sporekart.ai.configregistry.domain.ConfigurationEntry;
import com.sporekart.ai.configregistry.domain.ConfigType;
import com.sporekart.ai.configregistry.domain.ConfigValidationResult;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class ConfigValidationServiceImpl implements ConfigValidationService {

    private static final Pattern KEY_PATTERN = Pattern.compile("^[a-zA-Z0-9._-]+$");

    private final ConfigRegistryService configRegistryService;

    public ConfigValidationServiceImpl(ConfigRegistryService configRegistryService) {
        this.configRegistryService = configRegistryService;
    }

    @Override
    public ConfigValidationResult validateConfig(ConfigurationEntry entry) {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        if (entry.getConfigKey() == null || entry.getConfigKey().isBlank()) {
            errors.add("configKey must not be blank");
        } else if (!KEY_PATTERN.matcher(entry.getConfigKey()).matches()) {
            errors.add("configKey contains invalid characters: " + entry.getConfigKey());
        }
        if (entry.getConfigType() == null) {
            errors.add("configType must be set");
        }
        if (entry.getConfigValue() == null) {
            warnings.add("configValue is null");
        }
        if (entry.getModule() != null && entry.getModule().isBlank()) {
            warnings.add("module is blank");
        }
        if (entry.getEnvironment() != null && entry.getEnvironment().isBlank()) {
            warnings.add("environment is blank");
        }

        return new ConfigValidationResult(errors.isEmpty(), errors, warnings, Instant.now());
    }

    @Override
    public ConfigValidationResult validateAll() {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        List<ConfigurationEntry> all = configRegistryService.searchConfig("");
        for (ConfigurationEntry entry : all) {
            ConfigValidationResult result = validateConfig(entry);
            errors.addAll(result.errors());
            warnings.addAll(result.warnings());
        }
        return new ConfigValidationResult(errors.isEmpty(), errors, warnings, Instant.now());
    }

    @Override
    public ConfigValidationResult validateByType(ConfigType type) {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        List<ConfigurationEntry> entries = configRegistryService.listByType(type);
        for (ConfigurationEntry entry : entries) {
            ConfigValidationResult result = validateConfig(entry);
            errors.addAll(result.errors());
            warnings.addAll(result.warnings());
        }
        return new ConfigValidationResult(errors.isEmpty(), errors, warnings, Instant.now());
    }
}
