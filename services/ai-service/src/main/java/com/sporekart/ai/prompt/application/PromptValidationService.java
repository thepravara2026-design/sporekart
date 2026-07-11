package com.sporekart.ai.prompt.application;

import com.sporekart.ai.core.domain.AiConstants;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptTemplateEntity;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptVariableEntity;
import com.sporekart.ai.prompt.infrastructure.persistence.PromptVariableRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Service
public class PromptValidationService {

    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{\\{\\s*[a-zA-Z_][a-zA-Z0-9_]*\\s*}}");
    private static final Pattern INJECTION_PATTERN = Pattern.compile(
            "(?i)(\\{\\{.*\\}\\}.*\\{\\{|\\$\\{.*}|<\\\\(?i)script|javascript:)");
    private static final int MAX_TEMPLATE_LENGTH = 50000;
    private static final int MAX_VARIABLE_VALUE_LENGTH = 10000;

    private final PromptVariableRepository variableRepository;

    public PromptValidationService(PromptVariableRepository variableRepository) {
        this.variableRepository = variableRepository;
    }

    public void validateTemplate(PromptTemplateEntity template) {
        if (template.getName() == null || template.getName().isBlank()) {
            throw new PromptValidationException("Template name is required");
        }
        if (template.getName().length() > 255) {
            throw new PromptValidationException("Template name exceeds 255 characters");
        }
        if (template.getTemplateText() == null || template.getTemplateText().isBlank()) {
            throw new PromptValidationException("Template text is required");
        }
        if (template.getTemplateText().length() > MAX_TEMPLATE_LENGTH) {
            throw new PromptValidationException("Template text exceeds maximum length of " + MAX_TEMPLATE_LENGTH);
        }
        if (containsInjection(template.getTemplateText())) {
            throw new PromptValidationException("Template contains potentially unsafe content");
        }
        validateVariableSyntax(template.getTemplateText());
    }

    public void validateVariableSyntax(String templateText) {
        java.util.regex.Matcher matcher = Pattern.compile("\\{\\{\\s*\\w+\\s*}}").matcher(templateText);
        int lastEnd = 0;
        while (matcher.find()) {
            if (matcher.start() > lastEnd) {
                String between = templateText.substring(lastEnd, matcher.start());
                if (between.contains("{{") || between.contains("}}")) {
                    throw new PromptValidationException("Malformed variable syntax at position " + lastEnd);
                }
            }
            lastEnd = matcher.end();
        }
    }

    public void validateRenderVariables(PromptTemplateEntity template, Map<String, Object> variables) {
        List<PromptVariableEntity> definedVariables =
                variableRepository.findByTemplateIdAndIsDeletedFalseOrderByDisplayOrder(template.getId());

        if (definedVariables.isEmpty()) {
            validateDynamicVariables(template.getTemplateText(), variables);
            return;
        }

        Set<String> providedKeys = variables != null ? variables.keySet() : Set.of();
        for (PromptVariableEntity defined : definedVariables) {
            if (defined.isRequired() && !providedKeys.contains(defined.getName())) {
                throw new PromptValidationException("Required variable '" + defined.getName() + "' is missing");
            }
            Object value = variables != null ? variables.get(defined.getName()) : null;
            if (value != null) {
                String strValue = String.valueOf(value);
                if (strValue.length() > MAX_VARIABLE_VALUE_LENGTH) {
                    throw new PromptValidationException("Variable '" + defined.getName() + "' exceeds maximum length");
                }
                if (containsInjection(strValue)) {
                    throw new PromptValidationException("Variable '" + defined.getName() + "' contains unsafe content");
                }
                if (defined.getValidationRegex() != null && !defined.getValidationRegex().isEmpty()) {
                    try {
                        if (!Pattern.matches(defined.getValidationRegex(), strValue)) {
                            throw new PromptValidationException(
                                    "Variable '" + defined.getName() + "' does not match validation pattern");
                        }
                    } catch (PatternSyntaxException e) {
                        throw new PromptValidationException("Invalid validation regex for variable '" + defined.getName() + "'");
                    }
                }
            }
        }
    }

    private void validateDynamicVariables(String templateText, Map<String, Object> variables) {
        java.util.regex.Matcher matcher = Pattern.compile("\\{\\{\\s*(\\w+)\\s*}}").matcher(templateText);
        Set<String> referenced = new HashSet<>();
        while (matcher.find()) {
            referenced.add(matcher.group(1));
        }
        Set<String> provided = variables != null ? variables.keySet() : Set.of();
        for (String var : referenced) {
            if (!provided.contains(var)) {
                throw new PromptValidationException("Unresolved variable: " + var);
            }
        }
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            if (entry.getValue() != null) {
                String strValue = String.valueOf(entry.getValue());
                if (containsInjection(strValue)) {
                    throw new PromptValidationException("Variable '" + entry.getKey() + "' contains unsafe content");
                }
            }
        }
    }

    public boolean containsInjection(String text) {
        if (text == null) return false;
        return INJECTION_PATTERN.matcher(text).find();
    }

    public void validatePayloadSize(Map<String, Object> variables) {
        if (variables == null) return;
        long totalSize = variables.values().stream()
                .filter(v -> v != null)
                .mapToLong(v -> String.valueOf(v).length())
                .sum();
        if (totalSize > 100_000) {
            throw new PromptValidationException("Total variable payload exceeds maximum size of 100KB");
        }
    }
}
