package com.sporekart.ai.prompt.application;

import com.sporekart.ai.prompt.infrastructure.persistence.PromptTemplateEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PromptRenderService {

    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{\\{\\s*(\\w+)\\s*}}");
    private static final Pattern UNRESOLVED_PATTERN = Pattern.compile("\\{\\{\\s*\\w+\\s*}}");

    private final PromptValidationService validationService;

    public PromptRenderService(PromptValidationService validationService) {
        this.validationService = validationService;
    }

    public String render(PromptTemplateEntity template, Map<String, Object> variables) {
        validationService.validateRenderVariables(template, variables);
        validationService.validatePayloadSize(variables);

        String result = template.getTemplateText();
        if (variables == null || variables.isEmpty()) {
            if (UNRESOLVED_PATTERN.matcher(result).find()) {
                throw new PromptRenderException("Template contains unresolved variables but none provided");
            }
            return result;
        }

        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            if (entry.getValue() == null) continue;
            String placeholder = "{{" + entry.getKey() + "}}";
            String value = escapeValue(String.valueOf(entry.getValue()));
            result = result.replace(placeholder, value);
        }

        Matcher unresolved = UNRESOLVED_PATTERN.matcher(result);
        if (unresolved.find()) {
            throw new PromptRenderException("Unresolved variable: " + unresolved.group());
        }

        return result;
    }

    public String renderWithDefaults(PromptTemplateEntity template, Map<String, Object> variables,
                                      java.util.List<com.sporekart.ai.prompt.infrastructure.persistence.PromptVariableEntity> definedVariables) {
        String result = template.getTemplateText();

        for (com.sporekart.ai.prompt.infrastructure.persistence.PromptVariableEntity var : definedVariables) {
            Object value = variables != null ? variables.get(var.getName()) : null;
            if (value == null && var.getDefaultValue() != null) {
                value = var.getDefaultValue();
            }
            if (value != null) {
                result = result.replace("{{" + var.getName() + "}}", escapeValue(String.valueOf(value)));
            }
        }

        Matcher unresolved = UNRESOLVED_PATTERN.matcher(result);
        if (unresolved.find()) {
            throw new PromptRenderException("Unresolved variable: " + unresolved.group());
        }

        return result;
    }

    public String renderRaw(String templateText, Map<String, Object> variables) {
        if (templateText == null) {
            throw new PromptRenderException("Template text is required");
        }
        validationService.validatePayloadSize(variables);

        String result = templateText;
        if (variables != null) {
            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                if (entry.getValue() == null) continue;
                result = result.replace("{{" + entry.getKey() + "}}", escapeValue(String.valueOf(entry.getValue())));
            }
        }

        Matcher unresolved = UNRESOLVED_PATTERN.matcher(result);
        if (unresolved.find()) {
            throw new PromptRenderException("Unresolved variable: " + unresolved.group());
        }

        return result;
    }

    private String escapeValue(String value) {
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
