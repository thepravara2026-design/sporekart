package com.sporekart.ai.governance.application;

import com.sporekart.ai.governance.domain.*;
import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class GovernanceContextResolverImplTest {

    private final GovernanceContextResolverImpl resolver = new GovernanceContextResolverImpl();

    @Test
    void testResolveContext() {
        GovernanceRequest request = new GovernanceRequest(UUID.randomUUID(), "content", "generate",
            Map.of("type", "article"), Map.of(), "user1", List.of("admin"), OffsetDateTime.now());

        GovernanceContext context = resolver.resolveContext(request);
        assertNotNull(context);
        assertEquals("content", context.module());
        assertEquals(request.id(), context.requestId());
    }

    @Test
    void testResolveResource() {
        Map<String, Object> resource = resolver.resolveResource("content", "generate", Map.of("type", "article"));
        assertEquals("content", resource.get("module"));
        assertEquals("generate", resource.get("action"));
    }

    @Test
    void testResolveSubject() {
        Map<String, Object> subject = resolver.resolveSubject("user1", List.of("admin"));
        assertEquals("user1", subject.get("userId"));
        assertTrue((boolean) subject.get("authenticated"));
    }

    @Test
    void testResolveSubject_Unauthenticated() {
        Map<String, Object> subject = resolver.resolveSubject("", List.of());
        assertFalse((boolean) subject.get("authenticated"));
    }

    @Test
    void testResolveEnvironment() {
        Map<String, Object> env = resolver.resolveEnvironment();
        assertNotNull(env.get("timestamp"));
        assertEquals("1.0", env.get("version"));
    }
}
