package com.sporekart.ai.pipeline.context;

import com.sporekart.ai.pipeline.PipelineContext;
import com.sporekart.ai.pipeline.model.PipelineRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ContextBuilderTest {
    private ContextBuilder builder;
    private PipelineContext pipelineContext;

    @BeforeEach
    void setUp() {
        builder = new ContextBuilder();
        var request = PipelineRequest.builder()
                .requestId("req-1").tenantId("t-1").userId("u-1")
                .module("chat").correlationId("c-1")
                .conversationId("conv-1")
                .context(Map.of("prompt", "Hello", "userRole", "admin",
                        "userLocale", "fr-FR"))
                .source(com.sporekart.ai.pipeline.model.RequestSource.CRM)
                .workspace("workspace-1")
                .build();
        pipelineContext = new PipelineContext(request);
    }

    @Test
    void shouldBuildFullContext() {
        builder.build(pipelineContext);
        var assembled = pipelineContext.<Map<String, Object>>getAttribute("assembledContext");
        assertNotNull(assembled);
        assertTrue(assembled.containsKey("userId"));
        assertTrue(assembled.containsKey("tenantId"));
        assertTrue(assembled.containsKey("module"));
    }

    @Test
    void shouldIncludeConversationContext() {
        var target = new java.util.HashMap<String, Object>();
        builder.buildConversationContext(pipelineContext.request(), target);
        assertEquals("conv-1", target.get("conversationId"));
    }

    @Test
    void shouldIncludeUserContext() {
        var target = new java.util.HashMap<String, Object>();
        builder.buildUserContext(pipelineContext.request(), target);
        assertEquals("u-1", target.get("userId"));
        assertEquals("fr-FR", target.get("userLocale"));
    }

    @Test
    void shouldIncludeBusinessContext() {
        var target = new java.util.HashMap<String, Object>();
        builder.buildBusinessContext(pipelineContext.request(), target);
        assertEquals("CRM", target.get("source"));
    }

    @Test
    void shouldIncludeTenantContext() {
        var target = new java.util.HashMap<String, Object>();
        builder.buildTenantContext(pipelineContext.request(), target);
        assertEquals("t-1", target.get("tenantId"));
    }

    @Test
    void shouldIncludeSecurityContext() {
        var target = new java.util.HashMap<String, Object>();
        builder.buildSecurityContext(pipelineContext.request(), target);
        assertEquals("standard", target.get("securityLevel"));
    }

    @Test
    void shouldResolveVariableFromRequest() {
        var request = PipelineRequest.builder()
                .requestId("r").tenantId("t").userId("u").module("m")
                .correlationId("c").context(Map.of("prompt", "Hi"))
                .variables(Map.of("name", "John"))
                .build();
        var assembled = new java.util.HashMap<String, Object>();
        var result = builder.resolveVariable("name", request, assembled);
        assertEquals("John", result);
    }

    @Test
    void shouldReturnNullForUnknownVariable() {
        var result = builder.resolveVariable("nonexistent",
                pipelineContext.request(), Map.of());
        assertNull(result);
    }
}
