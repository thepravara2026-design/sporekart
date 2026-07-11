package com.sporekart.ai.gateway.infrastructure;

import com.sporekart.ai.core.api.RetryStrategy;
import com.sporekart.ai.core.domain.AiRequest;
import com.sporekart.ai.core.domain.AiResponse;

public class DefaultRetryStrategy implements RetryStrategy {
    private static final int MAX_RETRIES = 3;
    private static final long BASE_DELAY_MS = 1000;

    @Override
    public boolean shouldRetry(AiRequest request, AiResponse response, int attempt) {
        return !response.success() && attempt < MAX_RETRIES;
    }

    @Override
    public long getDelayMs(int attempt) {
        return BASE_DELAY_MS * (long) Math.pow(2, attempt - 1);
    }

    @Override
    public int getMaxRetries() {
        return MAX_RETRIES;
    }
}
