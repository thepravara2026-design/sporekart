package com.sporekart.ai.memory.api;

import com.sporekart.ai.memory.domain.ConsolidationPolicy;

public interface MemoryConsolidationService {
    void consolidate(ConsolidationPolicy policy);
    void prune(ConsolidationPolicy policy, int retentionDays);
    void archive(ConsolidationPolicy policy);
    void summarize(ConsolidationPolicy policy);
}
