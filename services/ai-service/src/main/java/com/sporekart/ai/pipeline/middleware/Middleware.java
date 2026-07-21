package com.sporekart.ai.pipeline.middleware;

import com.sporekart.ai.pipeline.PipelineContext;

@FunctionalInterface
public interface Middleware {
    void execute(PipelineContext context, MiddlewareChain chain);

    default String name() {
        return getClass().getSimpleName();
    }

    default int order() {
        return 100;
    }

    default boolean isEnabled() {
        return true;
    }
}
