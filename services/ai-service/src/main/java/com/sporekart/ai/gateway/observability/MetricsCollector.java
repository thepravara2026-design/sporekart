package com.sporekart.ai.gateway.observability;

import com.sporekart.ai.gateway.domain.GatewayMetrics;
import com.sporekart.ai.gateway.pipeline.PipelineContext;

import java.util.List;

public interface MetricsCollector {
    void record(PipelineContext context);
    void record(GatewayMetrics metrics);
    void recordLatency(String operation, long durationMs);
    void recordCount(String metric, long count);
    void recordError(String errorCode, String provider);
    List<GatewayMetrics> getRecentMetrics(int limit);
    void flush();
}
