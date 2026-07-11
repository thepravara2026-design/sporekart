package com.sporekart.ai.core.api;

import com.sporekart.ai.core.domain.AiResponse;
import com.sporekart.ai.core.domain.ResponseEnvelope;

public interface AIResponseHandler {
    <T> ResponseEnvelope<T> wrap(AiResponse response);
    <T> ResponseEnvelope<T> error(String errorCode, String errorMessage);
}
