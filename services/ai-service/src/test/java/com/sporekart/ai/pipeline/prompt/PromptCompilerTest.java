package com.sporekart.ai.pipeline.prompt;

import com.sporekart.ai.pipeline.PipelineContext;
import com.sporekart.ai.pipeline.model.PipelineRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PromptCompilerTest {
    private PromptCompiler compiler;

    @BeforeEach
    void setUp() {
        compiler = new PromptCompiler();
    }

    @Test
    void shouldResolveSimpleVariables() {
        var result = compiler.resolveVariables(
                "Hello ${name}!",
                Map.of("name", "John"),
                Map.of());
        assertEquals("Hello John!", result);
    }

    @Test
    void shouldResolveDoubleBraceVariables() {
        var result = compiler.resolveVariables(
                "Hello {{name}}!",
                Map.of("name", "Jane"),
                Map.of());
        assertEquals("Hello Jane!", result);
    }

    @Test
    void shouldResolveFromAssembledContext() {
        var result = compiler.resolveVariables(
                "Hello ${user}!",
                Map.of(),
                Map.of("user", "Admin"));
        assertEquals("Hello Admin!", result);
    }

    @Test
    void shouldKeepUnresolvedVariables() {
        var result = compiler.validatePrompt("Hello ${unknown}!");
        assertEquals("Hello [UNRESOLVED]!", result);
    }

    @Test
    void shouldNotModifyTextWithoutVariables() {
        var result = compiler.resolveVariables(
                "Hello world!",
                Map.of("name", "John"),
                Map.of());
        assertEquals("Hello world!", result);
    }

    @Test
    void shouldHandleMultipleVariables() {
        var result = compiler.resolveVariables(
                "${greeting}, ${name}!",
                Map.of("greeting", "Hi", "name", "Alice"),
                Map.of());
        assertEquals("Hi, Alice!", result);
    }

    @Test
    void shouldCompileInPipelineContext() {
        var request = PipelineRequest.builder()
                .requestId("r").tenantId("t").userId("u").module("m")
                .correlationId("c").context(Map.of("prompt", "Hello ${name}"))
                .variables(Map.of("name", "World"))
                .build();
        var context = new PipelineContext(request);
        context.selectProvider("OPENAI", "gpt-4");
        context.setAttribute("assembledContext", Map.of());

        compiler.compile(context);
        var compiled = context.<Map<String, Object>>getAttribute("compiledPrompt");
        assertNotNull(compiled);
        assertEquals("Hello World", compiled.get("prompt"));
        assertEquals("gpt-4", compiled.get("model"));
    }

    @Test
    void shouldHandleNullPrompt() {
        var request = PipelineRequest.builder()
                .requestId("r").tenantId("t").userId("u").module("m")
                .correlationId("c").context(Map.of())
                .build();
        var context = new PipelineContext(request);
        compiler.compile(context);
        assertNull(context.compiledPrompt());
    }
}
