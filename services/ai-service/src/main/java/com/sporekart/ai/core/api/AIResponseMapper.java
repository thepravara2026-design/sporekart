package com.sporekart.ai.core.api;

import com.sporekart.ai.core.domain.AiResponse;
import com.sporekart.ai.core.domain.ResponseEnvelope;

public interface AIResponseMapper {
    <T> ResponseEnvelope<T> toEnvelope(AiResponse response);
    <T> ResponseEnvelope<T> toErrorEnvelope(String errorCode, String errorMessage);
    <T> ResponseEnvelope<T> toErrorEnvelope(String errorCode, String errorMessage, String correlationId);
}
