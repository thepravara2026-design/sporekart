package com.sporekart.ai.pipeline.execution;

import com.sporekart.ai.pipeline.PipelineContext;
import com.sporekart.ai.pipeline.model.FinishReason;
import com.sporekart.ai.pipeline.model.PipelineResponse;
import com.sporekart.ai.pipeline.model.TokenUsage;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class ExecutionEngine {

    public void execute(PipelineContext pipelineContext) {
        var request = pipelineContext.request();
        var provider = pipelineContext.selectedProvider();
        var model = pipelineContext.selectedModel();
        var compiledPrompt = pipelineContext.<String>getAttribute("compiledPrompt");

        if (provider == null) {
            pipelineContext.fail("No provider selected for execution");
            return;
        }

        var start = Instant.now();
        try {
            var executionResult = invokeProvider(provider, model, compiledPrompt, request);
            var latency = Duration.between(start, Instant.now());

            var response = PipelineResponse.builder()
                    .responseId(UUID.randomUUID().toString())
                    .requestId(request.requestId())
                    .provider(provider)
                    .model(model)
                    .generatedOutput(executionResult.output)
                    .tokenUsage(executionResult.tokenUsage)
                    .latency(latency)
                    .finishReason(executionResult.finishReason)
                    .cost(executionResult.cost)
                    .success(executionResult.success)
                    .metadata(Map.of("executionTimestamp", start.toString()))
                    .build();

            pipelineContext.setResponse(response);
            pipelineContext.setAttribute("executionSuccess", true);
            pipelineContext.setAttribute("executionLatencyMs", latency.toMillis());
        } catch (Exception e) {
            pipelineContext.fail("Provider execution failed: " + e.getMessage());
            pipelineContext.setAttribute("executionError", e.getMessage());
            pipelineContext.setAttribute("executionSuccess", false);
        }
        pipelineContext.recordMiddleware("ExecutionEngine");
    }

    private ExecutionResult invokeProvider(String provider, String model, String prompt,
            com.sporekart.ai.pipeline.model.PipelineRequest request) {
        var inputTokens = prompt != null ? prompt.length() / 4 : 10;
        var outputTokens = request.maxTokens() / 2;
        var cost = calculateCost(provider, inputTokens, outputTokens);

        return new ExecutionResult(
                "[Mock " + provider + " response] " + (prompt != null ? prompt : ""),
                true,
                TokenUsage.of(inputTokens, outputTokens),
                FinishReason.STOP,
                cost);
    }

    private double calculateCost(String provider, int inputTokens, int outputTokens) {
        return switch (provider.toUpperCase()) {
            case "OPENAI" -> (inputTokens * 0.00003) + (outputTokens * 0.00006);
            case "GEMINI" -> (inputTokens * 0.0000025) + (outputTokens * 0.000005);
            case "CLAUDE" -> (inputTokens * 0.000015) + (outputTokens * 0.000075);
            case "GROQ" -> (inputTokens * 0.000001) + (outputTokens * 0.000002);
            case "OLLAMA" -> 0.0;
            default -> (inputTokens * 0.00001) + (outputTokens * 0.00003);
        };
    }

    private record ExecutionResult(String output, boolean success, TokenUsage tokenUsage,
            FinishReason finishReason, double cost) {}
}
