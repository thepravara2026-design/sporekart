package com.sporekart.ai.core.api;

import com.sporekart.ai.core.domain.AiRequest;
import com.sporekart.ai.core.domain.AiResponse;

public interface RetryStrategy {
    boolean shouldRetry(AiRequest request, AiResponse response, int attempt);
    long getDelayMs(int attempt);
    int getMaxRetries();
}
