package com.sporekart.ai.gateway.application;

import com.sporekart.ai.gateway.api.AiGateway;
import com.sporekart.ai.gateway.api.RateLimiter;
import com.sporekart.ai.gateway.api.RequestValidator;
import com.sporekart.ai.core.domain.AiRequest;
import com.sporekart.ai.core.domain.AiResponse;
import org.springframework.stereotype.Service;

@Service
public class GatewayService implements AiGateway {
    private final RateLimiter rateLimiter;
    private final RequestValidator requestValidator;

    public GatewayService(RateLimiter rateLimiter, RequestValidator requestValidator) {
        this.rateLimiter = rateLimiter;
        this.requestValidator = requestValidator;
    }

    @Override
    public AiResponse route(AiRequest request) {
        requestValidator.validate(request);
        if (!rateLimiter.tryAcquire(request.module())) {
            return AiResponse.failure("Rate limit exceeded for module: " + request.module());
        }
        throw new UnsupportedOperationException("Provider routing not yet implemented");
    }

    @Override
    public boolean supports(String providerType) {
        return true;
    }
}
