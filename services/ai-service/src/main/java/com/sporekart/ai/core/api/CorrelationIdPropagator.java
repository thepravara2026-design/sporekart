package com.sporekart.ai.core.api;

import com.sporekart.ai.core.domain.CorrelationId;

public interface CorrelationIdPropagator {
    CorrelationId getCurrent();
    void setCurrent(CorrelationId correlationId);
    void clear();
}
