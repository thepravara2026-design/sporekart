package com.sporekart.ai.automation.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.sporekart.ai.automation.api.AutomationAuditService;
import com.sporekart.ai.automation.domain.*;
import com.sporekart.ai.automation.infrastructure.persistence.LifecycleDefinitionRepository;
import com.sporekart.ai.automation.infrastructure.persistence.LifecycleHistoryRepository;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LifecycleManagerImplTest {

    @Mock
    private LifecycleDefinitionRepository definitionRepository;
    @Mock
    private LifecycleHistoryRepository historyRepository;
    @Mock
    private AutomationAuditService auditService;

    private LifecycleManagerImpl manager;

    @BeforeEach
    void setUp() {
        manager = new LifecycleManagerImpl(definitionRepository, historyRepository, auditService);
    }

    @Test
    void registerLifecycleShouldSaveAndAudit() {
        var def = new LifecycleDefinition(UUID.randomUUID(), "test", "policy",
            LifecycleStateType.CREATED, Map.of(), Map.of(), Instant.now(), Instant.now());

        when(definitionRepository.save(def)).thenReturn(def);

        var result = manager.registerLifecycle(def);

        assertEquals(def, result);
        verify(auditService).recordAudit(eq("LIFECYCLE_REGISTERED"), any(), any(), any(), any(), any());
    }

    @Test
    void getCurrentStateShouldReturnLastState() {
        var entityId = UUID.randomUUID();
        var state1 = new LifecycleState(UUID.randomUUID(), entityId, "policy",
            LifecycleStateType.CREATED, Map.of(), Instant.now(), Instant.now());
        var state2 = new LifecycleState(UUID.randomUUID(), entityId, "policy",
            LifecycleStateType.ACTIVE, Map.of(), Instant.now(), Instant.now());

        when(historyRepository.findByEntityIdAndEntityTypeOrderByEnteredAtAsc(entityId, "policy"))
            .thenReturn(List.of(state1, state2));

        var result = manager.getCurrentState(entityId, "policy");

        assertEquals(state2, result);
    }

    @Test
    void getCurrentStateShouldReturnNullWhenNoHistory() {
        var entityId = UUID.randomUUID();
        when(historyRepository.findByEntityIdAndEntityTypeOrderByEnteredAtAsc(entityId, "policy"))
            .thenReturn(List.of());

        var result = manager.getCurrentState(entityId, "policy");

        assertNull(result);
    }

    @Test
    void transitionShouldAllowValidTransition() {
        var entityId = UUID.randomUUID();
        var def = new LifecycleDefinition(UUID.randomUUID(), "test", "policy",
            LifecycleStateType.CREATED,
            Map.of(LifecycleStateType.CREATED, Map.of("approve", LifecycleStateType.ACTIVE)),
            Map.of(), Instant.now(), Instant.now());

        when(definitionRepository.findAll()).thenReturn(List.of(def));
        when(historyRepository.findByEntityIdAndEntityTypeOrderByEnteredAtAsc(entityId, "policy"))
            .thenReturn(List.of());
        when(historyRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var result = manager.transition(entityId, "policy", LifecycleStateType.ACTIVE, "admin", "approved");

        assertNotNull(result);
        assertEquals(LifecycleStateType.ACTIVE, result.currentState());
        verify(auditService).recordAudit(eq("LIFECYCLE_TRANSITION"), any(), any(), any(), any(), any());
    }

    @Test
    void transitionShouldThrowOnInvalidTransition() {
        var entityId = UUID.randomUUID();
        var def = new LifecycleDefinition(UUID.randomUUID(), "test", "policy",
            LifecycleStateType.CREATED,
            Map.of(LifecycleStateType.CREATED, Map.of("approve", LifecycleStateType.ACTIVE)),
            Map.of(), Instant.now(), Instant.now());

        when(definitionRepository.findAll()).thenReturn(List.of(def));
        when(historyRepository.findByEntityIdAndEntityTypeOrderByEnteredAtAsc(entityId, "policy"))
            .thenReturn(List.of());

        assertThrows(IllegalStateException.class,
            () -> manager.transition(entityId, "policy", LifecycleStateType.DELETED, "admin", "invalid"));
    }

    @Test
    void getHistoryShouldReturnAllStates() {
        var entityId = UUID.randomUUID();
        var states = List.of(
            new LifecycleState(UUID.randomUUID(), entityId, "policy",
                LifecycleStateType.CREATED, Map.of(), Instant.now(), Instant.now())
        );

        when(historyRepository.findByEntityIdAndEntityTypeOrderByEnteredAtAsc(entityId, "policy"))
            .thenReturn(states);

        var result = manager.getHistory(entityId, "policy");

        assertEquals(states, result);
    }

    @Test
    void getAllLifecyclesShouldReturnAllDefinitions() {
        var defs = List.of(
            new LifecycleDefinition(UUID.randomUUID(), "test", "policy",
                LifecycleStateType.CREATED, Map.of(), Map.of(), Instant.now(), Instant.now())
        );

        when(definitionRepository.findAll()).thenReturn(defs);

        var result = manager.getAllLifecycles();

        assertEquals(defs, result);
    }
}
