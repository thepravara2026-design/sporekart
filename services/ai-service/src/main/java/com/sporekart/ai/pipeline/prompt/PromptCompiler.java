package com.sporekart.ai.pipeline.prompt;

import com.sporekart.ai.pipeline.PipelineContext;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PromptCompiler {
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{([^}]+)\\}");
    private static final Pattern DOUBLE_BRACE_PATTERN = Pattern.compile("\\{\\{([^}]+)\\}\\}");

    public void compile(PipelineContext pipelineContext) {
        var request = pipelineContext.request();
        var context = request.context();
        var assembledContext = pipelineContext.<Map<String, Object>>getAttribute("assembledContext");

        String rawPrompt = extractRawPrompt(context);
        if (rawPrompt == null) {
            pipelineContext.setCompiledPrompt(null);
            pipelineContext.setAttribute("compilationError", "No prompt found in context");
            pipelineContext.recordMiddleware("PromptCompiler");
            return;
        }

        var resolved = resolveVariables(rawPrompt, request.variables(), assembledContext);
        var validated = validatePrompt(resolved);
        var compiled = buildFinalPayload(resolved, request, pipelineContext);

        pipelineContext.setCompiledPrompt(compiled);
        pipelineContext.setAttribute("compiledPrompt", resolved);
        pipelineContext.setAttribute("promptLength", resolved.length());
        pipelineContext.setAttribute("promptCompiled", true);
        pipelineContext.recordMiddleware("PromptCompiler");
    }

    private String extractRawPrompt(Map<String, Object> context) {
        if (context == null) return null;
        Object prompt = context.get("prompt");
        if (prompt == null) prompt = context.get("input");
        if (prompt == null) prompt = context.get("query");
        return prompt != null ? prompt.toString() : null;
    }

    String resolveVariables(String template, Map<String, Object> variables, Map<String, Object> assembledContext) {
        if (template == null) return null;

        var result = VARIABLE_PATTERN.matcher(template);
        var buffer = new StringBuffer();

        while (result.find()) {
            String varName = result.group(1).trim();
            String replacement = findVariableValue(varName, variables, assembledContext);
            if (replacement != null) {
                result.appendReplacement(buffer, Matcher.quoteReplacement(replacement));
            }
        }
        result.appendTail(buffer);

        var result2 = DOUBLE_BRACE_PATTERN.matcher(buffer.toString());
        var buffer2 = new StringBuffer();
        while (result2.find()) {
            String varName = result2.group(1).trim();
            String replacement = findVariableValue(varName, variables, assembledContext);
            if (replacement != null) {
                result2.appendReplacement(buffer2, Matcher.quoteReplacement(replacement));
            }
        }
        result2.appendTail(buffer2);

        return buffer2.toString();
    }

    private String findVariableValue(String varName, Map<String, Object> variables, Map<String, Object> assembledContext) {
        if (variables != null && variables.containsKey(varName)) {
            var val = variables.get(varName);
            return val != null ? val.toString() : null;
        }
        if (assembledContext != null && assembledContext.containsKey(varName)) {
            var val = assembledContext.get(varName);
            return val != null ? val.toString() : null;
        }
        return null;
    }

    String validatePrompt(String prompt) {
        if (prompt == null) return null;
        var unresolved = VARIABLE_PATTERN.matcher(prompt);
        if (unresolved.find()) {
            return prompt.replaceAll("\\$\\{[^}]+\\}", "[UNRESOLVED]");
        }
        return prompt;
    }

    private Map<String, Object> buildFinalPayload(String resolvedPrompt, PipelineRequest request,
            PipelineContext pipelineContext) {
        var payload = new java.util.LinkedHashMap<String, Object>();
        payload.put("prompt", resolvedPrompt);
        payload.put("model", pipelineContext.selectedModel());
        payload.put("temperature", request.temperature());
        payload.put("maxTokens", request.maxTokens());
        payload.put("topP", request.topP());
        payload.put("stream", request.stream());
        if (request.model() != null) payload.put("requestedModel", request.model());
        return payload;
    }
}
