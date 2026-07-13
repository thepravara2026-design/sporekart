package com.sporekart.ai.promptregistry.application;

import com.sporekart.ai.promptregistry.api.PromptValidationService;
import com.sporekart.ai.promptregistry.domain.PromptRegistryEntry;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PromptValidationServiceImpl implements PromptValidationService {

    private static final int MAX_PROMPT_LENGTH = 100_000;
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{\\{\\s*([a-zA-Z0-9_]+)\\s*}}");

    @Override
    public List<String> validateStructure(PromptRegistryEntry entry) {
        List<String> errors = new ArrayList<>();
        if (entry.getPromptId() == null || entry.getPromptId().isBlank()) {
            errors.add("promptId is required");
        }
        if (entry.getPromptName() == null || entry.getPromptName().isBlank()) {
            errors.add("promptName is required");
        }
        if (entry.getPromptText() == null || entry.getPromptText().isBlank()) {
            errors.add("promptText is required");
        }
        if (entry.getVersion() <= 0) {
            errors.add("version must be greater than zero");
        }
        if (entry.getStatus() == null) {
            errors.add("status is required");
        }
        return errors;
    }

    @Override
    public List<String> validateVariables(PromptRegistryEntry entry) {
        List<String> errors = new ArrayList<>();
        if (entry.getPromptText() == null || entry.getPromptText().isBlank()) {
            errors.add("promptText is required for variable validation");
            return errors;
        }
        Matcher matcher = VARIABLE_PATTERN.matcher(entry.getPromptText());
        while (matcher.find()) {
            String variable = matcher.group(1);
            if (entry.getMetadata() != null
                    && entry.getMetadata().containsKey("missing:" + variable)) {
                errors.add("Unresolved variable reference: " + variable);
            }
        }
        return errors;
    }

    @Override
    public List<String> validateLength(PromptRegistryEntry entry) {
        List<String> errors = new ArrayList<>();
        if (entry.getPromptText() != null && entry.getPromptText().length() > MAX_PROMPT_LENGTH) {
            errors.add("promptText exceeds maximum length of " + MAX_PROMPT_LENGTH + " characters");
        }
        if (entry.getPromptName() != null && entry.getPromptName().length() > 255) {
            errors.add("promptName exceeds maximum length of 255 characters");
        }
        return errors;
    }
}
