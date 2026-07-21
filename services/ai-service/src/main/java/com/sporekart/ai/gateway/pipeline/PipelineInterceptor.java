package com.sporekart.ai.gateway.pipeline;

public interface PipelineInterceptor {
    boolean before(PipelineContext context);
    void after(PipelineContext context);
    void onError(PipelineContext context, Throwable error);
    int order();
    boolean supports(PipelineStage stage);
}
