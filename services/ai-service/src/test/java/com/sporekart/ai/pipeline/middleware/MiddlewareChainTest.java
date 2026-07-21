package com.sporekart.ai.pipeline.middleware;

import com.sporekart.ai.pipeline.PipelineContext;
import com.sporekart.ai.pipeline.model.PipelineRequest;
import com.sporekart.ai.pipeline.validation.RequestValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class MiddlewareChainTest {
    private PipelineContext context;

    @BeforeEach
    void setUp() {
        var request = PipelineRequest.builder()
                .requestId("r").tenantId("t").userId("u").module("chat")
                .correlationId("c").context(Map.of("prompt", "Hello")).build();
        context = new PipelineContext(request);
    }

    @Test
    void shouldExecuteAllMiddlewares() {
        var counter = new AtomicInteger(0);
        var mw1 = new TestMiddleware("M1", 1, c -> counter.incrementAndGet());
        var mw2 = new TestMiddleware("M2", 2, c -> counter.incrementAndGet());
        var chain = new MiddlewareChain(List.of(mw1, mw2));
        chain.execute(context);
        assertEquals(2, counter.get());
    }

    @Test
    void shouldExecuteInOrder() {
        var order = new StringBuilder();
        var mw1 = new TestMiddleware("M1", 1, c -> order.append("1"));
        var mw2 = new TestMiddleware("M2", 2, c -> order.append("2"));
        var mw3 = new TestMiddleware("M3", 3, c -> order.append("3"));
        var chain = new MiddlewareChain(List.of(mw3, mw1, mw2));
        chain.execute(context);
        assertEquals("123", order.toString());
    }

    @Test
    void shouldSkipDisabledMiddleware() {
        var executed = new AtomicInteger(0);
        var disabled = new Middleware() {
            @Override public String name() { return "Disabled"; }
            @Override public int order() { return 1; }
            @Override public boolean isEnabled() { return false; }
            @Override public void execute(PipelineContext ctx, MiddlewareChain chain) {
                executed.incrementAndGet();
                chain.next(ctx);
            }
        };
        var enabled = new TestMiddleware("Enabled", 2, c -> executed.incrementAndGet());
        var chain = new MiddlewareChain(List.of(disabled, enabled));
        chain.execute(context);
        assertEquals(1, executed.get());
    }

    @Test
    void shouldStopOnFailure() {
        var failing = new Middleware() {
            @Override public String name() { return "Failing"; }
            @Override public int order() { return 1; }
            @Override public void execute(PipelineContext ctx, MiddlewareChain chain) {
                ctx.fail("Failed");
            }
        };
        var afterFail = new TestMiddleware("After", 2, c -> fail("Should not execute"));
        var chain = new MiddlewareChain(List.of(failing, afterFail));
        chain.execute(context);
        assertTrue(context.failed());
    }

    @Test
    void shouldIncludeValidationAndAuth() {
        var validator = new RequestValidator();
        var chain = new MiddlewareChain(List.of(
                new ValidationMiddleware(validator),
                new AuthenticationMiddleware(),
                new AuthorizationMiddleware()
        ));
        chain.execute(context);
        assertFalse(context.failed());
        assertEquals(3, context.middlewareExecuted().size());
    }

    @Test
    void shouldFailOnInvalidRequest() {
        var badRequest = PipelineRequest.builder()
                .requestId("").tenantId("").userId("").module("")
                .correlationId("").context(Map.of()).build();
        var badContext = new PipelineContext(badRequest);
        var validator = new RequestValidator();
        var chain = new MiddlewareChain(List.of(
                new ValidationMiddleware(validator)));
        chain.execute(badContext);
        assertTrue(badContext.failed());
    }

    @Test
    void shouldApplyRateLimiting() {
        var limiter = new RateLimitingMiddleware(5);
        for (int i = 0; i < 5; i++) {
            var ctx = new PipelineContext(PipelineRequest.builder()
                    .requestId("r" + i).tenantId("test-tenant").userId("u")
                    .module("m").correlationId("c").context(Map.of("prompt", "Hi")).build());
            var chain = new MiddlewareChain(List.of(limiter));
            chain.execute(ctx);
            assertFalse(ctx.failed(), "Request " + i + " should not be rate limited");
        }
        var exceeded = new PipelineContext(PipelineRequest.builder()
                .requestId("r6").tenantId("test-tenant").userId("u")
                .module("m").correlationId("c").context(Map.of("prompt", "Hi")).build());
        var chain2 = new MiddlewareChain(List.of(limiter));
        chain2.execute(exceeded);
        assertTrue(exceeded.failed());
    }

    @Test
    void providerSelectionShouldSetProvider() {
        var chain = new MiddlewareChain(List.of(new ProviderSelectionMiddleware()));
        chain.execute(context);
        assertNotNull(context.selectedProvider());
        assertNotNull(context.selectedModel());
    }

    private record TestMiddleware(String name, int order, java.util.function.Consumer<PipelineContext> action)
            implements Middleware {
        @Override public void execute(PipelineContext ctx, MiddlewareChain chain) {
            action.accept(ctx);
            ctx.recordMiddleware(name());
            chain.next(ctx);
        }
    }
}
