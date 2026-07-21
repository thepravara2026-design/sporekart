package com.sporekart.ai.pipeline.middleware;

import com.sporekart.ai.pipeline.PipelineContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MiddlewareChain {
    private final List<Middleware> middlewares;
    private int currentIndex = 0;

    public MiddlewareChain(List<Middleware> middlewares) {
        this.middlewares = new ArrayList<>(middlewares);
        this.middlewares.sort(Comparator.comparingInt(Middleware::order));
    }

    public void next(PipelineContext context) {
        if (context.failed()) return;
        if (currentIndex >= middlewares.size()) return;
        var middleware = middlewares.get(currentIndex++);
        if (middleware.isEnabled()) {
            middleware.execute(context, this);
        } else {
            next(context);
        }
    }

    public void execute(PipelineContext context) {
        currentIndex = 0;
        next(context);
    }

    public List<Middleware> getMiddlewares() {
        return List.copyOf(middlewares);
    }
}
