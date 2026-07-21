package com.sporekart.ai.gateway.security;

import com.sporekart.ai.gateway.domain.AuthenticationResult;
import com.sporekart.ai.gateway.pipeline.PipelineContext;

import java.util.List;
import java.util.Optional;

public interface SecurityManager {
    AuthenticationResult authenticate(PipelineContext context);
    void authorize(PipelineContext context, AuthenticationResult auth);
    void audit(PipelineContext context, AuthenticationResult auth);
    void registerHook(SecurityHook hook);
    List<SecurityHook> getHooks();
    boolean isSecurityEnabled();
    Optional<String> getTenantId(PipelineContext context);
}
