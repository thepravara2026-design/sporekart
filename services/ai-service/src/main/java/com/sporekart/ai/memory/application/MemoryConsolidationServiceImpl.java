package com.sporekart.ai.memory.application;

import com.sporekart.ai.memory.api.MemoryConsolidationService;
import com.sporekart.ai.memory.domain.ConsolidationPolicy;

import org.springframework.stereotype.Service;

@Service
public class MemoryConsolidationServiceImpl implements MemoryConsolidationService {

    @Override
    public void consolidate(ConsolidationPolicy policy) {
    }

    @Override
    public void prune(ConsolidationPolicy policy, int retentionDays) {
    }

    @Override
    public void archive(ConsolidationPolicy policy) {
    }

    @Override
    public void summarize(ConsolidationPolicy policy) {
    }
}
