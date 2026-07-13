package com.sporekart.ai.governance.application;

import com.sporekart.ai.governance.domain.*;
import com.sporekart.ai.governance.infrastructure.persistence.GovernanceRegistryEntity;
import com.sporekart.ai.governance.infrastructure.persistence.GovernanceRegistryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GovernanceRegistryServiceImplTest {

    @Mock private GovernanceRegistryRepository repository;
    private GovernanceRegistryServiceImpl registryService;

    @BeforeEach
    void setUp() {
        registryService = new GovernanceRegistryServiceImpl(repository);
    }

    @Test
    void testRegister() {
        GovernanceRegistry registry = new GovernanceRegistry(UUID.randomUUID(), "test", "content",
            "/api/v1/content", GovernanceScope.REQUEST, GovernanceMode.DEVELOPMENT,
            Map.of(), true, OffsetDateTime.now(), OffsetDateTime.now());

        when(repository.save(any())).thenReturn(new GovernanceRegistryEntity());
        GovernanceRegistry result = registryService.register(registry);
        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    void testFindByModule() {
        when(repository.findByModuleAndIsDeletedFalse("content")).thenReturn(List.of());
        assertTrue(registryService.findByModule("content").isEmpty());
    }

    @Test
    void testIsRegistered() {
        String module = "content";
        String endpoint = "/api/v1/content";
        when(repository.findByModuleAndEndpointAndIsDeletedFalse(module, endpoint))
            .thenReturn(Optional.empty());
        assertFalse(registryService.isRegistered(module, endpoint));
    }
}
