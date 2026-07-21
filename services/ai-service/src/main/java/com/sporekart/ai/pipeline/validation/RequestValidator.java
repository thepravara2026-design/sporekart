package com.sporekart.ai.pipeline.validation;

import com.sporekart.ai.pipeline.model.PipelineRequest;

import java.util.ArrayList;
import java.util.List;

public class RequestValidator {
    private static final int MAX_PROMPT_LENGTH = 100000;
    private static final int MAX_CONTEXT_SIZE = 50000;
    private static final int MAX_VARIABLES = 100;
    private static final int MAX_TENANT_ID_LENGTH = 100;
    private static final int MAX_USER_ID_LENGTH = 200;
    private static final int MAX_MODULE_LENGTH = 100;
    private static final double MAX_TEMPERATURE = 2.0;
    private static final int ABSOLUTE_MAX_TOKENS = 128000;

    public ValidationResult validate(PipelineRequest request) {
        var errors = new ArrayList<String>();
        var warnings = new ArrayList<String>();

        validateRequiredFields(request, errors);
        validatePrompt(request, errors, warnings);
        validateProvider(request, warnings);
        validateTokenLimits(request, errors);
        validateContextSize(request, errors, warnings);
        validateVariables(request, errors, warnings);
        validateConfiguration(request, errors, warnings);

        return new ValidationResult(errors.isEmpty(), errors, warnings);
    }

    private void validateRequiredFields(PipelineRequest request, List<String> errors) {
        if (request.requestId() == null || request.requestId().isBlank())
            errors.add("requestId is required");
        if (request.tenantId() == null || request.tenantId().isBlank())
            errors.add("tenantId is required");
        else if (request.tenantId().length() > MAX_TENANT_ID_LENGTH)
            errors.add("tenantId exceeds max length of " + MAX_TENANT_ID_LENGTH);
        if (request.userId() == null || request.userId().isBlank())
            errors.add("userId is required");
        else if (request.userId().length() > MAX_USER_ID_LENGTH)
            errors.add("userId exceeds max length of " + MAX_USER_ID_LENGTH);
        if (request.module() == null || request.module().isBlank())
            errors.add("module is required");
        else if (request.module().length() > MAX_MODULE_LENGTH)
            errors.add("module exceeds max length of " + MAX_MODULE_LENGTH);
        if (request.correlationId() == null || request.correlationId().isBlank())
            errors.add("correlationId is required");
    }

    private void validatePrompt(PipelineRequest request, List<String> errors, List<String> warnings) {
        var context = request.context();
        if (context == null || context.isEmpty()) {
            errors.add("context is required (must contain prompt or input data)");
            return;
        }

        Object prompt = context.get("prompt");
        if (prompt == null) {
            prompt = context.get("input");
        }
        if (prompt == null) {
            prompt = context.get("query");
        }
        if (prompt == null) {
            errors.add("context must contain a 'prompt', 'input', or 'query' field");
            return;
        }

        String promptStr = prompt.toString();
        if (promptStr.isBlank()) {
            errors.add("prompt must not be empty");
        } else if (promptStr.length() > MAX_PROMPT_LENGTH) {
            errors.add("prompt exceeds max length of " + MAX_PROMPT_LENGTH);
        }
        if (promptStr.length() > MAX_PROMPT_LENGTH / 2) {
            warnings.add("prompt is approaching max length limit");
        }
    }

    private void validateProvider(PipelineRequest request, List<String> warnings) {
        if (request.providerPreference() != null && !request.providerPreference().isBlank()) {
            var validProviders = List.of("OPENAI", "GEMINI", "CLAUDE", "AZURE_OPENAI",
                    "OLLAMA", "GROQ", "MISTRAL", "OPENROUTER", "BEDROCK", "TOGETHER_AI");
            if (validProviders.stream().noneMatch(p ->
                    p.equalsIgnoreCase(request.providerPreference()))) {
                warnings.add("Unrecognized provider preference: " + request.providerPreference());
            }
        }
    }

    private void validateTokenLimits(PipelineRequest request, List<String> errors) {
        if (request.maxTokens() <= 0) {
            errors.add("maxTokens must be positive");
        } else if (request.maxTokens() > ABSOLUTE_MAX_TOKENS) {
            errors.add("maxTokens exceeds absolute limit of " + ABSOLUTE_MAX_TOKENS);
        }
        if (request.temperature() < 0) {
            errors.add("temperature must not be negative");
        } else if (request.temperature() > MAX_TEMPERATURE) {
            errors.add("temperature exceeds maximum of " + MAX_TEMPERATURE);
        }
        if (request.topP() < 0 || request.topP() > 1.0) {
            errors.add("topP must be between 0 and 1");
        }
    }

    private void validateContextSize(PipelineRequest request, List<String> errors, List<String> warnings) {
        var context = request.context();
        if (context != null) {
            String serialized = context.toString();
            if (serialized.length() > MAX_CONTEXT_SIZE) {
                errors.add("context size exceeds max limit of " + MAX_CONTEXT_SIZE + " characters");
            } else if (serialized.length() > MAX_CONTEXT_SIZE / 2) {
                warnings.add("context is approaching max size limit");
            }
        }
    }

    private void validateVariables(PipelineRequest request, List<String> errors, List<String> warnings) {
        var variables = request.variables();
        if (variables != null) {
            if (variables.size() > MAX_VARIABLES) {
                errors.add("number of variables exceeds max limit of " + MAX_VARIABLES);
            }
            for (var entry : variables.entrySet()) {
                if (entry.getValue() == null) {
                    warnings.add("Variable '" + entry.getKey() + "' has null value");
                }
            }
        }
    }

    private void validateConfiguration(PipelineRequest request, List<String> errors, List<String> warnings) {
        if (request.model() != null && !request.model().isBlank()) {
            var model = request.model().toLowerCase();
            if (model.contains("unsupported") || model.contains("invalid")) {
                warnings.add("Model '" + request.model() + "' may not be supported");
            }
        }
        if (request.stream() && request.maxTokens() > 32000) {
            warnings.add("Streaming with maxTokens > 32000 may cause timeout");
        }
    }
}
