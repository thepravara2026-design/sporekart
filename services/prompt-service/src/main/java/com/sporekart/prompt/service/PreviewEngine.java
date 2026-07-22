package com.sporekart.prompt.service;

import com.sporekart.prompt.dto.response.PreviewResponse;
import com.sporekart.prompt.entity.PromptVersionEntity;
import com.sporekart.prompt.repository.PromptVersionRepository;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Pattern;

@Component
public class PreviewEngine {

    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{\\{\\s*(\\w+)\\s*}}");
    private static final Pattern OPTIONAL_VARIABLE_PATTERN = Pattern.compile("\\{\\{\\s*\\?(\\w+)\\s*}}");
    private static final Pattern NESTED_VARIABLE_PATTERN = Pattern.compile("\\{\\{\\s*(\\w+\\.\\w+)\\s*}}");

    private static final Set<String> RESERVED_VARIABLES = Set.of(
            "tenantId", "userId", "workspaceId", "timestamp", "correlationId",
            "environment", "locale", "timezone", "date", "time");

    private final PromptVersionRepository versionRepository;
    private final TokenEstimator tokenEstimator;

    public PreviewEngine(PromptVersionRepository versionRepository, TokenEstimator tokenEstimator) {
        this.versionRepository = versionRepository;
        this.tokenEstimator = tokenEstimator;
    }

    public PreviewResponse preview(UUID versionId, Map<String, String> variables) {
        var version = versionRepository.findById(versionId)
                .orElseThrow(() -> new IllegalArgumentException("Version not found: " + versionId));

        var resolvedVariables = new HashMap<String, String>();
        var missingVariables = new ArrayList<String>();
        var warnings = new ArrayList<String>();

        var allVariables = extractVariables(version.getPromptBody());
        if (version.getVariablesJson() != null && !version.getVariablesJson().isBlank()) {
            allVariables.addAll(parseVariablesFromJson(version.getVariablesJson()));
        }

        for (var varName : allVariables) {
            if (varName.startsWith("?")) {
                var optionalName = varName.substring(1);
                if (variables != null && variables.containsKey(optionalName)) {
                    resolvedVariables.put(optionalName, variables.get(optionalName));
                } else {
                    resolvedVariables.put(optionalName, null);
                }
            } else if (varName.contains(".")) {
                var parts = varName.split("\\.");
                var flatKey = parts[0] + "_" + parts[1];
                if (variables != null && variables.containsKey(flatKey)) {
                    resolvedVariables.put(varName, variables.get(flatKey));
                    resolvedVariables.put(flatKey, variables.get(flatKey));
                } else if (variables != null && variables.containsKey(varName)) {
                    resolvedVariables.put(varName, variables.get(varName));
                    resolvedVariables.put(flatKey, variables.get(varName));
                } else if (RESERVED_VARIABLES.contains(varName)) {
                    resolvedVariables.put(varName, "[" + varName + "]");
                } else {
                    missingVariables.add(varName);
                    resolvedVariables.put(varName, "[MISSING:" + varName + "]");
                }
            } else if (variables != null && variables.containsKey(varName)) {
                resolvedVariables.put(varName, variables.get(varName));
            } else if (RESERVED_VARIABLES.contains(varName)) {
                resolvedVariables.put(varName, "[" + varName + "]");
            } else {
                missingVariables.add(varName);
                resolvedVariables.put(varName, "[MISSING:" + varName + "]");
            }
        }

        if (!missingVariables.isEmpty()) {
            warnings.add("Missing variables: " + String.join(", ", missingVariables));
        }

        if (version.getPromptBody() != null) {
            validateUnsafePlaceholders(version.getPromptBody(), warnings);
        }

        var renderedPrompt = render(version.getPromptBody(), resolvedVariables);
        var renderedSystem = version.getSystemPrompt() != null
                ? render(version.getSystemPrompt(), resolvedVariables)
                : null;

        int estimatedPromptTokens = tokenEstimator.estimateTotalTokens(
                renderedPrompt, renderedSystem, null);
        int estimatedCompletionTokens = tokenEstimator.estimateCompletionTokens(renderedPrompt);
        var estimate = new PreviewResponse.TokenEstimate(
                estimatedPromptTokens, estimatedCompletionTokens,
                estimatedPromptTokens + estimatedCompletionTokens);

        return new PreviewResponse(renderedPrompt, renderedSystem, resolvedVariables,
                missingVariables, warnings, estimate);
    }

    public String render(String template, Map<String, String> variables) {
        if (template == null) return null;
        var result = template;

        result = NESTED_VARIABLE_PATTERN.matcher(result).replaceAll(mr -> {
            var key = mr.group(1);
            return resolveNested(key, variables);
        });

        result = OPTIONAL_VARIABLE_PATTERN.matcher(result).replaceAll(mr -> {
            var key = mr.group(1);
            return variables != null && variables.containsKey(key) && variables.get(key) != null
                    ? variables.get(key) : "";
        });

        result = VARIABLE_PATTERN.matcher(result).replaceAll(mr -> {
            var key = mr.group(1);
            if (variables != null && variables.containsKey(key) && variables.get(key) != null) {
                return variables.get(key);
            }
            return "[MISSING:" + key + "]";
        });

        return result;
    }

    private String resolveNested(String key, Map<String, String> variables) {
        var parts = key.split("\\.");
        if (parts.length == 2 && variables != null) {
            var nestedKey = parts[0] + "_" + parts[1];
            if (variables.containsKey(nestedKey)) {
                return variables.get(nestedKey);
            }
        }
        return "[MISSING:" + key + "]";
    }

    public Set<String> extractVariables(String template) {
        var variables = new LinkedHashSet<String>();
        if (template == null) return variables;

        var nestedMatcher = NESTED_VARIABLE_PATTERN.matcher(template);
        while (nestedMatcher.find()) {
            variables.add(nestedMatcher.group(1));
        }

        var optMatcher = OPTIONAL_VARIABLE_PATTERN.matcher(template);
        while (optMatcher.find()) {
            variables.add("?" + optMatcher.group(1));
        }

        var matcher = VARIABLE_PATTERN.matcher(template);
        while (matcher.find()) {
            var name = matcher.group(1);
            if (!name.contains(".")) {
                variables.add(name);
            }
        }

        return variables;
    }

    public void validateVariables(String template, Map<String, String> variables) {
        var missing = new ArrayList<String>();
        var matcher = VARIABLE_PATTERN.matcher(template);
        while (matcher.find()) {
            var key = matcher.group(1);
            if (!RESERVED_VARIABLES.contains(key) && (variables == null || !variables.containsKey(key))) {
                missing.add(key);
            }
        }
        if (!missing.isEmpty()) {
            throw new IllegalArgumentException("Missing required variables: " + String.join(", ", missing));
        }
    }

    private void validateUnsafePlaceholders(String template, List<String> warnings) {
        var unsafePattern = Pattern.compile("(\\$\\{[^}]+})");
        var matcher = unsafePattern.matcher(template);
        if (matcher.find()) {
            warnings.add("Template contains raw ${} placeholders: " + matcher.group(1));
        }
    }

    private List<String> parseVariablesFromJson(String json) {
        var variables = new ArrayList<String>();
        if (json == null || json.isBlank()) return variables;
        try {
            var trimmed = json.trim();
            if (trimmed.startsWith("[")) {
                var content = trimmed.substring(1, trimmed.length() - 1);
                for (var item : content.split(",")) {
                    var cleaned = item.trim().replaceAll("^\"|\"$", "");
                    if (!cleaned.isBlank()) variables.add(cleaned);
                }
            }
        } catch (Exception ignored) {}
        return variables;
    }
}
