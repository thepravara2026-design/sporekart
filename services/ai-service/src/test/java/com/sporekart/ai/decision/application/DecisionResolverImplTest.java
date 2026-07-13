package com.sporekart.ai.decision.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.sporekart.ai.decision.domain.*;
import com.sporekart.ai.decision.infrastructure.persistence.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.OffsetDateTime;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class DecisionResolverImplTest {

    @Mock private DecisionRepository decisionRepository;
    @Mock private DecisionRuleRepository ruleRepository;
    @Mock private DecisionRegistryRepository registryRepository;

    private DecisionResolverImpl resolver;

    @BeforeEach
    void setUp() {
        resolver = new DecisionResolverImpl(decisionRepository, ruleRepository, registryRepository);
    }

    @Test
    void resolveContextReturnsNonNullContext() {
        UUID reqId = UUID.randomUUID();
        DecisionRequest request = new DecisionRequest(reqId, "mod", "ALLOW", Map.of("k", "v"),
            Map.of("c", "v"), "user1", List.of("admin"), List.of("p1"), List.of("r1"),
            Map.of("res", "ok"), OffsetDateTime.now());

        DecisionContext context = resolver.resolveContext(request);

        assertNotNull(context);
        assertNotNull(context.id());
        assertEquals(reqId, context.requestId());
        assertEquals("mod", context.module());
        assertEquals("ALLOW", context.action());
    }

    @Test
    void findByIdReturnsOptionalEmptyForNotFound() {
        UUID id = UUID.randomUUID();
        when(decisionRepository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.empty());

        Optional<DecisionResult> result = resolver.findById(id);

        assertTrue(result.isEmpty());
        verify(decisionRepository).findByIdAndIsDeletedFalse(id);
    }
}
