package com.sporekart.ai.admin.api;

import java.util.List;
import java.util.Map;

public interface ConfigurationValidationService {
    boolean validateConfiguration(String key, String value, String module);
    List<String> validateAll(String module);
    Map<String, Boolean> validateImport(Map<String, String> configuration);
    boolean validateEnvironment(String environment);
}
