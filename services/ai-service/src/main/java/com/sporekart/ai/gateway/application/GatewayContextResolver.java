package com.sporekart.ai.gateway.application;

import com.sporekart.ai.core.api.AIContextResolver;
import com.sporekart.ai.core.domain.AiRequest;
import com.sporekart.ai.core.domain.CorrelationId;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class GatewayContextResolver implements AIContextResolver {

    @Override
    public Map<String, Object> resolve(AiRequest request, CorrelationId correlationId) {
        Map<String, Object> context = new LinkedHashMap<>();
        context.put("correlationId", correlationId.id());
        context.put("requestId", request.conversationId() != null ? request.conversationId() : UUID.randomUUID().toString());
        context.put("module", resolveModule(request));
        context.put("provider", resolveProvider(request));
        context.put("userId", resolveUserId(request));
        return context;
    }

    @Override
    public String resolveUserId(AiRequest request) {
        return request.parameters() != null
                ? (String) request.parameters().getOrDefault("userId", "anonymous")
                : "anonymous";
    }

    @Override
    public String resolveModule(AiRequest request) {
        return request.module() != null ? request.module() : "unknown";
    }

    @Override
    public String resolveProvider(AiRequest request) {
        return request.parameters() != null
                ? (String) request.parameters().getOrDefault("provider", "MOCK")
                : "MOCK";
    }
}
