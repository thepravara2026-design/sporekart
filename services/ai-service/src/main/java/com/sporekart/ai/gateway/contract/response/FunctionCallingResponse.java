package com.sporekart.ai.gateway.contract.response;

import java.util.List;
import java.util.Map;

public record FunctionCallingResponse(
    String id,
    String model,
    List<FunctionCall> calls,
    Usage usage
) {
    public record FunctionCall(String name, Map<String, Object> arguments, String result) {}
    public record Usage(int promptTokens, int completionTokens, int totalTokens) {}
}
