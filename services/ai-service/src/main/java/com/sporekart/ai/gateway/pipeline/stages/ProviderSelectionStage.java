package com.sporekart.ai.gateway.pipeline.stages;

import com.sporekart.ai.gateway.pipeline.PipelineContext;
import com.sporekart.ai.gateway.pipeline.PipelineStage;

public final class ProviderSelectionStage {

    private ProviderSelectionStage() {}

    public static void execute(PipelineContext context) {
        context.setAttribute("provider.selected", null);
        context.setAttribute("provider.strategy", "default");
    }

    public static PipelineStage stage() {
        return PipelineStage.PROVIDER_SELECTION;
    }
}
