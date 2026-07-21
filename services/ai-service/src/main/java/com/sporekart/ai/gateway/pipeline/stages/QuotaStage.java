package com.sporekart.ai.gateway.pipeline.stages;

import com.sporekart.ai.gateway.pipeline.PipelineContext;
import com.sporekart.ai.gateway.pipeline.PipelineStage;

public final class QuotaStage {

    private QuotaStage() {}

    public static void execute(PipelineContext context) {
        context.setAttribute("quota.checked", true);
        context.setAttribute("quota.available", true);
    }

    public static PipelineStage stage() {
        return PipelineStage.QUOTA_CHECK;
    }
}
