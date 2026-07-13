package com.sporekart.ai.admin.application;

import com.sporekart.ai.admin.api.ConfigurationValidationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ConfigurationValidationServiceImpl implements ConfigurationValidationService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean validateConfiguration(String key, String value, String module) {
        if (key == null || key.isBlank()) {
            log.warn("Validation failed: key is empty");
            return false;
        }
        if (value == null || value.isBlank()) {
            log.warn("Validation failed: value is empty for key {}", key);
            return false;
        }
        try {
            objectMapper.readTree(value);
        } catch (Exception e) {
            log.warn("Validation failed: value is not valid JSON for key {}: {}", key, e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public List<String> validateAll(String module) {
        var errors = new ArrayList<String>();
        if (module == null || module.isBlank()) {
            errors.add("Module must not be empty");
        }
        return errors;
    }

    @Override
    public Map<String, Boolean> validateImport(Map<String, String> configuration) {
        var results = new HashMap<String, Boolean>();
        if (configuration == null) {
            return results;
        }
        for (var entry : configuration.entrySet()) {
            boolean valid = entry.getKey() != null && !entry.getKey().isBlank()
                    && entry.getValue() != null && !entry.getValue().isBlank();
            if (valid) {
                try {
                    objectMapper.readTree(entry.getValue());
                } catch (Exception e) {
                    valid = false;
                }
            }
            results.put(entry.getKey(), valid);
        }
        return results;
    }

    @Override
    public boolean validateEnvironment(String environment) {
        return environment != null && !environment.isBlank();
    }
}
