package com.sporekart.ai.pipeline.context;

import com.sporekart.ai.pipeline.PipelineContext;
import com.sporekart.ai.pipeline.model.PipelineRequest;

import java.util.HashMap;
import java.util.Map;

public class ContextBuilder {

    public void build(PipelineContext pipelineContext) {
        var request = pipelineContext.request();
        var builtContext = new HashMap<String, Object>();

        buildConversationContext(request, builtContext);
        buildUserContext(request, builtContext);
        buildBusinessContext(request, builtContext);
        buildApplicationContext(request, builtContext);
        buildTenantContext(request, builtContext);
        buildSecurityContext(request, builtContext);

        pipelineContext.setAttribute("assembledContext", builtContext);
        pipelineContext.setAttribute("contextSize", builtContext.size());
        pipelineContext.recordMiddleware("ContextBuilder");
    }

    public Map<String, Object> buildConversationContext(PipelineRequest request, Map<String, Object> target) {
        if (request.conversationId() != null) {
            target.put("conversationId", request.conversationId());
            target.put("conversationHistory", request.context().getOrDefault("history", List.of()));
        }
        target.put("messages", request.context().getOrDefault("messages", List.of()));
        return target;
    }

    public Map<String, Object> buildUserContext(PipelineRequest request, Map<String, Object> target) {
        target.put("userId", request.userId());
        target.put("workspace", request.workspace());
        target.put("userPreferences", request.context().getOrDefault("userPreferences", Map.of()));
        target.put("userRole", request.context().getOrDefault("userRole", "user"));
        target.put("userLocale", request.context().getOrDefault("userLocale", "en-US"));
        return target;
    }

    public Map<String, Object> buildBusinessContext(PipelineRequest request, Map<String, Object> target) {
        target.put("module", request.module());
        target.put("source", request.source().name());
        target.put("businessEntity", request.context().getOrDefault("businessEntity", null));
        target.put("businessOperation", request.context().getOrDefault("businessOperation", null));
        target.put("businessMetadata", request.context().getOrDefault("businessMetadata", Map.of()));
        return target;
    }

    public Map<String, Object> buildApplicationContext(PipelineRequest request, Map<String, Object> target) {
        target.put("requestSource", request.source().name());
        target.put("correlationId", request.correlationId());
        target.put("workflowId", request.context().getOrDefault("workflowId", null));
        target.put("applicationVersion", request.context().getOrDefault("appVersion", "1.0.0"));
        return target;
    }

    public Map<String, Object> buildTenantContext(PipelineRequest request, Map<String, Object> target) {
        target.put("tenantId", request.tenantId());
        target.put("tenantConfig", request.context().getOrDefault("tenantConfig", Map.of()));
        target.put("tenantRegion", request.context().getOrDefault("tenantRegion", "default"));
        return target;
    }

    public Map<String, Object> buildSecurityContext(PipelineRequest request, Map<String, Object> target) {
        target.put("securityLevel", request.context().getOrDefault("securityLevel", "standard"));
        target.put("complianceLevel", request.context().getOrDefault("complianceLevel", "standard"));
        target.put("dataClassification", request.context().getOrDefault("dataClassification", "internal"));
        return target;
    }

    @SuppressWarnings("unchecked")
    public <T> T resolveVariable(String name, PipelineRequest request, Map<String, Object> assembledContext) {
        if (request.variables() != null && request.variables().containsKey(name)) {
            return (T) request.variables().get(name);
        }
        if (assembledContext.containsKey(name)) {
            return (T) assembledContext.get(name);
        }
        if (request.context().containsKey(name)) {
            return (T) request.context().get(name);
        }
        return null;
    }
}
