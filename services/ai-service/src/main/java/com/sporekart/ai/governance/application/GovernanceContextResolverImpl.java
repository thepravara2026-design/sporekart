package com.sporekart.ai.governance.application;

import com.sporekart.ai.governance.api.GovernanceContextResolver;
import com.sporekart.ai.governance.domain.*;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;

@Service
public class GovernanceContextResolverImpl implements GovernanceContextResolver {

    @Override
    public GovernanceContext resolveContext(GovernanceRequest request) {
        return new GovernanceContext(
            UUID.randomUUID(), request.id(), request.module(), request.action(),
            resolveResource(request.module(), request.action(), request.payload()),
            resolveSubject(request.userId(), request.roles()),
            resolveEnvironment(), OffsetDateTime.now()
        );
    }

    @Override
    public Map<String, Object> resolveResource(String module, String action, Map<String, Object> payload) {
        Map<String, Object> resource = new HashMap<>();
        resource.put("module", module);
        resource.put("action", action);
        resource.put("type", payload != null ? payload.getOrDefault("type", "unknown") : "unknown");
        return resource;
    }

    @Override
    public Map<String, Object> resolveSubject(String userId, List<String> roles) {
        Map<String, Object> subject = new HashMap<>();
        subject.put("userId", userId);
        subject.put("roles", roles);
        subject.put("authenticated", userId != null && !userId.isBlank());
        return subject;
    }

    @Override
    public Map<String, Object> resolveEnvironment() {
        Map<String, Object> env = new HashMap<>();
        env.put("timestamp", OffsetDateTime.now().toString());
        env.put("version", "1.0");
        return env;
    }
}
